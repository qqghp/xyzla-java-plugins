package com.xyzla.common.zip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

// https://blog.csdn.net/qq_24499615/article/details/84728313
// 解决 中文 解析失败 https://blog.csdn.net/u010018421/article/details/53222291
public class ZipFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);

    /**
     * 通过 zip 文件的存储位置 直接读取 zip 文件
     *
     * @param file
     * @throws Exception
     */
    public static List<String> readZipFile(String file) throws Exception {
        List<String> list = new ArrayList<String>();
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        // Charset.forName("UTF-8"));   StandardCharsets.UTF_8
        // Charset.forName("GBK")
        ZipInputStream zin = new ZipInputStream(in, Charset.forName("UTF-8"));
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {
            } else {
                logger.info("zip file: {}, file size: {} bytes", ze.getName(), ze.getSize());
                long size = ze.getSize();
                if (size > 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    while ((line = br.readLine()) != null) {
                        list.add(line);
//                        if (logger.isDebugEnabled()) {
//                            logger.debug(line);
//                        }
                    }
                    br.close();
                }
            }
        }
        zin.closeEntry();
        logger.info("zip file: {}, total line: {}.", file, list.size());
        return list;
    }

    /**
     * 通过 zip 文件的流 直接 读取 zip 文件
     *
     * @param zipInputStream
     * @throws IOException
     */
    public static void readZipFile(ZipInputStream zipInputStream) throws IOException {
        while ((zipInputStream.getNextEntry()) != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zipInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
