package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 2/11/2018.
 */

public class DatabaseLogicUtils {
    public static final String HPV_DELIMITER = ":";
    public static final String DEFAULT_HPV = "0.00";


    public static String getHighestPurchase(String values) {
        String[] valuesArr = values.split(HPV_DELIMITER);
        double highestValue = Double.parseDouble(valuesArr[0]);
        double temp;
        for (int i = 1; i < 4; i++) {
            temp = Double.parseDouble(valuesArr[i]);
            if (temp > highestValue) {
                highestValue = temp;
            }
        }
        return String.valueOf(highestValue);
    }


    public static String getHighestPurchaseEver(String values) {
        String[] valuesArr = values.split(HPV_DELIMITER);
        double highestValue = Double.parseDouble(valuesArr[0]);
        double temp;
        for (int i = 1; i < valuesArr.length; i++) {
            temp = Double.parseDouble(valuesArr[i]);
            if (temp > highestValue) {
                highestValue = temp;
            }
        }
        return String.valueOf(highestValue);
    }

    public static String deleteRecentPurchase(String values) {
        String[] valuesArr = values.split(HPV_DELIMITER);
        String result = valuesArr[1];
        for (int i = 2; i < values.length(); i++) {
            result += (HPV_DELIMITER + valuesArr[i]);
        }
        result += HPV_DELIMITER + DEFAULT_HPV;
        return result;
    }

    public static String getDefaultHpv() {
        return DEFAULT_HPV + HPV_DELIMITER + DEFAULT_HPV + HPV_DELIMITER +
                DEFAULT_HPV + HPV_DELIMITER + DEFAULT_HPV;
    }
}
