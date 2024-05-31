package com.pandora.www.httpToFtp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Config {

    public static String env;
    public static String userlogUrl;
    public static String startDay;
    public static String endDay;
    public static String ftpHost;
    public static int ftpPort;
    public static String ftpUsername;
    public static String ftpPassword;
    public static String httpFileSavePath;

    private static Logger LOG = LoggerFactory.getLogger(Config.class);

    static {
        Properties properties = new Properties();
//        env = "";
        env = System.getProperty("env");
        startDay = System.getProperty("startDay");
        endDay = System.getProperty("endDay");

        LOG.info("environment:" + env);

        try {
            switch (env) {
                case "prod":
                    properties.load(Config.class.getClassLoader().getResourceAsStream("prod/config.properties"));
                    break;
                case "test":
                    properties.load(Config.class.getClassLoader().getResourceAsStream("test/config.properties"));
                    break;
            }
            userlogUrl = properties.getProperty("usereventlog.history.url");
            ftpHost = properties.getProperty("ftp.host");
            ftpPort = Integer.parseInt(properties.getProperty("ftp.port"));
            ftpUsername = properties.getProperty("ftp.username");
            ftpPassword = properties.getProperty("ftp.password");
            httpFileSavePath = properties.getProperty("httpFile.savePath");

        } catch (Exception e) {
            LOG.error("init properties failure:" + e.getMessage());
            e.printStackTrace();
        }
    }


}
