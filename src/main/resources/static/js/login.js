$(document).ready(function() {
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
                data: loginForm,
                url: 'http://localhost:8080/api/login',
                type: 'GET',
                dataType: 'text',
                success: function(data) {
                    bootstrapAlert('success', data);
                },
                error: function(xhr, status, error) {
                    bootstrapAlert('danger', 'Unsuccessful login. Error: ' + error);
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