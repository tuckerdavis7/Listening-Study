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

//Function that creates a container to hold all alerts
function createAlertContainer() {
    if (!$('#alert-container').length) {
        let container = document.createElement('div');
        container.id = 'alert-container';
        $(container).css({
            'position': 'fixed',
            'top': '10px',
            'right': '10px',
            'z-index': '1050'
        });
        document.body.appendChild(container);
    }
}

//Function that creates custom alerts throughout application
function bootstrapAlert(type, message) {
    createAlertContainer();
    
    let alert = document.createElement('div');
    $(alert).addClass('alert alert-' + type + ' alert-dismissible fade show'); //follows bootstrap classes
    alert.setAttribute('role', 'alert');
    alert.innerHTML = message + `<button type="button" class="btn-close" aria-label="Close"></button>`;
  
    let closeButton = alert.querySelector('.btn-close');
    closeButton.addEventListener('click', function () {
        alert.classList.remove('show');
        setTimeout(() => {
            alert.remove();
            repositionAlerts();
        }, 150);
    });
  
    $('#alert-container').append(alert);
    repositionAlerts();
  
    //error alerts stay, otherwise hide after 6 seconds
    if (type !== 'danger') {
        setTimeout(() => {
            alert.classList.remove('show');
            setTimeout(() => {
                alert.remove();
                repositionAlerts();
            }, 150);
        }, 6000);
    }
}

//Function to reposition alerts when one is added or removed
function repositionAlerts() {
    let totalHeight = 0;
    $('#alert-container .alert').each(function(index) {
        $(this).css('transform', 'translateY(' + totalHeight + 'px)');
        totalHeight += $(this).outerHeight(true) + 10;
    });
}

//Universal logout button handling.  Built in case if more is required for API
function handleLogOut() {
    window.location.href = '/';
}