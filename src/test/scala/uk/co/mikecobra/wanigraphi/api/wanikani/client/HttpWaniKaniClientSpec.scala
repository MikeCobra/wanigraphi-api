package uk.co.mikecobra.wanigraphi.api.wanikani.client

import java.time.{LocalDateTime, ZoneOffset}

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.specs2.mutable.{BeforeAfter, Specification}
import uk.co.mikecobra.wanigraphi.api.wanikani.model._

class HttpWaniKaniClientSpec extends Specification {
  sequential // Every test needs to do stuff with WireMock

  val port = 8080
  val host = "localhost"

  trait StubServer extends BeforeAfter {
    val wireMockServer = new WireMockServer(wireMockConfig().port(port))

    def before = {
      wireMockServer.start()
      WireMock.configureFor(host, port)
    }

    def after = wireMockServer.stop()
  }

  "HttpWaniKaniService" should {
    "retrieve user information from the WaniKaniApi" in new StubServer {
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

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getUserInformation("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve kanji list from the WankiKani Api" in new StubServer {
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
          |      "user_specific": null
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
          userSpecific = None
        )
      )

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getKanjiList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve kanji list for single level from the WankiKani Api" in new StubServer  {
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
          |      "user_specific": null
          |    }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/kanji/1"))
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
          userSpecific = None
        ))

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getKanjiListForLevels("fakeApiKey", Seq(1))

      result.unsafeRun shouldEqual expected
    }

    "retrieve kanji list for multiple levels from the WankiKani Api" in new StubServer  {
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
          |      "user_specific": null
          |    }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/kanji/1,3"))
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
          userSpecific = None
        ))

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getKanjiListForLevels("fakeApiKey", Seq(1,3))

      result.unsafeRun shouldEqual expected
    }

    "retrieve radical list from the WankiKani Api" in new StubServer {
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
          |      "user_specific": null,
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
          userSpecific = None,
          imageData = None
        )
      )

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getRadicalsList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve radical list for single level from the WankiKani Api" in new StubServer {
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
          |      "user_specific": null,
          |      "image": null
          |  }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/radicals/1"))
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
          userSpecific = None,
          imageData = None
        )
      )

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getRadicalsListForLevels("fakeApiKey", Seq(1))

      result.unsafeRun shouldEqual expected
    }

    "retrieve radical list for multiple levels from the WankiKani Api" in new StubServer {
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
          |      "user_specific": null,
          |      "image": null
          |  }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/radicals/1,4"))
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
          userSpecific = None,
          imageData = None
        )
      )

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getRadicalsListForLevels("fakeApiKey", Seq(1, 4))

      result.unsafeRun shouldEqual expected
    }

    "retrieve vocabulary list from the WankiKani Api" in new StubServer {
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
          |     "user_specific": null
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
          userSpecific = None
        )
      )

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getVocabularyList("fakeApiKey")

      result.unsafeRun shouldEqual expected
    }

    "retrieve vocabulary list for single level from the WankiKani Api" in new StubServer  {
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
          |     "user_specific": null
          |  }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/vocabulary/1"))
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
          userSpecific = None
      ))

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getVocabularyListForLevels("fakeApiKey", Seq(1))

      result.unsafeRun shouldEqual expected
    }

    "retrieve vocabulary list for multiple levels from the WankiKani Api" in new StubServer {
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
          |     "user_specific": null
          |  }]
          |}
        """.stripMargin

      stubFor(get(urlEqualTo("/api/user/fakeApiKey/vocabulary/1,2"))
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
          userSpecific = None
        ))

      val service = HttpWaniKaniClient(s"http://$host:$port")

      val result = service.getVocabularyListForLevels("fakeApiKey", Seq(1,2))

      result.unsafeRun shouldEqual expected
    }
  }
}
