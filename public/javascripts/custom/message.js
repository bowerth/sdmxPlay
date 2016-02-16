$(function() {
    
    $("#getMessageSdmxProviderButton").click(function(event) {
        // make an ajax get request to get the message
        jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
            success: function(data) {
                console.log(data);
                $(".well").append($("<h4>").text("SDMX providers"));
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#getMessageSdmxFlowButton").click(function(event) {
        var provider = $("#inputProvider").val().toUpperCase();
        var pattern = $("#inputPattern").val();
        if (pattern != "") {
            var patternText = " with pattern " + pattern
        } else {
            var patternText = ""
        }        
        jsRoutes.controllers.MessageController.getMessageSdmxFlow(provider, pattern).ajax({
            success: function(data) {
                console.log(data);
                $(".well").append($("<h4>").text("Flows for provider " + provider + patternText));
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#getMessageSdmxDimensionButton").click(function(event) {
	var provider = $("#inputProvider").val();
        var query = $("#inputQuery").val();
	var queryarray = query.split(".");
	var flow = queryarray[0];
        jsRoutes.controllers.MessageController.getMessageSdmxDimension(provider, flow).ajax({
            success: function(data) {
                console.log(data);
                $(".well").append($("<h4>").text("Dimensions for flow " + flow + " of provider " + provider));
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#getMessageSdmxCodeButton").click(function(event) {
	var provider = $("#inputProvider").val();
    var query = $("#inputQuery").val();
	var queryarray = query.split(".");
	var flow = queryarray[0];
        jsRoutes.controllers.MessageController.getMessageSdmxCode(provider, flow).ajax({
            success: function(data) {
                console.log(data);
                $(".well").append($("<h4>").text("Dimension members for flow " + flow + " of provider " + provider));
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#clearMessageButton").click(function(event) {
        jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
            success: function(data) {
                console.log(data);
                $(".well").empty(); // empty div before
            }
        });
    });

});
