$(document).ready(function() {
    $('#setButton').click(function(event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            // let registrationForm = $('#quizSettings').serializeArray().reduce(function (acc, item) {
            //     acc[item.name] = item.value;
            //     return acc;
            // })
        
            // //check if serialization maps incorrect key value pairs
            // if (quizSettings.name && quizSettings.value) {
            //     registrationForm[registrationForm.name] = registrationForm.value;
            //     delete quizSettings.name;
            //     delete quizSettings.value;
            // }

            //ajax call here once implemented for API
            bootstrapAlert('success', 'Quiz set!');

            window.location.href='/student/quiz';
   
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
