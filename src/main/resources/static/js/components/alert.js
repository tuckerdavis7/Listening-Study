//Function that creates a container to hold all alerts
function createAlertContainer() {
    if (!$('#alert-container').length) {
        let container = document.createElement('div');
        container.id = 'alert-container';
        $(container).css({
            'position': 'fixed',
            'top': '10px',
            'right': '10px',
            'z-index': '1060'
        });
        document.body.appendChild(container);
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

//Function that creates custom alerts
function bootstrapAlert(type, message) {
    createAlertContainer();
    
    let alert = document.createElement('div');
    $(alert).addClass('alert alert-' + type + ' alert-dismissible fade show'); //follows bootstrap classes
    alert.setAttribute('role', 'alert');
    alert.innerHTML = message + `<button type="button" class="btn btn-close" aria-label="Close"></button>`;
  
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