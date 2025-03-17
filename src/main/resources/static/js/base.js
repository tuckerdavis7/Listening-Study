$(document).ready(function() {
    //set metadata for each page in footer
    $.ajax({
        url: 'http://localhost:8080/api/metadata',
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            let responseData = JSON.parse(data);
            $('#version').html('Version: ' + responseData.version);
            $('#userCount').html('User Count: ' + responseData.userCount);
            $('#lastUpdate').html('Last updated: ' + responseData.lastUpdate);
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', error);
        }
    });
})

//Function that handles universal logout.  Built in case if more is required for API
function handleLogOut() {
    window.location.href = '/';
}