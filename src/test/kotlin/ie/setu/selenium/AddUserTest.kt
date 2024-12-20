package ie.setu.selenium

import ie.setu.config.DbConfig
import ie.setu.config.JavalinConfig
import io.javalin.Javalin
import io.javalin.testtools.JavalinTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.WebDriverWait

class AddUserTest {
    private lateinit var app: Javalin
    private lateinit var driver: WebDriver
    private var vars: Map<String, Any>? = null
    private var wait: WebDriverWait? = null
    var js: JavascriptExecutor? = null

    @Before
    fun setUp() {
        //Connect to the remote database
        DbConfig().getDbConnection()
        driver = ChromeDriver()
        js = driver as JavascriptExecutor
        vars = HashMap()
    }

    @After
    fun tearDown() {
        driver.quit()
    }

    @Test
    fun addUser() {
        app = JavalinConfig().startJavalinService()

        JavalinTest.test(app) { _, client ->

            driver.get(client.origin)

            driver.findElement(By.linkText("More Details...")).click()
            run {
                val element: WebElement = driver.findElement(By.cssSelector(".btn:nth-child(1)"))
                val builder: Actions = Actions(driver)
                builder.moveToElement(element).perform()
            }
            run {
                val element: WebElement = driver.findElement(By.tagName("body"))
                val builder: Actions = Actions(driver)
                builder.moveToElement(element, 0, 0).perform()
            }

            driver.findElement(By.cssSelector(".fa-plus")).click()
            driver.findElement(By.name("name")).click()
            driver.findElement(By.name("name")).sendKeys("Homer Simpson")
            driver.findElement(By.name("email")).sendKeys("homer@simpson.com")

            driver.findElement(By.cssSelector(".card-body > .btn")).click()
            driver.findElement(By.linkText("Homer Simpson (homer@simpson.com)")).click()
            driver.findElement(By.cssSelector(".fas")).click()
            Assert.assertThat(
                driver.switchTo().alert().getText(),
                CoreMatchers.`is`<String>("Do you really want to delete?")
            )
            driver.switchTo().alert().accept()
        }
    }
}