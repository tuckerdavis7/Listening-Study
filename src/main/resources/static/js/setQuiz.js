$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/setquiz?classID=1',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log(data); 
            renderSelectElement(data.data);      
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', 'Error while updating designation: ' + error);
        }
    });

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

            $.ajax({
                 data: JSON.stringify(quizForm),
                 url: 'http://localhost:8080/api/setquiz',
                 type: 'POST',
                 contentType: 'application/json',
                 success: function(data) {
                     // window.location.href='/student/quiz';  
                     bootstrapAlert('success', 'Hi Bro');                      
                 },
                 error: function(xhr, status, error) {
                     bootstrapAlert('danger', 'Error while logging in: ' + error);
                 }
             });
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

function renderSelectElement(data) {
    const selectElement = document.querySelector('#playlist');
    selectElement.innerHTML = '';
        
    data.forEach(item => {
        const option = new Option(item.playlistName, item.ID);
        selectElement.appendChild(option);
    });
}

/*<option value="1">Playlist 1</option>
<option value="2">Playlist 2</option>
<option value="3">Playlist 3</option>*/