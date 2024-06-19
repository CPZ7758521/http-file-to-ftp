package com.pandora.www.httpToFtp.httpToFtp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class FtpUtil {

    private static Logger LOG = LoggerFactory.getLogger(FtpUtil.class);

    private static FTPClient getFTPClient(Ftp f) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(f.getIpAddr(), f.getPort());

            ftpClient.login(f.getUserName(), f.getPwd());

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                LOG.error("ftp服务器未连接成功！");
            } else {
                LOG.info("ftp服务器链接成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ftpClient;
    }

    public static void download(Ftp f, String ftpPath, String localPath, String fileName) throws IOException {
        FTPClient ftpClient = null;
        try {
            ftpClient = getFTPClient(f);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalActiveMode();
            ftpClient.changeWorkingDirectory(ftpPath);
            LOG.info("找到ftp路径，并成功切换到该路径！");

            File localFilePath = new File(localPath);
            if (!localFilePath.exists()) {
                localFilePath.mkdir();
            }

            File localFile = new File(localPath + fileName);
            FileOutputStream fos = new FileOutputStream(localFile);
            ftpClient.retrieveFile(fileName, fos);
            fos.close();
            ftpClient.logout();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ftpClient.logout();
        }
    }

}
