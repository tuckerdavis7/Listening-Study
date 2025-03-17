$(document).ready(function() {
    $('#loginRedirect').click(function(event) {
        event.preventDefault();
        window.location.href = "/login";
    });

    $('#registerRedirect').click(function(event) {
        event.preventDefault();
        window.location.href = "/register";
    });
})

/*function loginRedirect(){
    window.location.href = "/login";
}

function registerRedirect(){
    window.location.href = "/register";
}*/