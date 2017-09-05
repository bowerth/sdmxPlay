/*global $, jsRoutes, console*/

// used on page load and on button click
function getMessageSdmxHelp() {
    "use strict";
    jsRoutes.controllers.MessageController.getMessageSdmxHelp().ajax({
        success: function (data) {
            console.log(data);
            $("#infowell").empty();
            $("#infowell").html(data.value);
        }
    });
}

$(function () {
    "use strict";

    $("#getMessageSdmxHelpButton").click(function (event) {
        getMessageSdmxHelp();
    });

    $("#getMessageSdmxProviderButton").click(function (event) {
        // make an ajax get request to get the message
        jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
            success: function (data) {
                console.log(data);
                $("#infowell").empty(); // empty div before
                $("#infowell").append($("<h5>").text("SDMX providers"));
                $("#infowell").append($("<h5>").text(data.value));
            }
        });
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
                $("#infowell").empty(); // empty div before
                $("#infowell").append($("<h5>").text("Dimensions for flow " + flow + " of provider " + provider));
                $("#infowell").append($("<h5>").text(data.value));
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

//$(document).ready(function () {
//    "use strict";
//    getMessageSdmxHelp();
//});
