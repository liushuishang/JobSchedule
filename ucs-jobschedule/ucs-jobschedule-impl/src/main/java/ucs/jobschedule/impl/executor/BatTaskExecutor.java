package ucs.jobschedule.impl.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucs.jobschedule.impl.model.UCSTaskInstance;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ucs_yuananyun on 2016/3/7.
 */
public class BatTaskExecutor extends BaseTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(BatTaskExecutor.class);

    @Override
    protected boolean executeInternal(UCSTaskInstance taskInstance) {
        try {
            String taskCommand = taskInstance.getTaskCommand();
            String taskCommandParam = taskInstance.getTaskCommandParam();
            //把命令参数替换到命令
            taskCommand = combineCommandAndParam(taskCommand, taskCommandParam);
            callCmd(taskCommand);
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }


    public String getName() {
        return "BAT";
    }

    @Override
    public boolean revoke(UCSTaskInstance taskInstance) {
        return false;
    }



    private boolean callCmd(String command) {
        try {
            Process child = Runtime.getRuntime().exec("cmd.exe /C start " + command);
            InputStream in = child.getInputStream();
            int c;
//            while ((c = in.read()) != -1) {
//            }
            in.close();
            child.waitFor();
            return true;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

}
