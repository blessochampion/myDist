package mydist.mydist.printing;

public class PrintingFormatter {
    private static final int productNamePartitionSize = 15;
    private static final int ocPartitionSize = 4;
    private static final int opPartitionSize = 4;
    private static final int productTotalSize = 15;
    private static final String EMPTY_SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final int LINE_CHARS_LENGTH = 42;
    private static final String LINE_DIVIDER_ITEM = ".";
    public static final String COMPANY_NAME = "Scenario Developers";
    public static final String COMPANY_ADDRESS = "No 4, Folarin street, Magodo, Lagos";
    public static final String RETAILER = "Retailer:";
    public static final String SALES_REP = "Sales Rep:";
    public static final String INVOICE_NUMBER = "Invoice Number:";
    public static final String TRANSACTION_DATE = "Transaction Date:";


    public static String format(String productName, String oc, String op, String total) {
        String[] partitionedProductName = formatProductName(productName);
        if (partitionedProductName[0].length() < productNamePartitionSize) {
            partitionedProductName[0] = appendEmptySpace(partitionedProductName[0], productNamePartitionSize - partitionedProductName[0].length());
        }
        String formattedLine = partitionedProductName[0];
        formattedLine += EMPTY_SPACE;
        formattedLine += formatOC(oc);
        formattedLine += EMPTY_SPACE;
        formattedLine += formatOP(op);
        formattedLine += EMPTY_SPACE;
        formattedLine += formatTotal(total);
        if(partitionedProductName.length > 1){
            formattedLine += NEW_LINE + partitionedProductName[1];
        }

        return formattedLine;
    }

    public static String[] formatProductName(String productName) {
        int noOfPartitions = productName.length() / productNamePartitionSize;
        if (productName.length() % productNamePartitionSize != 0) {
            noOfPartitions++;
        }
        String[] partitions = new String[noOfPartitions];
        String currentPartition;
        int base;
        int maxOffset = productNamePartitionSize - 1;
        for (int i = 0; i < partitions.length; i++) {
            base = i * productNamePartitionSize;
            if ((productName.length()) <= (base + maxOffset + 1)) {
                currentPartition = productName.substring(base);
            } else {
                currentPartition = productName.substring(base, base + maxOffset + 1);
            }
            partitions[i] = currentPartition;
        }

        return partitions;
    }


    public static String formatOC(String unformattedOC) {
        String formattedOC = unformattedOC;
        if (unformattedOC.length() < ocPartitionSize) {
            int lengthDifference = ocPartitionSize - unformattedOC.length();
            formattedOC = appendEmptySpace(formattedOC, lengthDifference);
        }
        return formattedOC;
    }

    public static String formatOP(String unformattedOP) {
        String formattedOP = unformattedOP;
        if (unformattedOP.length() < opPartitionSize) {
            int lengthDifference = opPartitionSize - unformattedOP.length();
            formattedOP = appendEmptySpace(formattedOP, lengthDifference);
        }
        return formattedOP;
    }

    public static String formatTotal(String unformattedTotal) {
        String formattedTotal = unformattedTotal;
        if (unformattedTotal.length() < productTotalSize) {
            int lengthDifference = productTotalSize - unformattedTotal.length();
            formattedTotal = prependEmptySpace(formattedTotal, lengthDifference);
        }
        return formattedTotal;
    }

    public static String appendEmptySpace(String value, int length) {
        while (length > 0) {
            value += EMPTY_SPACE;
            length--;
        }
        return value;
    }

    public static String prependEmptySpace(String value, int length) {
        while (length > 0) {
            value = EMPTY_SPACE + value;
            length--;
        }
        return value;
    }

    public static String getLineDivider() {
        String divider = LINE_DIVIDER_ITEM;
        for(int i = 1 ; i <  LINE_CHARS_LENGTH; i++){
            divider += LINE_DIVIDER_ITEM;
        }

        return divider;
    }

    public static String formatCompanyName(String companyName) {
        return centerInPage(companyName);
    }

    private static boolean isEven(int length) {
        return length%2 == 0;
    }
    private static  String centerInPage(String text){
        int headerTitleLength = text.length();
        int leftSpaceLength = (LINE_CHARS_LENGTH - headerTitleLength)/2;

        if(isEven(headerTitleLength)){
            text = prependEmptySpace(text, leftSpaceLength);
            text = appendEmptySpace(text,leftSpaceLength);
        }else {
            text = prependEmptySpace(text, leftSpaceLength+1);
            text = appendEmptySpace(text, leftSpaceLength);
        }

        return text;
    }

    public static String formatCompanyAddress(String companyAddress)
    {
        int companyAddressLength = companyAddress.length();
        if(companyAddressLength <= LINE_CHARS_LENGTH){
            return centerInPage(companyAddress);
        }else {
            String formattedCompanyAddress = companyAddress.substring(0, LINE_CHARS_LENGTH);
            formattedCompanyAddress += "\n" + formatCompanyAddress(companyAddress.substring(LINE_CHARS_LENGTH));
            return formattedCompanyAddress;
        }
    }

    public static String formatNameValuePair(String retailerKey, String retailerValue) {
        String formattedName = retailerKey;
        int requiredEmptySpaceDividerLength = LINE_CHARS_LENGTH -(retailerKey.length() + retailerValue.length());
        for(int i = 0; i < requiredEmptySpaceDividerLength; i++){
            formattedName += EMPTY_SPACE;
        }
        formattedName += retailerValue;
        return formattedName;
    }
}
