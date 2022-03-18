package com.automation.page;

import com.automation.utils.HelperUtility;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.asserts.SoftAssert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class ProductPage extends BasePage {
    WebDriver driver;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /*
     *  WebElements : sortButton
     * */
    @FindBy(xpath="//*[@class='a-button a-button-dropdown a-button-small']/span/span")
    WebElement sortButton;

    /*
     *  Method : Click on Sort Button
     * */
    public void clickSortButton()
     {
         sortButton.click();
     }

    @FindAll({@FindBy(xpath="//*[@class='a-dropdown-item']/a")
    })
    List<WebElement> sortingOptions;

    /*
     *  Method : Select Sorting option
     * */
    public void selectFromSortMenu(String featureToBeSorted)
    {

        for( int i=0;i<sortingOptions.size();i++)
        {

            String dropDownOptions = sortingOptions.get(i).getAttribute("textContent");

            if(StringUtils.equalsIgnoreCase(dropDownOptions.toLowerCase(Locale.ROOT).trim(),
                    featureToBeSorted.toLowerCase(Locale.ROOT).trim())){
                sortingOptions.get(i).click();
            }
        }
    }

    /*
     *  Method : Sort the Product search Results
     * */
    public void sortProductByPrice(String sortedBy){
        clickSortButton();
        selectFromSortMenu(sortedBy);
    }

    /*
     *  WebElements : Declaration Result WebElements as String
     * */
    String mainSearchResultsElement="//*[contains(@cel_widget_id,'MAIN-SEARCH_RESULTS-{{XXXX}}')]";
    String productNameElement = "//span[contains(@class,'a-size-medium a-color-base a-text-normal')]";
    String prodRatingIconElement = "//*[contains(@class,'a-icon a-icon-star-small')]/span";
    String prodPriceDollarSymbolElement = "//span[contains(@class,'a-price')]//span[contains(@class,'a-price-symbol')]";
    String prodPriceDollarWholeValueElement = "//span[contains(@class,'a-price-whole')]";
    String prodPriceDollarFractionValueElement = "//span[contains(@class,'a-price-fraction')]";

    /*
    *  Verify Product Search Result Display with ProductName, Rating, Price
    * */
    public void verifyProductSearchResults(int noOfProductToRead, String searchedProductName){


        SoftAssert softAssert = new SoftAssert();

        List<WebElement> mainSearchResultsWebElements = driver.findElements(By.xpath(mainSearchResultsElement.replace("{{XXXX}}","")));
        int totalSearchResults = mainSearchResultsWebElements.size();

        if(totalSearchResults > 0){

            for(int i=1;i<(noOfProductToRead);i++){

                try{

                    String resultCell = mainSearchResultsElement.replace("{{XXXX}}",String.valueOf(i));

                    //Get Product Name
                    System.out.println("result Cell :"+resultCell);
                    String prodNameWebElementInResult = resultCell+productNameElement;

                    JavascriptExecutor js =(JavascriptExecutor)driver;
                    WebElement prodWebElement = driver.findElement(By.xpath(prodNameWebElementInResult));
                    js.executeScript("arguments[0].scrollIntoView(true);",prodWebElement);


                    String prodName = prodWebElement.getText();

                    //Get Rating Info
                    String prodRatingInfoWebElementInResult = resultCell+prodRatingIconElement;
                    List<WebElement> ratingIconWebElements = driver.findElements(By.xpath(prodRatingInfoWebElementInResult));
                    String prodRatingInfo = "";

                    if(ratingIconWebElements == null || ratingIconWebElements.size() == 0){
                        prodRatingInfo= "Not Available";
                    }else{
                        prodRatingInfo=ratingIconWebElements.get(0).getAttribute("textContent");
                    }

                    //Get Product Price
                    String prodPriceDollarSymbolInfoWebElementInResult = resultCell+prodPriceDollarSymbolElement;
                    List<WebElement> prodPriceDollarSymbolWebElements = driver.findElements(By.xpath(prodPriceDollarSymbolInfoWebElementInResult));
                    String dollarWholeValue = null,dollarFractionalValue = null;

                    if(!(prodPriceDollarSymbolWebElements == null && prodPriceDollarSymbolWebElements.size() == 0)){
                        String dollarSymbol = driver.findElement(By.xpath(prodPriceDollarSymbolInfoWebElementInResult)).getText();
                        System.out.println("dollarSymbol: "+dollarSymbol);

                        String prodPriceDollarWholeValueInfoWebElementInResult = resultCell+prodPriceDollarWholeValueElement;
                        dollarWholeValue =driver.findElement(By.xpath(prodPriceDollarWholeValueInfoWebElementInResult)).getText();
                        System.out.println(", dollarWholeValue: "+dollarWholeValue);

                        String prodPriceDollarFractionValueInfoWebElementInResult = resultCell+prodPriceDollarFractionValueElement;
                        dollarFractionalValue =driver.findElement(By.xpath(prodPriceDollarFractionValueInfoWebElementInResult)).getText();
                        System.out.println(", dollarFractionValue: "+dollarFractionalValue);
                    }

                    if(!StringUtils.contains(prodName.toLowerCase(Locale.ROOT),searchedProductName.toLowerCase(Locale.ROOT))){
                        softAssert.assertTrue(false,"Search Result ProductName:"+prodName+" contains no searched Productname:"+prodName);
                    }else {
                        softAssert.assertTrue(true,"Search Result ProductName:"+prodName+" contains searched Productname:"+prodName);
                    }


                    softAssert.assertTrue(HelperUtility.checkStringIsNumeric(dollarWholeValue),"Search Result ProductName:"+prodName+" Price's Whole Number is Numeric:");
                    softAssert.assertTrue(HelperUtility.checkStringIsNumeric(dollarFractionalValue),"Search Result ProductName:"+prodName+" Price's Fractional Number is Numeric:");


                    softAssert.assertTrue(HelperUtility.patternMatching(prodRatingInfo,"[0-5].[0-9] out of 5 stars"),
                            "Search Result ProductName:"+prodName+" Rating Info Matching Pattern [0-5].[0-9] out of 5 stars");

                }catch (Exception e){
                    softAssert.assertTrue(false,
                            "Exception happened. Exception :"+e.getMessage());
                }
            }
        }else{
            softAssert.assertTrue(false,"No Search Results.");
        }

        softAssert.assertAll();
    }

    public void verifyProductSortedByPrice(int topProductNumbers,String sortedBy){


        SoftAssert softAssert = new SoftAssert();
        ArrayList<Double> pricesRetrievedFromResult = new ArrayList<Double>();

        List<WebElement> mainSearchResultsWebElements = driver.findElements(By.xpath(mainSearchResultsElement.replace("{{XXXX}}","")));
        int totalSearchResults = mainSearchResultsWebElements.size();

        if(totalSearchResults > 0){

            for(int i=1;i<(topProductNumbers);i++){


                try{

                    String resultCell = mainSearchResultsElement.replace("{{XXXX}}",String.valueOf(i));

                    //Get Product Name
                    System.out.println("result Cell :"+resultCell);
                    String prodNameWebElementInResult = resultCell+productNameElement;

                    JavascriptExecutor js =(JavascriptExecutor)driver;
                    WebElement prodWebElement = driver.findElement(By.xpath(prodNameWebElementInResult));
                    js.executeScript("arguments[0].scrollIntoView(true);",prodWebElement);

                    //Get Product Price
                    String prodPriceDollarSymbolInfoWebElementInResult = resultCell+prodPriceDollarSymbolElement;
                    List<WebElement> prodPriceDollarSymbolWebElements = driver.findElements(By.xpath(prodPriceDollarSymbolInfoWebElementInResult));
                    String dollarWholeValue = null,dollarFractionalValue = null;

                    if(!(prodPriceDollarSymbolWebElements == null && prodPriceDollarSymbolWebElements.size() == 0)){
                        String dollarSymbol = driver.findElement(By.xpath(prodPriceDollarSymbolInfoWebElementInResult)).getText();
                        System.out.println("dollarSymbol: "+dollarSymbol);

                        String prodPriceDollarWholeValueInfoWebElementInResult = resultCell+prodPriceDollarWholeValueElement;
                        dollarWholeValue =driver.findElement(By.xpath(prodPriceDollarWholeValueInfoWebElementInResult)).getText();
                        System.out.println(", dollarWholeValue: "+dollarWholeValue);

                        String prodPriceDollarFractionValueInfoWebElementInResult = resultCell+prodPriceDollarFractionValueElement;
                        dollarFractionalValue =driver.findElement(By.xpath(prodPriceDollarFractionValueInfoWebElementInResult)).getText();
                        System.out.println(", dollarFractionValue: "+dollarFractionalValue);

                        pricesRetrievedFromResult.add(Double.parseDouble(dollarWholeValue.replace(",","")+"."+dollarFractionalValue.replace(",","")));
                    }

                }catch (Exception e){
                    softAssert.assertTrue(false,
                            "Exception happened. Exception :"+e.getMessage());
                }
            }

            ArrayList<Double> copy = new ArrayList<>();
            copy.addAll(pricesRetrievedFromResult);

            if(StringUtils.equalsAnyIgnoreCase(sortedBy,"desc")){
                Collections.sort(copy, Collections.reverseOrder());
            }else if(StringUtils.equalsAnyIgnoreCase(sortedBy,"asc")){
                Collections.sort(copy);
            }

            softAssert.assertTrue(pricesRetrievedFromResult.equals(copy),"Price Sorted By:"+sortedBy);

        }else{
            softAssert.assertTrue(false,"No Search Results.");
        }

        softAssert.assertAll();
    }


}



