package com.github.mengweijin.server.tool.demo;

import com.github.mengweijin.server.tool.AbstractDeploy;
import com.github.mengweijin.server.tool.Config;
import lombok.extern.slf4j.Slf4j;

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
                "mvn clean package -Pdev",
        };
    }

    @Override
    public String srcFilePath() {
        return "D:\\code\\vitality\\target\\vitality-admin.jar";
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
}
