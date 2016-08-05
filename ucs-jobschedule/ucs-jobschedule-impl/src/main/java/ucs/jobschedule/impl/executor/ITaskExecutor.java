package ucs.jobschedule.impl.executor;

import ucs.jobschedule.impl.model.UCSTaskInstance;

/**
 * Created by yuananyun on 2016/3/6.
 */
public interface ITaskExecutor {
     String getName();

    /**
     * 执行一个task
     * @param taskInstance
     */
    boolean execute(UCSTaskInstance taskInstance);

    /**
     * TaskInstance执行失败后执行回退命令
     * @param taskInstance
     * @return
     */
    boolean revoke(UCSTaskInstance taskInstance);
}
