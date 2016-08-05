package ucs.jobschedule.impl.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucs.jobschedule.impl.dao.UCSJobMapper;
import ucs.jobschedule.impl.dao.UCSTaskMapper;
import ucs.jobschedule.impl.model.UCSJob;
import ucs.jobschedule.impl.model.UCSJobInstance;
import ucs.jobschedule.impl.model.UCSState;
import ucs.jobschedule.impl.model.UCSTask;
import ucs.jobschedule.impl.parser.TaskCommandParamParser;
import ucs.jobschedule.impl.utils.MapBuilder;

import java.util.*;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
@SuppressWarnings("unchecked")
public class UCSJobManager extends BaseManager {
    private Logger logger = LoggerFactory.getLogger(UCSJobManager.class);
    private UCSJobMapper jobMapper;
    private UCSTaskMapper taskMapper;

    public UCSJobManager() {
        this.jobMapper = new UCSJobMapper();
        this.taskMapper = new UCSTaskMapper();

    }


    public List<UCSJob> getCurrentTimeExecutableJobs() {
        //刷新Task已经执行完成的Job的状态
        jdbcTemplate.update(SQLConstant_Mysql.UPDATE_TASK_COMPLETED_JOB_STATUS,
                MapBuilder.instance().put("lastExecutedTime", new Date()).map());

        Calendar calendar = Calendar.getInstance();
        List<UCSJob> ucsJobList = jdbcTemplate.query(SQLConstant_Mysql.GET_EXECUTABLE_JOBS,
                MapBuilder.instance()
                        .put("year", calendar.get(Calendar.YEAR))
                        .put("month", calendar.get(Calendar.MONTH) + 1)
                        .put("day", calendar.get(Calendar.DAY_OF_MONTH))
                        .put("currentTime", calendar.getTime())
                        .map(), jobMapper);
        return ucsJobList;
    }

    public void createJobInstance(UCSJob ucsJob) {
        if (ucsJob == null) return;
        //新建一个JobInstance
        UCSJobInstance jobInstance = new UCSJobInstance(ucsJob);
        if (insertJobInstance(jobInstance)) {
            createTaskInstancesOfJob(jobInstance);
        } else
            logger.error("创建Job实例失败，参数：" + ucsJob.toString());
    }

    //*******************************************私有方法**************************************************//
    private void createTaskInstancesOfJob(UCSJobInstance jobInstance) {
        List<UCSTask> ucsTaskList = jdbcTemplate.query(SQLConstant_Mysql.RETRIEVE_TASK_BY_JOB_ID, MapBuilder.instance().put("jobId", jobInstance.getJobId()).map(), taskMapper);
        //把任务和它依赖的任务都找出来，最后再统一提交入库
        Map<String, UCSTask> taskMap = new HashMap<String, UCSTask>();
        for (UCSTask ucsTask : ucsTaskList) {
            taskMap.put(ucsTask.getId(), ucsTask);
            joinDependentTasks(taskMap, ucsTask.getDependent());
        }
        String jobInstanceId = jobInstance.getId();
        String taskParam = TaskCommandParamParser.createTaskInstanceParam(jobInstance.getTaskParam());
        //批量插入TaskInstance
        insertTaskInstances(jobInstanceId, taskParam, taskMap);
    }

    /**
     * 批量插入TaskInstance
     *
     * @param jobInstanceId 所属Job实例Id
     * @param taskParam     任务的命令参数
     * @param taskMap
     */
    private void insertTaskInstances(String jobInstanceId, String taskParam, Map<String, UCSTask> taskMap) {
        for (UCSTask ucsTask : taskMap.values()) {
            jdbcTemplate.update(SQLConstant_Mysql.INSERT_TASK_INSTANCE,
                    MapBuilder.instance()
                            .put("taskId", ucsTask.getId())
                            .put("jobInstanceId", jobInstanceId)
                            .put("dependents", ucsTask.getDependent())
                            .put("taskType", ucsTask.getTaskType())
                            .put("taskCommand", ucsTask.getTaskCommand())
                            .put("taskCommandParam", taskParam)
                            .put("targetDataBase", ucsTask.getTargetDataBase())
                            .put("targetMachine", ucsTask.getTargetMachine())
                            .put("tryCount", ucsTask.getTryCount())
                            .put("timeout", ucsTask.getTimeout())
                            .put("failCommand", ucsTask.getFailCommand())
                            .put("failCommandType", ucsTask.getFailCommandType())
                            .put("status", UCSState.wait)
                            .put("createdDate", new Date()).map());
        }
    }

    /**
     * 查找依赖的任务并把它加入taskMap
     *
     * @param taskMap
     * @param taskDependent
     */
    private void joinDependentTasks(Map<String, UCSTask> taskMap, String taskDependent) {
        if (null == taskDependent || "".equals(taskDependent)) return;
        String[] dependentIdArray = taskDependent.split(",");
        if (dependentIdArray.length == 0) return;
        //是否还需要查询数据库获取依赖的Task
        boolean needRetrieve = false;
        for (int i = 0; i < dependentIdArray.length; i++) {
            String dependentId = dependentIdArray[i];
            if (!taskMap.containsKey(dependentId)) {
                needRetrieve = true;
                break;
            }
        }
        if (!needRetrieve) return;
        List<UCSTask> ucsTaskList = jdbcTemplate.query(SQLConstant_Mysql.RETRIEVE_TASK_IN_TASKID,
                MapBuilder.instance().put("ids", taskDependent).map(), taskMapper);
        for (UCSTask ucsTask : ucsTaskList) {
            taskMap.put(ucsTask.getId(), ucsTask);
            joinDependentTasks(taskMap, ucsTask.getDependent());
        }
    }


    /**
     * 插入一个Job实例
     *
     * @param jobInstance
     * @return
     */
    private boolean insertJobInstance(UCSJobInstance jobInstance) {
        try {
            jdbcTemplate.update(SQLConstant_Mysql.INSERT_JOB_INSTANCE,
                    MapBuilder.instance()
                            .put("id", jobInstance.getId())
                            .put("jobId", jobInstance.getJobId())
                            .put("createdDate", new Date())
                            .put("taskParam", jobInstance.getTaskParam())
                            .put("priority", jobInstance.getPriority())
                            .put("taskConcurrentNum", jobInstance.getTaskConcurrentNum())
                            .put("status", jobInstance.getStatus())
                            .map());
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 修改Job信息表的状态，免得重复扫描
     *
     * @param id
     * @param status
     */
    public void updateJobStatus(String id, String status) {
        jdbcTemplate.update(SQLConstant_Mysql.UPDATE_JOB_STATUS, MapBuilder.instance().put("id", id).put("status", status).map());
    }

    /**
     * 更新可以执行的任务实例的状态位
     */
    public boolean refreshExecutableTaskStatus() {
        jdbcTemplate.update(SQLConstant_Mysql.UPDATE_TASK_INSTANCE_SKIP_STATUS, MapBuilder.instance().map());
        long count = jdbcTemplate.update(SQLConstant_Mysql.UPDATE_TASK_INSTANCE_EXECUTABLE_STATUS, MapBuilder.instance().map());
        logger.info("更新{}个任务状态为可执行", count);
        return count > 0;
    }
}
