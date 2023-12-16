package com.github.mengweijin.server.tool;

import com.jcraft.jsch.JSch;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mengweijin
 */
@Slf4j
public class SshUtils {

    /**
     * hash算法查看：ssh -Q mac
     * kex算法查看：ssh -Q kex
     * 传输加密算法查看：ssh -Q cipher
     * Host 查看：ssh -Q HostKeyAlgorithms
     * 以上命令是查询 linux 上 openssh 支持的算法，这些算法不一定被打开。
     *
     * openssh 中打开的算法在：/etc/ssh/sshd_conf 文件中配置着。
     *
     * {@link JSch}
     */
    public static void printJschJarSupportedAlgorithm() {
        log.info("kex: "+ JSch.getConfig("kex"));
    }

    public static void main(String[] args) {
        SshUtils.printJschJarSupportedAlgorithm();
    }

}
