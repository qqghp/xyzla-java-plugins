package com.xyzla.common.convert;

import com.xyzla.common.util.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.Date;

@ReadingConverter
public class String2DateConvert implements Converter<String, Date> {


    @Override
    public Date convert(String source) {
        return DateUtil.parse(source, "yyyy-MM-dd HH:mm:ss,SSS");
    }
}
