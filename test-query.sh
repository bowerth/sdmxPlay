#!/bin/bash

# PROVIDER="ECB"
# QUERY="EXR.A.USD.EUR.SP00.A"
PROVIDER="INSEE"
QUERY="IPI-1990-CPF3/29-7.CVS-CJO"
# QUERY="SERIES_BDM/001565137"

# java -classpath lib/SDMX.jar it.bancaditalia.oss.sdmx.client.SdmxClientHandler.getProviders ""

# get flows (not working)
# java -classpath lib/SDMX.jar it.bancaditalia.oss.sdmx.client.SdmxClientHandler getFlows "ECB" ""

# get data
java -classpath lib/SDMX.jar it.bancaditalia.oss.sdmx.util.GetTimeSeries $PROVIDER $QUERY
