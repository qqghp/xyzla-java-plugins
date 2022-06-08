package com.xyzla.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public final class Base64Util {

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static String encode(String text) throws UnsupportedEncodingException {
        return encoder.encodeToString(text.getBytes("UTF-8"));
    }

    public static String decode(String encodedText) throws UnsupportedEncodingException {
        return new String(decoder.decode(encodedText), "UTF-8");
    }
}
