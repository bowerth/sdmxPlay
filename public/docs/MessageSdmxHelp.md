# sdmxPlay

Please find below a quick overview of the functionality.

#### Site URL

The URL is linked to the 'Provider Code' and 'Query' text input fields. For example, /ECB/EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A/ corresponds to Provider Code 'ECB' and Query 'EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A'.

#### Time Series Chart

If the selected query returns a time series with frequency that can be parsed by the implemented date transformation functions (Annual, Quarterly, Monthly), it will be displayed using dygraphs. Using mouse click-and-drag, the user can zoom in horizontally or vertically. The time series chart display can be reset with a double-click.

#### Min and Max Series

When selecting a large number of series and displaying actual values (e.g. instead of growth rates), one can easily identify the series with highest and lowest values returned by the query from the 'Min Series' and 'Max Series' fields.

#### Query

Enter a valid SDMX query where the format is depending on the data flow: [FLOW . ] FREQ . CURRENCY . CURRENCY\_DENOM . EXR\_TYPE . EXR_SUFFIX. The dimensions of a flow can be retrieved using the 'List Dimensions' button. E.g. to retrieve the dimensions of AMECO flow by ECB, simply replace 'EXR' in the Query field with 'AMECO' and click 'List Dimensions'. If interested in all members for one or more dimension, use '\*' e.g. 'EXR.A.\*.EUR.SP00.A' returns all available currencies in the flow, currently 44.

#### Provider Code

Valid provider codes can be retrieved using the 'List Providers' button. Please refer to the underlying SDMX Connectors linked at the bottom of the page for additional information.

#### Start and End Date

Limit the query to a specific time range (at the moment only accepting format 'yyyy'). This is reflected in the URL via an option parameter '?start=1999'

#### Download

Returns a csv file with time series in columns. The first column contains the date information in ascending order. When opened with spreadsheet programs (e.g. gnumeric or LibreOffice Calc), a line chart can be generated with ease.

#### Flow Pattern

Limit the list of returned flows for selected provider e.g. 'EXR\*|\*PUB' will return all flows starting with 'EXR' or ending with 'PUB'.

#### List Codes

List potentially available dimensions members for each dimensions.

### Example Queries

Example queries for various providers are listed below.

#### NBB

http://sdmx.rdata.work/NBB/QNA.1.B1G+D21\_M\_D31.VZ+VA+VB\_E+VF+VG\_U+VG\_I+VJ+VK+VL+VM\_N+VO+VQ+VR\_U.V.N.Q/?start=1995&end=2013