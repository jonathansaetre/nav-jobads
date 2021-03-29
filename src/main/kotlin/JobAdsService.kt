import dto.Jobad
import dto.JobadsPerWeek
import dto.WeekYear
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2

class JobAdsService(private val jobadsClient: JobadsClient) {


    private val REGEX_JAVA = "^java\\W|\\Wjava\$|^java\$|\\Wjava\\W".toRegex(RegexOption.IGNORE_CASE)
    private val REGEX_KOTLIN = "^kotlin\\W|\\Wkotlin\$|^kotlin\$|\\Wkotlin\\W".toRegex(RegexOption.IGNORE_CASE)
    private var WEEK_FIELDS: WeekFields = WeekFields.of(Locale.getDefault())


    fun fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJson(): List<JobadsPerWeek> {
        val today = LocalDateTime.now()
        val dateInPast = today.minusMonths(6)
        val jobadsFetched = jobadsClient.fetchJobAds(dateInPast)
        val javaAds = jobadsFetched.filter { jobAd -> REGEX_JAVA.containsMatchIn(jobAd.description) }
        val kotlinAds = jobadsFetched.filter { jobAd -> REGEX_KOTLIN.containsMatchIn(jobAd.description) }

        val javaAdsPerWeek = groupByWeek(javaAds)
        val kotlinAdsPerWeek = groupByWeek(kotlinAds)

        val adsPerWeek = HashMap<WeekYear, JobadsPerWeek>()
        javaAdsPerWeek.forEach { (week_year, liste) -> adsPerWeek.put(week_year,
            JobadsPerWeek(week = week_year.week, year = week_year.year, antAnnonserJava = liste.size)) }

        kotlinAdsPerWeek.forEach { (week_year, liste) ->
            adsPerWeek.getOrPut(week_year) { JobadsPerWeek(week = week_year.week, year = week_year.year,
                antAnnonserKotlin = liste.size) }.antAnnonserKotlin = liste.size }

        return ArrayList(adsPerWeek.toSortedMap().values)
    }

    private fun groupByWeek(jobadsList: List<Jobad>): Map<WeekYear, List<Jobad>> {
        return jobadsList.groupBy { x -> WeekYear(x.publishedDate.get(WEEK_FIELDS.weekOfYear()),
            x.publishedDate.year) }
    }

}