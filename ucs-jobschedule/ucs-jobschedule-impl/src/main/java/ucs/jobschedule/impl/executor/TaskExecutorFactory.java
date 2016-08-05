package ucs.jobschedule.impl.executor;

import java.util.Map;

/**
 * Created by yuananyun on 2016/3/6.
 */
public class TaskExecutorFactory {
    //    private static final Logger logger = LoggerFactory.getLogger(TaskExecutorFactory.class);
    private Map<String, ITaskExecutor> taskExecutorMap;

    public ITaskExecutor getTaskExecutor(String identifier) {
        if (!taskExecutorMap.containsKey(identifier)) {
            throw new IllegalArgumentException("找不到标识符为" + identifier + "的任务执行器!");
        }
        return taskExecutorMap.get(identifier);
    }

    public void setTaskExecutorMap(Map<String, ITaskExecutor> taskExecutorMap) {
        this.taskExecutorMap = taskExecutorMap;
    }
}
