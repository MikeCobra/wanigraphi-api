package uk.co.mikecobra.wanigraphi.api.wanikani.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser.decode
import org.specs2.mutable.Specification

class ApiResponseSpec extends Specification {
  "ApiResponse" should {
    "parse JSON with a list" in {
      val responseWithListJson: String =
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

      val expected = ApiResponse[Seq[Radical]](
        UserInformation(
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
        ),
        Seq(
          Radical(
            level = 1,
            character = Some("一"),
            meaning = "ground",
            userSpecific = Some(UserSpecific(
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
            )),
            imageData = None
          )
        )
      )

      val result = decode[ApiResponse[Seq[Radical]]](responseWithListJson)

      result shouldEqual Right(expected)
    }
  }
}
