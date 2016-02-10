package models

case class SdmxData(
  provider: String,
  query: String,
  start: String,
  end: String
  // ,
  // output: String
  // timerange: String, nseries: String
)
