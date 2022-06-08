package com.xyzla.common.convert;

import com.xyzla.common.util.ZonedUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;

@WritingConverter
public class LocalDateTime2StringConvert implements Converter<LocalDateTime, String> {

    @Override
    public String convert(LocalDateTime time) {
        return time.format(ZonedUtil.DTF_DTMS);
    }
}
