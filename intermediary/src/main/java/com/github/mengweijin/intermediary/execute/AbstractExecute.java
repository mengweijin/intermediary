package com.github.mengweijin.intermediary.execute;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.extra.ssh.Connector;
import org.dromara.hutool.extra.ssh.engine.jsch.JschSession;
import org.dromara.hutool.extra.ssh.engine.jsch.JschSftp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
@Slf4j
@Getter
public abstract class AbstractExecute implements IExecute {

    protected Connector connector;

    protected JschSession session;

    protected JschSftp sftp;

    public AbstractExecute(Connector connector) {
        this.connector = connector;
        this.session = new JschSession(connector);
        this.sftp = this.session.openSftp(StandardCharsets.UTF_8);
    }

    public abstract void run();

    @Override
    public void execute() {
        try {
            this.run();
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        } finally {
            this.close();
        }
    }

    @Override
    public void close() {
        if(this.sftp != null) {
            this.sftp.close();
        }
        if(this.session != null) {
            try {
                this.session.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
}
