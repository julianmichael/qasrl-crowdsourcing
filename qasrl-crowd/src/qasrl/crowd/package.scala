package qasrl

import jjm.DotKleisli
import jjm.{DotEncoder, DotDecoder}
import jjm.ling.en.InflectedForms

import io.circe.{Encoder, Decoder}
import io.circe.JsonCodec

// import nlpdata.datasets.wiktionary.InflectedForms

// import upickle.default._

package object crowd {

  def dollarsToCents(d: Double): Int = math.round(100 * d).toInt

  // import nlpdata.util.LowerCaseStrings._

  @JsonCodec case class QASRLGenerationPrompt[SID](id: SID, verbIndex: Int)

  @JsonCodec case class GenerationStatSummary(
    numVerbsCompleted: Int, // before validation: used to calculate coverage
    numQuestionsWritten: Int, // before validation: "
    workerStatsOpt: Option[QASRLGenerationWorkerStats]
  )

  @JsonCodec case class QASRLGenerationAjaxRequest[SID](
    workerIdOpt: Option[String],
    prompt: QASRLGenerationPrompt[SID]
  ) {
    type Response = QASRLGenerationAjaxResponse
  }

  @JsonCodec case class QASRLGenerationAjaxResponse(
    stats: GenerationStatSummary,
    tokens: Vector[String],
    inflectedForms: InflectedForms
  )

  object QASRLGenerationAjaxRequest {
    implicit def generationAjaxRequestDotEncoder[SID]: DotEncoder[QASRLGenerationAjaxRequest[SID]] = {
      new DotKleisli[Encoder, QASRLGenerationAjaxRequest[SID]] {
        def apply(request: QASRLGenerationAjaxRequest[SID]) =
            implicitly[Encoder[QASRLGenerationAjaxResponse]]
      }
    }
    implicit def generationAjaxRequestDotDecoder[SID]: DotDecoder[QASRLGenerationAjaxRequest[SID]] = {
      new DotKleisli[Decoder, QASRLGenerationAjaxRequest[SID]] {
        def apply(request: QASRLGenerationAjaxRequest[SID]) =
          implicitly[Decoder[QASRLGenerationAjaxResponse]]
      }
    }
  }

  @JsonCodecs case class QASRLValidationAjaxResponse(
    workerInfoOpt: Option[QASRLValidationWorkerInfoSummary],
    sentence: Vector[String]
  )

  @JsonCodec case class QASRLValidationAjaxRequest[SID](workerIdOpt: Option[String], id: SID) {
    type Response = QASRLValidationAjaxResponse
  }
  object QASRLValidationAjaxRequest {
    implicit def validationAjaxRequestDotEncoder[SID]: DotEncoder[QASRLValidationAjaxRequest[SID]] = {
      new DotKleisli[Encoder, QASRLValidationAjaxRequest[SID]] {
        def apply(request: QASRLValidationAjaxRequest[SID]) =
          implicitly[Encoder[QASRLValidationAjaxResponse]]
      }
    }
    implicit def validationAjaxRequestDotDecoder[SID]: DotDecoder[QASRLValidationAjaxRequest[SID]] = {
      new DotKleisli[Decoder, QASRLValidationAjaxRequest[SID]] {
        def apply(request: QASRLValidationAjaxRequest[SID]) =
          implicitly[Decoder[QASRLValidationAjaxResponse]]
      }
    }
  }
}
