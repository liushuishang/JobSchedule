package ucs.jobschedule.impl.manager;

import ucs.jobschedule.impl.dao.UCSTaskInstanceMapper;
import ucs.jobschedule.impl.model.UCSTaskInstance;
import ucs.jobschedule.impl.utils.MapBuilder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class UCSTaskManager extends BaseManager {
    private UCSTaskInstanceMapper ucsTaskInstanceMapper;

    public UCSTaskManager() {
        ucsTaskInstanceMapper = new UCSTaskInstanceMapper();
    }

    /**
     * 获取可执行的Task实例,按照所属JobInstance的优先级排序
     *
     * @param taskBatchCount 获取的Task实例的个数
     * @return
     */
    public List<UCSTaskInstance> getExecutableTaskInstances(int taskBatchCount) {
        List<UCSTaskInstance> dataResult = jdbcTemplate.query(SQLConstant_Mysql.RETRIEVE_EXECUTABLE_TASK_INSTANCE, MapBuilder.instance().put("top", taskBatchCount).map(), ucsTaskInstanceMapper);
        //更新刚刚查询的任务为执行状态,避免多线程并发下重复执行
        if (dataResult.size() > 0) {
            List<String> idList = new LinkedList<String>();
            for (UCSTaskInstance taskInstance : dataResult) {
                idList.add(taskInstance.getId());
            }
            jdbcTemplate.update(SQLConstant_Mysql.UPDATE_TASK_INSTANCE_RUNING_STATUS,
                    MapBuilder.instance().put("ids", idList).map());
        }
        return dataResult;
    }


    /**
     * 更新TaskInstance执行后的状态
     *
     * @param taskInstanceId
     * @param status
     * @return
     */
    public boolean updateTaskInstanceExecutedStatus(String taskInstanceId, String status, Date startDate) {
        return jdbcTemplate.update(SQLConstant_Mysql.UPDATE_TASK_INSTANCE_COMPLETED_STATUS,
                MapBuilder.instance().put("taskInstanceId", taskInstanceId).put("status", status)
                        .put("startTime", startDate)
                        .put("endTime", new Date()).map()) > 0;
    }


    /**
     * 判断任务是否已经业务上成功
     *
     * @param taskId    任务的Id
     * @param startDate 任务开始时间
     * @param endDate   任务结束时间
     * @return
     */
    public boolean isCompletedTaskSuccess(String taskId, Date startDate, Date endDate) {
        return jdbcTemplate.queryForInt(SQLConstant_Mysql.QUERY_COMPLETED_TASK_IS_SUCCESS,
                MapBuilder.instance().put("taskId", taskId).put("startDate", startDate).put("endDate", endDate).map()) > 0;
    }

}
