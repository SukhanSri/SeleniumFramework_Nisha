package com.automation.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage extends BasePage{
    WebDriver driver;



    public SearchPage(WebDriver driver)
    {
        this.driver=driver;
        PageFactory.initElements(driver,this);
    }



    //find the web element of the search field
    @FindBy(id="twotabsearchtextbox")
    WebElement searchTextField;

    @FindBy(id="nav-search-submit-button")
    WebElement searchButton;

    //Enter the text to be searched
    public void enterProductNameToBeSearched(String ProductName)
    {
        searchTextField.clear();
        searchTextField.sendKeys(ProductName);
    }
    //Click on SearchButton
    public void clickSearchButton()
    {
            searchButton.click();
    }

}
