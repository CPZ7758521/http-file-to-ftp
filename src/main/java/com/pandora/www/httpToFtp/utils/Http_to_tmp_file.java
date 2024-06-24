package com.pandora.www.httpToFtp.utils;

import com.pandora.www.httpToFtp.config.Config;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Http_to_tmp_file {
    String userlogUrl = Config.userlogUrl;
    private Logger LOG = LoggerFactory.getLogger(Http_to_tmp_file.class);

    public boolean download_from_http(String startDay, String endDay, String savePath) throws IOException {
        boolean result = false;
        deleteFile(savePath);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(startDay, df);
        LocalDate endDate = LocalDate.parse(endDay, df);
        LocalDate todayDate = LocalDate.now();

        long dateDiff = endDate.toEpochDay() - startDate.toEpochDay();
        long todayDiff = todayDate.toEpochDay() - endDate.toEpochDay();

        if (dateDiff <= 0 || todayDiff <= 0) {
            LOG.error("结束日期必须小于当前日期，大于当前日期是没有文件的，请传入正确的下载日期！");
            return result;
        } else {
            for (long i = 0; i < dateDiff; i++) {
                LOG.info("正在下载" + startDay + ".log");

                CloseableHttpClient httpClient = HttpClients.createDefault();

                String fileName = "/eventlog-" + startDay + ".log";

                HttpGet httpGet = new HttpGet(userlogUrl + "/eventlog" + fileName);

                InputStream content = httpClient.execute(httpGet).getEntity().getContent();

                byte[] getData = readInputStreamToByte(content);

                //文件保存位置
                File saveDir = new File(savePath);
                if (!saveDir.exists()) {
                    saveDir.mkdir();
                }
                File file = new File(saveDir + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(file);

                fos.write(getData);
                fos.close();
                content.close();
                LOG.info("下载：" + startDay + "文件 下载成功！");

                startDate = startDate.plusDays(1L);
                startDay = startDate.format(df);
            }

            LOG.info("共\t" + dateDiff + "\t 天");
            result = true;
            return result;
        }
    }

    public byte[] readInputStreamToByte(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private void deleteFile(String filePath) throws IOException {
        //delete local file
        File dir = new File(filePath);
        FileUtils.deleteDirectory(dir);
    }
}
