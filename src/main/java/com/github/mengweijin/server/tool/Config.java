package com.github.mengweijin.server.tool;

import lombok.Builder;
import lombok.Data;

/**
 * @author mengweijin
 */
@Data
@Builder
public class Config {
    private String host;
    private int port = 22;
    private String user = "root";
    private String password;
    private String workdir = "/opt/app";
}
