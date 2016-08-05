package ucs.jobschedule.impl.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucs.jobschedule.impl.model.UCSTaskInstance;

import java.util.Date;

/**
 * Shell类型任务的执行器
 * Created by yuananyun on 2016/3/6.
 */
public class ShellTaskExecutor extends BaseTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ShellTaskExecutor.class);

    public String getName() {
        return "SHELL";
    }

    @Override
    protected boolean executeInternal(UCSTaskInstance taskInstance) {
        try {
            String taskCommand = taskInstance.getTaskCommand();
            String taskCommandParam = taskInstance.getTaskCommandParam();
            //把命令参数替换到命令
            taskCommand = combineCommandAndParam(taskCommand, taskCommandParam);
            Date startDate = new Date();
            boolean flag= callShell(taskCommand);
            Date endDate = new Date();
            if(flag)
            {
                //查询数据库里面的状态是否已经成功
               if(! taskManager.isCompletedTaskSuccess(taskInstance.getTaskId(), startDate, endDate))
               {
                   logger.info("任务{}调用成功，但是业务上失败！", taskInstance.toString());
                   flag=false;
               }
            }
            return flag;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean revoke(UCSTaskInstance taskInstance) {
        try {
            String taskCommand = taskInstance.getTaskFailCommand();
            callShell(taskCommand);

            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }


    private boolean callShell(String shellString) {
        try {
            if (shellString == null || shellString.length() == 0) return true;
            String[] cmd = new String[]{"sh", "-c", shellString};
            Process process = Runtime.getRuntime().exec(cmd);

            int exitValue = process.waitFor();
            if (0 != exitValue) {
                logger.error("命令{}执行失败", shellString);
            }
            logger.info("命令{}执行成功！", shellString);
            return exitValue == 0;
        } catch (Throwable e) {
            logger.error("命令{}执行出现异常{}", new String[]{shellString, e.getMessage()});
            return false;
        }
    }

}
