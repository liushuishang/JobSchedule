package ucs.jobschedule.impl.dao;

import ucs.jobschedule.impl.model.UCSJob;
import org.springframework.jdbc.core.RowMapper;
import ucs.jobschedule.impl.model.UCSJobInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class UCSJobMapper implements RowMapper {
    public UCSJob mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UCSJob job=new UCSJob();
        job.setId(resultSet.getString("id"));
        job.setName(resultSet.getString("name"));
        job.setJobType(resultSet.getString("job_type"));
        job.setTaskParamDefinition(resultSet.getString("task_param_definition"));
        job.setTaskConcurrentNum(resultSet.getInt("task_concurrent_num"));
        job.setPriority(resultSet.getInt("priority"));
        job.setLastExecParam(resultSet.getString("last_exec_param"));

        return job;
    }
}
