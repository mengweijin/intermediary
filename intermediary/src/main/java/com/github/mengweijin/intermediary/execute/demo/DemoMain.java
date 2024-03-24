package com.github.mengweijin.intermediary.execute.demo;

import com.github.mengweijin.intermediary.util.SshMonitor;
import com.jcraft.jsch.Session;
import org.dromara.hutool.extra.ssh.Connector;
import org.junit.jupiter.api.Test;

/**
 * @author mengweijin
 */
public class DemoMain {

    Connector connector = Connector.of("127.0.0.1", 22, "root", "root");

    @Test
    public void deployJar() {
        new DemoDeployJar(connector).execute();
    }

    @Test
    public void deployVue() {
        new DemoDeployVue(connector).execute();
    }


    @Test
    public void monitor() {
        try (DemoDeployJar demo = new DemoDeployJar(connector)) {
            Session session = demo.getSession().getRaw();
            // "pwd" 命令本身不会阻塞终端，所以运行到这里，完成后会终止程序。
            SshMonitor.execAndMonitor(session, "pwd");
            // "tail -fn" 命令本身就会阻塞终端，持续监控终端输出，所以运行到这里，即使没有新的日志，也会持续监控，不会终止程序。
            SshMonitor.execAndMonitor(session, "cd /opt/vitality/logs && tail -f info.log -n 500");
        }
    }

}