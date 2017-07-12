package uk.co.mikecobra.wanigraphi.api.wanikani.model

import io.circe.Decoder

case class Kanji(
                level: Int,
                character: String,
                meaning: Seq[String],
                onyomi: Seq[String],
                kunyomi: Seq[String],
                importantReading: String,
                nanori: Option[String],
                userSpecific: UserSpecific
                )

object Kanji {
  implicit val decoder: Decoder[Kanji] = Decoder.forProduct8(
    "level",
    "character",
    "meaning",
    "onyomi",
    "kunyomi",
    "important_reading",
    "nanori",
    "user_specific"
  ){ (
    level: Int,
    character: String,
    meaning: String,
    onyomi: String,
    kunyomi: Option[String],
    importantReading: String,
    nanori: Option[String],
    userSpecific: UserSpecific
  ) =>
    Kanji(
      level,
      character,
      splitCommaDelimited(meaning),
      splitCommaDelimited(onyomi),
      kunyomi.map(splitCommaDelimited).getOrElse(Seq()),
      importantReading,
      nanori,
      userSpecific
    )
  }

  private def splitCommaDelimited(list: String): Seq[String] =
    list.split(",").map(_.trim)
}


