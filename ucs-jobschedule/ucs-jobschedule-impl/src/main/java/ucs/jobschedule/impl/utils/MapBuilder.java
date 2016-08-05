package ucs.jobschedule.impl.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class MapBuilder {
    private Map<Object,Object> _map;

    public MapBuilder() {
        _map=new HashMap<Object, Object>();
    }

    public static MapBuilder instance() {
        return new MapBuilder();
    }
    public MapBuilder put(Object key,Object value){
        _map.put(key, value);
        return this;
    }
    public Map map(){
        return _map;
    }
}
