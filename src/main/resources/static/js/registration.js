$(document).ready(function() {
    $('#registrationButton').click(function(event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let registrationForm = $('#registrationForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            })
        
            //check if serialization maps incorrect key value pairs
            if (registrationForm.name && registrationForm.value) {
                registrationForm[registrationForm.name] = registrationForm.value;
                delete registrationForm.name;
                delete registrationForm.value;
            }

            $.ajax({
                data: JSON.stringify([registrationForm]),
                url: 'http://localhost:8080/api/register',
                type: 'POST',
                contentType: 'application/json',
                success: function(data) {
                    bootstrapAlert('success', 'Registration successful!');
                    $('#registrationForm').hide();
                    $('#signInText').hide();
                    $('#navigationButton').show();                    
                },
                error: function(xhr, status, error) {
                    bootstrapAlert('danger', 'Error while registering: ' + error);
                }
            });
        }
    });

    $('#navigationButton').click(function(event) {
        event.preventDefault();
        window.location.href = '/'; // will need changed to /login
    });
})

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('registrationForm').querySelectorAll("[required]").forEach(function (i) {
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

    if ($('#password').val() !== $('#confirmPassword').val()) {
        bootstrapAlert('danger', 'Passwords do not match.');
        return false;
    }

    let emailRegularExpression = /^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!(emailRegularExpression.test($('#email').val()))){
        bootstrapAlert('danger', 'Invalid email format.');
        return false;
    }

    return true;
}