package uk.co.mikecobra.wanigraphi.api

import java.util.concurrent.{ExecutorService, Executors}

import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp
import uk.co.mikecobra.wanigraphi.api.config.WaniGraphiApiConfig


object WaniGraphiApi extends StreamApp {
  val pool : ExecutorService  = Executors.newCachedThreadPool()

  override def stream(args: List[String]): Stream[Task, Nothing] =
    BlazeBuilder
      .bindHttp(WaniGraphiApiConfig.HttpConfig.port, WaniGraphiApiConfig.HttpConfig.hostIp)
      .mountService(HelloWorld.service)
      .withServiceExecutor(pool)
      .serve
}
