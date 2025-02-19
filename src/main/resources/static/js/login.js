$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/metadata',
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            setMetadataOnPage(JSON.parse(data));
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', error);
        }
    });
    
    $('#loginButton').click(function(event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            let loginForm = $('#loginForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            })
        
            //checking if serializeArray reduction maps incorrect key value pairs
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
                    let color;
                    if (data.includes("Hello")) { //if successful login
                        color = 'success';
                        window.location.href = 'dashboard.html';
                    }
                    else { //unsuccessful login
                        color = 'danger';
                    }
                    localStorage.setItem('loginMessage', JSON.stringify({ color: color, message: data }));
                    bootstrapAlert(color, data);                                            
                },
                error: function(xhr, status, error) {
                    bootstrapAlert('danger', 'Error while logging in: ' + error);
                }
            });
        }
    })
})

function setMetadataOnPage(data) {
    $('#appName').html(data.appName + ' Login');
    $('#version').html('Version: ' + data.version);
    $('#userCount').html('User Count: ' + data.userCount);
    $('#lastUpdate').html('Last updated: ' + data.lastUpdate);
}

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