package com.pandora.www.httpToFtp;

import com.pandora.www.httpToFtp.config.Config;
import com.pandora.www.httpToFtp.utils.Ftp_util;
import com.pandora.www.httpToFtp.utils.Http_to_tmp_file;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application {
    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException {
        LOG.info("<--------程序开始运行--------------->");
        LOG.info("<--------开始运行Http用户数据文件下载--------------->");
        Http_to_tmp_file http_to_tmp_file = new Http_to_tmp_file();

        boolean downloadHttpSuccess = http_to_tmp_file.download_from_http(Config.startDay, Config.endDay, Config.httpFileSavePath);
        if (downloadHttpSuccess) {
            LOG.info("<----------------Http用户数据文件下载完成----------------->");
            LOG.info("<----------------开始运行上传用户数据文件到ftp远程文件系统----------------->");
            Ftp_util ftp_util = new Ftp_util();
            String host = Config.ftpHost;
            int port = Config.ftpPort;
            String username = Config.ftpUsername;
            String password = Config.ftpPassword;

            String basePath = "/db/log/20220929";
            String filePath = "/20221026";

            ftp_util.uploadFile(host, port, username, password, basePath, filePath, Config.httpFileSavePath);
            LOG.info("<-------------上传用户数据文件到ftp远程文件系统完成！----------->");
            LOG.info("<-------------程序运行结束----------->");
        } else {
            LOG.info("HTTP下载失败！未运行上传ftp！");
        }
    }
}
