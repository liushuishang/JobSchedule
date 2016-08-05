package ucs.jobschedule.impl.manager;


/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class SQLConstant_Mysql {
    public static final String GET_EXECUTABLE_JOBS =
            "SELECT *\n" +
                    "FROM SCH_JOB\n" +
                    "WHERE is_enable = 1\n" +
                    "      AND UNIX_TIMESTAMP(IFNULL(PERMIT_RUN_START_TIME, 0)) <= UNIX_TIMESTAMP(:currentTime)\n" +
                    "      AND UNIX_TIMESTAMP(IFNULL(PERMIT_RUN_END_TIME, 999999999)) > UNIX_TIMESTAMP(:currentTime)\n" +
                    "      AND UNIX_TIMESTAMP(REPLACE(REPLACE(REPLACE(EXEC_TIME_LIMIT, 'yyyy', :year), 'MM', :month), 'dd', :day)) <=\n" +
                    "          UNIX_TIMESTAMP(:currentTime)\n" +
                    "AND ((job_type='D' AND IFNULL(DAY(last_exec_time),0)<>:day)\n" +
                    "      OR (job_type='M' AND IFNULL(MONTH(last_exec_time),0)<>:month)\n" +
                    "      OR (job_type='Y' AND IFNULL(YEAR(last_exec_time),0)<>:year)) "+
                    "AND STATUS = 'W' order by priority desc";

    public static final String INSERT_JOB_INSTANCE =
            "insert into SCH_JOB_INSTANCE(id,job_id,created_date,task_param,task_concurrent_num,priority) values(:id,:jobId,:createdDate,:taskParam,:taskConcurrentNum,:priority)";

    public static final String RETRIEVE_TASK_BY_JOB_ID =
            "SELECT\n" +
                    "  st.*,\n" +
                    "  etd.dependent\n" +
                    "FROM SCH_TASK st\n" +
                    "  LEFT JOIN SCH_TASK_DEPENDENT etd ON st.name = etd.jobname AND etd.Enable = 1\n" +
                    "WHERE\n" +
                    "  st.is_enable = 1 AND st.job_id =:jobId\n" +
                    "ORDER BY etd.dependent asc";

    public static final String RETRIEVE_TASK_IN_TASKID =
            "SELECT\n" +
                    "  st.*,\n" +
                    "  etd.dependent\n" +
                    "FROM SCH_TASK st\n" +
                    "  LEFT JOIN SCH_TASK_DEPENDENT etd ON st.name = etd.jobname AND etd.Enable = 1\n" +
                    "WHERE\n" +
                    "  st.is_enable = 1 AND st.job_id in (:ids)\n" +
                    "ORDER BY etd.dependent asc";

    public static final String INSERT_TASK_INSTANCE =
            "INSERT INTO SCH_TASK_INSTANCE (\n" +
                    "  ID,\n" +
                    "  TASK_ID,\n" +
                    "  JOB_INSTANCE_ID,\n" +
                    "  DEPENDENTS,\n" +
                    "  TASK_TYPE,\n" +
                    "  TASK_COMMAND,\n" +
                    "  TASK_COMMAND_PARAM,\n" +
                    "  CREATED_DATE,\n" +
                    "  TARGET_DATABASE,\n" +
                    "  TARGET_MACHINE,\n" +
                    "  TRY_COUNT,\n" +
                    "  TIMEOUT,\n" +
                    "  FAIL_COMMAND,\n" +
                    "  FAIL_COMMAND_TYPE,\n" +
                    "  STATUS\n" +
                    ")\n" +
                    "VALUES\n" +
                    "  (\n" +
                    "    uuid(),\n" +
                    "    :taskId,\n" +
                    "    :jobInstanceId,\n" +
                    "    :dependents,\n" +
                    "    :taskType,\n" +
                    "    :taskCommand,\n" +
                    "    :taskCommandParam,\n" +
                    "    :createdDate,\n" +
                    "    :targetDataBase,\n" +
                    "    :targetMachine,\n" +
                    "    :tryCount,\n" +
                    "    :timeout,\n" +
                    "    :failCommand,\n" +
                    "    :failCommandType,\n" +
                    "    :status\n" +
                    "  )";

    public static final String UPDATE_JOB_STATUS =
            "update SCH_JOB set status=:status where id=:id";

    public static final String UPDATE_TASK_INSTANCE_EXECUTABLE_STATUS =
            "UPDATE SCH_TASK_INSTANCE\n" +
                    "SET STATUS = 'A'\n" +
                    "WHERE id IN\n" +
                    "      (SELECT a.id\n" +
                    "       FROM\n" +
                    "         (SELECT t1.id\n" +
                    "          FROM SCH_TASK_INSTANCE t1\n" +
                    "          WHERE NOT EXISTS(SELECT t2.id\n" +
                    "                           FROM SCH_TASK_INSTANCE t2\n" +
                    "                           WHERE FIND_IN_SET(t2.task_id, IFNULL(t1.dependents, ',')) AND t2.`STATUS` != 'S') and t1.status='W'\n" +
                    "         ) AS a)\n";


    public static final String UPDATE_TASK_INSTANCE_SKIP_STATUS =
            "UPDATE SCH_TASK_INSTANCE\n" +
                    "SET STATUS = 'K'\n" +
                    "WHERE id IN\n" +
                    "      (SELECT a.id\n" +
                    "       FROM\n" +
                    "         (SELECT t1.id\n" +
                    "          FROM SCH_TASK_INSTANCE t1\n" +
                    "          WHERE EXISTS(SELECT t2.id\n" +
                    "                       FROM SCH_TASK_INSTANCE t2\n" +
                    "                       WHERE FIND_IN_SET(t2.task_id, IFNULL(t1.dependents, ',')) AND t2.`STATUS` in ('K','F','T')) and t1.status='W'\n" +
                    "         ) AS a)";


    public static final String RETRIEVE_EXECUTABLE_TASK_INSTANCE =
            " select task.*,jobIns.task_param  from SCH_JOB_INSTANCE jobIns,SCH_TASK_INSTANCE task where " +
                    "jobIns.status='W' and task.job_instance_id=jobIns.id and task.status='A' order by jobIns.priority desc limit :top";

    public static final String UPDATE_TASK_INSTANCE_RUNING_STATUS =
            " update SCH_TASK_INSTANCE set status='R'   where status='A' and id in ( :ids ) ";

    public static final String UPDATE_TASK_COMPLETED_JOB_STATUS =
            "update SCH_JOB job,SCH_JOB_INSTANCE jobins " +
                    "set job.status='W',job.last_exec_time=:lastExecutedTime,job.last_exec_param=jobins.task_param,jobins.status='C',jobins.end_time=:lastExecutedTime " +
                    "where job.id=jobins.job_id and  not exists (select id from SCH_TASK_INSTANCE where status in ('W','A','R') and job_instance_id=jobins.id) ";

    public static final String UPDATE_TASK_INSTANCE_COMPLETED_STATUS =
//            "update  SCH_TASK_INSTANCE set status=:status ,end_time=:endTime  where status='R' and id=:taskInstanceId";
            "update  SCH_TASK_INSTANCE set status=:status ,end_time=:endTime,start_time=:startTime  where  id=:taskInstanceId";

    public static final String QUERY_COMPLETED_TASK_IS_SUCCESS =
            "select count(*) from ETL_JOB_DEPENDENT_STATUS  where id=:taskId and ProcessedDate>=:startDate and StatusChangeTime<=:endDate and ExecStatus='S'";
}
