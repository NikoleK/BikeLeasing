package ui

import net.serenitybdd.annotations.Managed
import net.serenitybdd.annotations.Steps
import net.serenitybdd.junit5.SerenityJUnit5Extension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openqa.selenium.WebDriver

@ExtendWith(SerenityJUnit5Extension::class)
@DisplayName("Bikeleasing website")
class BikeleasingUITests {

    @Managed(driver = "chrome")
    lateinit var driver: WebDriver

    @Steps
    lateinit var websiteSteps: BikeleasingUISteps

    @Test
    @DisplayName("Open www.bikeleasing.de and verify the URL and logo")
    fun openBikeleasingHomepageAndVerifyUrl() {
        websiteSteps.openHomepage(driver)
        websiteSteps.verifyCurrentUrlContainsBikeleasing(driver)
        // Not in the requirement. Checks that the page DOM is available. Depends on Logo name
        websiteSteps.verifyLogoIsVisible(driver)
    }
}
