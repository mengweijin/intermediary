package com.github.mengweijin.server.tool.demo;

import cn.hutool.core.io.FileUtil;
import com.github.mengweijin.server.tool.AbstractDeploy;
import com.github.mengweijin.server.tool.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author mengweijin
 */
@Slf4j
public class DemoDeployJar extends AbstractDeploy {

    public DemoDeployJar(Config config) {
        super(config);
    }

    @Override
    public String[] buildCmd() {
        return new String[]{
                "cmd /c D:",
                "cd D:\\code\\vitality",
                "mvn clean package -Dmaven.test.skip=true -Pdev",
        };
    }

    @Override
    public File srcFile() {
        String path = "D:\\code\\vitality\\target\\vitality-admin.jar";
        return FileUtil.file(path);
    }

    @Override
    public String[] serverCmd() {
        return new String[]{
                "cd " + workdir,
                "./app.sh stop",
                "rm -rf app.jar",
                "cd " + uploadDir,
                "cp vitality-admin.jar " + workdir + "/app.jar",
                "cd " + workdir,
                "./app.sh start",
        };
    }

    @Override
    public void cleanDeployedTempFile() {

    }

    @Override
    public String[] monitorCmd() {
        return new String[]{
                "cd " + workdir,
                "tail -f ./logs/info.log -n 500",
        };
    }
}
