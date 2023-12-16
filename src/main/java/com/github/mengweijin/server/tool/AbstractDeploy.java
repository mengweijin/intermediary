package com.github.mengweijin.server.tool;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.Session;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author mengweijin
 */
@Slf4j
@Getter
public abstract class AbstractDeploy implements Closeable {
    protected Config config;
    protected String workdir;
    protected String uploadDir;
    public Session session;
    public Sftp sftp;


    public AbstractDeploy(Config config) {
        this.config = config;
        this.workdir = config.getWorkdir();
        uploadDir = config.getWorkdir() + "/upload_" + LocalDateTimeUtil.format(LocalDateTime.now(), "yyyy-MM-dd_HH_mm_ss");
        this.session = JschUtil.createSession(config.getHost(), config.getPort(), config.getUser(), config.getPassword());
        this.sftp = JschUtil.createSftp(session);
    }

    public void run() {
        try {
            log.info("build......");
            ProcessUtils.exec(this.buildCmd());

            log.info("make upload dir: " + uploadDir);
            sftp.mkDirs(uploadDir);

            //进入远程上传目录
            log.info("cd upload dir: " + uploadDir);
            sftp.cd(uploadDir);

            //上传本地文件
            log.info("upload file: " + this.srcFile().getAbsolutePath());
            sftp.put(this.srcFile().getAbsolutePath(), uploadDir, new SftpMonitor(), Sftp.Mode.OVERWRITE);

            String command = String.join(" && ", this.serverCmd());
            log.info("exec server command: " + command);
            JschUtil.exec(session, command, StandardCharsets.UTF_8);

            if(monitorCmd() != null && monitorCmd().length > 0) {
                log.info("monitor command: " + command);
                SshMonitor.execAndMonitor(session, String.join(" && ", this.monitorCmd()));
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        } finally {
            this.close();
        }
    }

    public abstract String[] buildCmd();

    public abstract File srcFile();

    public abstract String[] serverCmd();

    public abstract String[] monitorCmd();

    @Override
    public void close() {
        if(sftp != null){
            log.info("close sftp......");
            sftp.close();
        }
        if(sftp != null){
            log.info("close session......");
            session.disconnect();
        }
    }
}
