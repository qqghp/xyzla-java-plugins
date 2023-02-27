package com.xyzla.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.xyzla.common.util.ZonedUtil;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Long 转 LocalDateTime
 */
public class StringLocalDateTimeDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return LocalDateTime.parse(p.getValueAsString(), ZonedUtil.DTF_DTMS);
    }
}
