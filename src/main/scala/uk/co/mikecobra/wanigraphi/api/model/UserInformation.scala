package uk.co.mikecobra.wanigraphi.api.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.Decoder

case class UserInformation(
                            username: String,
                            gravatar: String,
                            level: Int,
                            title: String,
                            about: String,
                            website: Option[String],
                            twitter: Option[String],
                            topicsCount: Int,
                            postsCount: Int,
                            creationDate: LocalDateTime,
                            vacationDate: Option[LocalDateTime]
                          )

object UserInformation {

  implicit val decoder: Decoder[UserInformation] = Decoder.forProduct11(
    "username",
    "gravatar",
    "level",
    "title",
    "about",
    "website",
    "twitter",
    "topics_count",
    "posts_count",
    "creation_date",
    "vacation_date"
  ) { (
        username: String,
        gravatar: String,
        level: Int,
        title: String,
        about: String,
        website: Option[String],
        twitter: Option[String],
        topicsCount: Int,
        postsCount: Int,
        creationDate: Long,
        vacationDate: Option[Long]
      ) =>
    UserInformation(
      username,
      gravatar,
      level,
      title,
      about,
      website,
      twitter,
      topicsCount,
      postsCount,
      LocalDateTime.ofEpochSecond(creationDate, 0, ZoneOffset.UTC),
      vacationDate.map {
        LocalDateTime.ofEpochSecond(_, 0, ZoneOffset.UTC)
      }
    )
  }


}