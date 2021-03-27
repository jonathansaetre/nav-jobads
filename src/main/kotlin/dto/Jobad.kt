package dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Jobad (@JsonProperty("published") val publishedDate: LocalDateTime, @JsonProperty("description") val description: String)