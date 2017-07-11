package uk.co.mikecobra.wanigraphi.api.wanikani.service

import fs2.Task
import uk.co.mikecobra.wanigraphi.api.wanikani.model.{Kanji, Radical, UserInformation, Vocabulary}

trait WaniKaniService {
  def getUserInformation(apiKey: String): Task[UserInformation]

  def getKanjiList(apiKey: String): Task[Seq[Kanji]]

  def getRadicalsList(apiKey: String): Task[Seq[Radical]]

  def getVocabularyList(apiKey: String): Task[Seq[Vocabulary]]
}
