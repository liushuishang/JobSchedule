package ucs.jobschedule.tbschedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ucs_yuananyun on 2016/3/3.
 */
public class UCSTaskScheduleStarter {
    private static final Logger logger = LoggerFactory.getLogger(UCSTaskScheduleStarter.class);

    public static void main(String[] args) throws Exception {
        logger.info("开始启动任务调度程序……");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationcontext.xml");
//        Object b = ctx.getBean("scheduleManagerFactory");
        logger.info("任务调度程序启动完成");
    }


}
