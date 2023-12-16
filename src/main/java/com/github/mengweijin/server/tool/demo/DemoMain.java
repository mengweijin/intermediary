package com.github.mengweijin.server.tool.demo;

import com.github.mengweijin.server.tool.AbstractDeploy;
import com.github.mengweijin.server.tool.Config;

/**
 * @author mengweijin
 */
public class DemoMain {

    public static Config config() {
        return Config.builder()
                .workdir("/opt/app")
                .host("127.0.0.1")
                .port(22)
                .user("root")
                .password("root")
                .build();
    }

    public static void main(String[] args) {
        deployJar();
        deployVue();
    }

    public static void deployJar() {
        AbstractDeploy deploy = new DemoDeployJar(config());
        deploy.run();
    }

    public static void deployVue() {
        AbstractDeploy deploy = new DemoDeployVue(config());
        deploy.run();
    }
}
