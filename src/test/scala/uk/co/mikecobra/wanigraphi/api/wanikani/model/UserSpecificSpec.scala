package uk.co.mikecobra.wanigraphi.api.wanikani.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser._
import org.specs2.mutable.Specification

class UserSpecificSpec extends Specification {

  "UserSpecific" should {
    "parse standard JSON" in {
      val userSpecificStandardJson: String =
        """
          |{
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480679357,
          |        "available_date": 1480689357,
          |        "burned": true,
          |        "burned_date": 1499116030,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": 5,
          |        "reading_incorrect": 4,
          |        "reading_max_streak": 2,
          |        "reading_current_streak": 6,
          |        "meaning_note": "means two",
          |        "reading_note": "sounds like knee",
          |        "user_synonyms": ["deux", "one plus one"]
          |      }
        """.stripMargin

      val expected = UserSpecific(
        srs = "burned",
        srsNumeric = 9,
        unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        availableDate = Some(LocalDateTime.ofEpochSecond(1480689357, 0, ZoneOffset.UTC)),
        burned = true,
        burnedDate = Some(LocalDateTime.ofEpochSecond(1499116030, 0, ZoneOffset.UTC)),
        meaningStats = SrsStats(
          correct = 8,
          incorrect = 0,
          maxStreak = 8,
          currentStreak = 8
        ),
        readingStats = Some(SrsStats(
          correct = 5,
          incorrect = 4,
          maxStreak = 2,
          currentStreak = 6
        )),
        meaningNote = Some("means two"),
        readingNote = Some("sounds like knee"),
        userSynonyms = Seq("deux", "one plus one")
      )

      val result = decode[UserSpecific](userSpecificStandardJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with 0 burned date" in {
      val userSpecificZeroBurnedDateJson: String =
        """
          |{
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480679357,
          |        "available_date": 1480689357,
          |        "burned": true,
          |        "burned_date": 0,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": 5,
          |        "reading_incorrect": 4,
          |        "reading_max_streak": 2,
          |        "reading_current_streak": 6,
          |        "meaning_note": "means two",
          |        "reading_note": "sounds like knee",
          |        "user_synonyms": ["deux", "one plus one"]
          |      }
        """.stripMargin

      val expected = UserSpecific(
        srs = "burned",
        srsNumeric = 9,
        unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        availableDate = Some(LocalDateTime.ofEpochSecond(1480689357, 0, ZoneOffset.UTC)),
        burned = true,
        burnedDate = None,
        meaningStats = SrsStats(
          correct = 8,
          incorrect = 0,
          maxStreak = 8,
          currentStreak = 8
        ),
        readingStats = Some(SrsStats(
          correct = 5,
          incorrect = 4,
          maxStreak = 2,
          currentStreak = 6
        )),
        meaningNote = Some("means two"),
        readingNote = Some("sounds like knee"),
        userSynonyms = Seq("deux", "one plus one")
      )

      val result = decode[UserSpecific](userSpecificZeroBurnedDateJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with never available date" in {
      val userSpecificZeroBurnedDateJson: String =
        """
          |{
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480679357,
          |        "available_date": 5099288400,
          |        "burned": true,
          |        "burned_date": 1480689357,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": 5,
          |        "reading_incorrect": 4,
          |        "reading_max_streak": 2,
          |        "reading_current_streak": 6,
          |        "meaning_note": "means two",
          |        "reading_note": "sounds like knee",
          |        "user_synonyms": ["deux", "one plus one"]
          |      }
        """.stripMargin

      val expected = UserSpecific(
        srs = "burned",
        srsNumeric = 9,
        unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        availableDate = None,
        burned = true,
        burnedDate = Some(LocalDateTime.ofEpochSecond(1480689357, 0, ZoneOffset.UTC)),
        meaningStats = SrsStats(
          correct = 8,
          incorrect = 0,
          maxStreak = 8,
          currentStreak = 8
        ),
        readingStats = Some(SrsStats(
          correct = 5,
          incorrect = 4,
          maxStreak = 2,
          currentStreak = 6
        )),
        meaningNote = Some("means two"),
        readingNote = Some("sounds like knee"),
        userSynonyms = Seq("deux", "one plus one")
      )

      val result = decode[UserSpecific](userSpecificZeroBurnedDateJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with no reading statistics" in {
      val userSpecificNoReadingStatsJson: String =
        """
          |{
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480679357,
          |        "available_date": 5099288400,
          |        "burned": true,
          |        "burned_date": 1480689357,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": null,
          |        "reading_incorrect": null,
          |        "reading_max_streak": null,
          |        "reading_current_streak": null,
          |        "meaning_note": "means two",
          |        "reading_note": "sounds like knee",
          |        "user_synonyms": ["deux", "one plus one"]
          |      }
        """.stripMargin

      val expected = UserSpecific(
        srs = "burned",
        srsNumeric = 9,
        unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        availableDate = None,
        burned = true,
        burnedDate = Some(LocalDateTime.ofEpochSecond(1480689357, 0, ZoneOffset.UTC)),
        meaningStats = SrsStats(
          correct = 8,
          incorrect = 0,
          maxStreak = 8,
          currentStreak = 8
        ),
        readingStats = None,
        meaningNote = Some("means two"),
        readingNote = Some("sounds like knee"),
        userSynonyms = Seq("deux", "one plus one")
      )

      val result = decode[UserSpecific](userSpecificNoReadingStatsJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with empty user synonyms" in {
      val userSpecificNoReadingStatsJson: String =
        """
          |{
          |        "srs": "burned",
          |        "srs_numeric": 9,
          |        "unlocked_date": 1480679357,
          |        "available_date": 5099288400,
          |        "burned": true,
          |        "burned_date": 1480689357,
          |        "meaning_correct": 8,
          |        "meaning_incorrect": 0,
          |        "meaning_max_streak": 8,
          |        "meaning_current_streak": 8,
          |        "reading_correct": null,
          |        "reading_incorrect": null,
          |        "reading_max_streak": null,
          |        "reading_current_streak": null,
          |        "meaning_note": "means two",
          |        "reading_note": "sounds like knee",
          |        "user_synonyms": null
          |      }
        """.stripMargin

      val expected = UserSpecific(
        srs = "burned",
        srsNumeric = 9,
        unlockedDate = LocalDateTime.ofEpochSecond(1480679357, 0, ZoneOffset.UTC),
        availableDate = None,
        burned = true,
        burnedDate = Some(LocalDateTime.ofEpochSecond(1480689357, 0, ZoneOffset.UTC)),
        meaningStats = SrsStats(
          correct = 8,
          incorrect = 0,
          maxStreak = 8,
          currentStreak = 8
        ),
        readingStats = None,
        meaningNote = Some("means two"),
        readingNote = Some("sounds like knee"),
        userSynonyms = Seq()
      )

      val result = decode[UserSpecific](userSpecificNoReadingStatsJson)

      result shouldEqual Right(expected)
    }
  }
}
