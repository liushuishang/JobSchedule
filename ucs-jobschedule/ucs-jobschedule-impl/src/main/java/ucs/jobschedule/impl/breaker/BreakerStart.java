package ucs.jobschedule.impl.breaker;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ucs_yuananyun on 2016/3/5.
 */
public class BreakerStart {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/implcontext.xml");
        UCSJobHandler jobHandler = (UCSJobHandler) ctx.getBean("UCSJobHandler");
        while (true) {
            try {
                jobHandler.createJobInstance(jobHandler.getCurrentTimeExecutableJobs());
                Thread.sleep(1000);
                jobHandler.scanExecutableTask();
                Thread.sleep(1000);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
