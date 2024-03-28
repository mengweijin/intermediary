DROP TABLE IF EXISTS SYS_DEMO;
CREATE TABLE SYS_DEMO (
  ID                            bigint NOT NULL COMMENT '主键ID',
  TITLE                         varchar(255) DEFAULT NULL COMMENT '标题',
  DESCRIPTION                   varchar(4000) DEFAULT NULL COMMENT '内容',
  RELEASED                      int DEFAULT 0 COMMENT '是否已发布。{0=否, 1=是}',
  FILES 							BLOB DEFAULT NULL COMMENT '文件',
  CREATE_BY                     bigint DEFAULT NULL COMMENT '创建者',
  CREATE_TIME                   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UPDATE_BY 	                bigint DEFAULT NULL COMMENT '更新者',
  UPDATE_TIME 	                datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
  PRIMARY KEY (id)
);
