package parser

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Config @JsonCreator constructor(
    @JsonProperty("assessments")
    val tagOptions: List<String>
)