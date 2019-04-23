package com.ykshou.society.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by BG242387 on 2018/2/9.
 */
public class RepairedDatasource extends DruidDataSource {
    @Override
    public DruidPooledConnection getConnection(long maxWaitMillis) throws SQLException {
        DruidPooledConnection connection = super.getConnection(maxWaitMillis);
        //保证数据库编码正确
        PreparedStatement ps = connection.prepareStatement("set names utf8mb4");
        ps.execute();
        return connection;
    }
}
