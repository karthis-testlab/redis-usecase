package com.redis.jedis.code.test.suites.ui;

import com.redis.jedis.code.practices.datastructures.usecases.RedisManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class DeleteLeadTest {

    private RedisManager redis;
    private ChromeDriver driver;
    private String leadId;

    @BeforeClass
    public void login() {
        redis = new RedisManager();
        leadId = redis.popFromTable("Leads");
        if(leadId.equals("")) throw new RuntimeException("No test data found");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.get("http://leaftaps.com/opentaps");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("demosalesmanager");
        driver.findElement(By.id("password")).sendKeys("crmsfa");
        driver.findElement(By.className("decorativeSubmit")).click();
        driver.findElement(By.linkText("CRM/SFA")).click();
        driver.findElement(By.linkText("Leads")).click();
    }

    @Test
    public void deleteLead() {
        try {
            driver.findElement(By.linkText("Find Leads")).click();
            driver.findElement(By.name("id")).sendKeys(leadId);
            driver.findElement(By.xpath("//button[text()='Find Leads']")).click();
            Thread.sleep(5000);
            driver.findElement(By.xpath("//table[@class='x-grid3-row-table']/tbody/tr[1]/td[1]//a")).click();
            driver.findElement(By.linkText("Delete")).click();
        } catch (Exception e) {
            e.printStackTrace();
            redis.pushToTable("Leads", leadId);
        }
    }

    @AfterClass
    public void close() {
        driver.close();
    }

}