package com.qinbin.p2p.net;

import java.util.Map;

/**
 * Created by teacher on 2016/4/23.
 */
public abstract class BaseRequest  {
    public abstract String getUrl();
    public abstract HttpMethod getMethod();
    public abstract Map<String ,String> getParams();
}
