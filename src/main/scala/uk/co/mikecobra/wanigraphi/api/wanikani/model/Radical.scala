package uk.co.mikecobra.wanigraphi.api.wanikani.model

import io.circe.Decoder

case class ImageData(
                      filename: String,
                      contentType: String,
                      filesize: Int,
                      url: String
                    )

case class Radical(
                    level: Int,
                    character: Option[String],
                    meaning: String,
                    userSpecific: Option[UserSpecific],
                    imageData: Option[ImageData]
                  )

object Radical {
  implicit val decoder: Decoder[Radical] = Decoder.forProduct8(
    "level",
    "character",
    "meaning",
    "image_file_name",
    "image_content_type",
    "image_file_size",
    "user_specific",
    "image"
  ) {
    (
      level: Int,
      character: Option[String],
      meaning: String,
      imageFilename: Option[String],
      imageContentType: Option[String],
      imageFilesize: Option[Int],
      userSpecific: Option[UserSpecific],
      imageUrl: Option[String]
    ) =>
      Radical(
        level,
        character,
        meaning,
        userSpecific,
        handleImageData(
          imageFilename,
          imageContentType,
          imageFilesize,
          imageUrl
        )
      )
  }

  private def handleImageData(
                               filenameOption: Option[String],
                               contentTypeOption: Option[String],
                               filesizeOption: Option[Int],
                               urlOption: Option[String]
                             ): Option[ImageData] =
    for {
      filename <- filenameOption
      contentType <- contentTypeOption
      filesize <- filesizeOption
      url <- urlOption
    } yield ImageData(filename, contentType, filesize, url)
}

