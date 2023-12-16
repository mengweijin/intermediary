package com.github.mengweijin.server.tool;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author mengweijin
 */
@Slf4j
public abstract class AbstractDeploy {
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
            log.info("upload file: " + this.srcFilePath());
            sftp.put(this.srcFilePath(), uploadDir, new SftpMonitor(), Sftp.Mode.OVERWRITE);

            String command = String.join(" && ", this.serverCmd());
            log.info("exec server command: " + command);
            JschUtil.exec(session, command, StandardCharsets.UTF_8);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        } finally {
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

    public abstract String[] buildCmd();

    public abstract String srcFilePath();

    public abstract String[] serverCmd();
}
