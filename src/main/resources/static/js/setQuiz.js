$(document).ready(function() {
    $('#setButton').click(function(event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let quizForm = $('#quizSettings').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            })
        
            //check if serialization maps incorrect key value pairs
            if (quizForm.name && quizForm.value) {
                quizForm[quizForm.name] = quizForm.value;
                delete quizForm.name;
                delete quizForm.value;
            }

            console.log(quizForm);

            // $.ajax({
            //     data: JSON.stringify(quizForm),
            //     url: 'http://localhost:8080/api/setquiz',
            //     type: 'POST',
            //     contentType: 'application/json',
            //     success: function(data) {
            //         // window.location.href='/student/quiz';                        
            //     },
            //     error: function(xhr, status, error) {
            //         bootstrapAlert('danger', 'Error while logging in: ' + error);
            //     }
            // });
        }
    });
});


function validateFormData() {
    let allAreFilled = true;
    document.getElementById('quizSettings').querySelectorAll("[required]").forEach(function (i) {
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
