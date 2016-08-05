package ucs.jobschedule.impl.model;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class UCSTask extends Named {
    private String dependent;
    private String  taskType;
    private String taskCommand;
    private String taskParam;
    private String targetDataBase;
    private String targetMachine;
    private int tryCount;
    private long timeout;
    private String failCommand;
    private String failCommandType;
    private char status;


    public String getDependent() {
        return dependent;
    }

    public void setDependent(String dependent) {
        this.dependent = dependent;
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

    public String getTaskParam() {
        return taskParam;
    }

    public void setTaskParam(String taskParam) {
        this.taskParam = taskParam;
    }

    public String getTargetDataBase() {
        return targetDataBase;
    }

    public void setTargetDataBase(String targetDataBase) {
        this.targetDataBase = targetDataBase;
    }

    public String getTargetMachine() {
        return targetMachine;
    }

    public void setTargetMachine(String targetMachine) {
        this.targetMachine = targetMachine;
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

    public String getFailCommand() {
        return failCommand;
    }

    public void setFailCommand(String failCommand) {
        this.failCommand = failCommand;
    }

    public String getFailCommandType() {
        return failCommandType;
    }

    public void setFailCommandType(String failCommandType) {
        this.failCommandType = failCommandType;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
