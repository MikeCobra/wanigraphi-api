package uk.co.mikecobra.wanigraphi.api.wanikani.model

import io.circe.Decoder

case class Vocabulary(
                  level: Int,
                  character: String,
                  kana: Seq[String],
                  meaning: Seq[String],
                  userSpecific: UserSpecific
                )

object Vocabulary {
  implicit val decoder: Decoder[Vocabulary] = Decoder.forProduct5(
    "level",
    "character",
    "kana",
    "meaning",
    "user_specific"
  ){ (
       level: Int,
       character: String,
       kana: String,
       meaning: String,
       userSpecific: UserSpecific
     ) =>
    Vocabulary(
      level,
      character,
      splitCommaDelimited(kana),
      splitCommaDelimited(meaning),
      userSpecific
    )
  }

  private def splitCommaDelimited(list: String): Seq[String] =
    list.split(",").map(_.trim)
}



