package models

case class SdmxDimension(
  provider: String,
  flow: String,
  dimension_id: Array[String]
  // dimension_label: Array[String]
)
