import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

val JSON_MAPPER = JsonMapper.builder().addModule(KotlinModule()).addModule(JavaTimeModule()).build()!!

fun main() {
    val jobAdsService = JobAdsService(JobadsClient())
    val result = jobAdsService.fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJson()

    println(JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(result))
}
