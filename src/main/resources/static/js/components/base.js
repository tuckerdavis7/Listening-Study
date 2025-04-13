$(document).ready(function() {
    //set metadata for each page in footer
    $.ajax({
        url: 'http://localhost:8080/api/configuration',
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            let responseData = JSON.parse(data);
            $('#version').html('Version: ' + responseData?.data?.version);
            $('#userCount').html('User Count: ' + responseData?.data?.userCount);
            $('#lastUpdate').html('Last updated: ' + responseData?.data?.lastUpdate);
            $('#userRole').val(responseData?.data?.role);
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