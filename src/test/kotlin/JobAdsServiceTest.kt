
import dto.Jobad
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

internal class JobAdsServiceTest {

    @Mock
    private lateinit var jobadsClientMock: JobadsClient

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }



    @Test
    fun fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJsonTest() {

        val jobads = ArrayList<Jobad>()
        jobads.add(Jobad(LocalDateTime.now(), "asdasd java a"))
        jobads.add(Jobad(LocalDateTime.now(), "asdasd java."))
        jobads.add(Jobad(LocalDateTime.now(), "Java asdasd"))
        jobads.add(Jobad(LocalDateTime.now(), "javaasdasd"))

        jobads.add(Jobad(LocalDateTime.now(), "asdasd kotlin a"))
        jobads.add(Jobad(LocalDateTime.now(), "asdasd kotlin."))
        jobads.add(Jobad(LocalDateTime.now(), "KOTLIN asdasd"))
        jobads.add(Jobad(LocalDateTime.now(), "javKOTLINsdasd"))

        `when`(jobadsClientMock.fetchJobAds(any(LocalDateTime::class.java))).thenReturn(jobads)

        val result = fetchJobsAds_last6Month_containsJavaOrKotlin_prettyJson()

        val k = ""
    }

    object MockitoHelper {
        fun <T> anyObject(): T {
            Mockito.any<T>()
            return uninitialized()
        }
        @Suppress("UNCHECKED_CAST")
        fun <T> uninitialized(): T =  null as T
    }
}

