package uk.co.mikecobra.wanigraphi.api.wanikani.client

import fs2.Task
import org.http4s.client.Client
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s.circe._
import uk.co.mikecobra.wanigraphi.api.wanikani.model._

class HttpWaniKaniClient(host: String) extends WaniKaniClient {

  val httpClient: Client = PooledHttp1Client()

  override def getUserInformation(apiKey: String): Task[UserInformation] =
    httpClient
      .expect(s"$host/api/user/$apiKey/user-information")(jsonOf[ApiResponse[Option[String]]])
      .map(_.userInformation)

  override def getKanjiList(apiKey: String): Task[Seq[Kanji]] =
    httpClient
      .expect(s"$host/api/user/$apiKey/kanji")(jsonOf[ApiResponse[Seq[Kanji]]])
      .map(_.requestedInformation)

  override def getKanjiListForLevels(apiKey: String, levels: Seq[Int]): Task[Seq[Kanji]] =
    httpClient
      .expect(s"$host/api/user/$apiKey/kanji/${levels.mkString(",")}")(jsonOf[ApiResponse[Seq[Kanji]]])
      .map(_.requestedInformation)

  override def getRadicalsList(apiKey: String): Task[Seq[Radical]] =
    httpClient
      .expect(s"$host/api/user/$apiKey/radicals")(jsonOf[ApiResponse[Seq[Radical]]])
      .map(_.requestedInformation)

  override def getRadicalsListForLevels(apiKey: String, levels: Seq[Int]): Task[Seq[Radical]] =
    httpClient
      .expect(s"$host/api/user/$apiKey/radicals/${levels.mkString(",")}")(jsonOf[ApiResponse[Seq[Radical]]])
      .map(_.requestedInformation)

  override def getVocabularyList(apiKey: String): Task[Seq[Vocabulary]] =
    httpClient
      .expect(s"$host/api/user/$apiKey/vocabulary")(jsonOf[ApiResponse[Seq[Vocabulary]]])
      .map(_.requestedInformation)

  override def getVocabularyListForLevels(apiKey: String, levels: Seq[Int]): Task[Seq[Vocabulary]] =
    httpClient
      .expect(s"$host/api/user/$apiKey/vocabulary/${levels.mkString(",")}")(jsonOf[ApiResponse[Seq[Vocabulary]]])
      .map(_.requestedInformation)
}

object HttpWaniKaniClient {
  def apply(host: String): HttpWaniKaniClient = new HttpWaniKaniClient(host)
}
