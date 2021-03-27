
import dto.Jobad
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime

internal class JobAdsServiceTest {



    @Ignore
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

        val mockJobadsClient = mock(JobadsClient::class.java)

        `when`(mockJobadsClient.fetchJobAds(MockitoHelper.anyObject())).thenReturn(jobads)

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

