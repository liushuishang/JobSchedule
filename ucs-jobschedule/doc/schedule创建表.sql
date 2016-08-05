DROP TABLE IF EXISTS SCH_JOB;

/*==============================================================*/
/* Table: SCH_JOB                                               */
/*==============================================================*/
CREATE TABLE SCH_JOB
(
   ID                   VARCHAR(38) NOT NULL,
   NAME                 VARCHAR(100) NOT NULL,
   JOB_TYPE             CHAR NOT NULL DEFAULT 'D' COMMENT 'H:小时任务;D：日任务；M：月任务；Y：年任务；W：周任务',
   PERMIT_RUN_START_TIME DATETIME NOT NULL COMMENT '允许执行时段的开始时间',
   PERMIT_RUN_END_TIME  DATETIME COMMENT '允许执行时段的结束时间',
   EXEC_TIME_LIMIT      VARCHAR(20) COMMENT '执行时间限制，表示执行的时间点，字母部分将替换成实际值。如： yyyy-MM-dd xx::xx::xx，yyyy-MM-dd将被替换成实际日期。',
   TASK_PARAM_DEFINITION VARCHAR(1000) COMMENT '[
                {
                    "name": "KETTLE_PATH",
                    "defaultValue": "/opt/kettle/data-integration",
            	"value":""
                    "increment": -1,
                    "canAdditive": false,
                    "type": "string(date、string、number)",
                    "unit": ""(for data:yyyy,MM,dd,HH,mm,ss)
                },
                {
                    "name": "yesterday",
                    "defaultValue": "2016-03-08",
            	"value":""
                    "increment": -1,
                    "canAdditive": true,
                    "type": "date",
                    "unit": "dd"
                }
            
            ]',
   TASK_CONCURRENT_NUM  INT(3) COMMENT '可以并发执行的Task个数',
   CREATED_DATE         DATETIME NOT NULL,
   PRIORITY             INT COMMENT 'job优先级',
   DESCRIPTIOHN         VARCHAR(200),
   IS_ENABLE            TINYINT(1) NOT NULL DEFAULT 1 COMMENT '0:不可用;1;可用',
   STATUS               CHAR NOT NULL DEFAULT 'W' COMMENT 'W:等待调度;R:正在运行',
   LAST_EXEC_TIME       DATETIME,
   LAST_EXEC_PARAM      VARCHAR(1000),
   PRIMARY KEY (ID)
);

ALTER TABLE SCH_JOB COMMENT '记录Job的信息';

DROP TABLE IF EXISTS SCH_TASK;

/*==============================================================*/
/* Table: SCH_TASK                                              */
/*==============================================================*/
CREATE TABLE SCH_TASK
(
   ID                   VARCHAR(38) NOT NULL,
   NAME                 VARCHAR(100) NOT NULL,
   JOB_ID               VARCHAR(38) NOT NULL,
   TASK_TYPE            VARCHAR(10) NOT NULL COMMENT 'SHELLl：Shell任务；SQL：SQL语句任务',
   TASK_COMMAND         VARCHAR(1000),
   DESCRIPTIOHN         VARCHAR(200),
   CREATED_DATE         DATETIME NOT NULL,
   IS_ENABLE            TINYINT(1) NOT NULL DEFAULT 1 COMMENT '1：可用；2：不可用',
   TARGET_DATABASE      VARCHAR(50),
   TARGET_MACHINE       VARCHAR(20),
   TRY_COUNT            INT,
   FAIL_COMMAND         VARCHAR(1000),
   FAIL_COMMAND_TYPE    VARCHAR(10),
   TIMEOUT              LONG,
   PRIMARY KEY (ID)
);

ALTER TABLE SCH_TASK COMMENT 'Task的信息表';

ALTER TABLE SCH_TASK ADD CONSTRAINT FK_Reference_TASK_JOB FOREIGN KEY (JOB_ID)
      REFERENCES SCH_JOB (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;
DROP INDEX INDEX_JOB_INSTANCE_ID ON SCH_TASK_INSTANCE;

DROP TABLE IF EXISTS SCH_TASK_INSTANCE;

/*==============================================================*/
/* Table: SCH_TASK_INSTANCE                                     */
/*==============================================================*/
CREATE TABLE SCH_TASK_INSTANCE
(
   ID                   VARCHAR(38) NOT NULL,
   TASK_ID              VARCHAR(38),
   JOB_INSTANCE_ID      VARCHAR(38),
   DEPENDENTS           VARCHAR(500) COMMENT '逗号分隔的TaskId',
   TASK_TYPE            VARCHAR(10) NOT NULL COMMENT 'SHELLl：Shell任务；SQL：SQL语句任务',
   TASK_COMMAND         VARCHAR(1000),
   TASK_COMMAND_PARAM   VARCHAR(1000),
   CREATED_DATE         DATETIME NOT NULL,
   TARGET_DATABASE      VARCHAR(50),
   TARGET_MACHINE       VARCHAR(20),
   TRY_COUNT            INT,
   FAIL_COMMAND         VARCHAR(1000),
   FAIL_COMMAND_TYPE    VARCHAR(10),
   TIMEOUT              LONG COMMENT '单位是秒',
   START_TIME           DATETIME,
   END_TIME             DATETIME,
   STATUS               CHAR DEFAULT 'W' COMMENT 'W:等待调度;A:可执行;R:执行中;S:成功;F:失败;K:跳过;T:处理超时',
   PRIMARY KEY (ID)
);


ALTER TABLE SCH_TASK_INSTANCE COMMENT 'Task的实例表,表示要执行的某次任务';

/*==============================================================*/
/* Index: INDEX_JOB_INSTANCE_ID                                 */
/*==============================================================*/
CREATE INDEX INDEX_JOB_INSTANCE_ID ON SCH_TASK_INSTANCE
(
   JOB_INSTANCE_ID
);

ALTER TABLE SCH_TASK_INSTANCE ADD CONSTRAINT FK_Reference_INSTANCE_TASK FOREIGN KEY (TASK_ID)
      REFERENCES SCH_TASK (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE SCH_TASK_INSTANCE ADD CONSTRAINT FK_Reference_TASK_JOB FOREIGN KEY (JOB_INSTANCE_ID)
      REFERENCES SCH_JOB_INSTANCE (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;



DROP TABLE IF EXISTS SCH_JOB_INSTANCE;

/*==============================================================*/
/* Table: SCH_JOB_INSTANCE                                      */
/*==============================================================*/
CREATE TABLE SCH_JOB_INSTANCE
(
   ID                   VARCHAR(38) NOT NULL,
   JOB_ID               VARCHAR(38),
   CREATED_DATE         DATETIME NOT NULL,
   START_TIME           DATETIME,
   END_TIME             DATETIME,
   STATUS               CHAR DEFAULT 'W' COMMENT 'W：等待调度;R：执行中;C:已完成;',
   TASK_PARAM           VARCHAR(1000),
   TASK_CONCURRENT_NUM  INT(3) COMMENT '可以并发执行的Task个数',
   PRIORITY             INT COMMENT 'job优先级',
   PRIMARY KEY (ID)
);

ALTER TABLE SCH_JOB_INSTANCE COMMENT '表示Job的一次执行的信息';

ALTER TABLE SCH_JOB_INSTANCE ADD CONSTRAINT FK_Reference_INSTANCE_JOB FOREIGN KEY (JOB_ID)
      REFERENCES SCH_JOB (ID) ON DELETE RESTRICT ON UPDATE RESTRICT;


DROP TABLE IF EXISTS SCH_TASK_DEPENDENT;

/*==============================================================*/
/* Table: SCH_TASK_DEPENDENT                                    */
/*==============================================================*/
CREATE TABLE SCH_TASK_DEPENDENT
(
   Id                   VARCHAR(38) NOT NULL,
   JobName              VARCHAR(100) NOT NULL,
   Description          VARCHAR(1000),
   Dependent            VARCHAR(255),
   ENABLE               TINYINT DEFAULT 1 COMMENT '0：不可用；1：可用',
   PRIMARY KEY (Id)
);

ALTER TABLE SCH_TASK_DEPENDENT COMMENT '任务依赖表';

