$(document).ready(function() {
    //checks for ajax errors related to URL fishing and unauthroized access
    $(document).ajaxError(function(event, xhr, settings) {
        const response = JSON.parse(xhr.responseText);
        try {
            if (xhr.status === 401) {
                if (response.error === "session_expired") {
                    window.location.href = response.redirect || "/";
                }
            }
            else if (xhr.status === 403) {
                if (response.error === "unauthorized") {
                    window.location.href = response.redirect || "/unauthorized";
                }
            }
        } catch (e) {}
    });

    //set metadata for each page in footer
    $.ajax({
        url: 'http://localhost:8080/api/metadata',
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            let responseData = JSON.parse(data);
            $('#version').html('Version: ' + responseData?.data?.version);
            $('#userCount').html('User Count: ' + responseData?.data?.userCount);
            $('#lastUpdate').html('Last updated: ' + responseData?.data?.lastUpdate);
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', error);
        }
    });
})

//Function that handles universal logout
function handleLogOut() {
    $.ajax({
        url: 'http://localhost:8080/api/login/logout',
        type: 'POST',
        contentType: 'application/json',
        success: function(data) {
            window.location.href = '/';
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', error);
        }
    })
}