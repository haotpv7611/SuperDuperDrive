package com.fpt.haotpv.SuperDuperDrive;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeneralTest {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private static final String FIRST_NAME = "FPT";
    private static final String LAST_NAME = "Udacity";
    private static final String PASSWORD = "123";
    private static final String USERNAME = "haotpv";
    private static final String USERNAME2 = "createNote";
    private static final String USERNAME3 = "editNote";
    private static final String USERNAME4 = "deleteNote";
    private static final String USERNAME5 = "createCredential";
    private static final String USERNAME6 = "editCredential";
    private static final String USERNAME7 = "deleteCredential";
    private static final String NOTE_TITLE = "Create a new Note";
    private static final String NOTE_TITLE_2 = "Create another new Note";
    private static final String NOTE_TITLE_3 = "Delete Note";
    private static final String NOTE_DESCRIPTION = "Test function Create a new Note";
    private static final String NOTE_DESCRIPTION_2 = "Test function Create another new Note";
    private static final String NOTE_DESCRIPTION_3 = "Test function Delete Note";
    private static final String NEW_NOTE_TITLE = "Edit Note";
    private static final String NEW_NOTE_DESCRIPTION = "Test function Edit Note";
    private static final String CREDENTIAL_URL = "https://www.youtube.com/";
    private static final String CREDENTIAL_URL_2 = "https://www.google.com.vn/";
    private static final String CREDENTIAL_URL_3 = "https://stackoverflow.com/";
    private static final String NEW_CREDENTIAL_URL = "https://www.udacity.com/";


    private void doMockSignUp(String username) {
        // Visit the sign-up page.
        driver.get("http://localhost:" + this.port + "/signup");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(FIRST_NAME);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(LAST_NAME);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(PASSWORD);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }

    private void doLogIn(String username) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);
        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(username);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(PASSWORD);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        // Check if we have been redirected to the home page.
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
    }

    @Test
    public void testSignupLoginLogoutFlow() {
        // Create a test account
        doMockSignUp(USERNAME);
        doLogIn(USERNAME);

        WebDriverWait webDriverWait = new WebDriverWait(this.driver, 10);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();

        // Check if we have been redirected to the log in page.
        webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    @Test
    public void testCreateNote() {
        // Create a test account
        doMockSignUp(USERNAME2);
        doLogIn(USERNAME2);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //select add new note, fill out note form and click save
        selectAddNewNote();
        doMockNoteForm(NOTE_TITLE, NOTE_DESCRIPTION);

        //get last element in note list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        List<WebElement> noteTitleList = driver.findElements(By.id("note-title-view"));
        List<WebElement> noteDescriptionList = driver.findElements(By.id("note-description-view"));
        String actualTitle = noteTitleList.get(noteTitleList.size() - 1).getText();
        String actualDescription = noteDescriptionList.get(noteDescriptionList.size() - 1).getText();

        Assertions.assertEquals(NOTE_TITLE, actualTitle);
        Assertions.assertEquals(NOTE_DESCRIPTION, actualDescription);
    }

    @Test
    public void testEditNote() {
        // Create a test account
        doMockSignUp(USERNAME3);
        doLogIn(USERNAME3);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //select add new note, fill out note form and click save
        selectAddNewNote();
        doMockNoteForm(NOTE_TITLE, NOTE_DESCRIPTION);
        selectAddNewNote();
        doMockNoteForm(NOTE_TITLE_2, NOTE_DESCRIPTION_2);

        //get last element in note list and click edit
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        List<WebElement> editNoteButtonList = driver.findElements(By.id("edit-note-button"));
        WebElement editButton = editNoteButtonList.get(editNoteButtonList.size() - 1);
        editButton.click();

        //fill out note form and click save
        doMockNoteForm(NEW_NOTE_TITLE, NEW_NOTE_DESCRIPTION);

        //get last element in note list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        List<WebElement> noteTitleList = driver.findElements(By.id("note-title-view"));
        List<WebElement> noteDescriptionList = driver.findElements(By.id("note-description-view"));
        String actualTitle = noteTitleList.get(noteTitleList.size() - 1).getText();
        String actualDescription = noteDescriptionList.get(noteDescriptionList.size() - 1).getText();

        Assertions.assertEquals(NEW_NOTE_TITLE, actualTitle);
        Assertions.assertEquals(NEW_NOTE_DESCRIPTION, actualDescription);
    }

    @Test
    public void testDeleteNote() {
        // Create a test account
        doMockSignUp(USERNAME4);
        doLogIn(USERNAME4);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //select add new note, fill out note form and click save
        selectAddNewNote();
        doMockNoteForm(NOTE_TITLE, NOTE_DESCRIPTION);
        selectAddNewNote();
        doMockNoteForm(NOTE_TITLE_3, NOTE_DESCRIPTION_3);

        //get last element in note list and click edit
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        List<WebElement> editNoteButtonList = driver.findElements(By.id("delete-note-button"));
        WebElement deleteButton = editNoteButtonList.get(editNoteButtonList.size() - 1);
        deleteButton.click();

        // Check if we have been redirected to the result page and back to home page.

        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/result", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        //select note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        //get last element in note list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        List<WebElement> noteTitleList = driver.findElements(By.id("note-title-view"));
        List<WebElement> noteDescriptionList = driver.findElements(By.id("note-description-view"));
        String actualTitle = noteTitleList.get(noteTitleList.size() - 1).getText();
        String actualDescription = noteDescriptionList.get(noteDescriptionList.size() - 1).getText();

        Assertions.assertFalse(NOTE_TITLE_3.equalsIgnoreCase(actualTitle));
        Assertions.assertFalse(NOTE_DESCRIPTION_3.equalsIgnoreCase(actualDescription));
    }

    private void selectAddNewNote() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //select note tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();

        //click add a new note button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-note-button")));
        WebElement createNoteButton = driver.findElement(By.id("create-note-button"));
        createNoteButton.click();
    }

    private void doMockNoteForm(String title, String description) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        noteTitle.click();
        noteTitle.clear();
        noteTitle.sendKeys(title);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        noteDescription.click();
        noteDescription.clear();
        noteDescription.sendKeys(description);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-button")));
        WebElement createNoteButton = driver.findElement(By.id("save-note-button"));
        createNoteButton.click();

        // Check if we have been redirected to the result page and back to home page and select note tab
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/result", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
        noteTab.click();
    }

    @Test
    public void testCreateCredential() {
        // Create a test account
        doMockSignUp(USERNAME5);
        doLogIn(USERNAME5);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //fill out credential form and click save
        selectAddNewCredential();
        doMockCredentialForm(CREDENTIAL_URL);

        //get last element in note list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        List<WebElement> credentialUrlList = driver.findElements(By.id("credential-url-view"));
        List<WebElement> credentialUsernameList = driver.findElements(By.id("credential-username-view"));
        String actualUrl = credentialUrlList.get(credentialUrlList.size() - 1).getText();
        String actualUsername = credentialUsernameList.get(credentialUsernameList.size() - 1).getText();

        Assertions.assertEquals(CREDENTIAL_URL, actualUrl);
        Assertions.assertEquals(USERNAME, actualUsername);
    }

    @Test
    public void testEditCredential() {
        // Create a test account
        doMockSignUp(USERNAME6);
        doLogIn(USERNAME6);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //fill out credential form and click save
        selectAddNewCredential();
        doMockCredentialForm(CREDENTIAL_URL);
        selectAddNewCredential();
        doMockCredentialForm(CREDENTIAL_URL_2);

        //get last element in credential list and click edit
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        List<WebElement> editCredentialButtonList = driver.findElements(By.name("edit-credential-button"));
        WebElement editButton = editCredentialButtonList.get(editCredentialButtonList.size() - 1);
        editButton.click();

        //fill out credential form and click save
        doMockCredentialForm(NEW_CREDENTIAL_URL);

        //get last element in credential list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-view")));
        List<WebElement> credentialUrlList = driver.findElements(By.id("credential-url-view"));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username-view")));
        List<WebElement> credentialUsernameList = driver.findElements(By.id("credential-username-view"));
        String actualUrl = credentialUrlList.get(credentialUrlList.size() - 1).getText();
        String actualUsername = credentialUsernameList.get(credentialUsernameList.size() - 1).getText();

        Assertions.assertEquals(NEW_CREDENTIAL_URL, actualUrl);
        Assertions.assertEquals(USERNAME, actualUsername);
    }

    @Test
    public void testDeleteCredential() {
        // Create a test account
        doMockSignUp(USERNAME7);
        doLogIn(USERNAME7);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //fill out credential form and click save
        selectAddNewCredential();
        doMockCredentialForm(CREDENTIAL_URL);
        selectAddNewCredential();
        doMockCredentialForm(CREDENTIAL_URL_3);

        //get last element in credential list and click edit
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        List<WebElement> deleteNoteButtonList = driver.findElements(By.name("delete-credential-button"));
        WebElement deleteButton = deleteNoteButtonList.get(deleteNoteButtonList.size() - 1);
        deleteButton.click();

        // Check if we have been redirected to the result page and back to home page.
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/result", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        //select credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();

        ///get last element in credential list
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials")));
        List<WebElement> credentialUrlList = driver.findElements(By.id("credential-url-view"));
        String actualUrl = credentialUrlList.get(credentialUrlList.size() - 1).getText();

        Assertions.assertFalse(CREDENTIAL_URL_3.equalsIgnoreCase(actualUrl));
    }

    private void selectAddNewCredential() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        //select credential tab
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();

        //click add a new credential button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-credential-button")));
        WebElement createCredentialButton = driver.findElement(By.id("create-credential-button"));
        createCredentialButton.click();
    }

    private void doMockCredentialForm(String url) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 10);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        credentialUrl.click();
        credentialUrl.clear();
        credentialUrl.sendKeys(url);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        credentialUsername.click();
        credentialUsername.clear();
        credentialUsername.sendKeys(USERNAME);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        credentialPassword.click();
        credentialPassword.clear();
        credentialPassword.sendKeys(PASSWORD);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential-button")));
        WebElement createCredentialButton = driver.findElement(By.id("save-credential-button"));
        createCredentialButton.click();

        // Check if we have been redirected to the result page and back to home page and select credential tab
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("http://localhost:" + this.port + "/result", driver.getCurrentUrl());
        driver.get("http://localhost:" + this.port + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credentialTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialTab.click();
    }
}
