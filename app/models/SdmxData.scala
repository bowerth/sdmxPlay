package models

case class SdmxData(
  provider: String,
  query: String,
  start: Option[String],
  end: Option[String],
  output: String,
  labels: String,
  min: String,
  max: String
)
