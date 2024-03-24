package com.github.mengweijin.intermediary.util;

import com.github.mengweijin.intermediary.util.thread.SftpPrintThread;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.extra.ssh.engine.jsch.ChannelType;
import org.dromara.hutool.extra.ssh.engine.jsch.JschUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author mengweijin
 */
@Slf4j
public class SshMonitor {

    public static final Long TIMEOUT = 10000L;

    public static void execAndMonitor(Session session, String cmd) {
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        final ChannelExec channel = (ChannelExec) JschUtil.createChannel(session, ChannelType.EXEC, TIMEOUT);
        channel.setPty(false);
        channel.setCommand(ByteUtil.toBytes(cmd, StandardCharsets.UTF_8));

        try(InputStream in = channel.getInputStream(); InputStream err = channel.getErrStream()) {
            channel.connect();
            new SftpPrintThread(in, countDownLatch).start();
            new SftpPrintThread(err, countDownLatch).start();
            countDownLatch.await();
        } catch (JSchException | IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new IORuntimeException(e);
        } finally {
            JschUtil.close(channel);
        }
    }

    /**
     * hash算法查看：ssh -Q mac
     * kex算法查看：ssh -Q kex
     * 传输加密算法查看：ssh -Q cipher
     * Host 查看：ssh -Q HostKeyAlgorithms
     * 以上命令是查询 linux 上 openssh 支持的算法，这些算法不一定被打开。
     * <p>
     * openssh 中打开的算法在：/etc/ssh/sshd_conf 文件中配置着。
     * <p>
     * 要使用新的加密算法就要使用高版本jdk。参考官方文档：<a href="https://github.com/mwiede/jsch">https://github.com/mwiede/jsch</a>
     * <p>
     * {@link JSch}
     */
    public static void printJschJarSupportedAlgorithm() {
        log.info("server_host_key: "+ JSch.getConfig("server_host_key"));
        log.info("kex: "+ JSch.getConfig("kex"));
    }

}
