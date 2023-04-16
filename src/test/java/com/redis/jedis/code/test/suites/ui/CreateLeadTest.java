package com.redis.jedis.code.test.suites.ui;

import com.github.javafaker.Faker;
import com.redis.jedis.code.practices.datastructures.usecases.RedisManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import java.time.Duration;

public class CreateLeadTest {

    private RedisManager redis;
    private ChromeDriver driver;
    private Faker faker;

    @BeforeClass
    public void login() {
        redis = new RedisManager();
        faker = new Faker();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.get("http://leaftaps.com/opentaps");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("DemoSalesManager");
        driver.findElement(By.id("password")).sendKeys("crmsfa");
        driver.findElement(By.className("decorativeSubmit")).click();
        driver.findElement(By.linkText("CRM/SFA")).click();
    }

    @Test
    public void createNewLead() {
        try {
            driver.findElement(By.linkText("Create Lead")).click();
            driver.findElement(By.id("createLeadForm_companyName")).sendKeys(faker.company().name());
            driver.findElement(By.id("createLeadForm_firstName")).sendKeys(faker.name().firstName());
            driver.findElement(By.id("createLeadForm_lastName")).sendKeys(faker.name().lastName());
            driver.findElement(By.className("smallSubmit")).click();
            String text = driver.findElement(By.id("viewLead_companyName_sp")).getText();
            String leadNumber = text.replaceAll("[^0-9]", "");
            redis.pushToTable("Leads", leadNumber);
        } catch (Exception ignored) {

        }
    }

    @AfterClass
    public void close() {
        driver.close();
    }

}