function handleLoginAttempt() {

    event.preventDefault();

    $.ajax({
      url: 'http://localhost:8080/api/login',
      type: 'GET',
      dataType: 'text',
      success: function(data) {
          bootstrapAlert('info', data);
      },
      error: function(xhr, status, error) {
          bootstrapAlert('danger', error);
      }
  });
  }