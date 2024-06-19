package com.pandora.www.httpToFtp.fromhttp;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class From_http {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String userlogUrl = "http://127.0.0.1:9988";
        HttpGet httpGet = new HttpGet(userlogUrl + "/exstatic/eventlog/" + "/eventlog-" + "20220928" + ".log");

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        InputStream contentInputstream = entity.getContent();


        byte[] getData = readInputStream(contentInputstream);

        String data = new String(getData);

        String datas = StringEscapeUtils.unescapeHtml(data);

        String savePath = "E:\\";
        String fileName = "eventlog" + "20220928" + "cpz.log";

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }

        File file = new File(saveDir + File.separator + fileName);

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter fow = new OutputStreamWriter(fos);
        fow.write(datas);

        if (fow != null) {
            fow.close();
        }

        if (fos != null) {
            fos.close();
        }

        System.out.println("info:" + userlogUrl + " download success");


    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
