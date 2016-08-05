package ucs.jobschedule.impl.model;

/**
 * Created by yuananyun on 2016/3/6.
 */
public class UCSTaskInstance extends Named {
    private String taskId;
    private String jobInstanceId;
    private String taskType;
    private String taskCommand;
    private String taskCommandParam;
    private String taskFailCommand;
    private String taskFailCommandType;

    private String targetDatabase;
    private String targetMachine;
    private String createdDate;

    private int tryCount;
    private long timeout;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(String jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskCommand() {
        return taskCommand;
    }

    public void setTaskCommand(String taskCommand) {
        this.taskCommand = taskCommand;
    }

    public String getTaskCommandParam() {
        return taskCommandParam;
    }

    public void setTaskCommandParam(String taskCommandParam) {
        this.taskCommandParam = taskCommandParam;
    }

    public String getTaskFailCommand() {
        return taskFailCommand;
    }

    public void setTaskFailCommand(String taskFailCommand) {
        this.taskFailCommand = taskFailCommand;
    }

    public String getTaskFailCommandType() {
        return taskFailCommandType;
    }

    public void setTaskFailCommandType(String taskFailCommandType) {
        this.taskFailCommandType = taskFailCommandType;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public String getTargetMachine() {
        return targetMachine;
    }

    public void setTargetMachine(String targetMachine) {
        this.targetMachine = targetMachine;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "UCSTaskInstance{" +
                "taskId='" + taskId + '\'' +
                ", taskType='" + taskType + '\'' +
                '}';
    }
}


