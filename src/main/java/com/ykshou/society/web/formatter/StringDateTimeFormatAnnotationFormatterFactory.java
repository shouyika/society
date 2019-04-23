package com.ykshou.society.web.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bl02656 on 2015-06-25.
 */
public class StringDateTimeFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<StringDateTimeFormat> {
    private final Set<Class<?>> fieldTypes;

    private final StringDateTimeFormatter formatter;

    public StringDateTimeFormatAnnotationFormatterFactory() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(Date.class);
        this.fieldTypes = set;
        this.formatter = new StringDateTimeFormatter();
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return fieldTypes;
    }

    @Override
    public Printer<?> getPrinter(StringDateTimeFormat annotation, Class<?> fieldType) {
        return formatter;
    }

    @Override
    public Parser<?> getParser(StringDateTimeFormat annotation, Class<?> fieldType) {
        return formatter;
    }
}
