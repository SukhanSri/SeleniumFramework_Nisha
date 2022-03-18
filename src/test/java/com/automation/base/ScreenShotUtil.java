package com.automation.base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ScreenShotUtil extends BaseTest{
    public static String screenShotPath;

    public static void captureScreenshot(String methodName) throws IOException {

        Calendar cal = new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int date = cal.get(Calendar.DATE);
        int day = cal.get(Calendar.HOUR_OF_DAY);

        //Take screenshot and store it in volatile memory with reference name scrFile
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {

            //Create unique file name and store in under screenshot folder of root directory
            screenShotPath = System.getProperty("user.dir") + "/test-output/screenshots/" + methodName + "_" + year + "_" + date+ "_" + (month + 1) + "_" + day + "_" + min + "_" + sec + ".jpeg";

            //Copy the file to destination
            FileHandler.copy(scrFile, new File(screenShotPath));
            Reporter.log("<a href='"+ screenShotPath + "'> <img src='"+ screenShotPath + "' height='100' width='100'/> </a>");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

    }
}
