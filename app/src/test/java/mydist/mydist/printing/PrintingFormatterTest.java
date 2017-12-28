package mydist.mydist.printing;

import org.junit.Assert;
import org.junit.Test;

public class PrintingFormatterTest
{
    /*Tests for ProductName*/
    @Test
    public void formatProductNameLessThanPartitionSize(){
        Assert.assertArrayEquals(new String[]{"As"},
                PrintingFormatter.formatProductName("As"));
    }

    @Test
    public void formatProductNameEqualsPartitionSize(){
        Assert.assertArrayEquals(new String[]{"As1swedsw3dfres"},
                PrintingFormatter.formatProductName("As1swedsw3dfres"));
    }

    @Test
    public void formatProductNameGreaterPartitionSize(){
        Assert.assertArrayEquals(new String[]{"As1swedsw3dfres", "t1"},
                PrintingFormatter.formatProductName("As1swedsw3dfrest1"));
    }

    /*Test for OC*/
    @Test
    public void formatOC(){
        Assert.assertEquals("2   ", PrintingFormatter.formatOC("2"));
    }

    /*Tests for  OP and Total*/
    @Test
    public void formatOP(){
        Assert.assertEquals("2   ", PrintingFormatter.formatOP("2"));
    }

    @Test
    public void formatOPAlreadyFormatted(){
        Assert.assertEquals("2345", PrintingFormatter.formatOP("2345"));
    }

    /*Test Space appender*/
    @Test
    public void appendEmptySpaceToZeroLengthDifference(){
        Assert.assertEquals("2" , PrintingFormatter.appendEmptySpace("2", 0));
    }

    @Test
    public void appendEmptySpaceToLengthDifferenceGreaterThanZero(){
        Assert.assertEquals("2  ", PrintingFormatter.appendEmptySpace("2", 2));
    }

    /*Test Space prepender*/
    @Test
    public void prependEmptySpaceToZeroLengthDifference(){
        Assert.assertEquals("2" , PrintingFormatter.prependEmptySpace("2", 0));
    }
    @Test
    public void prependEmptySpaceToLengthDifferenceGreaterThanZero(){
        Assert.assertEquals("  2", PrintingFormatter.prependEmptySpace("2", 2));
    }

    /*Test Total*/
    @Test
    public void formatTotal(){
        Assert.assertEquals("           2.00", PrintingFormatter.formatTotal("2.00"));
    }

    @Test
    public void singleValues(){
        Assert.assertEquals("a               1    1               1.00",
                PrintingFormatter.format("a", "1", "1", String.format("%,.2f",1.00)));
    }

    @Test
    public void commaSeparatedTotal(){
        Assert.assertEquals("a               1    1           1,000.00",
                PrintingFormatter.format("a", "1", "1", String.format("%,.2f",1000.00)));
    }

    @Test
    public void newLineSeparatedLongProductName(){
        Assert.assertEquals("aaaaaaaaaaaaaaa 1    1           1,000.00\na",
                PrintingFormatter.format("aaaaaaaaaaaaaaaa", "1", "1", String.format("%,.2f",1000.00)));
    }

    /*Test the Title*/
    @Test
    public void titleCorrectlyPositioned(){
        Assert.assertEquals("Product         OC   OP             Price", PrintingFormatter.format(
                "Product", "OC", "OP", "Price"
        ));
    }

    /*Test the Line divider*/
    @Test
    public void lineDivider(){
        Assert.assertEquals("..........................................", PrintingFormatter.getLineDivider() );
    }

    /*Test header Title*/
    @Test
    public void headerTitleFullLength(){
        Assert.assertEquals("rgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswde",
                PrintingFormatter.formatCompanyName("rgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswde"));
    }

    @Test
    public void companyNameLengthLessByOne(){
        Assert.assertEquals(" rgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswd",
                PrintingFormatter.formatCompanyName("rgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswd"));
    }

    @Test
    public void companyNameLengthLessByEven(){
        Assert.assertEquals(" rgfgfdertygfrthjubfgtrewsdfrtyuinhgdresw ",
                PrintingFormatter.formatCompanyName("rgfgfdertygfrthjubfgtrewsdfrtyuinhgdresw"));
    }

    @Test
    public void companyNameLengthLessByOdd(){
        Assert.assertEquals("  rgfgfdertygfrthjubfgtrewsdfrtyuinhgdres ",
                PrintingFormatter.formatCompanyName("rgfgfdertygfrthjubfgtrewsdfrtyuinhgdres"));
    }

    /*Test Company Address*/
    @Test
    public void companyAddressLessThanMaxLineChars(){
        Assert.assertEquals(" rgfgfdertygfrthjubfgtrewsdfrtyuinhgdresw ",
                PrintingFormatter.formatCompanyAddress("rgfgfdertygfrthjubfgtrewsdfrtyuinhgdresw"));
    }
    @Test
    public void companyAddressEqualsMaxLineChars(){
        Assert.assertEquals("qrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswq",
                PrintingFormatter.formatCompanyAddress("qrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswq"));
    }

    @Test
    public void companyAddressGreaterThanOneLine(){
        Assert.assertEquals("qrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswq\n                     a                    ",
                PrintingFormatter.formatCompanyAddress("qrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswqa"));
    }

    @Test
    public void companyAddressGreaterTwoLines(){
        Assert.assertEquals("qrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswq\nqrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswq\n                     a                    ",
                PrintingFormatter.formatCompanyAddress("qrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswqqrgfgfdertygfrthjubfgtrewsdfrtyuinhgdreswqa"));
    }

    /*Test */

}