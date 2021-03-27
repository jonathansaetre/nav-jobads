import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dto.Jobad
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class JobadsClient {
    private val JOBADS_API_URL = "https://arbeidsplassen.nav.no/public-feed/api/v1/ads"
    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwdWJsaWMudG9rZW4udjFAbmF2Lm5vIiwiYXVkIjoiZmVlZC1hcGktdjEiLCJpc3MiOiJuYXYubm8iLCJpYXQiOjE1NTc0NzM0MjJ9.jNGlLUF9HxoHo5JrQNMkweLj_91bgk97ZebLdfx3_UQ"
    private val jsonMapper = JsonMapper.builder().addModule(KotlinModule()).addModule(JavaTimeModule()).build()
    private var jobadListReader = jsonMapper.readerFor(object : TypeReference<List<Jobad>>() {})

    private val httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(10))
        .build()


    fun fetchJobAds(fromPublishDate: LocalDateTime?): List<Jobad> {
        val jobAds = ArrayList<Jobad>()
        var page = 0
        var tempPublishDate: LocalDateTime? = null
        while(true) {
            val jsonNode = fetchPage(tempPublishDate, page)
            jobAds.addAll(mapToJobAds(jsonNode))
            if (jsonNode.get("last").asBoolean()) {
                tempPublishDate = jobAds[jobAds.size - 1].publishedDate
                if(tempPublishDate.isBefore(fromPublishDate)) break
                page = 0
            }
            page += 1
        }
        jobAds.removeIf { x -> x.publishedDate.isBefore(fromPublishDate) }
        return jobAds
    }

    private fun fetchPage(fromPublishDate: LocalDateTime? = null, page: Int = 0): JsonNode {
        var url = "$JOBADS_API_URL?page=$page"
        if(fromPublishDate != null) url = url.plus("&published=[*,${DATE_FORMATTER.format(fromPublishDate)})")
        val request: HttpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .setHeader("Authorization", "Bearer $jwtToken")
            .build()

        val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) throw IOException("Jobads-request failed, error=\"${response.body()}\", " +
                "statusCode=${response.statusCode()}")

        return jsonMapper.readTree(response.body())
    }

    private fun mapToJobAds(jsonNode: JsonNode): List<Jobad> {
        val contentArrayNode = jsonNode.get("content") as ArrayNode
        return jobadListReader.readValue(contentArrayNode)
    }
}
