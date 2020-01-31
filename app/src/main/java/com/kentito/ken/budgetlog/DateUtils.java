package com.kentito.ken.budgetlog;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateUtils {
    private SimpleDateFormat sdf;
    private static DateUtils instance = null;
    private DateUtils(){
        sdf = new SimpleDateFormat("M/dd/YY HH:mm", Locale.getDefault());
    }
    String getTime(){
        return sdf.format(new Date());
    }
    static DateUtils getInstance(){
        if (instance == null){
            instance = new DateUtils();
        }
        return instance;
    }

}
