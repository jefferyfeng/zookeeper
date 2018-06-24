package com.baizhi.api_test;

import com.baizhi.bean.MethodInvokeMeta;
import com.baizhi.service.DemoService;

public class TestPrintf {
    public static void main(String[] args) {
        MethodInvokeMeta request = new MethodInvokeMeta();
        request.setTargetInterfaces(DemoService.class);
        request.setMethod("sum");
        request.setParameterTypes(new Class[]{Integer.class,Integer.class});
        request.setArgs(new Object[]{2,3});
        System.out.println("Send Request: ");
        System.out.printf("[%-13s]%s\n","interface"," => "+request.getTargetInterfaces());
        System.out.printf("[%-13s]%s\n","method"," => "+request.getMethod());
        System.out.printf("[%-13s]%s","parameterType"," => ");
        for (int i = 0; i < request.getParameterTypes().length; i++) {
            if(i==request.getParameterTypes().length-1){
                System.out.println(request.getParameterTypes()[i]);
            }else{
                System.out.print(request.getParameterTypes()[i]+",");
            }
        }
        System.out.printf("[%-13s]%s","args"," => ");
        for (int i = 0; i < request.getArgs().length; i++) {
            if(i==request.getArgs().length-1){
                System.out.println(request.getArgs()[i]);
            }else{
                System.out.print(request.getArgs()[i]+",");
            }
        }
    }
}
