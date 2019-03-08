package com.waterfairy.videoplayer.tool;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/2/20 15:00
 * @info:
 */
public class CacheProxy {

    public final Config config;

    public CacheProxy(Config config) {
        this.config = config;
    }


    public boolean isCacheAble() {
        return config.cacheAble;
    }

    public String getCachePath() {
        return config.cachePath;
    }

    public boolean isUserExtension() {
        return config.userExtension;
    }

    public static class Builder {
        /**
         * 是否可以缓存
         */
        private boolean cacheAble = true;
        private String cachePath;
        private boolean userExtension;


        public Builder cacheAble(boolean cacheAble) {
            this.cacheAble = cacheAble;
            return this;
        }

        public Builder cachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        public Builder userExtension(boolean userExtension) {
            this.userExtension = userExtension;
            return this;
        }


        public CacheProxy build() {
            return new CacheProxy(new Config(cacheAble, cachePath, userExtension));
        }
    }

    private static class Config {
        public boolean cacheAble = true;
        public String cachePath;
        public boolean userExtension;

        public Config(boolean cacheAble, String cachePath, boolean userExtension) {
            this.cacheAble = cacheAble;
            this.cachePath = cachePath;
            this.userExtension = userExtension;
        }
    }
}
