package com.pandora.www.httpToFtp.httpToFtp;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Ftp ftp = new Ftp();
        ftp.setIpAddr("127.0.0.1");
        ftp.setPort(2121);
        ftp.setUserName("root");
        ftp.setPwd("root");

        String ftpPath = "/root/log/";
        String localPath = "E:\\";
        String fileName = "eventlog.log";
        FtpUtil.download(ftp, ftpPath, localPath, fileName);
    }
}
