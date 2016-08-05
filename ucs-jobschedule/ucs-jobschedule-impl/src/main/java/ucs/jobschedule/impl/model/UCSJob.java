package ucs.jobschedule.impl.model;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class UCSJob extends Named {
    private String jobType;
    private String taskParamDefinition;
    private int taskConcurrentNum;
    private int priority;
    private String lastExecParam;

    public UCSJob() {
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


    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getLastExecParam() {
        return lastExecParam;
    }

    public void setLastExecParam(String lastExecParam) {
        this.lastExecParam = lastExecParam;
    }

    @Override
    public String toString() {
        return "UCSJob{" +
                "jobType='" + jobType + '\'' +
                ", priority=" + priority +
                '}';
    }

    public String getTaskParamDefinition() {
        return taskParamDefinition;
    }

    public void setTaskParamDefinition(String taskParamDefinition) {
        this.taskParamDefinition = taskParamDefinition;
    }
}
