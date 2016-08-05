package ucs.jobschedule.impl.model;

/**
 * Created by ucs_yuananyun on 2016/3/4.
 */
public class Named {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String id;
    protected String name;
}
