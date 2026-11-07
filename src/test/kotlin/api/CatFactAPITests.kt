package api

import net.serenitybdd.annotations.Steps
import net.serenitybdd.junit5.SerenityJUnit5Extension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(SerenityJUnit5Extension::class)
@DisplayName("Cat Facts API")
class CatFactAPITests {

    @Steps
    lateinit var catFactsSteps: CatFactsSteps

    @Test
    @DisplayName("Fetch cat facts and validate count and length")
    fun testGetCatFacts() {
        val receivedCatFacts = catFactsSteps.getCatFacts(5)
        catFactsSteps.validateFacts(receivedCatFacts, expectedCount = 5)
        catFactsSteps.checkFirstFactLength(receivedCatFacts)
    }
}
