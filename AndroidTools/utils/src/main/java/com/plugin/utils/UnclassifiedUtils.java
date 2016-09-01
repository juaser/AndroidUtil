package com.plugin.utils;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 未归类的工具类
 * </pre>
 */
public class UnclassifiedUtils {

   private static volatile UnclassifiedUtils mInstance = null;
   
   private UnclassifiedUtils(){
   }
   
   public static UnclassifiedUtils getInstance() {
       UnclassifiedUtils instance=mInstance;
       if(instance==null){
           synchronized (UnclassifiedUtils.class) {
               instance =mInstance;
               if (instance == null) {
                   instance = new UnclassifiedUtils();
                   mInstance=instance;
               }
           }   
       }
       return instance;
   }




}