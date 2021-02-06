package ru.appline.baseTests;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class BaseTests {
    static  ChromeOptions options;
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected JavascriptExecutor jse;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
      options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10, 1000);
    }



    @BeforeEach
    void beforeEach() {

        //переходим по ссылке
        String baseUrl = "https://www.sberbank.ru/ru/person";

        driver.get(baseUrl);
        jse = (JavascriptExecutor) driver;
    }

    @AfterAll
   static void afterAll() {
        driver.quit();
    }

}