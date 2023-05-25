package com.xyzla;

import com.xyzla.common.csv.CsvUtil;
import com.xyzla.common.util.JacksonUtil;

import java.util.List;

public class CsvUtilTest {
    public static void main(String[] args) {
        String csvPath = "/tmp/aa.csv";
        int skip = 0;
        int limit = 1;
        List<String[]> rows;
        while ((rows = CsvUtil.readSkipLimit(csvPath, skip, limit)).size() > 0) {
            System.out.println(skip + "  " + limit + " " + JacksonUtil.toJson(rows));
            skip = skip + limit;
        }
    }
}
