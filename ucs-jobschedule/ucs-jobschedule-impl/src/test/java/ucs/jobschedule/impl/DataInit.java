package ucs.jobschedule.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ucs.jobschedule.impl.utils.MapBuilder;

import java.io.*;

/**
 * Created by ucs_yuananyun on 2016/3/8.
 */
public class DataInit {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationcontext.xml");
        NamedParameterJdbcTemplate jdbcTemplate = (NamedParameterJdbcTemplate) ctx.getBean("namedParamsJdbcTemplate");

        File file = new File("E:\\yuananyun\\workbench\\schedule\\ucs-jobschedule\\ucs-jobschedule-impl\\src\\test\\resources\\data\\cron_day_job_ods_QT.txt");
        if (file.isFile() && file.exists()) { //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), "utf-8");//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                if (lineTxt.length() < 10) continue;
                String[] commandArray = lineTxt.split(" ");
                String taskName = commandArray[11];
                if (taskName.length() > 0) {
                    jdbcTemplate.update("update SCH_TASK set task_type='SHELL',task_command=:command where name=:taskName",
                            MapBuilder.instance().put("command", lineTxt).put("taskName", taskName).map());
                }
            }
        }
    }

}
