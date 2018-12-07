package com.example.zp.hexdecconverter;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Converted {

    public boolean isOriginDec;
    public long decValue;
    public String hexValue;

    public Converted(){

    }

    public Converted(boolean isOriginDec, long decValue, String hexValue){
        this.isOriginDec = isOriginDec;
        this.decValue = decValue;
        this.hexValue = hexValue;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("isOriginDec", isOriginDec);
        result.put("decValue", decValue);
        result.put("hexValue", hexValue);
        return result;
    }

    public long getDecValue(){
        return this.decValue;
    }

    public String getHexValue(){
        return this.hexValue;
    }

}
