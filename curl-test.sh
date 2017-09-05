#!/bin/bash

HOST="http://sdmx.rdata.work"
# HOST="http://localhost:9000"
# URLS="/ECB/EXR.A.USD+GBP+CAD+AUD.EUR.SP00.A/ /ECB/EXR.Q.USD+GBP+CAD+AUD.EUR.SP00.A/"
URLS="/EUROSTAT/nama_gdp_c.A.EUR_HAB.B11.AT/"

for URL in $URLS
do
	curl -s $HOST$URL > /dev/null
	curl $HOST/getdownloadsdmx
done
