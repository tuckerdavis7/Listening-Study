$(document).ready(function() {
    //checks for existing sessionID on login
    $.ajax({
        url: 'http://localhost:8080/api/login',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            let responseData = data.data;
            if (responseData.role) {
                let redirectURL = "/" + responseData.role + "/dashboard";
                window.location.href = redirectURL;
            }
                                           
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', 'Error while checkign session for login: ' + error);
        }
    });

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