package com.ykshou.society.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_0;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class WebSocketServerHandler extends SimpleChannelInboundHandler {

    private static final Timer timer;

    private static final ConcurrentHashMap<String, ChannelHandlerContext> CONNECTION_MAP = new ConcurrentHashMap<>();

    static {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Enumeration<ChannelHandlerContext> contexts = CONNECTION_MAP.elements();
                while (contexts.hasMoreElements()) {
                    ChannelHandlerContext context = contexts.nextElement();
                    context.writeAndFlush(new TextWebSocketFrame("每十秒通知一下"));
                }
            }
        }, new Date(), 10000L);
    }

    private byte state; // 0 - none, 1 - initialized, 2 - destroyed

    private static void sendHttpResponse(
            ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            // Tell the client we're going to close the connection.
            res.headers().set(CONNECTION, CLOSE);
            ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        } else {
            if (req.protocolVersion().equals(HTTP_1_0)) {
                res.headers().set(CONNECTION, KEEP_ALIVE);
            }
            ctx.writeAndFlush(res);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // Allow only GET methods.
        if (!GET.equals(req.method())) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

//        if ("/test".equals(req.uri())) {
//            ByteBuf content = Unpooled.copiedBuffer("<html><body>test</body></html>", StandardCharsets.UTF_8);
//            FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, content);
//            sendHttpResponse(ctx, req, res);
//        } else {
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
//        }
      /*  // Send the demo page and favicon.ico
        if ("/".equals(req.uri())) {
            ByteBuf content = WebSocketServerBenchmarkPage.getContent(getWebSocketLocation(req));
            FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);

            res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpUtil.setContentLength(res, content.readableBytes());

            sendHttpResponse(ctx, req, res);
            return;
        } else {
        if ("/favicon.ico".equals(req.uri())) {
            FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
            sendHttpResponse(ctx, req, res);
            return;
        }
*/
        // Handshake

    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            // Echo the frame
            ctx.write(new TextWebSocketFrame(((TextWebSocketFrame) frame).text()));
            return;
        }
        throw new UnsupportedMessageTypeException("消息" + frame.getClass() + "不支持");
//        if (frame instanceof BinaryWebSocketFrame) {
//            // Echo the frame
//            ctx.write(frame.retain());
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        init(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        destroy(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive()) {
            init(ctx);
        }
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        destroy(ctx);
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
            init(ctx);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        destroy(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                ctx.writeAndFlush(new CloseWebSocketFrame(200, "闲置过久"))
                        .addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void init(ChannelHandlerContext ctx) {
        if (state != 0) {
            return;
        }
        state = 1;
        CONNECTION_MAP.put(ctx.channel().id().asLongText(), ctx);
    }

    private void destroy(ChannelHandlerContext ctx) {
        if (state != 1) {
            return;
        }
        state = 2;
        CONNECTION_MAP.remove(ctx.channel().id().asLongText());
    }

}
