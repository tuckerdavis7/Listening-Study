$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/metadata',
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            //set metadata on page
            let responseData = JSON.parse(data);
            $('#appName').html(responseData.appName + ' Login');
            $('#version').html('Version: ' + responseData.version);
            $('#userCount').html('User Count: ' + responseData.userCount);
            $('#lastUpdate').html('Last updated: ' + responseData.lastUpdate);
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', error);
        }
    });
    
    $('#loginButton').click(function(event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let loginForm = $('#loginForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            })
        
            //check if serialization maps incorrect key value pairs
            if (loginForm.name && loginForm.value) {
                loginForm[loginForm.name] = loginForm.value;
                delete loginForm.name;
                delete loginForm.value;
            }
        
            $.ajax({
                data: JSON.stringify(loginForm),
                url: 'http://localhost:8080/api/login',
                type: 'POST',
                contentType: 'application/json',
                success: function(data) {
                    let responseData = JSON.parse(data);
                    if (responseData.status === "success") {
                        window.location.href = 'dashboard.html';
                    }
                    else {
                        bootstrapAlert('danger', 'Invalid login credentials.');
                    }                                         
                },
                error: function(xhr, status, error) {
                    bootstrapAlert('danger', 'Error while logging in: ' + error);
                }
            });
        }
    })
})

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('loginForm').querySelectorAll("[required]").forEach(function (i) {
        if (!allAreFilled) {
            return;
        }
        if (!i.value) {
            allAreFilled = false;
            return;
        }
    })
    if (!allAreFilled) {
        bootstrapAlert('danger', 'Fill all fields.');
        return false;
    }
    return true;
}