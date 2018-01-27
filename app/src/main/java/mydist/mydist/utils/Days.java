package mydist.mydist.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Blessing.Ekundayo on 12/28/2017.
 */

public enum Days {
    ALL("ALL"),
    SUNDAY("SUNDAY"),
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
    SUN("Sun"),
    MON("Mon"),
    TUE("Tue"),
    WED("Wed"),
    THUR("Thur"),
    FRI("Fri"),
    SAT("Sat");
    private final String text;

    private Days(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String getTodayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = dateFormat.format(new Date());
        return todayDate;
    }

    public static String getRetailerDate() {
        return "R" + getTodayDate();
    }

    public static String getThisWeek() {
        Calendar now = Calendar.getInstance();
        int weekNo = now.get(Calendar.WEEK_OF_MONTH);
        if (weekNo < 1) {
            weekNo = 1;
        } else if (weekNo > 4) {
            weekNo = 4;
        }

        return "wk" + weekNo;
    }
}
