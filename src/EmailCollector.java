/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author KhalidSultan
 */
public class EmailCollector {

    static String email = "kalid.sultan.95@gmail.com";
    static String password = "PASSWORD_GOES_HERE";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\Gecko\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();

        String url = "https://accounts.google.com/signin/v2/identifier?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&flowName=GlifWebSignIn&flowEntry=ServiceLogin";
        String loggedInUrl = "https://mail.google.com/mail/u/0/#inbox";

        driver.get(url);
        System.out.println("Found gmail.");

        driver.findElement(By.id("identifierId")).click();
        driver.findElement(By.id("identifierId")).sendKeys(email);

        driver.findElement(By.id("identifierNext")).click();

        System.out.println("Found password input page.");

        WebDriverWait wait = new WebDriverWait(driver, 30);
        Thread.sleep(2000);
        driver.findElement(By.name("password")).sendKeys(password, Keys.ENTER);
        Thread.sleep(2000);
        wait.until(ExpectedConditions.urlToBe(loggedInUrl));
        if (driver.getCurrentUrl().equalsIgnoreCase(loggedInUrl)) {
            FileWriter fw = null;
            try {
                System.out.println("Logged In Successfully");
                fw = new FileWriter("UnreadEmails.txt");
                List<WebElement> unreadEmails = driver.findElements(By.xpath("//*[@class='zA zE']"));
                int unreadMessageCount = unreadEmails.size(); 
                fw.append("Total Unread Messages Count " + unreadMessageCount + "\n");
                int i = 1;
                for (WebElement webElement : unreadEmails) {
                    if (webElement.isDisplayed()) {

                        String sender = webElement.findElement(By.xpath(".//td[5]/div[2]/span/span")).getText();
                        String subject = webElement.findElement(By.xpath(".//td[6]/div/div/div/span/span")).getText();
                        StringBuilder builder = new StringBuilder();
                        builder.append("Message #").append(i).append("\n");
                        builder.append("\tSender : ").append(sender).append("\n");
                        builder.append("\tSubject : ").append(subject).append("\n");
                        builder.append("\n");
                        i += 1;
                        fw.append(builder.toString());
                    }
                }
                driver.close();
                System.out.println("Browser is closed.");
                return;
            } 
            catch (IOException ex) {
                Logger.getLogger(EmailCollector.class.getName()).log(Level.SEVERE, null, ex);
            } 
            finally {
                try {
                    fw.close();
                    System.out.println("File is closed.");
                } 
                catch (IOException ex) {
                    Logger.getLogger(EmailCollector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Failed to Log In.");
        driver.close();
        System.out.println("Browser is closed.");

    }
}
