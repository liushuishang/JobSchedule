package ucs.jobschedule.impl.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Date;


/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public abstract class BaseManager {
    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;



}
