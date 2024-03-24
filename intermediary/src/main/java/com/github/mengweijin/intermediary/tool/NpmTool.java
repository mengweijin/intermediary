package com.github.mengweijin.intermediary.tool;

import com.github.mengweijin.intermediary.util.Constants;
import com.github.mengweijin.intermediary.util.ProcessMonitor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
@Slf4j
public class NpmTool {

    /**
     *  cmd /C D:
     *  cd D:\\code\\vitality\\vitality-ui
     *  npm install
     *  npm run build:prod
     */
    public static void build(String[] cmd) {
        log.info("npm build: " + String.join(Constants.CMD_DELIMITER, cmd));
        ProcessMonitor.exec(cmd);
    }

}
