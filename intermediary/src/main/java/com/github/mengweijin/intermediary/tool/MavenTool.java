package com.github.mengweijin.intermediary.tool;

import com.github.mengweijin.intermediary.util.ProcessMonitor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
@Slf4j
public class MavenTool {

    /**
     *  cmd /C D:
     *  cd D:\\code\\vitality
     *  mvn clean package -Dmaven.test.skip=true -Pdev
     */
    public static void build(String[] cmd) {
        ProcessMonitor.exec(cmd);
    }

    /**
     *  cmd /C D:
     *  cd D:\\code\\vitality
     *  mvn clean
     */
    public static void clean(String[] cmd) {
        ProcessMonitor.exec(cmd);
    }
}
