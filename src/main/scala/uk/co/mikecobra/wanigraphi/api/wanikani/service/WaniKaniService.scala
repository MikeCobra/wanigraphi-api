package uk.co.mikecobra.wanigraphi.api.wanikani.service

import fs2.Task
import uk.co.mikecobra.wanigraphi.api.wanikani.model.Radical

trait WaniKaniService {
  def getRadicalsList(apiKey: String): Task[Seq[Radical]]
}
