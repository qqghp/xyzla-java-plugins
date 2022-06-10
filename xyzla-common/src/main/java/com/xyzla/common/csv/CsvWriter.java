package com.xyzla.common.csv;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvWriter {

    private static final Logger logger = LoggerFactory.getLogger(CsvWriter.class);

    @Async
    public void write(String filePath, String filePrefix, String[] headers, String[] data) throws IOException {
        Path path = Paths.get(filePath);
        logger.debug("csv path {}", path);

        // 判断文件若不存在则创建 并写入表头
        if (!Files.exists(path)) {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            try (CSVWriter writer = new CSVWriter(new FileWriter(path.toString()))) {
                writer.writeNext(headers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // default all fields are enclosed in double quotes
        // default separator is a comma
        try (CSVWriter writer = new CSVWriter(new FileWriter(path.toString(), true))) {
            writer.writeNext(data);
        }
    }


}
