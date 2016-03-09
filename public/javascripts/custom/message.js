$(function() {
    
    $("#getMessageSdmxProviderButton").click(function(event) {
        // make an ajax get request to get the message
        jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
            success: function(data) {
                console.log(data);
		$(".well").empty(); // empty div before
                $(".well").append($("<h5>").text("SDMX providers"));
                $(".well").append($("<h5>").text(data.value));
            }
        });
    });

    $("#getMessageSdmxFlowButton").click(function(event) {
        var provider = $("#inputProvider").val().toUpperCase();
        var pattern = $("#inputPattern").val();
        // if (pattern != "") {
        //     var patternText = " with pattern " + pattern;
        // } else {
        //     var patternText = "";
        // }
	var patternText = (pattern != "") ? (" with pattern " + pattern) : ("");
        jsRoutes.controllers.MessageController.getMessageSdmxFlow(provider, pattern).ajax({
            success: function(data) {
                console.log(data);
		// $(".well").empty(); // empty div before
		// $(".well").append($("<h4>").text("Flows for provider " + provider + patternText));
                // $(".well").append($("<h5>").text(data.value));

		$('#SdmxCodesDiv').hide();
		$('#SdmxFlowsDiv').show();

		$('#SdmxFlows').DataTable({
		    "filter": false,
		    "paging": false,
		    "bDestroy": true,
		    "dom": '<"toolbar">frtip',
		    // "columnDefs": [
		    // 	{ "visible": false, "targets": 0 }
		    // ],
		    data: data.value,
		    columns:[
			{title: "ID", data: "id"},
			{title: "Label", data: "label"}
		    ]
		});
		$("div.toolbar").append($("<h5>").text("Flows for provider " + provider + patternText));

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
		$(".well").empty(); // empty div before
                $(".well").append($("<h5>").text("Dimensions for flow " + flow + " of provider " + provider));
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
		// $(".well").empty(); // empty div before
                // $(".well").append($("<h5>").text("Dimension members for flow " + flow + " of provider " + provider));

                // $(".well").append($("<h5>").text(data.value));

		$('#SdmxFlowsDiv').hide();
		$('#SdmxCodesDiv').show();
		
		$('#SdmxCodes').DataTable({
		    "filter": false,
		    "paging": false,
		    "bDestroy": true,
		    "dom": '<"toolbar">frtip',
		    data: data.value,
		    columns:[
			{title: "Dimension", data: "dim"}, 
			{title: "ID", data: "id"},
			{title: "Codes", data: "label"}
		    ],
		    // "columnDefs": [
		    // 	{ "visible": false, "targets": 0 }
		    // ],
		    "order": [[ 0, 'asc' ]],
		    // "displayLength": 25,
		    "drawCallback": function ( settings ) {
			var api = this.api();
			var rows = api.rows( {page:'current'} ).nodes();
			var last=null;
			
			api.column(0, {page:'current'} ).data().each( function ( group, i ) {
			    if ( last !== group ) {
				$(rows).eq( i ).before(
				    '<tr class="group"><td colspan="5">'+group+'</td></tr>'
				);
				
				last = group;
			    }
			} );
		    }
		});

		// // Order by the grouping
		// $('#SdmxCodes tbody').on( 'click', 'tr.group', function () {
		//     var currentOrder = table.order()[0];
		//     if ( currentOrder[0] === 0 && currentOrder[1] === 'asc' ) {
		// 	table.order( [ 0, 'desc' ] ).draw();
		//     }
		//     else {
		// 	table.order( [ 0, 'asc' ] ).draw();
		//     }
		// } );
		
		$("div.toolbar").append($("<h5>").text("Dimension members for flow " + flow + " of provider " + provider));
		
            }
        });
    });

    // $("#clearMessageButton").click(function(event) {
    //     jsRoutes.controllers.MessageController.getMessageSdmxProvider().ajax({
    //         success: function(data) {
    //             console.log(data);
    //             $(".well").empty(); // empty div before

    // 		// $('#myTable').DataTable().fnClearTable();

    //         }
    //     });
    // });

});
