package com.baizhi.local;

import com.baizhi.bean.HostAndPort;
import com.baizhi.bean.MethodInvokeMeta;
import com.baizhi.bean.Result;

public interface RpcClient {
    Result call(MethodInvokeMeta mim, HostAndPort hap);
}
