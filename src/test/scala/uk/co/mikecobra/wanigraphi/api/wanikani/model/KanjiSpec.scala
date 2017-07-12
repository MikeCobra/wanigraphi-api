package uk.co.mikecobra.wanigraphi.api.wanikani.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser.decode
import org.specs2.mutable.Specification

class KanjiSpec extends Specification {

  "Kanji" should {
    "parse JSON" in {
      val kanjiJson: String =
        """
          |{
          |  "level": 1,
          |  "character": "二",
          |  "meaning": "two",
          |  "onyomi": "に",
          |  "kunyomi": "ふた",
          |  "important_reading": "onyomi",
          |  "nanori": null,
          |  "user_specific": {
          |    "srs": "burned",
          |    "srs_numeric": 9,
          |    "unlocked_date": 1480839063,
          |    "available_date": 5098719600,
          |    "burned": true,
          |    "burned_date": 1498720856,
          |    "meaning_correct": 8,
          |    "meaning_incorrect": 0,
          |    "meaning_max_streak": 8,
          |    "meaning_current_streak": 8,
          |    "reading_correct": 8,
          |    "reading_incorrect": 0,
          |    "reading_max_streak": 8,
          |    "reading_current_streak": 8,
          |    "meaning_note": null,
          |    "reading_note": null,
          |    "user_synonyms": null
          |  }
          |}
        """.stripMargin

      val expected = Kanji(
        level = 1,
        character = "二",
        meaning = Seq("two"),
        onyomi = Seq("に"),
        kunyomi = Seq("ふた"),
        importantReading = "onyomi",
        nanori = None,
        userSpecific = UserSpecific(
          srs = "burned",
          srsNumeric = 9,
          unlockedDate = LocalDateTime.ofEpochSecond(1480839063, 0, ZoneOffset.UTC),
          availableDate = None,
          burned = true,
          burnedDate = Some(LocalDateTime.ofEpochSecond(1498720856, 0, ZoneOffset.UTC)),
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

      val result = decode[Kanji](kanjiJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with lists" in {
      val kanjiWithListsJson =
        """
          |{
          |  "level": 1,
          |  "character": "下",
          |  "meaning": "below, down, under, beneath",
          |  "onyomi": "か, げ",
          |  "kunyomi": "した, さが, くだ, お",
          |  "important_reading": "onyomi",
          |  "nanori": null,
          |  "user_specific": {
          |    "srs": "burned",
          |    "srs_numeric": 9,
          |    "unlocked_date": 1480839042,
          |    "available_date": 5099324400,
          |    "burned": true,
          |    "burned_date": 1499327018,
          |    "meaning_correct": 8,
          |    "meaning_incorrect": 0,
          |    "meaning_max_streak": 8,
          |    "meaning_current_streak": 8,
          |    "reading_correct": 8,
          |    "reading_incorrect": 0,
          |    "reading_max_streak": 8,
          |    "reading_current_streak": 8,
          |    "meaning_note": null,
          |    "reading_note": null,
          |    "user_synonyms": null
          |  }
          |}""".stripMargin

      val expected = Kanji(
        level = 1,
        character = "下",
        meaning = Seq("below", "down", "under", "beneath"),
        onyomi = Seq("か", "げ"),
        kunyomi = Seq("した", "さが", "くだ", "お"),
        importantReading = "onyomi",
        nanori = None,
        userSpecific = UserSpecific(
          srs = "burned",
          srsNumeric = 9,
          unlockedDate = LocalDateTime.ofEpochSecond(1480839042, 0, ZoneOffset.UTC),
          availableDate = None,
          burned = true,
          burnedDate = Some(LocalDateTime.ofEpochSecond(1499327018, 0, ZoneOffset.UTC)),
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

      val result = decode[Kanji](kanjiWithListsJson)

      result shouldEqual Right(expected)
    }

    "parse JSON with null kunyomi field" in {
      val kanjiWithNullKunyomiJson =
        """{
          |  "level": 1,
          |  "character": "工",
          |  "meaning": "construction, industry",
          |  "onyomi": "こう, く",
          |  "kunyomi": null,
          |  "important_reading": "onyomi",
          |  "nanori": null,
          |  "user_specific": {
          |    "srs": "burned",
          |    "srs_numeric": 9,
          |    "unlocked_date": 1480839042,
          |    "available_date": 5099324400,
          |    "burned": true,
          |    "burned_date": 1499327018,
          |    "meaning_correct": 8,
          |    "meaning_incorrect": 0,
          |    "meaning_max_streak": 8,
          |    "meaning_current_streak": 8,
          |    "reading_correct": 8,
          |    "reading_incorrect": 0,
          |    "reading_max_streak": 8,
          |    "reading_current_streak": 8,
          |    "meaning_note": null,
          |    "reading_note": null,
          |    "user_synonyms": null
          |  }
          |}""".stripMargin

      val expected = Kanji(
        level = 1,
        character = "工",
        meaning = Seq("construction", "industry"),
        onyomi = Seq("こう", "く"),
        kunyomi = Seq(),
        importantReading = "onyomi",
        nanori = None,
        userSpecific = UserSpecific(
          srs = "burned",
          srsNumeric = 9,
          unlockedDate = LocalDateTime.ofEpochSecond(1480839042, 0, ZoneOffset.UTC),
          availableDate = None,
          burned = true,
          burnedDate = Some(LocalDateTime.ofEpochSecond(1499327018, 0, ZoneOffset.UTC)),
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

      val result = decode[Kanji](kanjiWithNullKunyomiJson)

      result shouldEqual Right(expected)
    }
  }
}
