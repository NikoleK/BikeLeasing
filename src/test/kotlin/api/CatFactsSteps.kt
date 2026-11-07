package api

import io.restassured.http.ContentType
import net.serenitybdd.annotations.Step
import net.serenitybdd.core.di.SerenityInfrastructure
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration
import net.serenitybdd.rest.SerenityRest.given
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse

open class CatFactsSteps {

    private val environmentVariables by lazy {
        SerenityInfrastructure.getEnvironmentVariables()
    }

    private val baseUrl: String
        get() = EnvironmentSpecificConfiguration
            .from(environmentVariables)
            .getProperty("api.base.url")
            ?: error("Missing 'api.base.url' in serenity.conf")

    private val factsPath: String
        get() = EnvironmentSpecificConfiguration
            .from(environmentVariables)
            .getProperty("api.facts.path")
            ?: error("Missing 'api.facts.path' in serenity.conf")

    @Step("Fetch cat facts from API")
    open fun getCatFacts(limit: Int): List<CatFact> {
        val response = given()
            .contentType(ContentType.JSON)
            .baseUri(baseUrl)
            .queryParam("limit", limit)
            .get(factsPath)
            .then()
            .statusCode(200)
            .extract()
            .response()

        return response.`as`(CatFactResponse::class.java).data
    }

    @Step("Validate returned facts count")
    open fun validateFacts(facts: List<CatFact>, expectedCount: Int) {
        assertFalse(facts.isEmpty(), "data should not be empty")
        assertEquals(expectedCount, facts.size, "expected $expectedCount facts")
    }

    @Step("Assert first fact length matches text")
    open fun checkFirstFactLength(facts: List<CatFact>) {
        val first = facts.first()
        // The published schema does not require fact or length and sets no minLength.
        // An empty fact with length 0 is valid. Compare the two values as they are.
        assertEquals(first.length, first.fact.length)
    }
}
