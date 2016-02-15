$(function() {
    // add a click handler to the button
    
    // $("#getMessageButton").click(function(event) {
    //     // make an ajax get request to get the message
    //     // jsRoutes.controllers.MessageController.getMessage().ajax({
    //     jsRoutes.controllers.MessageController.getMessage().ajax({
    //         success: function(data) {
    //             console.log(data);
    //             $(".well").append($("<h1>").text(data.value));
    //         }
    //     });
    // });

    $("#getMessageSdmxProviderButton").click(function(event) {
        // make an ajax get request to get the message
        // jsRoutes.controllers.MessageController.getMessage().ajax({
        jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
            success: function(data) {
                console.log(data);
                $(".well").empty();
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#getMessageSdmxFlowButton").click(function(event) {
        // make an ajax get request to get the message
        // jsRoutes.controllers.MessageController.getMessage().ajax({
        var provider = $("#inputProvider").val().toUpperCase();
        // jsRoutes.controllers.MessageController.getMessageSdmxFlow().ajax({
        jsRoutes.controllers.MessageController.getMessageSdmxFlow(provider).ajax({
            success: function(data) {
                console.log(data);
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

});