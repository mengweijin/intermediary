package com.github.mengweijin.intermediary.execute.demo;

import com.github.mengweijin.intermediary.execute.AbstractExecute;
import com.github.mengweijin.intermediary.tool.MavenTool;
import com.github.mengweijin.intermediary.tool.SftpTool;
import com.github.mengweijin.intermediary.tool.SshTool;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.compress.ZipUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.extra.ssh.Connector;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author mengweijin
 */
@Slf4j
public class DeployVue extends AbstractExecute {

    public DeployVue(Connector connector) {
        super(connector);
    }

    String[] NPM_BUILD_CMD = new String[]{
            "cmd /c D:",
            "cd D:\\code\\vitality\\vitality-ui",
            "npm install",
            "npm run build:prod",
    };

    public File getNpmBuildTargetFile() {
        return ZipUtil.zip("D:\\code\\vitality\\vitality-ui\\dist");
    }

    String UPLOAD_DIR = "/opt/vitality/deploy_vue_" + TimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd_HH_mm");


    String[] DEPLOY_CMD = new String[]{
            "cd " + UPLOAD_DIR,
            "rm -rf /usr/local/nginx/html",
            "unzip -O CP936 -d /usr/local/nginx/html dist.zip",
            "cd /usr/local/nginx/sbin/",
            "./nginx -s reload",
    };

    @Override
    public void run() {
        MavenTool.build(NPM_BUILD_CMD);

        File zipFile = this.getNpmBuildTargetFile();
        SftpTool.upload(sftp, zipFile, UPLOAD_DIR);

        SshTool.exec(session, DEPLOY_CMD);

        FileUtil.del(zipFile);
    }

}
