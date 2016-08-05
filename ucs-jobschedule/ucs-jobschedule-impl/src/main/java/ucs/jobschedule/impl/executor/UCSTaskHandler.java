package ucs.jobschedule.impl.executor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ucs.jobschedule.impl.manager.UCSTaskManager;
import ucs.jobschedule.impl.model.UCSTaskInstance;

import java.util.List;

/**
 * 主要实现Task的执行逻辑
 * Created by ucs_yuananyun on 2016/3/4.
 */
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@SuppressWarnings("unchecked")
public class UCSTaskHandler {
    private static final Logger logger = LoggerFactory.getLogger(UCSTaskHandler.class);
    @Autowired
    private UCSTaskManager ucsTaskManager;
    @Autowired
    private TaskExecutorFactory taskExecutorFactory;


    public boolean executeTaskInstances(List<UCSTaskInstance> ucsTaskInstanceList) {
        if (ucsTaskInstanceList == null) return true;
        try {
            for (UCSTaskInstance ucsTaskInstance : ucsTaskInstanceList) {
                logger.info("开始执行任务：{}", ucsTaskInstance.toString());
                if (!taskExecutorFactory.getTaskExecutor(ucsTaskInstance.getTaskType()).execute(ucsTaskInstance)) {
                    String taskFailCommand = ucsTaskInstance.getTaskFailCommand();
                    String taskFailCommandType = ucsTaskInstance.getTaskFailCommandType();
                    if (!StringUtils.isBlank(taskFailCommandType) && !StringUtils.isBlank(taskFailCommand)) {
                        logger.info("任务：{} 执行失败，现在执行任务失败脚本……", ucsTaskInstance.getId());
                        if (taskExecutorFactory.getTaskExecutor(taskFailCommandType).revoke(ucsTaskInstance))
                            logger.info("任务{}失败脚本执行成功！", ucsTaskInstance.getId());
                        else logger.info("任务{}失败脚本执行失败！", ucsTaskInstance.getId());
                    }
                } else
                    logger.info("任务：{} 成功执行完成！", ucsTaskInstance.toString());
            }
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    public List<UCSTaskInstance> getExecutableTaskInstances(int taskBatchCount) {
        try {
            logger.info("请求查询{}条可执行任务", taskBatchCount);
            List taskList = ucsTaskManager.getExecutableTaskInstances(taskBatchCount);
            logger.info("实际查询到{}条可执行任务", taskList.size());
//            if(taskList.size()==0) {
//                logger.info("没有查询到可执行的Task，休眠10秒钟");
//                Thread.sleep(10000);
//            }
            return taskList;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }
}
