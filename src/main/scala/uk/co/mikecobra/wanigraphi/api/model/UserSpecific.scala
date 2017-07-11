package uk.co.mikecobra.wanigraphi.api.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.Decoder

case class SrsStats(
                     correct: Int,
                     incorrect: Int,
                     maxStreak: Int,
                     currentStreak: Int
                   )

case class UserSpecific(
                         srs: String,
                         srsNumeric: Int,
                         unlockedDate: LocalDateTime,
                         availableDate: Option[LocalDateTime],
                         burned: Boolean,
                         burnedDate: Option[LocalDateTime],
                         meaningStats: SrsStats,
                         readingStats: Option[SrsStats],
                         meaningNote: Option[String],
                         readingNote: Option[String],
                         userSynonyms: Seq[String]
                       )

object UserSpecific {

  implicit val decoder: Decoder[UserSpecific] = Decoder.forProduct17(
    "srs",
    "srs_numeric",
    "unlocked_date",
    "available_date",
    "burned",
    "burned_date",
    "meaning_correct",
    "meaning_incorrect",
    "meaning_max_streak",
    "meaning_current_streak",
    "reading_correct",
    "reading_incorrect",
    "reading_max_streak",
    "reading_current_streak",
    "meaning_note",
    "reading_note",
    "user_synonyms"
  ) { (
        srs: String,
        srsNumeric: Int,
        unlockedDate: Long,
        availableDate: Long,
        burned: Boolean,
        burnedDate: Long,
        meaningCorrect: Int,
        meaningIncorrect: Int,
        meaningMaxStreak: Int,
        meaningCurrentStreak: Int,
        readingCorrect: Option[Int],
        readingIncorrect: Option[Int],
        readingMaxStreak: Option[Int],
        readingCurrentStreak: Option[Int],
        meaningNote: Option[String],
        readingNote: Option[String],
        userSynonyms: Option[Seq[String]]
      ) =>
    UserSpecific(
      srs,
      srsNumeric,
      LocalDateTime.ofEpochSecond(unlockedDate, 0, ZoneOffset.UTC),
      handleAvailableDate(availableDate),
      burned,
      handleBurnedDate(burnedDate),
      SrsStats(
        meaningCorrect,
        meaningIncorrect,
        meaningMaxStreak,
        meaningCurrentStreak),
      handleReadingStats(
        readingCorrect,
        readingIncorrect,
        readingMaxStreak,
        readingCurrentStreak),
      meaningNote,
      readingNote,
      userSynonyms.getOrElse(Seq())
    )
  }

  private def handleReadingStats(
                                  correctOption: Option[Int],
                                  incorrectOption: Option[Int],
                                  maxStreakOption: Option[Int],
                                  currentStreakOption: Option[Int]): Option[SrsStats] =
    for {
      correct <- correctOption
      incorrect <- incorrectOption
      maxStreak <- maxStreakOption
      currentStreak <- currentStreakOption
    } yield SrsStats(correct, incorrect, maxStreak, currentStreak)

  private def handleAvailableDate(epoch: Long): Option[LocalDateTime] =
    if (epoch >= 5000000000L) None
    else Some(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC))

  private def handleBurnedDate(epoch: Long): Option[LocalDateTime] =
    if (epoch == 0L) None
    else Some(LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC))
}