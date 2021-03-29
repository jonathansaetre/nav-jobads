

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dto.Jobad
import dto.JobadsPerWeek
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2

private val jsonMapper = JsonMapper.builder().addModule(KotlinModule()).addModule(JavaTimeModule()).build()
private val jobadsClient = JobadsClient()

private val REGEX_KOTLIN = "(?i)\\Wkotlin\\W".toRegex()
private val REGEX_JAVA = "(?i)\\Wjava\\W".toRegex()
private var WEEK_FIELDS: WeekFields = WeekFields.of(Locale.getDefault())

fun fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJson(): String {
    val today = LocalDateTime.now()
    val dateInPast = today.minusMonths(6)
    val jobadsFetched = jobadsClient.fetchJobAds(dateInPast)
    val javaAds = jobadsFetched.filter { jobAd -> REGEX_JAVA.containsMatchIn(jobAd.description) }
    val kotlinAds = jobadsFetched.filter { jobAd -> REGEX_KOTLIN.containsMatchIn(jobAd.description) }

    val javaAdsPerWeek = groupByWeek(javaAds)
    val kotlinAdsPerWeek = groupByWeek(kotlinAds)

    val adsPerWeek = HashMap<Int, JobadsPerWeek>()
    javaAdsPerWeek.forEach { (week, liste) -> adsPerWeek.put(week,
        JobadsPerWeek(week = week, year = liste.getOrNull(0)?.publishedDate?.year ?: 0, antAnnonserJava = liste.size)) }

    kotlinAdsPerWeek.forEach { (week, liste) ->
        adsPerWeek.getOrPut(week) { JobadsPerWeek(week = week, year = liste.getOrNull(0)?.publishedDate?.year ?: 0,
            antAnnonserKotlin = liste.size) }.antAnnonserKotlin = liste.size }

    return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(adsPerWeek.values)
}

private fun groupByWeek(jobadsList: List<Jobad>): Map<Int, List<Jobad>> {
    return jobadsList.groupBy { x -> x.publishedDate.get(WEEK_FIELDS.weekOfYear()) }
}