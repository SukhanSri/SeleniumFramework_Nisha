package com.automation.utils;

import java.util.regex.Pattern;

public class HelperUtility {

    public static boolean checkStringIsNumeric(String strNum){

        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean patternMatching(String actualStr,String pattern){

        return Pattern.matches(pattern,actualStr);

    }
}
