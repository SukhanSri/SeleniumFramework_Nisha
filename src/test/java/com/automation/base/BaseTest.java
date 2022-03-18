package com.automation.base;


import org.openqa.selenium.WebDriver;

public class BaseTest  {

     public static WebDriver driver;

     public void setUpBrowser()
     {
          driver=BrowserSetUp.getBrowserInstance();

     }

}
