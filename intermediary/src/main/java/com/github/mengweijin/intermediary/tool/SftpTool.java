package com.github.mengweijin.intermediary.tool;

import com.github.mengweijin.intermediary.util.SftpMonitor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.extra.ssh.engine.jsch.JschSftp;

import java.io.File;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
@Slf4j
public class SftpTool {

    public static void upload(JschSftp sftp, File targetFile, String uploadDir) {
        log.info("make upload dir: " + uploadDir);
        sftp.mkDirs(uploadDir);
        sftp.cd(uploadDir);
        //上传本地文件
        log.info("upload file: " + targetFile.getAbsolutePath());
        sftp.put(targetFile.getAbsolutePath(), uploadDir, new SftpMonitor(), JschSftp.Mode.OVERWRITE);
    }
}
