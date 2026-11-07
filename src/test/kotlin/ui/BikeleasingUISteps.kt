package ui

import net.serenitybdd.annotations.Step
import net.serenitybdd.core.di.SerenityInfrastructure
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
import org.junit.jupiter.api.Assertions.assertTrue
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URI
import java.time.Duration

open class BikeleasingUISteps {

    private val environmentVariables by lazy {
        SerenityInfrastructure.getEnvironmentVariables()
    }

    private val baseUrl: String
        get() = EnvironmentSpecificConfiguration
            .from(environmentVariables)
            .getProperty("web.base.url")
            ?: error("Missing 'web.base.url' in serenity.conf or WEB_BASE_URL")

    private val expectedHost: String
        get() = EnvironmentSpecificConfiguration
            .from(environmentVariables)
            .getProperty("web.expected.host")
            ?.removePrefix("www.")
            ?: error("Missing 'web.expected.host' in serenity.conf or WEB_EXPECTED_HOST")

    @Step("Open the Bikeleasing website")
    open fun openHomepage(browser: WebDriver) {
        browser.get(baseUrl)
    }

    @Step("Verify the current URL is correct")
    open fun verifyCurrentUrlContainsBikeleasing(browser: WebDriver) {
        val host = expectedHost
        val currentUrl = WebDriverWait(browser, Duration.ofSeconds(10))
            .withMessage { "Expected host '$host' after redirect" }
            .until { driver ->
                val url = driver.currentUrl ?: return@until null
                val currentHost = runCatching { URI(url).host }.getOrNull()
                    ?.removePrefix("www.")
                if (currentHost.equals(host, ignoreCase = true)) url else null
            }

        val actualHost = URI(currentUrl).host?.removePrefix("www.")
        assertTrue(
            actualHost.equals(host, ignoreCase = true),
            "Expected host '$host', but was: $currentUrl"
        )
    }

    @Step("Verify the Bikeleasing logo is visible")
    open fun verifyLogoIsVisible(browser: WebDriver) {
        val logo = WebDriverWait(browser, Duration.ofSeconds(3))
            .withMessage { "Bikeleasing logo was not visible" }
            .until { driver ->
                driver.findElements(By.cssSelector("img[alt='Bikeleasing Logo']"))
                    .firstOrNull { it.isDisplayed }
            }

        assertTrue(
            logo.getAttribute("src").orEmpty().contains("logo-bikeleasing"),
            "Expected the logo image to be the Bikeleasing logo, but was: ${logo.getAttribute("src")}"
        )
    }
}
