package models

case class SdmxData(
  // class can have different name than "SdmxData"
  provider: String,
  query: String,
  // start: String,
  // end: String
  start: Option[String],
  end: Option[String]
  // ,
  // output: String
  // timerange: String, nseries: String
)
