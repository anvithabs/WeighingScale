package com.game;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
/*
* @author Anvitha Sadananda
* Algorithm for minimum number of weighings for any possible fake bar position
*
* There are 9 bars
* Weigh them in groups of 3 i.e split the 9 bars into group1(0,1,2) group2(3,4,5) and group3(6,7,8)
* Weigh group1 and group2 - if they are equal implies the fake bar is in group3.
*                           Otherwise the fake bar is in the group that weighs less
* In the less weighing group, weigh any 2 bars
* - if they are equal implies the fake bar is the one
*   from the group that is not on the scale.
* Otherwise the fake bar is the one that weighs less
*
* With 2 weighing the fake bar can be found
* */
public class WeighingGame {
    static WebDriver driver = new ChromeDriver();

    public static void main(String[] args) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
            driver.get("http://ec2-54-208-152-154.compute-1.amazonaws.com/");
            String firstRoundResult = firstRound();
            System.out.println("firstRoundResult " + firstRoundResult);
            if (firstRoundResult.contains("<")) {
                String[] resultArray = firstRoundResult.split("<");
                String[] leftResult = resultArray[0].trim().replace("[", "")
                        .replace("]", "").split(",");
                String secondRoundResult = secondRound(leftResult[0], leftResult[1]);
                System.out.println("secondRoundResult " + secondRoundResult);
                if (secondRoundResult.contains("<")) {
                    driver.findElement(By.id("coin_" + leftResult[0])).click();
                    System.out.println("fake gold bar number "+leftResult[0]);
                } else if (secondRoundResult.contains(">")) {
                    driver.findElement(By.id("coin_" + leftResult[1])).click();
                    System.out.println("fake gold bar number "+leftResult[1]);
                } else {
                    driver.findElement(By.id("coin_" + leftResult[2])).click();
                    System.out.println("fake gold bar number "+leftResult[2]);
                }
            } else if (firstRoundResult.contains(">")) {
                String[] resultArray = firstRoundResult.split(">");

                String[] rightResult = resultArray[1].trim().replace("[", "")
                        .replace("]", "").split(",");
                String secondRoundResult = secondRound(rightResult[0], rightResult[1]);
                System.out.println("secondRoundResult " + secondRoundResult);
                if (secondRoundResult.contains("<")) {
                    driver.findElement(By.id("coin_" + rightResult[0])).click();
                    System.out.println("fake gold bar number "+rightResult[0]);
                } else if (secondRoundResult.contains(">")) {
                    driver.findElement(By.id("coin_" + rightResult[1])).click();
                    System.out.println("fake gold bar number "+rightResult[1]);
                } else {
                    driver.findElement(By.id("coin_" + rightResult[2])).click();
                    System.out.println("fake gold bar number "+rightResult[2]);
                }
            } else if (firstRoundResult.contains("=")) {
                //equal scenario
                String secondRoundResult = secondRound(driver.findElement(By.id("coin_6")).getText(),
                        driver.findElement(By.id("coin_7")).getText());
                System.out.println("secondRoundResult " + secondRoundResult);
                if (secondRoundResult.contains("<")) {
                    driver.findElement(By.id("coin_6")).click();
                    System.out.println("fake gold bar number 6");
                } else if (secondRoundResult.contains(">")) {
                    driver.findElement(By.id("coin_7")).click();
                    System.out.println("fake gold bar number 7");
                } else {
                    driver.findElement(By.id("coin_8")).click();
                    System.out.println("fake gold bar number 8");
                }
            }
            System.out.println("Alert ** " + driver.switchTo().alert().getText());
        } finally {
            driver.quit();
        }
    }


    public static String firstRound() {
        WebElement left_0 = new WebDriverWait(driver, Duration.ofSeconds(10).getSeconds())
                .until(ExpectedConditions.elementToBeClickable(By.id("left_0")));
        left_0.sendKeys(driver.findElement(By.id("coin_0")).getText());
        driver.findElement(By.id("left_1")).sendKeys(driver.findElement(By.id("coin_1")).getText());
        driver.findElement(By.id("left_2")).sendKeys(driver.findElement(By.id("coin_2")).getText());
        driver.findElement(By.id("right_0")).sendKeys(driver.findElement(By.id("coin_3")).getText());
        driver.findElement(By.id("right_1")).sendKeys(driver.findElement(By.id("coin_4")).getText());
        driver.findElement(By.id("right_2")).sendKeys(driver.findElement(By.id("coin_5")).getText());
        driver.findElement(By.id("weigh")).click();
        System.out.println("clicked weigh firstRound");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='game-info']/ol/li")));
        List<WebElement> resultElements = driver.findElements(By.xpath("//div[@class='game-info']/ol/li"));
        WebElement result = resultElements.get(resultElements.size() - 1);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='game-info']/ol/li")));
        return result.getText();
    }

    public static String secondRound(String left, String right) {
        WebElement ele = driver.findElement(By.xpath("//button[contains(text(),'Reset')]"));
        ele.click();
        System.out.println("clicked reset");
        // driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.findElement(By.id("left_0")).sendKeys(left);
        driver.findElement(By.id("right_0")).sendKeys(right);
        driver.findElement(By.id("weigh")).click();
        System.out.println("clicked weigh secondRound");
        new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='game-info']/ol/li[2]")));
        List<WebElement> resultElements = driver.findElements(By.xpath("//div[@class='game-info']/ol/li"));
        WebElement result = resultElements.get(resultElements.size() - 1);
        return result.getText();
    }
}
