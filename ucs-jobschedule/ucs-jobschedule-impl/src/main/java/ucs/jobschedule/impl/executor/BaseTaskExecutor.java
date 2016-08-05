package ucs.jobschedule.impl.executor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ucs.jobschedule.impl.manager.UCSTaskManager;
import ucs.jobschedule.impl.model.UCSState;
import ucs.jobschedule.impl.model.UCSTaskInstance;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by yuananyun on 2016/3/6.
 */
public abstract class BaseTaskExecutor implements ITaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(BaseTaskExecutor.class);
    @Autowired
    protected UCSTaskManager taskManager;
    private ExecutorService service = Executors.newSingleThreadExecutor();


    public boolean execute(final UCSTaskInstance taskInstance) {
        if (taskInstance == null) return false;
        String taskInstanceId = taskInstance.getId();
        String taskCommand = taskInstance.getTaskCommand();
        Date startDate = new Date();
        if (taskCommand == null || "".equals(taskCommand.trim())) {
            logger.error("任务{}缺少执行脚本！", taskInstance.toString());
            taskManager.updateTaskInstanceExecutedStatus(taskInstanceId, UCSState.fail,startDate);
            return false;
        }
        long timeout = taskInstance.getTimeout();
        if (timeout <= 0)
            timeout = Long.MAX_VALUE;

        if (service.isShutdown()) service = Executors.newSingleThreadExecutor();
        Future<Boolean> resultFuture = service.submit(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                int tryCount = taskInstance.getTryCount();
                if (tryCount <= 0) tryCount = 1;
                while (tryCount > 0) {
                    if (executeInternal(taskInstance))
                        return true;
                    tryCount--;
                    logger.info("任务{}执行失败，重试一次", taskInstance.toString());
                }
                return false;
            }
        });
        String execStatus;
        try {
            Boolean result = resultFuture.get(timeout, TimeUnit.SECONDS);
            execStatus = result ? UCSState.success : UCSState.fail;
        } catch (InterruptedException e) {
            logger.error("任务线程跑飞了！");
            execStatus = UCSState.fail;
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
            execStatus = UCSState.fail;
        } catch (TimeoutException e) {
            logger.error("任务{}处理超时！", taskInstance.toString());
            service.shutdownNow();
            execStatus = UCSState.timeout;
        }
        //执行完成后修改任务状态
        taskManager.updateTaskInstanceExecutedStatus(taskInstanceId, execStatus,startDate);
        return UCSState.success.equals(execStatus);
    }

    protected abstract boolean executeInternal(UCSTaskInstance taskInstance);

    public abstract boolean revoke(UCSTaskInstance taskInstance);

    /**
     * 把命令中的参数占位符替换成参数值
     *
     * @param command
     * @param taskCommandParam
     * @return
     */
    protected String combineCommandAndParam(String command, String taskCommandParam) {
        try {
            if (StringUtils.isBlank(taskCommandParam) || StringUtils.isBlank(command))
                return command;
            String taskCommand = command;
            List<Map> paramArray = JSON.parseArray(taskCommandParam, Map.class);
            for (Map paramMap : paramArray) {
                String name = MapUtils.getString(paramMap, "name");
                if (StringUtils.isBlank(name)) continue;
                taskCommand = taskCommand.replaceAll("\\$\\{" + name + "\\}", MapUtils.getString(paramMap, "value"));
            }
            return taskCommand;
        } catch (Exception ex) {
            logger.error("命令{}的参数替换失败！，原因：{}", new String[]{command, ex.getMessage()});
            return "";
        }
    }

}
