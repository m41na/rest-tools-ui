$(function() {
	
    $('#login-form-link').click(function(e) {
		$("#login-form").delay(100).fadeIn(100);
 		$("#register-form").fadeOut(100);
		$('#register-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});
    
	$('#register-form-link').click(function(e) {
		$("#register-form").delay(100).fadeIn(100);
 		$("#login-form").fadeOut(100);
		$('#login-form-link').removeClass('active');
		$(this).addClass('active');
		e.preventDefault();
	});

	$('#recover-link').click(function(e) {
 		$("#recover-panel").delay(100).fadeIn(100);
		$("#signin-panel").fadeOut(100);
		e.preventDefault();
	});
	
	$('#cancel-recover').click(function(e) {
 		$("#signin-panel").delay(100).fadeIn(100);
		$("#recover-panel").fadeOut(100);
		e.preventDefault();
	});
});