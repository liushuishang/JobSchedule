package ucs.jobschedule.impl.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucs.jobschedule.impl.parser.TaskCommandParamParser;

import java.text.ParseException;
import java.util.*;


/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class UCSJobInstance extends Named {
    private static final Logger logger = LoggerFactory.getLogger(UCSJobInstance.class);
    private String jobId;
    private String jobType;
    private String taskParam;
    private int taskConcurrentNum;
    private int priority;
    private String status;


    public UCSJobInstance(UCSJob ucsJob) {
        id = UUID.randomUUID().toString();
        this.name = ucsJob.getName();
        this.jobId = ucsJob.getId();
        this.jobType = ucsJob.getJobType();
        this.taskConcurrentNum = ucsJob.getTaskConcurrentNum();
        this.priority = ucsJob.getPriority();
        this.taskParam = getJobTaskParam(ucsJob);
        this.status = UCSState.wait;

    }

    private String getJobTaskParam(UCSJob ucsJob) {
        String lastExecParam = ucsJob.getLastExecParam();
        if (lastExecParam == null || "".equals(lastExecParam))
            lastExecParam = ucsJob.getTaskParamDefinition();
        if (lastExecParam == null || lastExecParam.length() == 0) return "";

        try {
            return TaskCommandParamParser.createJobInstanceParam(lastExecParam);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getTaskParam() {
        return taskParam;
    }

    public void setTaskParam(String taskParam) {
        this.taskParam = taskParam;
    }

    public int getTaskConcurrentNum() {
        return taskConcurrentNum;
    }

    public void setTaskConcurrentNum(int taskConcurrentNum) {
        this.taskConcurrentNum = taskConcurrentNum;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UCSJobInstance{" +
                "jobId='" + jobId + '\'' +
                ", jobType='" + jobType + '\'' +
                ", priority=" + priority +
                '}';
    }
}
