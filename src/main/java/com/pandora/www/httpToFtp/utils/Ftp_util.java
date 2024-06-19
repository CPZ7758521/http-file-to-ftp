package com.pandora.www.httpToFtp.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Ftp_util {
    private Logger LOG = LoggerFactory.getLogger(Ftp_util.class);
    public boolean uploadFile(String host, int port, String username, String password, String basePath, String filePath) {
        boolean result = false;

        FTPClient ftp = new FTPClient();

        try {
            int reply;
            ftp.connect(host, port);

            boolean login = ftp.login(username, password);

            if (login) {
                LOG.info("ftp 登录成功！");
            }

            //设置被动模式
            ftp.enterLocalPassiveMode();

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }

            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                String[] dirs = filePath.split("/");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
