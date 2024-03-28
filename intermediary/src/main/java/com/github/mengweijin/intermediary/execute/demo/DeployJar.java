package com.github.mengweijin.intermediary.execute.demo;

import com.github.mengweijin.intermediary.execute.AbstractExecute;
import com.github.mengweijin.intermediary.tool.MavenTool;
import com.github.mengweijin.intermediary.tool.SftpTool;
import com.github.mengweijin.intermediary.tool.SshTool;
import com.github.mengweijin.intermediary.util.SshMonitor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.extra.ssh.Connector;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author mengweijin
 */
@Slf4j
public class DeployJar extends AbstractExecute {

    public DeployJar(Connector connector) {
        super(connector);
    }

    String[] MAVEN_BUILD_CMD = new String[]{
            "cmd /C D:",
            "cd D:\\code\\vitality",
            "mvn clean package -Dmaven.test.skip=true -Ph2",
    };

    File MAVEN_BUILD_TARGET_FILE = FileUtil.file("D:\\code\\vitality\\vitality-admin\\target\\vitality-admin-1.4.1-SNAPSHOT.jar");

    String UPLOAD_DIR = "/opt/vitality/deploy_jar_" + TimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd_HH_mm");

    String[] DEPLOY_CMD = new String[]{
            "cd /opt/vitality",
            "./app.sh stop",
            "rm -rf app.jar",
            "cp " + UPLOAD_DIR + "/vitality-admin-1.4.1-SNAPSHOT.jar /opt/vitality/app.jar",
            "./app.sh start",
    };

    String[] MAVEN_CLEAN_CMD = new String[]{
            "cmd /C D:",
            "cd D:\\code\\vitality",
            "mvn clean",
    };

    @Override
    protected void run() {
        MavenTool.build(MAVEN_BUILD_CMD);

        SftpTool.upload(sftp, MAVEN_BUILD_TARGET_FILE, UPLOAD_DIR);

        SshTool.exec(session, DEPLOY_CMD);

        MavenTool.clean(MAVEN_CLEAN_CMD);

        SshMonitor.execAndMonitor(session.getRaw(), "cd /opt/vitality/logs/debug && tail -f debug.log -n 500");
    }
}
