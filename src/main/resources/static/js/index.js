$(document).ready(function() {
    $('#loginRedirect').click(function(event) {
        event.preventDefault();
        window.location.href = "/login";
    });

    $('#registerRedirect').click(function(event) {
        event.preventDefault();
        window.location.href = "/register";
    });

    $('#mainLogin').click(function(event) {
        event.preventDefault();
        window.location.href = "/login";
    });

    $('#mainRegister').click(function(event) {
        event.preventDefault();
        window.location.href = "/register";
    });

})