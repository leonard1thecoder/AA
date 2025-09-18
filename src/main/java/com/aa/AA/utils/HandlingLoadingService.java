package com.aa.AA.utils;

public class HandlingLoadingService {
    public static String  handleServiceHandler(String serviceHandler){
      var string = "";
        if(serviceHandler == null)
            string = "START_SERVICE";
        else
            string = serviceHandler;
        return string;
    }
}
