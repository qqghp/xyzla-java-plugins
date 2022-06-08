package com.xyzla.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

public class HashUtil {

    private static final Logger logger = LoggerFactory.getLogger(HashUtil.class);

    public static void sha512Hex(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(MessageFormat.format("File not found. {0}", file.toString()));
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        String hex = DigestUtils.sha512Hex(fileInputStream);
        // String hex = new DigestUtils(SHA_512).digestAsHex(file);
        if (logger.isDebugEnabled()) {
            logger.debug("File Path: {}. sha512 hex: {}", file.toString(), hex);
        }
        System.out.println(hex);
    }

    public static String sha1Hex(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(MessageFormat.format("File not found. {0}", file.toString()));
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        String hex = DigestUtils.sha1Hex(fileInputStream);
        //                String hex = new DigestUtils(SHA_1).digestAsHex(file);
        if (logger.isDebugEnabled()) {
            logger.debug("File Path: {}, sha1 hex: {}", file.toString(), hex);
        }
        return hex;
    }

    public static String md5(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(MessageFormat.format("File not found. {0}", file.toString()));
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        String hex = DigestUtils.md5Hex(fileInputStream);
        if (logger.isDebugEnabled()) {
            logger.debug("File Path: {}, md5 hex: {}", file.toString(), hex);
        }
        return hex;
    }
}
