package ucs.jobschedule.impl.parser;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.*;

/**
 * 任务的命令参数解析器
 * Created by ucs_yuananyun on 2016/3/9.
 */
public class TaskCommandParamParser {

    //    [
//    {
//        "name": "KETTLE_PATH",
//            "defaultValue": "/opt/kettle/data-integration",
//            "value":""
//        "increment": -1,
//            "canAdditive": false,
//            "type": "string(date、string、number)",
//            "unit": ""(for data:yyyy,MM,dd,HH,mm,ss)
//    },
//    {
//        "name": "yesterday",
//            "defaultValue": "2016-03-08",
//            "value":""
//        "increment": -1,
//            "canAdditive": true,
//            "type": "date",
//            "unit": "dd"
//    }
//
//    ]
    private static final String NAME_FIELD = "name";
    private static final String CAN_ADDITIVE_FIELD = "canAdditive";
    private static final String INCREMENT_FIELD = "increment";
    private static final String DEFAULT_VALUE_FIELD = "defaultValue";
    private static final String VALUE_FIELD = "value";
    private static final String TYPE_FIELD = "type";
    private static final String DATE_PATERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取Job实例的参数
     *
     * @param lastParam
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public static String createJobInstanceParam(String lastParam) throws ParseException {
        if (StringUtils.isBlank(lastParam)) return null;
        List<Map> lastParamObjectArray = JSON.parseArray(lastParam, Map.class);

        for (Map paramMap : lastParamObjectArray) {
            if (paramMap.size() == 0) continue;
            if(MapUtils.getString(paramMap, VALUE_FIELD)==null)
                paramMap.put(VALUE_FIELD, MapUtils.getString(paramMap, DEFAULT_VALUE_FIELD));

            //如果不能累加就保持默认
            boolean canAdditive = MapUtils.getBoolean(paramMap, CAN_ADDITIVE_FIELD);
            if (!canAdditive) continue;
            //如果参数的增量为空则跳过
            String increment = MapUtils.getString(paramMap, INCREMENT_FIELD);
            if (StringUtils.isBlank(increment)) continue;

            String paramType = MapUtils.getString(paramMap, TYPE_FIELD);

            if ("string".equals(paramType)) {
                String lastValue = MapUtils.getString(paramMap, VALUE_FIELD);
                paramMap.put(VALUE_FIELD, lastValue + increment);
            } else if ("number".equals(paramType)) {
                Long lastValue = MapUtils.getLong(paramMap, VALUE_FIELD);
                paramMap.put(VALUE_FIELD, lastValue + Long.parseLong(increment));
            } else if ("date".equals(paramType)) {
                String lastValue = MapUtils.getString(paramMap, VALUE_FIELD);
                if (StringUtils.isBlank(lastValue)) continue;
                if (!lastValue.contains(":")&&lastValue.length()<11)
                    lastValue = lastValue.trim() + " 00:00:00";

                Date lastDate = DateUtils.parseDate(lastValue, new String[]{DATE_PATERN});
                String unit = MapUtils.getString(paramMap, "unit");
                if(StringUtils.isBlank(unit)) continue;

                int dateDelta = Integer.parseInt(increment);
                if ("yyyy".equals(unit)) {
                    lastDate = DateUtils.addYears(lastDate, dateDelta);
                } else if ("MM".equals(unit)) {
                    lastDate = DateUtils.addMonths(lastDate, dateDelta);
                } else if ("dd".equals(unit)) {
                    lastDate = DateUtils.addDays(lastDate, dateDelta);
                } else if ("HH".equals(unit)) {
                    lastDate = DateUtils.addHours(lastDate, dateDelta);
                } else if ("mm".equals(unit)) {
                    lastDate = DateUtils.addMinutes(lastDate, dateDelta);
                } else if ("ss".equals(unit)) {
                    lastDate = DateUtils.addSeconds(lastDate, dateDelta);
                }
                paramMap.put(VALUE_FIELD, DateFormatUtils.format(lastDate, DATE_PATERN));
            }
        }
        return JSON.toJSONString(lastParamObjectArray);
    }

    /**
     * 获取任务实例的参数
     *
     * @param jobInstanceParam
     * @return
     */
    public static String createTaskInstanceParam(String jobInstanceParam) {
        if (StringUtils.isBlank(jobInstanceParam)) return null;
        List<Map<String, Object>> paramMapList = new LinkedList<Map<String, Object>>();
        Map<String, Object> paramMap = null;
        List<Map> lastParamObjectArray = JSON.parseArray(jobInstanceParam, Map.class);
        for (Map param : lastParamObjectArray) {
            if (param.size() == 0) continue;
            paramMap = new HashMap<String, Object>();
            paramMap.put(NAME_FIELD, param.get(NAME_FIELD));
            paramMap.put(VALUE_FIELD, param.get(VALUE_FIELD));

            paramMapList.add(paramMap);
        }
        return JSON.toJSONString(paramMapList);
    }

}
