package com.github.mengweijin.intermediary.execute;

import java.io.Closeable;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
public interface IExecute extends Closeable {

    void execute();

}
