package com.mara007.tryselenium;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AppTest {
    WebDriver driver = null;

    @Before
    public void initWebDriver() {
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);
        if (driver == null)
            driver = new FirefoxDriver();
    }

    @After
    public void closeWebDriver() {
        //driver.close();
    }

    public static String getCellPath(int row, int col) {
        // /html/body/table/tbody/tr[6]/td[2]
        return "//table/tbody/tr[" + Integer.toString(row) + "]/td[" + Integer.toString(col) + "]";
    }

    public void fillVisitor(String f, String l) {
        driver.get("http://localhost:5000");
        WebElement fname = driver.findElement(By.id("fname"));
        WebElement lname = driver.findElement(By.id("lname"));
        WebElement submit = driver.findElement(By.xpath("/html/body/form/input[3]"));

        fname.sendKeys(f);
        lname.sendKeys(l);
        submit.click();
    }

    @Test
    public void deleteVisitors() {
        System.out.println("Delete Visitors");
        driver.get("http://localhost:5000/delete_visitors");

        String expH2 = "list of visitors deleted!!";
        System.out.println("Check that page contains " + expH2);
        String H2 = driver.findElement(By.tagName("h2")).getText();
        assertEquals("Check that page contains '" + expH2 + "'", expH2, H2);
    }

    @Test
    public void emptyVisitor() {
        System.out.println("Check invalid Visitor input");
        fillVisitor("", "");

        String expH1 = "Error: you must enter at least one entry!";
        String H1 = driver.findElement(By.tagName("h1")).getText();
        assertEquals("Expected text " + expH1, expH1, H1);
    }

    @Test
    public void addVisitors() {
        Map<String, String> visitors = new LinkedHashMap<>();
        for (int i = 0; i < 10; ++i)
            visitors.put("Name" + Integer.toString(i), "Visitor" + Integer.toString(i));

        System.out.println("Clear previous Visitors");
        driver.get("http://localhost:5000/delete_visitors");

        System.out.println("Add Visitors");
        for (String v : visitors.keySet()) {
            fillVisitor(v, visitors.get(v));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            }
        }

        System.out.println("Check Visitors");
        driver.get("http://localhost:5000/show_visitors");

        int row = 2;
        for (String n : visitors.keySet()) {
            String fname = driver.findElement(By.xpath(getCellPath(row, 1))).getText();
            String lname = driver.findElement(By.xpath(getCellPath(row, 2))).getText();
            System.out.println("Checking visitors table[" + Integer.toString(row) + "] : name = "
                + fname + ", last name = " + lname + ". Expected : " + n + ", " + visitors.get(n));

            assertEquals(n, fname);
            assertEquals(visitors.get(n), lname);

            ++row;
        }
    }
}
