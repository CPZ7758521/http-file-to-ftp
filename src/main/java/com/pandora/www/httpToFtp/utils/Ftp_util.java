package com.pandora.www.httpToFtp.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Ftp_util {
    private Logger LOG = LoggerFactory.getLogger(Ftp_util.class);
    public boolean uploadFile(String host, int port, String username, String password, String basePath, String filePath, String localPath) {
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

            //如果切换目录失败，说明没有该目录，需要创建目录。
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                String[] dirs = filePath.split("/");
                String tempPath = basePath;

                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) {
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            LOG.error("创建文件目录：\t" + tempPath + "失败!");
                            return result;
                        } else {
                            LOG.info("创建文件目录：\t" + tempPath + "成功！");
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            } else {
                LOG.info("要上传的路径已经存在，请谨慎操作，会覆盖文件。");
                return result;
            }

            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            File uploadFilePath = new File(localPath);
            File[] files = uploadFilePath.listFiles();
            for (File file : files) {
                String uploadFileName = file.getName();
                File fileAbsoluteFile = file.getAbsoluteFile();
                FileInputStream fis = new FileInputStream(fileAbsoluteFile);
                if (!ftp.storeFile(uploadFileName, fis)) {
                    return result;
                }
                fis.close();
                LOG.info("file" + uploadFileName + "上传成功！");
            }

            ftp.logout();
            result = true;

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

    public boolean downloadFile(String host, int port, String username, String password, String remotPath, String fileName, String localPath, boolean downloadPathALLFile) {
        boolean result = false;

        FTPClient ftp = new FTPClient();

        try {
            int reply;
            ftp.connect(host, port);
            boolean login = ftp.login(username, password);
            if (login) {
                LOG.info("登录成功！");
            }

            ftp.enterLocalPassiveMode();
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                LOG.info("断开链接");
                ftp.disconnect();
                return result;
            }

            if (ftp.changeWorkingDirectory(remotPath)) {
                LOG.info("切换路径成功！");
            } else {
                LOG.error("切换路径失败！remot path 错误：" + remotPath);
            }

            FTPFile[] ftpFiles = ftp.listFiles();
            if (downloadPathALLFile) {
                for (FTPFile ftpFile : ftpFiles) {
                    File localFile = new File(localPath + "/" + ftpFile.getName());
                    FileOutputStream fos = new FileOutputStream(localFile);

                    ftp.retrieveFile(ftpFile.getName(), fos);
                    fos.close();
                    LOG.info("下载成功！");
                }
            } else {
                for (FTPFile ftpFile : ftpFiles) {
                    if (ftpFile.getName().equals(fileName)) {
                        File localFile = new File(localPath + "/" + ftpFile.getName());
                        FileOutputStream fos = new FileOutputStream(localFile);

                        ftp.retrieveFile(ftpFile.getName(), fos);
                        fos.close();
                        LOG.info("下载成功！");
                    }
                }
            }

            ftp.logout();
            return result;

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

    public static void main(String[]args)throws FileNotFoundException {
        Ftp_util ftp_util = new Ftp_util();
        String host="10.89.114.31";
        int port = 2121;
        String username = "dtg";
        String password = "LTkSFXgH";
        String remotePath = "/taidb/log/20220929/";
        String fileName = null;
        String localPath = "E:/taikang_code/master-data-stream-process/myLocalProject/http-to-ftp/src/main/java/com/tk/outp";
        ftp_util.downloadFile(host,port,username,password,remotePath,fileName, localPath,true);
        String basePath = "/taidb/log/20220929/";
        String filePath = "20221025";
        String uploadFilePath = "E:/taikang_code/master-data-stream-process/myLocalProject/http-to-ftp/src/main/java/com/t";
        ftp_util.uploadFile(host,port,username,password,basePath,filePath,uploadFilePath);
    }

}
