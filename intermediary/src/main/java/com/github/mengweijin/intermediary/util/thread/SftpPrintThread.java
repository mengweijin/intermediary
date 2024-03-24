package com.github.mengweijin.intermediary.util.thread;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author mengweijin
 * @date 2023/12/16
 */
@Slf4j
public class SftpPrintThread extends Thread {
    private final InputStream inputStream;
    private final CountDownLatch countDownLatch;

    public SftpPrintThread(InputStream in, CountDownLatch countDownLatch) {
        this.inputStream = in;
        this.countDownLatch = countDownLatch;
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
            countDownLatch.countDown();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
