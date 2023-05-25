package com.xyzla.common.csv;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    // FEFF because this is the Unicode char represented by the UTF-8 byte order mark (EF BB BF).
    public static final String UTF8_BOM = "\uFEFF";

    /**
     * CSV: 只读取 第一行
     *
     * @param csvPath
     * @return
     */
    public static String[] readFirstRow(String csvPath) {
        Path myPath = Paths.get(csvPath);
        CSVParser parser = new CSVParserBuilder().withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();

        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8);
             CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {
            String[] columns = reader.readNext();
            columns[0] = removeUTF8BOM(columns[0]);
            return columns;
        } catch (IOException ex) {
            logger.error("io exception", ex);
        } catch (CsvException ex) {
            logger.error("csv exception", ex);
        }
        return null;
    }

    /**
     * https://www.rgagnon.com/javadetails/java-handle-utf8-file-with-bom.html
     *
     * @param s
     * @return
     */
    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    /**
     * CSV: 从 第一行 读取
     *
     * @param csvPath csv 文件地址
     * @return 返回 @{List<String[]>}
     */
    public static List<String[]> readAllFromFirstRow(String csvPath) {
        return readAll(csvPath, 0);
    }

    /**
     * CSV: 从 第二行 读取
     *
     * @param csvPath csv 文件地址
     * @return 返回 @{List<String[]>}
     */
    public static List<String[]> readAllFromSecondRow(String csvPath) {
        return readAll(csvPath, 1);
    }

    /**
     * CSV: 读取
     *
     * @param csvPath csv 文件地址
     * @return 返回 @{List<String[]>}
     */
    public static List<String[]> readAll(String csvPath, int fromRow) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("csv path {}", csvPath);
        List<String[]> allData = null;
        try {
            // Create an object of file reader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(csvPath);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(fromRow)
                    .build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            logger.error("error: {}", e);
        }
        return allData;
    }


    /**
     * 分批 读取 csv 文件
     *
     * @param csvPath
     * @param skip
     * @param limit
     * @return
     */
    public static List<String[]> readSkipLimit(String csvPath, int skip, int limit) {
        List<String[]> rows = new ArrayList<>();
        Path myPath = Paths.get(csvPath);
        CSVParser parser = new CSVParserBuilder().withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8);
             CSVReader reader = new CSVReaderBuilder(br).withSkipLines(skip).withCSVParser(parser).build()) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                rows.add(row);
                // 当 list 到达单批次最大数量后 直接返回
                if (rows.size() == limit) {
                    return rows;
                }
            }
        } catch (IOException ex) {
            logger.error("io exception", ex);
        } catch (CsvException ex) {
            logger.error("csv exception", ex);
        }
        return rows;
    }

    public static List<String[]> read(char sepator, String path) throws IOException, CsvException {
        Path myPath = Paths.get(path);
        CSVParser parser = new CSVParserBuilder().withSeparator(sepator).build();
        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8);
             CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {

            List<String[]> rows = reader.readAll();

//            for (String[] row : rows) {
//                for (String e : row) {
//                    if (Strings.isNullOrEmpty(e)) {
//                        System.out.format("- ");
//                    } else {
//                        System.out.format("%s ", e);
//                    }
//                }
//                System.out.println();
//            }

            return rows;
        }
    }


    /**
     * CSV: 一次写入一行
     *
     * @param filePath
     * @param columns
     */
    public static void writeRow(String filePath, String[] columns) {
        Path path = Paths.get(filePath);
        logger.debug("csv path {}", path);

        // 判断文件若不存在则创建 并写入表头
        if (!Files.exists(path)) {
            if (!Files.exists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException ex) {
                    logger.error("create directory exception.", ex);
                }
            }
        }

        // default all fields are enclosed in double quotes
        // default separator is a comma
        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toString(), true))) {
            writer.writeNext(columns);
        } catch (IOException ex) {
            logger.error("write exception.", ex);
        }
    }

    /**
     * CSV: 一次写入多行
     *
     * @param filePath
     * @param rows
     * @throws IOException
     */
    public static void writeRows(String filePath, List<String[]> rows) {
        Path path = Paths.get(filePath);
        logger.debug("csv path {}", path);

        // 判断文件若不存在则创建 并写入表头
        if (!Files.exists(path)) {
            if (!Files.exists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException ex) {
                    logger.error("create directory exception.", ex);
                }
            }
        }

        // default all fields are enclosed in double quotes
        // default separator is a comma
        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toString(), true))) {
            writer.writeAll(rows);
        } catch (IOException ex) {
            logger.error("write exception.", ex);
        }
    }
}
