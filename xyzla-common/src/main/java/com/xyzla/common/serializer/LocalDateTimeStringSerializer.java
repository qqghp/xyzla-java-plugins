package com.xyzla.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xyzla.common.util.ZonedUtil;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * LocalDateTime 转 String
 */
public class LocalDateTimeStringSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(ZonedUtil.DTF_DTMS));
    }
}
