package ucs.jobschedule.impl.breaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ucs.jobschedule.impl.manager.UCSJobManager;
import ucs.jobschedule.impl.model.UCSJob;
import ucs.jobschedule.impl.model.UCSState;

import java.util.List;

/**
 * 主要负责生成Job实例和按照Job给定的条件查询Task并生成Task实例
 * Created by ucs_yuananyun on 2016/3/4.
 */
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UCSJobHandler {
    private static final Logger logger = LoggerFactory.getLogger(UCSJobHandler.class);
    @Autowired
    private UCSJobManager jobManager;

    public List<UCSJob> getCurrentTimeExecutableJobs() {
        try {
            List<UCSJob> jobList = jobManager.getCurrentTimeExecutableJobs();
            logger.info("获取到{}个需要调度的Job", jobList.size());
            return jobList;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 扫描可执行的Job，并创建Job实例
     *
     * @return
     */
    public boolean createJobInstance(List<UCSJob> jobList) {
        if (jobList == null || jobList.size() == 0) return true;
        try {
            for (UCSJob ucsJob : jobList) {
                logger.info("开始创建{}的实例", ucsJob.toString());
                jobManager.createJobInstance(ucsJob);
                //修改Job信息表中的状态为R，免得重复扫描
                jobManager.updateJobStatus(ucsJob.getId(), UCSState.run);
                logger.info("创建{}的实例成功！", ucsJob.toString());
            }
            return jobList.size() > 0;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * 扫描可执行的Task并修改状态为可执行
     */
    public boolean scanExecutableTask() {
        try {
            //更新可以执行的任务的状态位
            return jobManager.refreshExecutableTaskStatus();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

}
