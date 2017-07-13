package uk.co.mikecobra.wanigraphi.api.wanikani.client

import fs2.Task
import uk.co.mikecobra.wanigraphi.api.wanikani.model.{Kanji, Radical, UserInformation, Vocabulary}

trait WaniKaniClient {
  def getUserInformation(apiKey: String): Task[UserInformation]

  def getKanjiList(apiKey: String): Task[Seq[Kanji]]

  def getKanjiListForLevels(apiKey: String, levels: Seq[Int]): Task[Seq[Kanji]]

  def getRadicalsList(apiKey: String): Task[Seq[Radical]]

  def getRadicalsListForLevels(apiKey: String, levels: Seq[Int]): Task[Seq[Radical]]

  def getVocabularyList(apiKey: String): Task[Seq[Vocabulary]]

  def getVocabularyListForLevels(apiKey: String, levels: Seq[Int]): Task[Seq[Vocabulary]]
}
