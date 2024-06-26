package Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import BaseFramework.GlobalStore;

public class UIMethods {
    static int MaxInteractiveWaitTime = 15;
    static int pageLoadTime = 36;
    GlobalStore GS = new GlobalStore();

    /* All UI Read Methods */

    /**
     * Wait for this field to visible on Home Page
     */

    public boolean waitForThisFieldToBeVisible(WebElement UIElement, WebDriver driver) {
        WebDriverWait wait;
        boolean isVisible = true;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(pageLoadTime));//Prachi- added
            wait.until(ExpectedConditions.visibilityOf(UIElement));

        } catch (NoSuchElementException NSE) {
           GS.reportStep(NSE.getMessage(), "FAIL", driver);
           GS.takeScreenShot(driver);
            isVisible = false;
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
            GS.takeScreenShot(driver);
            isVisible = false;
        }

        return isVisible;
    }

    /**
     * Read data between div tags
     **/
    public String getMyText(WebElement UIElement, WebDriver driver) {
        String data = "NoDataFound";
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(pageLoadTime));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
            data = UIElement.getText();
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
            GS.takeScreenShot(driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
            GS.takeScreenShot(driver);
        }
        //GS.reportStep("Data Returning is: " + data, "PASS", driver);
        return data;
    }

    /**
     * It modifies existing text field value to new value 1. Retrive existing value
     * 2. Add new name to the existing one 3. Now, set this new value to the field
     *
     * @param driver
     * @param fieldName
     * @param newName
     * @return
     */
    public boolean getTextAndModify(WebDriver driver, WebElement fieldName, String newName) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(MaxInteractiveWaitTime));
            wait.until(ExpectedConditions.visibilityOf(fieldName));
            Thread.sleep(500);
            String data = fieldName.getAttribute("value");
            if (data != null) {
                fieldName.clear();
                isfieldEmpty(driver, fieldName);
                GS.reportStep("\tNew Data entering is:" + (data + newName), "INFO", driver);
                fieldName.sendKeys(data + newName);
            }
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
            GS.takeScreenShot(driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
            GS.takeScreenShot(driver);
        } catch (Exception e) {
            GS.reportStep(e.getMessage(), "FAIL_FAIL", driver);
            GS.takeScreenShot(driver);
        }
        return status;
    }

   
    /**
     * It gets HREF
     *
     * @param UIElement
     * @param driver
     * @return
     */
    public String getMyHREFUrl(WebElement UIElement, WebDriver driver) {

        String data = "NoDataFound";
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
            data = UIElement.getAttribute("href");
            GS.reportStep("DEBUG:  Data read from WEB UI=" + data, "INFO", driver);
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        }
        GS.reportStep("Data Returning is: " + data, "PASS", driver);
        return data;
    }

    public String getMyTextAttribute(WebElement UIElement, WebDriver driver) {
        String data = "NoDataFound";
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
            data = UIElement.getAttribute("title");
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        }
        GS.reportStep("Data Returning is: " + data, "PASS", driver);
        return data;

    }

    public int clickProfile(WebElement profileIcon, WebDriver driver, WebElement profileLabel) {
        String data = "NoDataFound";
        WebDriverWait wait = null;
        int status = 1;

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            wait.until(ExpectedConditions.visibilityOf(profileIcon));
            profileIcon.click();
            Thread.sleep(500);
            profileLabel.click();
            status = 0;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }

        return status;
    }

  

    /**
     * Verify given text is present on web page
     */
    public int verifyTextPresentOnPage(WebDriver driver, String whatToLookFor) {
        int status = 0;

        try {
            Thread.sleep(3000);//prachi - added
            UIMethods.waitForPageLoad(driver);
            boolean isPresent = driver.getPageSource().contains(whatToLookFor);
            if (isPresent) {
                GS.reportStep("\tCheck: Expected message(" + whatToLookFor + ") was found", "PASS", driver);
                status = 1;
            } else
                GS.reportStep("\tCheck: Expected message(" + whatToLookFor + ") was not found", "INFO", driver);

        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }

        return status;
    }

    public int verifyLoginRefreshPage(WebDriver driver, WebElement email, WebElement pwd) {
        String data = "NoDataFound";
        WebDriverWait wait = null;
        int status = 1;
        try {
            // Check email data
            String val1 = email.getText();
            // Check password data
            String val2 = pwd.getText();
            Thread.sleep(GS.waitTime);
            if (val1.isEmpty() || val2.isEmpty()) {
                GS.reportStep("After refresh values are gone", "PASS", driver);
                status = 0;
            } else {
                GS.reportStep("Login refresh is not working properly", "FAIL", driver);
            }

        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);

        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }

        return status;
    }

    /**
     * Click on link
     *
     * @param UIElement
     * @param driver
     * @return
     */
    public boolean ClickOnLink(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
//			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(UIElement));
//			mouseOverEventOnWebElement(UIElement, driver);
//			((JavascriptExecutor) driver).executeScript("arguments[0].click();", UIElement);
            wait.until( new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply( WebDriver webDriver ) {
                    try {
                        UIElement.click();
                        return true;
                    } catch ( StaleElementReferenceException e ) {
                        GS.reportStep( e.getMessage() + "\n", "INFO", driver);
                        GS.reportStep( "Trying again...", "INFO", driver);
                        return false;
                    }
                }
            } );
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL",driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }
    /**
     * Click on button
     *
     * @param UIElement
     * @param driver
     * @return
     */
  /*  public boolean ClickOnRadioBtn(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        try {
            UIElement.click();
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }*/

    /**
     * Click on Button
     */
    public boolean CheckButtonExistsAndClick(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            //wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loader")));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(UIElement));
            mouseOverEventOnWebElement(UIElement, driver);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", UIElement);
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;

    }


    /**
     * Click on Button Using Java script
     */
    public boolean clickJs(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", UIElement);
            status = true;
        }  catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;

    }

    /**
     * Enter data to text field, and before enter check if the field is visible
     *
     * @param UIElement
     * @param driver
     * @param data
     * @return
     */
    public boolean enterDataToTextField(WebElement UIElement, WebDriver driver, String data) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));//Prachi -added
          //  WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
         //   JavascriptExecutor jse = (JavascriptExecutor)driver;
         //   jse.executeScript("arguments[0].click()", element);
            UIElement.clear();
            UIElement.sendKeys(data);
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }

  /*  public boolean selectDropDownMenuItem(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            // UIElement.sendKeys(data);
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }*/

    public boolean selectDropDownMenuItemByEnteringText(WebElement UIElement, WebDriver driver, String data) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            UIElement.sendKeys(data);
            UIElement.click();
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }

    /** Text field operations */

    /**
     * It just enters the text to given field, with out checking if the field is
     * displayed or not
     *
     * @param UIElement
     * @param driver
     * @param data
     */
    public static void justEnterText(WebElement UIElement, WebDriver driver, String data) {
        UIElement.sendKeys(data);

    }

    /**
     * It enters the text into text field and PREEE ENTER Key
     *
     * @param UIElement
     * @param driver
     * @param data
     * @return
     */
    public boolean enterTextAndPressEnter(WebElement UIElement, WebDriver driver, String data) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            element.clear();
            UIElement.sendKeys(data);
            Thread.sleep(5000);
            UIElement.sendKeys(Keys.RETURN);
            Thread.sleep(GS.waitTime);
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }

    /*It clicks Keys
     * @author
     * @param UIElement
     * @param driver
     * @param keys
     * @return
     */
    public boolean clickKeys(WebElement UIElement, WebDriver driver, Keys key) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            element.click();
            Actions actions = new Actions(driver);

            actions.sendKeys(key).build().perform();//press down arrow key

            actions.sendKeys(key).build().perform();//press enter
            Thread.sleep(GS.waitTime);
        }catch (InterruptedException e) {
            GS.reportStep("Clicking On Security Link not working", "FAIL", driver);
        }
        return status;
    }
    /**
     * It enters the text into text field and PREEE ENTER Key
     *
     * @param UIElement
     * @param driver
     * @param data
     * @return
     */
    public boolean enterTextAndPressEnterforcombodropdown(WebElement UIElement, WebDriver driver, String data) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            element.click();
            Actions actions = new Actions(driver);
            if (data.equalsIgnoreCase("Vendor Manager")){
                for(int i = 0; i <= 1; i++){

                    actions.sendKeys(Keys.DOWN).build().perform();//press down arrow key
                    Thread.sleep(GS.waitTime);

                }
                //element.sendKeys(Keys.ENTER);
                actions.sendKeys(Keys.ENTER).build().perform();//press enter
                Thread.sleep(GS.waitTime);
                status = true;
            }else if(data.equalsIgnoreCase("Proctor")){
                for(int i = 0; i <= 6; i++){

                    actions.sendKeys(Keys.DOWN).build().perform();//press down arrow key
                    Thread.sleep(GS.waitTime);
                }
                actions.sendKeys(Keys.ENTER).build().perform();//press enter
                Thread.sleep(GS.waitTime);
                status = true;
            }else if(data.equalsIgnoreCase("Everytime")){

                //actions.sendKeys("Everytime").build().perform();//press down arrow key

                actions.sendKeys(Keys.ENTER).build().perform();//press enter
                Thread.sleep(GS.waitTime);
                status = true;
            }else
            {
                actions.sendKeys(Keys.DOWN).build().perform();//press down arrow key
                Thread.sleep(GS.waitTime);
                actions.sendKeys(Keys.ENTER).build().perform();//press enter
                status = true;
            }

        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }


    /**
     * It reads the data from the field and return to the caller
     *
     * @param driver
     * @param UIElement
     * @return
     */
    public String getDataFromTextField(WebDriver driver, WebElement UIElement) {
        String data = "EMPTY";
        WebDriverWait wait = null;

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));;
            //isElementPresent(driver, element);//Prachi - added
            data = UIElement.getAttribute("value");
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return data;

    }


    //Prachi added
    //start code
    /**
     * It reads the data from the field and return to the caller if field is empty/clear
     * Also it sets the cursor back to starting position
     *
     * @param driver
     * @param UIElement
     * @return
     */
    public boolean isfieldEmpty(WebDriver driver, WebElement UIElement) {
        String data = "EMPTY";
        WebDriverWait wait = null;
        boolean status=false;

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            isElementPresent(driver, element);//Prachi - added
            data = UIElement.getAttribute("value");
            UIMethods.waitForPageLoad(driver);//Prachi added
            //if(data.isBlank()) {
            if(!data.isEmpty()) {//Prachi - added to support Java 11
                if(driver.toString().contains("FirefoxDriver"))//Firefox
                {
                    for(int j=0; j<=30;j++)
                    {
                        UIElement.sendKeys(Keys.BACK_SPACE);
                    }
                    status = true;
                }else if(driver.toString().contains("ChromeDriver"))//Chrome
                {
                    for(int j=0; j<=30;j++)
                    {
                        UIElement.sendKeys("\u0008");
                    }
                    status = true;
                }
                else if(driver.toString().contains("EdgeDriver"))//Edge
                {
                    for(int j=0; j<=30;j++)
                    {
                        UIElement.sendKeys("\u0008");
                        UIElement.sendKeys(Keys.BACK_SPACE);
                    }
                    status = true;
                }
                UIMethods.waitForPageLoad(driver);//Prachi added
            }
            else
                return true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;

    }

    //end code

    /**
     * Search and pick a record
     *
     * @param UIElement
     * @param driver
     * @param whichRecordToSelect
     * @return
     */
    public boolean searchAndSelectRecord(WebElement UIElement, WebDriver driver, String whichRecordToSelect) {
        boolean status = false;
        String xpath = "//span[contains(text(),";
        try {
            //Verify if any role is to be selected from dropdown
            if(whichRecordToSelect.equalsIgnoreCase("Vendor Manager") || whichRecordToSelect.equalsIgnoreCase("Proctor")
                    || whichRecordToSelect.equalsIgnoreCase("Avail Administrator") ||whichRecordToSelect.equalsIgnoreCase("Vendor Administrator")
                    || whichRecordToSelect.equalsIgnoreCase("Vendor Field Representative") || whichRecordToSelect.equalsIgnoreCase("HCP")) {

                enterTextAndPressEnterforcombodropdown(UIElement, driver, whichRecordToSelect);
                Thread.sleep(GS.waitTime);
            }else {
                // Search for record
                enterTextAndPressEnter(UIElement, driver, whichRecordToSelect);
                Thread.sleep(GS.waitTime);
            }
            // Select record
            xpath = xpath + "'" + whichRecordToSelect + "')]";
            GS.reportStep("XPATH=" + xpath, "INFO", driver);
            WebElement e = driver.findElement(By.xpath(xpath));
            mouseOverEventOnWebElement(e, driver);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", e);
            // e.click();
            Thread.sleep(GS.waitTime);
            status = true;
        } catch (NoSuchElementException NE) {
            GS.reportStep(NE.getMessage(), "ERROR");
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "ERROR");
        }
        return status;
    }

    /**
     * Search and select record that exactly matches
     *
     * @param UIElement
     * @param driver
     * @param whichRecordToSelect
     * @return
     */
    public boolean searchAndSelectExactRecord(WebElement UIElement, WebDriver driver,
                                              String whichRecordToSelect) {
        boolean status = false;
        String xpath = "//span[contains(text(),";
        try {
            // Search for record
            enterTextAndPressEnter(UIElement, driver, whichRecordToSelect);
            Thread.sleep(GS.waitTime);
            // Select record
            xpath = xpath + "'" + whichRecordToSelect + "')]";
            GS.reportStep("XPATH=" + xpath, "INFO", driver);
            UIMethods.waitForPageLoad(driver);
            WebElement e = driver.findElement(By.xpath(xpath));
            mouseOverEventOnWebElement(e, driver);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", e);
            // e.click();
            Thread.sleep(GS.waitTime);
            status = true;
        } catch (NoSuchElementException NE) {
            GS.reportStep(NE.getMessage(), "ERROR");
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "ERROR");
        }
        return status;
    }

    /**
     * Mouse Over to the Web Element
     *
     * @param UIElement
     * @param driver
     * @return
     */

    public boolean mouseOverEventOnWebElement(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
            Actions actions = new Actions(driver);
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", UIElement);//Prachi - added
            actions.moveToElement(UIElement).perform();
            Thread.sleep(3000);
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;

    }

    /**
     * This method will click on React JS Drop down
     *
     * @param driver
     * @return
     */
    public boolean ClickONReactJSDropDown(WebDriver driver) {
        boolean status = false;
        String xpath = "//div[@class='react-select__indicators css-1wy0on6']//div";
        try {
            WebElement dropDown = driver.findElement(By.xpath(xpath));
            dropDown.click();
            Thread.sleep(2000);//prachi increased wait by 1 sec
            // Grab the elements
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));//Prachi - increased wait by 2 secs
            wait.until(ExpectedConditions.elementToBeClickable((By.className("react-select__menu"))));
            List<WebElement> options = driver.findElements(By.className("react-select__menu"));
            for (WebElement option : options) {
                option.click();
                status = true;
            }
            // Click on downarrow
            dropDown.click();
        } catch (Exception e) {
            GS.reportStep(e.getMessage(), "FAIL",driver);
        }
        return status;
    }

    /**
     * This method will click on Drop down
     *
     * @param driver
     * @return
     */
    public boolean ClickONDropDown(WebDriver driver, WebElement UIElement) {
        boolean status = false;
        try {// Click on downarrow
            UIElement.click();
            status = true;
        } catch (Exception e) {
            GS.reportStep(e.getMessage(), "FAIL", driver);
        }
        return status;
    }

    /**
     * This method will download userguide
     *
     * @param driver
     * @return
     */
    public  boolean clickdownloaduserguide(WebDriver driver)
    {
        boolean status= false;
        try {
            String xpath  = "//button[@class='MuiButtonBase-root MuiIconButton-root jss5034']";
            WebElement download = driver.findElement(By.xpath(xpath));
            download.click();
            status= true;
        }
        catch (Exception e)
        {
            GS.reportStep(e.getMessage(), "FAIL", driver);
        }
        return status;
    }
    /**
     * It will select the date from tomorrow to next month same date
     *
     * @param driver
     */
    public static void selectFromDateandToDateFromDatePicker(WebDriver driver) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate todayDate = LocalDate.now().plusDays(1);
        LocalDate nextmont = todayDate.plusMonths(1);
        String fromDate = dtf.format(todayDate);
        String toDate = dtf.format(nextmont);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
        WebElement fromDateElement = wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(By.xpath("//div[@class = 'create-event__recurring-part']/div[1]/div/div/input"))));
        String fromDateText = fromDateElement.getAttribute("value");
        for (int i = 0; i <= fromDateText.length(); i++) {
            fromDateElement.sendKeys(Keys.BACK_SPACE);
        }
        fromDateElement.sendKeys(fromDate);
        fromDateElement.sendKeys(Keys.ENTER);
        WebElement toDateElement = wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(By.xpath("//div[@class = 'create-event__recurring-part']/div[2]/div/div/input"))));
        String toDateString = toDateElement.getAttribute("value");
        for (int i = 0; i <= toDateString.length(); i++) {
            toDateElement.sendKeys(Keys.BACK_SPACE);
        }
        toDateElement.sendKeys(toDate);
        toDateElement.sendKeys(Keys.ENTER);
    }

//	/**
//	 * This method will click on Drop down
//	 *
//	 * @param driver
//	 * @return
//	 */
//	public boolean ClickONDropDown(WebDriver driver, WebElement UIElement) {
//		boolean status = false;
//		WebDriverWait wait = null;
//			try {
//				wait = new WebDriverWait(driver, pageLoadTime);
//				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(UIElement));
//			// Click on downarrow
//			UIElement.click();
//			status = true;
//			} catch (Exception e) {
//			GS.reportStep(e.getMessage(), "FAIL", driver);
//		}
//		return status;
//	}


    /**
     * ... Delete for any element
     *
     * @param driver
     * @param UIElement
     * @return
     */

    public boolean deleteRecord(WebDriver driver, WebElement UIElement) {
        WebDriverWait wait = null;
        boolean status = false;

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(4));
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            GS.reportStep("Deleting started....", "INFO", driver);
            element = driver.findElement(By.xpath(("(//td[@class='table__action-table-cell']//div)[2]")));
            mouseOverEventOnWebElement(element, driver);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

            // element.click();
            Thread.sleep(500);
            element = driver.findElement(By.xpath("//*[text()='Delete']"));
            mouseOverEventOnWebElement(element, driver);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

            // element.click();
            Thread.sleep(500);
            element = driver.findElement(By.xpath("//button[text()='Delete']"));
            mouseOverEventOnWebElement(element, driver);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

            // element.click();
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;

    }

    /*
     * Verify if the element is present and clickable
     */
    public boolean isElementPresent(WebDriver driver, WebElement UIElement) {
        boolean status = false;
        WebDriverWait wait;

        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(32));//Prachi - increased by 2 secs
            WebElement element = wait.until(ExpectedConditions.visibilityOf(UIElement));
            element = wait.until(ExpectedConditions.elementToBeClickable(UIElement));
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL", driver);
        }
        return status;
    }

    /**
     * It will return the text from the attribute given.
     * @param UIElement
     * @param driver
     * @param attribute
     * @return
     */
    public String getTextFromAttribute(WebElement UIElement, WebDriver driver, String attribute) {
        String data = "NoDataFound";
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
            data = UIElement.getAttribute(attribute);
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        }
        GS.reportStep("Data Returning is: " + data, "PASS", driver);
        return data;

    }

    /**
     *     See if the given tag present in the current page
     * @param driver
     * @param tagName
     * @return
     */
    public boolean isTagPresent(WebDriver driver, String tagName) {
        boolean status=true;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement UIElement = driver.findElement(By.tagName(tagName));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
        } catch (NoSuchElementException NSE) {
            status=false;
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
            status=false;
        }

        return status;
    }

    /**
     *   Search if the given text element is found on the web page
     * @param driver
     * @param textToLookFor
     * @return
     */
    public boolean isThisTextPresent(WebDriver driver, String textToLookFor) {
        boolean status=false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement UIElement = driver.findElement(By.xpath(textToLookFor));
            wait.until(ExpectedConditions.visibilityOf(UIElement));
            status=true;
        } catch (NoSuchElementException NSE) {
            status=false;
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
            status=false;
        }
        return status;
    }

    /**
     * Check if the given link is found and clickable
     * @param UIElement
     * @param driver
     * @return
     */
    public boolean isLinkFound(WebElement UIElement, WebDriver driver) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(UIElement));
            status = true;
        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
    }

    /**
     * Verify given text is present in a web element
     */
    public boolean verifyTextOfAnElement(WebDriver driver, WebElement element, String whatToLookFor) {
        WebDriverWait wait = null;
        boolean status = false;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(MaxInteractiveWaitTime));
            wait.until(ExpectedConditions.visibilityOf(element));
            boolean isPresent = element.getText().contains(whatToLookFor);
            if (isPresent) {
                GS.reportStep("\tCheck: Expected message(" + whatToLookFor + ") was found", "PASS", driver);
                status = true;
            } else
                GS.reportStep("\tCheck: Expected message(" + whatToLookFor + ") was not found", "FAIL", driver);

        } catch (NoSuchElementException NSE) {
            GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            GS.reportStep(TO.getMessage(), "FAIL", driver);
        } catch (Exception E) {
            GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }

        return status;
    }

    /**
     * Wait for the page to load with default page load time 30secs
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
                .equals("complete"));
    }

    /**
     * Verify that search result has the searched term
     *
     * @param driver
     * @param searchTerm
     * @return
     */
    public boolean verifySearchResult(WebDriver driver, String searchTerm) {
        List<WebElement> searchResult = driver.findElements(By.xpath("//table[@class='ui table']//td//span"));
        for (WebElement result : searchResult) {
            String fetchedName = getMyText(result, driver);
            if (fetchedName.equalsIgnoreCase(searchTerm))
                return true;
        }
        return false;
    }

    /**
     * Verify that search result contains the searched term
     *
     * @param driver
     * @param searchTerm
     * @return
     */
    public boolean verifySearchResultForSearchTerm(WebDriver driver, String searchTerm) {
        List<WebElement> searchResult = driver.findElements(By.xpath("//table[@class='ui table']//td//span"));
        for (WebElement result : searchResult) {
            String fetchedName = getMyText(result, driver);
            if (fetchedName.contains(searchTerm))
                return true;
        }
        return false;
    }


    /**
     * This method will click on backspace key for the UIelement passed.
     * @param driver
     * @param UIElement
     */
    public static void pressBackSpace(WebDriver driver, WebElement UIElement) {
        UIElement.sendKeys(Keys.BACK_SPACE);
    }

    /**
     * This method will return enabled or disabled for passed UIelement.
     * @param driver
     * @return
     */
    public static boolean VerifyIsEnabledForUIElement(WebDriver driver, WebElement UIElement) {
        boolean status = false;
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver,  Duration.ofSeconds(pageLoadTime));
            wait.until( new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply( WebDriver webDriver ) {
                    try {
                        UIElement.isEnabled();
                        return true;
                    } catch ( StaleElementReferenceException e ) {
//						GS.reportStep( e.getMessage() + "\n", "INFO", driver);
//						GS.reportStep( "Trying again...", "INFO", driver);
                        return false;
                    }
                }
            } );
            if(UIElement.isEnabled())
                status = true;

            return status;
        } catch (NoSuchElementException NSE) {
            //GS.reportStep(NSE.getMessage(), "FAIL", driver);
        } catch (TimeoutException TO) {
            //GS.reportStep(TO.getMessage(), "FAIL",driver);
        } catch (Exception E) {
            //GS.reportStep(E.getMessage(), "FAIL_FAIL", driver);
        }
        return status;
        //return UIElement.isEnabled();
    }

    /**
     * This method will return enabled or disabled for passed UIelement.
     * @param driver
     * @return
     */
    public static boolean VerifyUIElementStatus(WebDriver driver, WebElement UIElement) {
        return UIElement.isEnabled();
    }

    public static WebElement getFromWithin( WebDriver driver, WebElement UIElement, String xPath) {
        WebElement element;
        try {
            element = UIElement.findElement(By.xpath(xPath));
        } catch (Exception E) {
            element =null;
        }
        return element;
    }



    /**
     * This method will click on the UIelement passed.
     * @param driver
     * @param UIElement
     */
    public static void clickElement(WebDriver driver, WebElement UIElement) {
        UIElement.click();
    }
    /**
     * Switch to child window
     * @param driver
     */
    public void switchToChildWindow(WebDriver driver)
    {
        try {
            String pwin=driver.getWindowHandle();
            Set<String> win=driver.getWindowHandles();
            win.remove(pwin);
            driver.switchTo().window(win.iterator().next());
            GS.reportStep("Switched to child window", "PASS", driver);
        } catch (Exception e) {
            GS.reportStep("Unable to switch to to child window", "FAIL", driver);
        }
    }
    /**
     * Switch to frame using webelement
     * @param element
     * @param driver
     */

    public void switchToFrame(WebElement element, WebDriver driver)
    {
        try {
            driver.switchTo().frame(element);
            GS.reportStep("Switched to frame ", "PASS", driver);
        } catch (Exception e) {
            GS.reportStep("There is no frame unable to switch to frame ", "FAIL", driver);
        }
    }
    /*
     * Verify if the element is  not present and clickable
     */
    public boolean isElementNotPresent(WebDriver driver, WebElement UIElement) {
        try {
            UIElement.isDisplayed();
            return false;
        } catch (Exception e) {
            return true;
        }

    }
    /**
     * This method will verify the status of the checkbox.
     * @param driver
     * @return
     */
    public static boolean verifyIsSelected(WebDriver driver, WebElement UIElement) {
        return UIElement.isSelected();
    }

}
