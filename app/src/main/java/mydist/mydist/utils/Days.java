package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 12/28/2017.
 */

public enum Days
{
    ALL("ALL"),
    SUNDAY("SUNDAY"),
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY");
    private final String text;

    private Days(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
