package com.github.mengweijin.server.tool.thread;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author mengweijin
 * @date 2023/12/16
 */
@Slf4j
public class PrintTerminalDaemonThread extends Thread {
    private final InputStream inputStream;

    public PrintTerminalDaemonThread(InputStream in) {
        this.inputStream = in;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        try (
                InputStreamReader inReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(inReader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
