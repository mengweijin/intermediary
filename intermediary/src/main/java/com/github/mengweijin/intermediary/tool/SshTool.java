package com.github.mengweijin.intermediary.tool;

import com.github.mengweijin.intermediary.util.Constants;
import com.github.mengweijin.intermediary.util.SshMonitor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.extra.ssh.engine.jsch.JschSession;

import java.nio.charset.StandardCharsets;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
@Slf4j
public class SshTool {

    /**
     *  cd /opt/vitality
     *  ./app.sh stop
     *  rm -rf app.jar
     *  cp /opt/vitality/upload/vitality-admin.jar /opt/vitality/app.jar
     *  ./app.sh start
     */
    public static void exec(JschSession session, String[] cmd) {
        String command = String.join(Constants.CMD_DELIMITER, cmd);
        log.info("exec command: " + command);
        String result = session.exec(command, StandardCharsets.UTF_8);
        System.out.println(result);
    }

    /**
     *  cd /opt/vitality
     *  tail -f ./logs/info.log -n 500
     */
    public static void monitor(JschSession session, String[] cmd) {
        String command = String.join(Constants.CMD_DELIMITER, cmd);
        log.info("exec command: " + command);
        SshMonitor.execAndMonitor(session.getRaw(), command);
    }

}
