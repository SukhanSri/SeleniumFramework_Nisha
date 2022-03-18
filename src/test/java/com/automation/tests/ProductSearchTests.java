package com.automation.tests;



import com.automation.base.BaseTest;
import com.automation.page.ProductPage;
import com.automation.page.SearchPage;
import org.testng.annotations.*;

import static com.automation.page.BasePage.applicationURL;


public class ProductSearchTests extends BaseTest {

    ProductPage productPage;
    SearchPage searchPage;

   @DataProvider(name="testDataForSearchProducts")
    Object[][] testDataForSearchProducts()
    {
        String searchProducts[][]={{"Bluetooth headset"},{"34 inch LED monitor"}};
        return (searchProducts);
    }

    @BeforeTest
    public void setUp()
    {
        setUpBrowser();
        driver.get(applicationURL);
    }

    @Test(alwaysRun=true,description = "Enter the ProductNames, and verify that top 3 products appearing correctly",dataProvider= "testDataForSearchProducts")
    public void test_search_products(String proName)  {
       searchPage=new SearchPage(driver);
       productPage = new ProductPage(driver);

       searchPage.enterProductNameToBeSearched(proName);
       searchPage.clickSearchButton();

       productPage.verifyProductSearchResults(3,proName);

    }

    @Test(alwaysRun=true,description = "Enter the ProductNames, sort price from high to low.",dependsOnMethods={"test_search_products"})
    public void test_sort_searched_product_by_price_high_to_low() {

        searchPage=new SearchPage(driver);


        searchPage.enterProductNameToBeSearched("Bluetooth headset");
        searchPage.clickSearchButton();

        //Sort By Price : High to Low
        productPage.sortProductByPrice("Price: High to Low");

        productPage.verifyProductSortedByPrice(3,"desc");

    }

    @AfterTest
    public void teardown()
    {
        driver.close();
    }

}
