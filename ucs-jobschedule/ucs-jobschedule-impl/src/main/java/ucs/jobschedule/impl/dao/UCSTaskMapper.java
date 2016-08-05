package ucs.jobschedule.impl.dao;

import org.springframework.jdbc.core.RowMapper;
import ucs.jobschedule.impl.model.UCSTask;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class UCSTaskMapper implements RowMapper {
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UCSTask ucsTask = new UCSTask();
        ucsTask.setId(resultSet.getString("id"));
        ucsTask.setName(resultSet.getString("name"));
        ucsTask.setTaskType(resultSet.getString("task_type"));
        ucsTask.setTaskCommand(resultSet.getString("task_command"));
        ucsTask.setTargetDataBase(resultSet.getString("target_database"));
        ucsTask.setTargetMachine(resultSet.getString("target_machine"));
        ucsTask.setTryCount(resultSet.getInt("try_count"));
        ucsTask.setTimeout(resultSet.getInt("timeout"));
        ucsTask.setFailCommandType(resultSet.getString("fail_command_type"));
        ucsTask.setFailCommand(resultSet.getString("fail_command"));
        ucsTask.setDependent(resultSet.getString("dependent"));
        ucsTask.setStatus('W');

        return ucsTask;
    }

}
