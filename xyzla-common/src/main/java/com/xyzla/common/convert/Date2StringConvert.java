package com.xyzla.common.convert;


import com.xyzla.common.util.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.Date;

@WritingConverter
public class Date2StringConvert implements Converter<Date, String> {

    @Override
    public String convert(Date source) {
        return DateUtil.format(source, "yyyy-MM-dd HH:mm:ss,SSS");
    }


}
