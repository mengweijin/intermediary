package com.github.mengweijin.intermediary.util;

import com.jcraft.jsch.SftpProgressMonitor;
import lombok.extern.slf4j.Slf4j;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author mengweijin
 */
@Slf4j
public class SftpMonitor implements SftpProgressMonitor {
    /** 开始传输时间 */
    public long startTime = 0L;
    /** 文件总大小 */
    private long totalSize = 0L;
    /** 已传输的大小 */
    private long transferredSize = 0L;

    private boolean isTransferredEnd = false;
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public SftpMonitor() {}

    /**
     * 传输开始时，会调用init方法。其中op为操作类型，即为 {@link SftpProgressMonitor} 中定义的PUT/GET，max为文件的大小
     */
    @Override
    public void init(int op, String src, String dest, long max) {
        log.info("传输文件：" + src + " 至：" + dest);
        startTime = System.currentTimeMillis();

        totalSize = max;
        if (totalSize != 0) {
            final DecimalFormat df = new DecimalFormat("#.##");
            service.scheduleAtFixedRate(() -> {
                if (!isTransferredEnd) {
                    if (transferredSize != totalSize) {
                        double d = ((double) transferredSize * 100) / (double) totalSize;
                        log.info("Current progress: " + df.format(d) + "%");
                    } else {
                        // 已传输大小等于文件总大小，则已完成
                        isTransferredEnd = true;
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    /**
     * 当每次传输一个数据块后，会调用count方法，参数为这一次传输的数据块大小
     */
    @Override
    public boolean count(long count) {
        transferredSize += count;
        return count > 0;
    }

    /**
     * 传输结束时，会调用 end 方法
     */
    @Override
    public void end() {
        service.shutdown();
        log.info("Current progress: 100%");
        long endTime = System.currentTimeMillis();
        log.info("传输完成！用时：" + (endTime - startTime) / 1000 + "s");
        log.info("Transferred Size : " + transferredSize/1024 + "KB");
        log.info("Transferred Size : " + transferredSize/1024/1024 + "MB");
    }

}
