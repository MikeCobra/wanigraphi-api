package uk.co.mikecobra.wanigraphi.api.wanikani.model

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.parser._
import org.specs2.mutable.Specification

class UserInformationSpec extends Specification {
  "UserInformation" >> {
    "should parse JSON" >> {
      val userInformationJson: String =
        """
          |{
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
          |  }
        """.stripMargin

      val expected = UserInformation(
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
      )

      val result = decode[UserInformation](userInformationJson)

      result shouldEqual Right(expected)
    }
  }
}
