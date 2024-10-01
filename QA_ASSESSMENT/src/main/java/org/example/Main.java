package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Main {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Explicit wait
    }

    @Test(priority = 1)
    void verifyJumiaLandingPage() {
        // Step 1: Navigate to e-commerce website
        driver.get("https://www.jumia.com.ng/");
        String pageTitle = driver.getTitle();
        assertEquals(pageTitle, "Jumia Nigeria | Online Shopping for Electronics, Fashion, Home, Beauty & Sport");
    }

    @Test(priority = 2)
    void searchForItem() {
        // Step 2: Search for a specific product
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='fi-q']")));
        searchBox.sendKeys("laptop");
        searchBox.submit();

        // Wait for the search results and assert first product is displayed
        WebElement firstResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Hp PROBOOK 450G6 8TH GEN INTEL CORE I5 8GB RAM 256')]")));
        Assert.assertTrue(firstResult.isDisplayed(), "Product is displayed");
    }
//
    @Test(priority = 3)
    void addToCart() {
        // Step 3: Click on the first product
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[contains(text(),'Hp PROBOOK 450G6 8TH GEN INTEL CORE I5 8GB RAM 256')]")));
        firstProduct.click();

        // Step 4: Add the product to the cart
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Add to cart')]")));
        addToCartButton.click();

        // Assert that the product was added to the cart (Cart popup or confirmation message)
        WebElement cartConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'cart-popup')]")));
        Assert.assertTrue(cartConfirmation.isDisplayed(), "Product successfully added to the cart");
    }

    @Test(priority = 4)
    void proceedToCheckout() {
        // Step 5: Click the proceed to checkout button
        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'/cart')]")));
        checkoutButton.click();

        // Step 6: Verify the user is on the checkout page
        WebElement checkoutPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Cart Summary')]")));
        Assert.assertTrue(checkoutPageTitle.isDisplayed(), "Checkout page is displayed");
    }

    @AfterClass
    void tearDown() {
        // Quit the browser window
        if (driver != null) {
            driver.quit();
        }
    }
}
