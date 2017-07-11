package uk.co.mikecobra.wanigraphi.api.wanikani.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser.decode
import org.specs2.mutable.Specification

class VocabularySpec extends Specification {

  "Vocabulary" should {
    "parse JSON" in {
      val vocabularyJson: String =
        """
          |{
          |  "level": 1,
          |  "character": "十",
          |  "kana": "じゅう",
          |  "meaning": "ten",
          |  "user_specific": {
          |    "srs": "enlighten",
          |    "srs_numeric": 8,
          |    "unlocked_date": 1481007820,
          |    "available_date": 1502611200,
          |    "burned": false,
          |    "burned_date": 0,
          |    "meaning_correct": 10,
          |    "meaning_incorrect": 1,
          |    "meaning_max_streak": 6,
          |    "meaning_current_streak": 4,
          |    "reading_correct": 10,
          |    "reading_incorrect": 0,
          |    "reading_max_streak": 10,
          |    "reading_current_streak": 10,
          |    "meaning_note": null,
          |    "reading_note": null,
          |    "user_synonyms": null
          |  }
          |}
        """.stripMargin

      val expected = Vocabulary(
        level = 1,
        character = "十",
        kana = Seq("じゅう"),
        meaning = Seq("ten"),
        userSpecific = UserSpecific(
          srs = "enlighten",
          srsNumeric = 8,
          unlockedDate = LocalDateTime.ofEpochSecond(1481007820, 0, ZoneOffset.UTC),
          availableDate = Some(LocalDateTime.ofEpochSecond(1502611200, 0, ZoneOffset.UTC)),
          burned = false,
          burnedDate = None,
          meaningStats = SrsStats(
            correct = 10,
            incorrect = 1,
            maxStreak = 6,
            currentStreak = 4
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

      val result = decode[Vocabulary](vocabularyJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with lists" in {
      val vocabularyWithListsJson =
        """
          |{
          |  "level": 1,
          |  "character": "アメリカ人",
          |  "kana": "あめりかじん, アメリカじん",
          |  "meaning": "american, american person",
          |  "user_specific": {
          |    "srs": "burned",
          |    "srs_numeric": 9,
          |    "unlocked_date": 1481007840,
          |    "available_date": 5099281200,
          |    "burned": true,
          |    "burned_date": 1499284424,
          |    "meaning_correct": 10,
          |    "meaning_incorrect": 1,
          |    "meaning_max_streak": 7,
          |    "meaning_current_streak": 7,
          |    "reading_correct": 10,
          |    "reading_incorrect": 0,
          |    "reading_max_streak": 10,
          |    "reading_current_streak": 10,
          |    "meaning_note": null,
          |    "reading_note": null,
          |    "user_synonyms": null
          |  }
          |}""".stripMargin

      val expected = Vocabulary(
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

      val result = decode[Vocabulary](vocabularyWithListsJson)

      result shouldEqual Right(expected)
    }
  }
}
