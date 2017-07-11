package uk.co.mikecobra.wanigraphi.api.wanikani.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser._
import org.specs2.mutable.Specification

class RadicalSpec extends Specification {
  "Radical" should {
    "parse JSON with character" in {
      val radicalWithCharacterJson: String =
        """
          |{
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
          |    }
        """.stripMargin

      val expected = Radical(
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

      val result = decode[Radical](radicalWithCharacterJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with image" in {
      val radicalWithCharacterJson: String =
        """
          |{
          |      "level": 1,
          |      "character": null,
          |      "meaning": "leaf",
          |      "image_file_name": "leaf.png",
          |      "image_content_type": "image/png",
          |      "image_file_size": 13708,
          |      "user_specific": {
          |        "srs": "guru",
          |        "srs_numeric": 6,
          |        "unlocked_date": 1480679357,
          |        "available_date": 1500321600,
          |        "burned": false,
          |        "burned_date": 0,
          |        "meaning_correct": 10,
          |        "meaning_incorrect": 2,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 1,
          |        "reading_correct": null,
          |        "reading_incorrect": null,
          |        "reading_max_streak": null,
          |        "reading_current_streak": null,
          |        "meaning_note": null,
          |        "reading_note": null,
          |        "user_synonyms": null
          |      },
          |      "image": "https://cdn.wanikani.com/images/radicals/ca386aec7915ab74e52ec506ed2e532d4631bb06.png"
          |    }
        """.stripMargin

      val expected = Radical(
        level = 1,
        character = None,
        meaning = "leaf",
        userSpecific = UserSpecific(
          srs = "guru",
          srsNumeric = 6,
          unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
          availableDate = Some(LocalDateTime.ofEpochSecond(1500321600, 0, ZoneOffset.UTC)),
          burned = false,
          burnedDate = None,
          meaningStats = SrsStats(
            correct = 10,
            incorrect = 2,
            maxStreak = 8,
            currentStreak = 1
          ),
          readingStats = None,
          meaningNote = None,
          readingNote = None,
          userSynonyms = Seq()
        ),
        imageData = Some(ImageData(
          filename = "leaf.png",
          contentType = "image/png",
          filesize = 13708,
          url = "https://cdn.wanikani.com/images/radicals/ca386aec7915ab74e52ec506ed2e532d4631bb06.png"
        ))
      )

      val result = decode[Radical](radicalWithCharacterJson)

      result shouldEqual Right(expected)
    }
  }
}