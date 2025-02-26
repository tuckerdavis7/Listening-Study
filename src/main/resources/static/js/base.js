$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/metadata',
        type: 'GET',
        contentType: 'application/json',
        success: function(data) {
            //set metadata on page
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

function bootstrapAlert(type, message) {
    let alert = document.createElement('div');
    $(alert).addClass('alert alert-' + type + ' alert-dismissible fade show'); //follows bootstrap classes
    alert.setAttribute('role', 'alert');
    alert.innerHTML = message + `<button type="button" class="btn-close" aria-label="Close"></button>`;
  
    let closeButton = alert.querySelector('.btn-close');
    closeButton.addEventListener('click', function () {
        alert.classList.remove('show');
        setTimeout(() => alert.remove(), 500);
    });
  
    document.body.appendChild(alert);
  
    //error alerts stay, otherwise hide after 6 seconds
    if (type !== 'danger') {
        setTimeout(() => {
            alert.classList.remove('show');
            setTimeout(() => alert.remove(), 500);
        }, 6000);
    }
  }
