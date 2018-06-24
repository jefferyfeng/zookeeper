package com.baizhi.bean;

import java.io.Serializable;

public class MethodInvokeMeta implements Serializable{
    private Class<?> targetInterfaces;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public Class<?> getTargetInterfaces() {
        return targetInterfaces;
    }

    public void setTargetInterfaces(Class<?> targetInterfaces) {
        this.targetInterfaces = targetInterfaces;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
