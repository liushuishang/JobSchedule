package ucs.jobschedule.impl.executor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ucs.jobschedule.impl.model.UCSTaskInstance;

import java.util.List;

/**
 * Created by ucs_yuananyun on 2016/3/7.
 */
public class ExecutorStart {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/implcontext.xml");
        UCSTaskHandler taskHandler = (UCSTaskHandler) ctx.getBean("UCSTaskHandler");
        while (true) {
            try {
                List<UCSTaskInstance> taskInstances = taskHandler.getExecutableTaskInstances(10);
                if(taskInstances.size()==0)
                    Thread.sleep(2000);
                taskHandler.executeTaskInstances(taskInstances);
                Thread.sleep(500);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
