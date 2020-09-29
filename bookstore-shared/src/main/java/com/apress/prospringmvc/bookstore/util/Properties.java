package com.apress.prospringmvc.bookstore.util;

public class Properties {

    public static java.util.Properties of(String ... args){
        java.util.Properties properties = new java.util.Properties();
        int index = 0;
       do {
           properties.setProperty(args[index], args[++index]);
           ++index;
       } while(index < args.length);
       return properties;
    }
}
