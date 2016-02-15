package models

case class SdmxCode(
  provider: String,
  flow: String,
  dimension: String,
  code_id: Array[String],
  code_label: Array[String]
)
