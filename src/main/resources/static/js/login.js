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

    $('#loginButton').click(function(event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let loginForm = $('#loginForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            }, {})
        
            //check if serialization maps incorrect key value pairs
            if (loginForm.name && loginForm.value) {
                loginForm[loginForm.name] = loginForm.value;
                delete loginForm.name;
                delete loginForm.value;
            }
        
            $.ajax({
                data: JSON.stringify(loginForm),
                url: 'http://localhost:8080/api/login/verify',
                type: 'POST',
                contentType: 'application/json',
                success: function(data) {
                    let responseData = JSON.parse(data);

                    if (responseData?.status === "success") {
                        switch(responseData.data.role) {
                            case "administrator":
                                window.location.href = '/administrator/dashboard';
                                break;
                            
                            case "moderator":
                                window.location.href='/moderator/dashboard'
                                break;

                            case "teacher":
                                window.location.href='/teacher/dashboard'
                                break;
                            
                            case "student":
                            window.location.href='/student/dashboard'
                            break;
                        }
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