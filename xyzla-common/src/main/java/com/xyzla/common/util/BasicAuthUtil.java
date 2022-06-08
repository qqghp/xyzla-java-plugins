package com.xyzla.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class BasicAuthUtil {

    private static final Logger logger = LoggerFactory.getLogger(BasicAuthUtil.class);

    public static String generateBasic(String username, String password) throws UnsupportedEncodingException {
        String base64 = Base64Util.encode(username + ":" + password);
        if (logger.isDebugEnabled()) {
            logger.debug("base64: {}", base64);
        }
        return base64;
    }
}
