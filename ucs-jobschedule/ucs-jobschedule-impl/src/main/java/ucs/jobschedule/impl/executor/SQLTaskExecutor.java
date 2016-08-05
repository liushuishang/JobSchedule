package ucs.jobschedule.impl.executor;

import ucs.jobschedule.impl.model.UCSTaskInstance;

/**
 * Created by yuananyun on 2016/3/6.
 */
public class SQLTaskExecutor extends BaseTaskExecutor {
    public String getName() {
        return "SQL";
    }


    @Override
    protected boolean executeInternal(UCSTaskInstance taskInstance) {
        return false;
    }

    @Override
    public boolean revoke(UCSTaskInstance taskInstance) {
        return false;
    }
}
