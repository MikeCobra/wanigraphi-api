package uk.co.mikecobra.wanigraphi.api

import org.http4s.{Method, Request}
import org.http4s.Uri.uri
import org.specs2.Specification

class ServiceSpecification extends Specification {
  def is = s2"""
    Saying hello should respond $e1
    """

  def e1 = {
    val request = Request(Method.GET, uri("/hello/mike"))
    val task = HelloWorld.service.run(request)
    val response = task.unsafeRun().toOption
    response.isDefined
  }

}
