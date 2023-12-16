package com.github.mengweijin.server.tool.demo;

import com.github.mengweijin.server.tool.AbstractDeploy;
import com.github.mengweijin.server.tool.Config;
import com.github.mengweijin.server.tool.SshMonitor;

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
        try (AbstractDeploy deploy = new DemoDeployJar(config())) {
            deploy.run();
        }
    }

    public static void deployVue() {
        try (AbstractDeploy deploy = new DemoDeployVue(config())) {
            deploy.run();
        }
    }

    public static void monitor() {
        try (AbstractDeploy deploy = new DemoDeployJar(config())) {
            // "pwd" 命令本身不会阻塞终端，所以运行到这里，完成后会终止程序。
            SshMonitor.execAndMonitor(deploy.getSession(), "pwd");
            // "tail -fn" 命令本身就会阻塞终端，持续监控终端输出，所以运行到这里，即使没有新的日志，也会持续监控，不会终止程序。
            SshMonitor.execAndMonitor(deploy.getSession(), "cd /ufc/logs && tail -fn 500 info.log");
        }
    }
}
