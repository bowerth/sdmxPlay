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
        jsRoutes.controllers.MessageController.getMessageSdmxFlow(provider).ajax({
            success: function(data) {
                console.log(data);
                $(".well").append($("<h4>").text("Flows for provider " + provider));
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