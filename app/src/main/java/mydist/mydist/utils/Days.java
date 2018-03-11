package mydist.mydist.utils;

import android.util.Log;

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
    private static final String WEEK_ABBREVIATION = "wk";

    private Days(final String text) {
        this.text = text;
    }

    public static String getTodaysDay() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        return dateFormat.format(date);
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

    public static String[] getFirstDateOfTheMonth() {
        Calendar firstDaYOfMonth = Calendar.getInstance();   // this takes current date
        firstDaYOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String firstDate = dateFormat.format(firstDaYOfMonth.getTime());
        return firstDate.split("/");
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
        return WEEK_ABBREVIATION + weekNo;
    }

    public static String getCurrentDay() {
        Calendar now = Calendar.getInstance();
        int weekNo = now.get(Calendar.WEEK_OF_MONTH);
        if (weekNo < 1) {
            weekNo = 1;
        } else if (weekNo > 4) {
            weekNo = 4;
        }
        int value = now.get(Calendar.DAY_OF_WEEK);
        return getDay(value);
    }

    public static String getDay(int day) {
        switch (day) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thur";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            default:
                return "";
        }
    }
}
