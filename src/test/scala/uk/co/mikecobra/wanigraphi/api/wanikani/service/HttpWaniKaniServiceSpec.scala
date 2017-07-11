package uk.co.mikecobra.wanigraphi.api.wanikani.service

import java.time.{LocalDateTime, ZoneOffset}

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterEach
import uk.co.mikecobra.wanigraphi.api.wanikani.model._

class HttpWaniKaniServiceSpec extends Specification with BeforeAfterEach {

  val port = 8080
  val host = "localhost"

  val wireMockServer = new WireMockServer(wireMockConfig().port(port))

  def before: Unit = {
    wireMockServer.start()
    WireMock.configureFor(host, port)
  }

  def after: Unit = wireMockServer.stop()

  "HttpWaniKaniService" should {
    "retrieve user information from the WaniKaniApi" in {
      val waniKaniResponse =
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
          |  "requested_information": null
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/user-information"))
        .willReturn(
          aResponse()
            .withBody(waniKaniResponse)
            .withHeader("Content-type", "application/json")
            .withStatus(200)
        )
      )

      val expected = UserInformation(
        username = "MikeCobra",
        gravatar = "6f072b92be8107f47522f2e8c359c7b0",
        level = 14,
        title = "Turtles",
        about = "",
        website = None,
        twitter = None,
        topicsCount = 0,
        postsCount = 0,
        creationDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        vacationDate = Some(LocalDateTime.ofEpochSecond(1480643132, 0, ZoneOffset.UTC))
      )

      val service = HttpWaniKaniService(s"http://$host:$port")

      val result = service.getUserInformation("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve kanji list from the WankiKani Api" in {
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
          |      "character": "川",
          |      "meaning": "river",
          |      "onyomi": "せん",
          |      "kunyomi": "かわ",
          |      "important_reading": "kunyomi",
          |      "nanori": null,
          |      "user_specific": {
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480838989,
          |        "available_date": 5099108400,
          |        "burned": true,
          |        "burned_date": 1499109857,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": 8,
          |        "reading_incorrect": 0,
          |        "reading_max_streak": 8,
          |        "reading_current_streak": 8,
          |        "meaning_note": null,
          |        "reading_note": null,
          |        "user_synonyms": null
          |      }
          |    }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/kanji"))
        .willReturn(
          aResponse()
            .withBody(wanikaniResponse)
            .withHeader("Content-type", "application/json")
            .withStatus(200)
        )
      )

      val expected = Seq(
        Kanji(
          level = 1,
          character = "川",
          meaning = Seq("river"),
          onyomi = Seq("せん"),
          kunyomi = Seq("かわ"),
          importantReading = "kunyomi",
          nanori = None,
          userSpecific = UserSpecific(
            srs = "burned",
            srsNumeric = 9,
            unlockedDate = LocalDateTime.ofEpochSecond(1480838989, 0, ZoneOffset.UTC),
            availableDate = None,
            burned = true,
            burnedDate = Some(LocalDateTime.ofEpochSecond(1499109857, 0, ZoneOffset.UTC)),
            meaningStats = SrsStats(
              correct = 8,
              incorrect = 0,
              maxStreak = 8,
              currentStreak = 8
            ),
            readingStats = Some(SrsStats(
              correct = 8,
              incorrect = 0,
              maxStreak = 8,
              currentStreak = 8
            )),
            meaningNote = None,
            readingNote = None,
            userSynonyms = Seq()
          )
        )
      )

      val service = HttpWaniKaniService(s"http://$host:$port")

      val result = service.getKanjiList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve radical list from the WankiKani Api" in {
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

      val service = HttpWaniKaniService(s"http://$host:$port")

      val result = service.getRadicalsList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve vocabulary list from the WankiKani Api" in {
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
          |     "level": 1,
          |     "character": "アメリカ人",
          |     "kana": "あめりかじん, アメリカじん",
          |     "meaning": "american, american person",
          |     "user_specific": {
          |       "srs": "burned",
          |       "srs_numeric": 9,
          |       "unlocked_date": 1481007840,
          |       "available_date": 5099281200,
          |       "burned": true,
          |       "burned_date": 1499284424,
          |       "meaning_correct": 10,
          |       "meaning_incorrect": 1,
          |       "meaning_max_streak": 7,
          |       "meaning_current_streak": 7,
          |       "reading_correct": 10,
          |       "reading_incorrect": 0,
          |       "reading_max_streak": 10,
          |       "reading_current_streak": 10,
          |       "meaning_note": null,
          |       "reading_note": null,
                  "user_synonyms": null
          |     }
          |  }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/vocabulary"))
        .willReturn(
          aResponse()
            .withBody(wanikaniResponse)
            .withHeader("Content-type", "application/json")
            .withStatus(200)
        )
      )

      val expected = Seq(
        Vocabulary(
          level = 1,
          character = "アメリカ人",
          kana = Seq("あめりかじん", "アメリカじん"),
          meaning = Seq("american", "american person"),
          userSpecific = UserSpecific(
            srs = "burned",
            srsNumeric = 9,
            unlockedDate = LocalDateTime.ofEpochSecond(1481007840, 0, ZoneOffset.UTC),
            availableDate = None,
            burned = true,
            burnedDate = Some(LocalDateTime.ofEpochSecond(1499284424, 0, ZoneOffset.UTC)),
            meaningStats = SrsStats(
              correct = 10,
              incorrect = 1,
              maxStreak = 7,
              currentStreak = 7
            ),
            readingStats = Some(SrsStats(
              correct = 10,
              incorrect = 0,
              maxStreak = 10,
              currentStreak = 10
            )),
            meaningNote = None,
            readingNote = None,
            userSynonyms = Seq()
          )
        )
      )

      val service = HttpWaniKaniService(s"http://$host:$port")

      val result = service.getVocabularyList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }
  }
}
