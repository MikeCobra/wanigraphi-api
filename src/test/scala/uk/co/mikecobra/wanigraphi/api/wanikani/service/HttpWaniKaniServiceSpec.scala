package uk.co.mikecobra.wanigraphi.api.wanikani.service

import java.time.{LocalDateTime, ZoneOffset}

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.specs2.mutable.{BeforeAfter, Specification}
import uk.co.mikecobra.wanigraphi.api.wanikani.model._

class HttpWaniKaniServiceSpec extends Specification {

  val Port = 8080
  val Host = "localhost"

  trait StubServer extends BeforeAfter {
    val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

    def before: Unit = {
      wireMockServer.start()
      WireMock.configureFor(Host, Port)
    }

    def after: Unit = wireMockServer.stop()
  }

  "HttpWaniKaniService" should {
    "should retrieve radical list from the WankiKani Api" in new StubServer {
      val wanikaniResponse: String =
        """
          |{
          |  "user_information": {
          |    "username": "MikeCobra",
          |    "gravatar": "6f072b92be8107f47522f2e8c359c7b0",
          |    "level": 14,
          |    "title": "Turtles",
          |    "about": "",
          |    "website": null,
          |    "twitter": null,
          |    "topics_count": 0,
          |    "posts_count": 0,
          |    "creation_date": 1480679357,
          |    "vacation_date": 1480643132
          |  },
          |  "requested_information": [{
          |      "level": 1,
          |      "character": "一",
          |      "meaning": "ground",
          |      "image_file_name": null,
          |      "image_content_type": null,
          |      "image_file_size": null,
          |      "user_specific": {
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480679357,
          |        "available_date": 5099104800,
          |        "burned": true,
          |        "burned_date": 1499105095,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": null,
          |        "reading_incorrect": null,
          |        "reading_max_streak": null,
          |        "reading_current_streak": null,
          |        "meaning_note": null,
          |        "reading_note": null,
          |        "user_synonyms": null
          |      },
          |      "image": null
          |  }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/radicals"))
        .willReturn(
          aResponse()
            .withBody(wanikaniResponse)
            .withHeader("Content-type", "application/json")
            .withStatus(200)
        )
      )

      val expected = Seq(
        Radical(
          level = 1,
          character = Some("一"),
          meaning = "ground",
          userSpecific = UserSpecific(
            srs = "burned",
            srsNumeric = 9,
            unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
            availableDate = None,
            burned = true,
            burnedDate = Some(LocalDateTime.ofEpochSecond(1499105095, 0, ZoneOffset.UTC)),
            meaningStats = SrsStats(
              correct = 8,
              incorrect = 0,
              maxStreak = 8,
              currentStreak = 8
            ),
            readingStats = None,
            meaningNote = None,
            readingNote = None,
            userSynonyms = Seq()
          ),
          imageData = None
        )
      )

      val service = HttpWaniKaniService(s"http://$Host:$Port")

      val result = service.getRadicalsList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }
  }
}
