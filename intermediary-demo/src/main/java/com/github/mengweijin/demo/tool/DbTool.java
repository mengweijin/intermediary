package com.github.mengweijin.demo.tool;

import com.github.mengweijin.intermediary.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.io.SerializeUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.db.Db;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.ds.simple.SimpleDataSource;

import java.io.File;
import java.util.List;

/**
 * @author mengweijin
 * @date 2024/3/24
 */
@Slf4j
public class DbTool {

    public static void serializeTableData(Db db, String sql, File backupFile) {
        List<Entity> entityList = db.query(sql);
        byte[] bytes = SerializeUtil.serialize(entityList);
        FileUtil.writeBytes(bytes, backupFile);
    }


    public static void main(String[] args) {


    }

    public static void backup() {
        String sql = "select * from SYS_DEMO";
        File file = FileUtil.file(Constants.PROJECT_PATH + "sys_demo.bak");
        serializeTableData(db(), sql, file);
    }

    public static Db db() {
        DbConfig dbConfig = DbConfig.of("jdbc:mysql://localhost:3306/vitality?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "root",
                "root");
        Db db = Db.of(new SimpleDataSource(dbConfig));
        FieldUtil.setFieldValue(db, "dbConfig", dbConfig);
        return db;
    }
}
