package com.xyzla.common.convert;

import com.xyzla.common.util.ZonedUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;

@ReadingConverter
public class String2LocalDateTimeConvert implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source, ZonedUtil.DTF_DTMS);
    }
}
