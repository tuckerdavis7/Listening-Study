$(document).ready(function() {
    $('#loginButton').click(function(event) {
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
                    //if count is 1, then do this alert
                    bootstrapAlert('success', 'Login successful!');
                    //otherwise, do this
                    //bootstrapAlert('danger', 'Credentials did not match, please try again.')
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