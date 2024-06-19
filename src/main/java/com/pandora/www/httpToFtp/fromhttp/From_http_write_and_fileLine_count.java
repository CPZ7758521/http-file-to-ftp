package com.pandora.www.httpToFtp.fromhttp;

import com.pandora.www.httpToFtp.config.Config;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class From_http_write_and_fileLine_count {

    private static Logger LOG = LoggerFactory.getLogger(From_http_write_and_fileLine_count.class);

    public static void main(String[] args) throws IOException {
        LOG.info("文件中共有：" + getFileLineCount() + "个文件。");
    }

    public static int getFileLineCount() throws IOException {

        int num = 0;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(Config.userlogUrl);

        String savePath = "";
        String fileName = "";

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        FileWriter fw = new FileWriter(savePath + fileName, true);

        BufferedWriter bw = new BufferedWriter(fw);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        InputStream inputStream = entity.getContent();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.matches(".*/eventlog/.*")) {
                LOG.info(line);
                num ++;
                bw.write(line);
                bw.newLine();
            }
        }
        bw.flush();
        fw.flush();

        bw.close();
        fw.close();

        return num;
    }
}
