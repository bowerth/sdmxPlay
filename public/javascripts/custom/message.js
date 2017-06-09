/*global $, jsRoutes, console*/

$(function () {
    "use strict";

    $("#getMessageSdmxProviderButton").click(function (event) {
        // make an ajax get request to get the message
        jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
            success: function (data) {
                console.log(data);
                $(".well").empty(); // empty div before
                $(".well").append($("<h5>").text("SDMX providers"));
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#getMessageSdmxHelpButton").click(function (event) {
        jsRoutes.controllers.MessageController.getMessageSdmxHelp().ajax({
            success: function (data) {
                console.log(data);
                $(".well").empty();
//                $(".well").html("<h5>Hello2</h5>");
                $(".well").html(data.value);
//                $(".well").append($("<h5>").text(data.value));
            }
        });
//        $(".well").empty(); // empty div before
//        // $(".well").append($("<h5>").text("Hello"));
//        $(".well").append("<h3>sdmxPlay</h3><p>Please find below a quick overview of the functionality.</p>");
//        $(".well").append("<h4>Site URL</h4><p>The URL is linked to the 'Provider Code' and 'Query' text input fields. For example, /ECB/EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A/ corresponds to Provider Code 'ECB' and Query 'EXR.A+M+Q.USD+GBP+CAD+AUD.EUR.SP00.A'.</p>");
//        $(".well").append("<h4>Time Series Chart</h4><p>If the selected query returns a time series with frequency that can be parsed by the implemented date transformation functions (Annual, Quarterly, Monthly), it will be displayed using dygraphs. Using mouse click-and-drag, the user can zoom in horizontally or vertically. The time series chart display can be reset with a double-click.</p>");
//        $(".well").append("<h4>Min and Max Series</h4><p>When selecting a large number of series and displaying actual values (e.g. instead of growth rates), one can easily identify the series with highest and lowest values returned by the query from the 'Min Series' and 'Max Series' fields.</p>");
//        $(".well").append("<h4>Query</h4><p>Enter a valid SDMX query where the format is depending on the data flow: [FLOW . ] FREQ . CURRENCY . CURRENCY_DENOM . EXR_TYPE . EXR_SUFFIX. The dimensions of a flow can be retrieved using the 'List Dimensions' button. E.g. to retrieve the dimensions of AMECO flow by ECB, simply replace 'EXR' in the Query field with 'AMECO' and click 'List Dimensions'. If interested in all members for one or more dimension, use '*' e.g. 'EXR.A.*.EUR.SP00.A' returns all available currencies in the flow, currently 44.</p>");
//        $(".well").append("<h4>Provider Code</h4><p>Valid provider codes can be retrieved using the 'List Providers' button. Please refer to the underlying SDMX Connectors linked at the bottom of the page for additional information.</p>");
//        $(".well").append("<h4>Start and End Date</h4><p>Limit the query to a specific time range (at the moment only accepting format 'yyyy'). This is reflected in the URL via an option parameter '?start=1999'</p>");
//        $(".well").append("<h4>Download</h4><p>Returns a csv file with time series in columns. The first column contains the date information in ascending order. When opened with spreadsheet programs (e.g. gnumeric or LibreOffice Calc), a line chart can be generated with ease.</p>");
//        $(".well").append("<h4>Flow Pattern</h4><p>Limit the list of returned flows for selected provider e.g. 'EXR*|*PUB' will return all flows starting with 'EXR' or ending with 'PUB'.</p>");
//        $(".well").append("<h4>List Codes</h4><p>List potentially available dimensions members for each dimensions.</p>");
//        $(".well").append("<h3>Example Queries</h3><p>Example queries for various providers are listed below.</p>");
//        $(".well").append("<h4>NBB</h4><p>http://sdmx.rdata.work/NBB/QNA.1.B1G+D21_M_D31.VZ+VA+VB_E+VF+VG_U+VG_I+VJ+VK+VL+VM_N+VO+VQ+VR_U.V.N.Q/?start=1995&end=2013</p>");
    });

    $("#getMessageSdmxFlowButton").click(function (event) {
        var provider = $("#inputProvider").val().toUpperCase(),
            pattern = $("#inputPattern").val(),
            patternText = (pattern !== "") ? (" with pattern " + pattern) : ("");
        jsRoutes.controllers.MessageController.getMessageSdmxFlow(provider, pattern).ajax({
            success: function (data) {
                console.log(data);

                $('#SdmxCodesDiv').hide();
                $('#SdmxFlowsDiv').show();

                $('#SdmxFlows').DataTable({
                    "filter": false,
                    "paging": false,
                    "bDestroy": true,
                    "dom": '<"toolbar">frtip',
                    data: data.value,
                    columns: [
                        {title: "ID", data: "id"},
                        {title: "Label", data: "label"}
                    ]
                });
                $("div.toolbar").append($("<h5>").text("Flows for provider " + provider + patternText));

            }
        });
    });

    $("#getMessageSdmxDimensionButton").click(function (event) {
        var provider = $("#inputProvider").val(),
            query = $("#inputQuery").val(),
            queryarray = query.split("."),
            flow = queryarray[0];
        jsRoutes.controllers.MessageController.getMessageSdmxDimension(provider, flow).ajax({
            success: function (data) {
                console.log(data);
                $(".well").empty(); // empty div before
                $(".well").append($("<h5>").text("Dimensions for flow " + flow + " of provider " + provider));
                $(".well").append($("<h5>").text(data.value));

            }
        });
    });

    $("#getMessageSdmxCodeButton").click(function (event) {
        var provider = $("#inputProvider").val(),
            query = $("#inputQuery").val(),
            queryarray = query.split("."),
            flow = queryarray[0];

        jsRoutes.controllers.MessageController.getMessageSdmxCode(provider, flow).ajax({
            success: function (data) {
                console.log(data);

                $('#SdmxFlowsDiv').hide();
                $('#SdmxCodesDiv').show();

                $('#SdmxCodes').DataTable({
                    "filter": false,
                    "paging": false,
                    "bDestroy": true,
                    "dom": '<"toolbar">frtip',
                    data: data.value,
                    columns: [
                        {title: "Dimension", data: "dim"},
                        {title: "ID", data: "id"},
                        {title: "Codes", data: "label"}
                    ],
                    "order": [[ 0, 'asc' ]],
                    "drawCallback": function (settings) {
                        var api = this.api(),
                            rows = api.rows({page: 'current'}).nodes(),
                            last = null;

                        api.column(0, {page: 'current'}).data().each(function (group, i) {
                            if (last !== group) {
                                $(rows).eq(i).before(
                                    '<tr class="group"><td colspan="5">' + group + '</td></tr>'
                                );

                                last = group;
                            }
                        });
                    }
                });

                $("div.toolbar").append($("<h5>").text("Dimension members for flow " + flow + " of provider " + provider));

            }
        });
    });

});
