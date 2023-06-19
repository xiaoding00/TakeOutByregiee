package com.itidng.result;


//报错当前员工ID
public class CurrentId {

    //threadLocal用于保存当前线程员工ID
    public static ThreadLocal<Long>  threadLocal =new ThreadLocal();

    //存入ID
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    //取出ID
    public static Long getCurrentId(){
        Long id= threadLocal.get();
//        threadLocal.remove();
        return id;
    }

}
