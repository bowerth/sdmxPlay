# sdmxPlay

Please find below a quick overview of the functionality.

### Example Queries

Example queries for various providers are listed below.

- [ECB/EXR](/ECB/EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A/)
  Exchange Rates
- [EUROSTAT/migr_imm7ctb](/EUROSTAT/migr_imm7ctb.A.TOTAL.TOTAL.NR.T.*/) 
  Immigration by sex, country of birth and broad group of citizenship
- [IMF/DM](/IMF/DM.*.PPPPC.WEO.I.A/)
  Data Mapper (Concept: `PPPPC`) GDP based on PPP per capita (!)
- [NBB/QNA](/NBB/QNA.1.B1G+D21_M_D31.VZ+VA+VB_E+VF+VG_U+VG_I+VJ+VK+VL+VM_N+VO+VQ+VR_U.V.N.Q/?start=1995&end=2013)
  Quarterly and annual aggregates
- [INSEE/SERIES_BDM](/INSEE/SERIES_BDM.001565137/)
  BDM time series
- [INSEE/IPI-2010](/INSEE/IPI-2010.*.*.*.20-4.*.*.*.*.*/)
  Industrial production index
- [INSEE/ENQ-CONJ-PROMO-IMMO](/INSEE/ENQ-CONJ-PROMO-IMMO.T.ECB_DEMLOG_ANC.S.SOLDE_PROPORTION.FM.POURCENT.BRUT/)
  Outlook survey in the real-estate development
- [INSEE/CNA-2010-FBCF-SI](/INSEE/CNA-2010-FBCF-SI.A.CNA_FBCF_SI.S12ES14AF.VAL.*.VALEUR_ABSOLUE.FE.EUROS_COURANTS.BRUT/)
  Gross fixed capital formation (GFCF) by institutional sectors
- [ISTAT/144_125](/ISTAT/144_125.A.IT.1.07+10+00ST.*.8/)
  Consumer price index for the whole nation - annual average (NIC - until 2010)
- [ISTAT/143_497](/ISTAT/143_497.A.IT.4.*.*/)
  House Price Index
- [OECD/STAN08BIS](/OECD/STAN08BIS.*.INTP.1041/)
  STAN Database for Structural Analysis (ISIC Rev. 3, SNA93)
- [OECD/STANI4](/OECD/STANI4.*.INTP.0539/)
  STAN Database for Structural Analysis (ISIC Rev. 4, SNA93)
- [OECD/STANI4_2016](/OECD/STANI4_2016.*.INTP.D05T39/)
  STAN Database for Structural Analysis (ISIC Rev. 4, SNA08)
- [UIS/FF](/UIS/FF.*.*.US+FR._T._T._T._T._T._T.EXHIB._T/)
  Feature film
- [INEGI/DF_STEI](/INEGI/DF_STEI.*.*.C2173+C2172+C2171+C37013+C37012+C37011+C1700+C1411...../?start=2005)
  Dataflow Short Term Economic Indicators
- [WB/WDI](/WB/WDI.A.BM_GSR_NFSV_CD.*/?start=1980&end=2015)
  World Development Indicators: Service imports (BoP, current US$)
- ILO/DF\_YI\_ALL\_EMP\_TEMP\_SEX\_AGE\_NB [link](/ILO/DF_YI_ALL_EMP_TEMP_SEX_AGE_NB.YI.MEX.A.463.*.*.*/)
- [ABS/CPI](/ABS/CPI.1.*.40027.*.Q/?start=1980&end=2017)
  Consumer Price Index (CPI) 16th Series

#### Site URL

The URL is linked to the 'Provider Code' and 'Query' text input fields. For
example, /ECB/EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A/ corresponds to Provider Code
'ECB' and Query 'EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A'.

#### Time Series Chart

If the selected query returns a time series with frequency that can be parsed by
the implemented date transformation functions (Annual, Quarterly, Monthly), it
will be displayed using dygraphs. Using mouse click-and-drag, the user can zoom
in horizontally or vertically. The time series chart display can be reset with a
double-click.

#### Min and Max Series

When selecting a large number of series and displaying actual values (e.g.
instead of growth rates), one can easily identify the series with highest and
lowest values returned by the query from the 'Min Series' and 'Max Series'
fields.

#### Query

Enter a valid SDMX query where the format is depending on the data flow: [FLOW .
] FREQ . CURRENCY . CURRENCY\_DENOM . EXR\_TYPE . EXR_SUFFIX. The dimensions of
a flow can be retrieved using the 'List Dimensions' button. E.g. to retrieve the
dimensions of AMECO flow by ECB, simply replace 'EXR' in the Query field with
'AMECO' and click 'List Dimensions'. If interested in all members for one or
more dimension, use '\*' e.g. 'EXR.A.\*.EUR.SP00.A' returns all available
currencies in the flow, currently 44.

#### Provider Code

Valid provider codes can be retrieved using the 'List Providers' button. Please
refer to the underlying SDMX Connectors linked at the bottom of the page for
additional information.

#### Start and End Date

Limit the query to a specific time range (at the moment only accepting format
'yyyy'). This is reflected in the URL via an option parameter '?start=1999'

#### Download

Returns a csv file with time series in columns. The first column contains the
date information in ascending order. When opened with spreadsheet programs (e.g.
gnumeric or LibreOffice Calc), a line chart can be generated with ease.

#### Flow Pattern

Limit the list of returned flows for selected provider e.g. 'EXR\*|\*PUB' will
return all flows starting with 'EXR' or ending with 'PUB'.

#### List Codes

List potentially available dimensions members for each dimensions.

#### Full List of Tables

A data flows from SDMX and non-SDMX providers have been listed in
the
[opendata-tables](https://bowerth.gitbooks.io/opendata-tables/content/)
gitbook.


#### Command Line Example

Using the command line interface and the download option, routines to perform batch downloads can be envisaged.

`$ curl -s 'http://sdmx.rdata.work/ECB/EXR.A.USD.EUR.SP00.A/?start=2010&end=2017' > /dev/null`

`$ curl http://sdmx.rdata.work/getdownloadsdmx > sdmx-result.csv`

`$ cat sdmx-result.csv`

`TIME_PERIOD,EXR.A.USD.EUR.SP00.A`

`2010/01/01,1.325716666666667`

`2011/01/01,1.391955252918288`

`2012/01/01,1.284788671875001`

`2013/01/01,1.328118039215687`

`2014/01/01,1.328500784313724`

`2015/01/01,1.109512890624999`

`2016/01/01,1.106903112840467`

`...`
