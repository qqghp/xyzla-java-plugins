package com.xyzla.common.zip;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    /**
     * 统计文件中的 行数  ☆☆☆☆☆
     *
     * @param filePath
     * @return
     */
    public static int getFileLineNum(String filePath) {
        try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filePath))) {
            lineNumberReader.skip(Long.MAX_VALUE);
            int lineNumber = lineNumberReader.getLineNumber();
            return lineNumber + 1;//实际上是读取换行符数量 , 所以需要+1
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 统计文件中的行数
     *
     * @param filePath
     * @return
     */
    public static long getFileLineNum2(String filePath) {
        try {
            return Files.lines(Paths.get(filePath)).count();
        } catch (IOException e) {
            return -1;
        }
    }
}
