//custom alerts
function bootstrapAlert(type, message) {
  let alert = document.createElement('div');
  $(alert).addClass('alert alert-' + type + ' alert-dismissible fade show');
  alert.setAttribute('role', 'alert');
  alert.innerHTML = message + `<button type="button" class="btn-close" aria-label="Close"></button>`;

  let closeButton = alert.querySelector('.btn-close');
  closeButton.addEventListener('click', function () {
      alert.classList.remove('show');
      setTimeout(() => alert.remove(), 500);
  });

  document.body.appendChild(alert);

  //error alerts stay, otherwise hide after 7 seconds
  if (type !== 'danger') {
      setTimeout(() => {
          alert.classList.remove('show');
          setTimeout(() => alert.remove(), 500);
      }, 7000);
  }
}