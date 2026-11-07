package api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// The requirement checks fact and length only. Extra properties on a fact are out of scope.
@JsonIgnoreProperties(ignoreUnknown = true)
data class CatFact(
    val fact: String = "",
    val length: Int = 0,
)

// The requirement reads the data list only. Pagination fields on the response are out of scope.
@JsonIgnoreProperties(ignoreUnknown = true)
data class CatFactResponse(
    val data: List<CatFact> = emptyList(),
)
