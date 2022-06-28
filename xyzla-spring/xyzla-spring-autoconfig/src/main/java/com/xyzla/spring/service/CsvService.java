package com.xyzla.spring.service;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class CsvService {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    @Async
    public void write(String filePath, String[] data) throws IOException {
        LocalDate localDate = LocalDate.now();
        Path path = Paths.get(filePath);
        logger.debug("csv path {}", path);

        // 判断文件若不存在则创建 并写入表头
        if (!Files.exists(path)) {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
        }

        // default all fields are enclosed in double quotes
        // default separator is a comma
        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toString(), true))) {
            writer.writeNext(data);
        }
    }

    /**
     * 从 第一行 读取 csv
     *
     * @param csvPath csv 文件地址
     * @return 返回 @{List<String[]>}
     */
    public List<String[]> readAllFromFirstLine(String csvPath) {
        return readAll(csvPath, 0);
    }

    /**
     * 从 第二行 读取 csv
     *
     * @param csvPath csv 文件地址
     * @return 返回 @{List<String[]>}
     */
    public List<String[]> readAllFromSecondLine(String csvPath) {
        return readAll(csvPath, 1);
    }

    /**
     * 读取 csv
     *
     * @param csvPath csv 文件地址
     * @return 返回 @{List<String[]>}
     */
    public List<String[]> readAll(String csvPath, int fromRow) {
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

    public List<String[]> read(char sepator, String path) throws IOException, CsvException {
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
}
