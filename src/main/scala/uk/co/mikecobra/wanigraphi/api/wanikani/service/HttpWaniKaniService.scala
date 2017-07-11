package uk.co.mikecobra.wanigraphi.api.wanikani.service

import fs2.Task
import org.http4s.client.Client
import org.http4s.client.blaze.PooledHttp1Client
import org.http4s.circe._
import uk.co.mikecobra.wanigraphi.api.wanikani.model.{ApiResponse, Radical}

class HttpWaniKaniService(host: String) extends WaniKaniService {

  val httpClient: Client = PooledHttp1Client()

  override def getRadicalsList(apiKey: String): Task[Seq[Radical]] =
    httpClient.expect(s"$host/api/user/$apiKey/radicals")(jsonOf[ApiResponse[Seq[Radical]]])
      .map(_.requestedInformation)
}

object HttpWaniKaniService {
  def apply(host: String): HttpWaniKaniService = new HttpWaniKaniService(host)
}
