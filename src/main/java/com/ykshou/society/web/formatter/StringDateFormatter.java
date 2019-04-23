package com.ykshou.society.web.formatter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.format.Formatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringDateFormatter implements Formatter<Date> {

    public static void main(String[] args) {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatter(new StringDateFormatter());
        Object obj = conversionService
                .convert(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), Date.class);
        System.out.println(obj);
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        if (null == object) {
            return null;
        }
        return object.getTime() + "";
    }

}
