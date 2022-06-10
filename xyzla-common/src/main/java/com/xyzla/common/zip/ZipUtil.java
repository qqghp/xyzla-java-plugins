package com.xyzla.common.zip;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

//https://www.jianshu.com/p/1535d3b13236
@Service
public class ZipUtil {

    private static final int BUFFER = 8192;

    /**
     * 压缩单个文件 不推荐
     */
    public static void zipFile(String filePath, String zipPath) {
        try {
            File file = new File(filePath);
            File zipFile = new File(zipPath);
            InputStream input = new FileInputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            int temp = 0;
            while ((temp = input.read()) != -1) {
                zipOut.write(temp);
            }
            input.close();
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void compress(String srcPath, String dstPath) throws IOException {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcPath + "不存在！");
        }

        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try {
            out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(out, new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            compress(srcFile, zipOut, baseDir);
        } finally {
            if (null != zipOut) {
                zipOut.close();
                out = null;
            }

            if (null != out) {
                out.close();
            }
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }
        } finally {
            if (null != bis) {
                bis.close();
            }
        }
    }


    public static void decompress(String zipFile, String dstPath) throws IOException {
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            }
        }
        zip.close();
    }


    public static void main(String[] args) throws Exception {
        String targetFolderPath = "/Users/ryan/workspace/CARRIER_CM_M2M_XA";
        String rawZipFilePath = "/Users/ryan/workspace/raw.zip";
        String newZipFilePath = "/Users/ryan/workspace/new.zip";

//        //将Zip文件解压缩到目标目录
//        new ZipService().decompress(rawZipFilePath, targetFolderPath);

        //将目标目录的文件压缩成Zip文件
//        new ZipService().compress(targetFolderPath, newZipFilePath);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        new ZipUtil().zipFile("/Users/ryan/workspace/CARRIER_CM_M2M_XA/xian/2929222165_202105_20210521_6.txt", "/Users/ryan/workspace/2929222165_202105_20210521_6.zip");
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
        StopWatch stopWatch2 = new StopWatch();
        stopWatch2.start();
        new ZipUtil().compress("/Users/ryan/workspace/CARRIER_CM_M2M_XA/xian/2929222165_202105_20210521_6.txt", "/Users/ryan/workspace/2929222165_202105_20210521_6_2.zip");
        stopWatch2.stop();
        System.out.println(stopWatch2.getTotalTimeMillis());
    }
}
