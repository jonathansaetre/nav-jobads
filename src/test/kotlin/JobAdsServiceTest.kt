
import dto.Jobad
import dto.JobadsPerWeek
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

internal class JobAdsServiceTest {

    @Mock
    private lateinit var jobadsClientMock: JobadsClient

    private lateinit var jobAdsService: JobAdsService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        jobAdsService = JobAdsService(jobadsClientMock)
    }


    @Test
    fun fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJsonTest() {

        val publishDate1 = LocalDateTime.of(2020,12, 1, 0,0)
        val publishDate2 = LocalDateTime.of(2021,1, 4, 0,0)
        val publishDate3 = LocalDateTime.of(2021,2, 1, 0,0)
        val jobads = ArrayList<Jobad>()
        jobads.add(Jobad(publishDate3, "kotlin"))

        jobads.add(Jobad(publishDate1, "asdasd java a"))
        jobads.add(Jobad(publishDate1, "asdasd java."))
        jobads.add(Jobad(publishDate1, "Java asdasd"))
        jobads.add(Jobad(publishDate1, "javaasdasd"))
        jobads.add(Jobad(publishDate1, "javKO java"))
        jobads.add(Jobad(publishDate1, "java"))
        jobads.add(Jobad(publishDate1, "sdfsdfsdjava"))
        jobads.add(Jobad(publishDate1, "javasdfsdfsdf"))
        jobads.add(Jobad(publishDate1, "java!"))

        jobads.add(Jobad(publishDate1, "asdasd kotlin a"))
        jobads.add(Jobad(publishDate1, "asdasd kotlin."))
        jobads.add(Jobad(publishDate1, "KOTLIN asdasd"))
        jobads.add(Jobad(publishDate1, "javKOTLINsdasd"))
        jobads.add(Jobad(publishDate1, "javKO kotlin"))
        jobads.add(Jobad(publishDate1, "kotlin"))
        jobads.add(Jobad(publishDate1, "sdfsdfkotlin"))
        jobads.add(Jobad(publishDate1, "kotlinsdfsdf"))

        jobads.add(Jobad(publishDate2, "java"))

        `when`(jobadsClientMock.fetchJobAds(any(LocalDateTime::class.java))).thenReturn(jobads)

        val result = jobAdsService.fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJson()

        val exptectedResult = arrayListOf(
            JobadsPerWeek(49, 2020, 6, 5),
            JobadsPerWeek(1, 2021, 1, 0),
            JobadsPerWeek(5, 2021, 0, 1)
        )

        assertEquals(result.size, exptectedResult.size)
        assertEquals(exptectedResult[0].week, result[0].week)
        assertEquals(exptectedResult[0].year, result[0].year)
        assertEquals(exptectedResult[0].antAnnonserJava, result[0].antAnnonserJava)
        assertEquals(exptectedResult[0].antAnnonserKotlin, result[0].antAnnonserKotlin)

        assertEquals(exptectedResult[1].week, result[1].week)
        assertEquals(exptectedResult[1].year, result[1].year)
        assertEquals(exptectedResult[1].antAnnonserJava, result[1].antAnnonserJava)
        assertEquals(exptectedResult[1].antAnnonserKotlin, result[1].antAnnonserKotlin)

        assertEquals(exptectedResult[2].week, result[2].week)
        assertEquals(exptectedResult[2].year, result[2].year)
        assertEquals(exptectedResult[2].antAnnonserJava, result[2].antAnnonserJava)
        assertEquals(exptectedResult[2].antAnnonserKotlin, result[2].antAnnonserKotlin)
    }
}

