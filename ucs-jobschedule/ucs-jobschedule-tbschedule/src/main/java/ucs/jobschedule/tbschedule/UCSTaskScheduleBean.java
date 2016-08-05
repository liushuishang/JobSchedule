package ucs.jobschedule.tbschedule;

import com.taobao.pamirs.schedule.IScheduleTaskDealMulti;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ucs.jobschedule.impl.executor.UCSTaskHandler;
import ucs.jobschedule.impl.model.UCSTaskInstance;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ucs_yuananyun on 2016/3/2.
 */
public class UCSTaskScheduleBean implements IScheduleTaskDealMulti<UCSTaskInstance> {
    private static final Logger logger = LoggerFactory.getLogger(UCSTaskScheduleBean.class);
    @Autowired
    private UCSTaskHandler ucsTaskHandler;

    public boolean execute(UCSTaskInstance[] tasks, String ownSign) throws Exception {
        return ucsTaskHandler.executeTaskInstances(Arrays.asList(tasks));
    }

    public List<UCSTaskInstance> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
       if(ucsTaskHandler==null)
           logger.error("UCSTaskHandler 实例为null！");
        return ucsTaskHandler.getExecutableTaskInstances(eachFetchDataNum);
    }

    public Comparator<UCSTaskInstance> getComparator() {
        return new Comparator<UCSTaskInstance>() {
            public int compare(UCSTaskInstance o1, UCSTaskInstance o2) {
                return o1.getCreatedDate().compareTo(o2.getCreatedDate());
            }

            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
        };
    }
}
