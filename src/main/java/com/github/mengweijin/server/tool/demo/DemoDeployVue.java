package com.github.mengweijin.server.tool.demo;

import cn.hutool.core.util.ZipUtil;
import com.github.mengweijin.server.tool.AbstractDeploy;
import com.github.mengweijin.server.tool.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author mengweijin
 */
@Slf4j
public class DemoDeployVue extends AbstractDeploy {

    public DemoDeployVue(Config config) {
        super(config);
    }

    @Override
    public String[] buildCmd() {
        return new String[]{
                "cmd /c D:",
                "cd D:\\code\\vitality\\vitality-ui",
                "npm install",
                "npm run build:prod",
        };
    }

    @Override
    public String srcFilePath() {
        File zip = ZipUtil.zip("D:\\code\\vitality\\vitality-ui\\dist");
        log.info("Zipped file " + zip.getAbsolutePath());
        return zip.getAbsolutePath();
    }

    @Override
    public String[] serverCmd() {
        return new String[]{
                "rm -rf /usr/local/nginx/html",
                "cd " + uploadDir,
                "unzip -O CP936 -d /usr/local/nginx/html dist.zip",
                "cd /usr/local/nginx/sbin/",
                "./nginx -s reload",
        };
    }
}
