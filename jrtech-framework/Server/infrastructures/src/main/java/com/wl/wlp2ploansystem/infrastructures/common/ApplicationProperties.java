package com.wl.wlp2ploansystem.infrastructures.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Application-*.yml映射类
 */
@ConfigurationProperties(prefix = "my", ignoreUnknownFields = false)
@Configuration
public class ApplicationProperties {

    /**
     * 应用程序名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 登陆验证码配置
     */
    private final Kaptcha kaptcha = new Kaptcha();

    /**
     * 文件上传配置
     */
    private final Fileupload fileupload = new Fileupload();

    /**
     * 数据库备份配置
     */
    private final Dbbak dbbak = new Dbbak();


    /**
     * 安全配置
     */
    private final Security security = new Security();


    /**
     * Cors 配置
     */
    private final CorsConfiguration cors = new CorsConfiguration();


    /***
     * UEditor编辑器文件上传设置
     */
    private final Ueditor ueditor = new Ueditor();

    public Kaptcha getKaptcha() {
        return kaptcha;
    }
    public  Fileupload getFileupload() { return  fileupload; }
    public  Dbbak getDbbak() { return  dbbak; }
    public Security getSecurity() {
        return this.security;
    }
    public CorsConfiguration getCors() {
        return this.cors;
    }

    public Ueditor getUeditor() {
        return ueditor;
    }
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public static class Security {
        private final Authentication authentication = new Authentication();

        public Security() {
        }

        public Authentication getAuthentication() {
            return this.authentication;
        }

        public static class Authentication {

            private final Jwt jwt = new Jwt();

            public Authentication() {
            }

            public Jwt getJwt() {
                return this.jwt;
            }

            public static class Jwt {
                private String secret;
                private int tokenValidityInSeconds = 1800;

                public Jwt() {
                }

                public String getSecret() {
                    return this.secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public int getTokenValidityInSeconds() {
                    return this.tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(int tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }
            }

        }

    }
    public static class Kaptcha {

        private Boolean enabled;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean value) {
            this.enabled = value;
        }
    }
    public static class Fileupload {
        private String imagePath;
        private String filePath;

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String value) {
            this.imagePath = value;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String value) {
            this.filePath = value;
        }
    }
    public static class Dbbak {
        private String filePath;

        private String script;


        public String getScript() {
            return script;
        }

        public void setScript(String script) {
            this.script = script;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String value) {
            this.filePath = value;
        }
    }

    public static class Ueditor {

        /**
         * config.json的文件存放地址
         */
        private String config;
        /**
         * 是否同统一上传地址：图片上传地址，视频上传地址...
         */
        private boolean unified;
        /**
         * 文件上传路径
         */
        private String uploadPath;
        /**
         * 文件url前缀
         */
        private String urlPrefix;

        public String getConfig() {
            return config;
        }

        public void setConfig(String config) {
            this.config = config;
        }

        public String getUploadPath() {
            return uploadPath;
        }

        public void setUploadPath(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public String getUrlPrefix() {
            return urlPrefix;
        }

        public void setUrlPrefix(String urlPrefix) {
            this.urlPrefix = urlPrefix;
        }

        public boolean getUnified() {
            return unified;
        }

        public void setUnified(boolean unified) {
            this.unified = unified;
        }
    }
}