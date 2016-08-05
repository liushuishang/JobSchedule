package ucs.jobschedule.impl.dao;

import org.springframework.jdbc.core.RowMapper;
import ucs.jobschedule.impl.model.UCSTaskInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yuananyun on 2016/3/6.
 */
public class UCSTaskInstanceMapper implements RowMapper {
    public UCSTaskInstance mapRow(ResultSet resultSet, int i) throws SQLException {
        UCSTaskInstance taskInstance = new UCSTaskInstance();
        taskInstance.setId(resultSet.getString("id"));
//        taskInstance.setName(resultSet.getString("name"));
        taskInstance.setTaskId(resultSet.getString("task_id"));
        taskInstance.setJobInstanceId(resultSet.getString("job_instance_id"));
        taskInstance.setTaskType(resultSet.getString("task_type"));
        taskInstance.setTaskCommand(resultSet.getString("task_command"));
        taskInstance.setTaskCommandParam(resultSet.getString("task_command_param"));
        taskInstance.setTaskFailCommand(resultSet.getString("fail_command"));
        taskInstance.setTaskFailCommandType(resultSet.getString("fail_command_type"));
        taskInstance.setTargetDatabase(resultSet.getString("target_database"));
        taskInstance.setTargetMachine(resultSet.getString("target_machine"));
        taskInstance.setTryCount(resultSet.getInt("try_count"));
        taskInstance.setTimeout(resultSet.getLong("timeout"));
        taskInstance.setCreatedDate(resultSet.getString("created_date"));

        return taskInstance;
    }
}
