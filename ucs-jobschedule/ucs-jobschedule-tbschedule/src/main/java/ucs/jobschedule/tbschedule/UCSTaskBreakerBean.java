package ucs.jobschedule.tbschedule;

import com.taobao.pamirs.schedule.IScheduleTaskDealMulti;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.springframework.beans.factory.annotation.Autowired;
import ucs.jobschedule.impl.breaker.UCSJobHandler;
import ucs.jobschedule.impl.model.UCSJob;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ucs_yuananyun on 2016/3/7.
 */
public class UCSTaskBreakerBean implements IScheduleTaskDealMulti<UCSJob> {
    @Autowired
    private UCSJobHandler ucsJobHandler;

    public boolean execute(UCSJob[] tasks, String ownSign) throws Exception {
        return ucsJobHandler.createJobInstance(Arrays.asList(tasks)) &&
                ucsJobHandler.scanExecutableTask();
    }

    public List<UCSJob> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        ucsJobHandler.scanExecutableTask();
        return ucsJobHandler.getCurrentTimeExecutableJobs();
    }

    public Comparator<UCSJob> getComparator() {
        return new Comparator<UCSJob>() {
            public int compare(UCSJob o1, UCSJob o2) {
                return o1.getPriority() - o2.getPriority();
            }
        };
    }

}
