package com.baizhi.local;

public interface RpcClient {
    Result call(MethodInvokeMeta mim, HostAndPort hap);
}
