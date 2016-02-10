$(document).ready(function() {
// $(function () {

	$('#btnSubmitSdmx').click(function(event) {
		var provider = $("#inputProvider").val().toUpperCase();
		var query = $("#inputQuery").val();
		var start = $("#inputStart").val();
		var end = $("#inputEnd").val();
        window.location.replace("../../../../" + provider + "/" + query + "/" + start + "/" + end + "/");
  	});

	$("#inputProvider").keyup(function(event) {
		if (event.which == 13) {
			$('#btnSubmitSdmx').click();
		}
	}).keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});

	$("#inputQuery").keyup(function(event) {
		if (event.which == 13) {
			$('#btnSubmitSdmx').click();
		}
	}).keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});

	$("#inputStart").keyup(function(event) {
		if (event.which == 13) {
			$('#btnSubmitSdmx').click();
		}
	}).keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});

	$("#inputEnd").keyup(function(event) {
		if (event.which == 13) {
			$('#btnSubmitSdmx').click();
		}
	}).keydown(function(event) {
		if (event.which == 13) {
			event.preventDefault();
		}
	});

	$("#inputQuery").select();
});
