  const pathArr = location.href.split('/');
  const classID = pathArr[pathArr.length - 1];

  var teacherTable, teachers, teacherColumns;

  $(document).ready(function () {
    getTeachers();

    teacherColumns = [
        { data: "teacherID", class: "charcolumn", width: "2 rem"},
        { data: "firstName", class: "charcolumn", width: "3 rem"},
        { data: "lastName", class: "charcolumn", width: "3 rem"},
        { data: "email", class: "charcolumn", width: "1 rem"},
        { data: "null", class: "text-center", defaultContent: `
            <button class="btn btn-warning disable-btn">Disable</button>
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ];

    //event listener for disable button
    $('#teacherTable tbody').on('click', '.disable-btn', function() {
        const button = $(this);

        if(button.hasClass('btn-warning')) {
            button.text('Enable');
            button.removeClass('btn-warning').addClass('btn-secondary');
        }
        else {
            button.text('Disable');
            button.removeClass('btn-secondary').addClass('btn-warning');
        }
    });

    //event listener for remove button
    let rowRemove;

    $('#teacherTable tbody').on('click', '.remove-btn', function() {
        const row = $(this).closest('tr');
        rowRemove = row;

        $('#removeConfirmation').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        if(rowRemove) {
            teacherTable.row(rowRemove).remove().draw();
            $('#removeConfirmation').modal('hide');
        }
    });

    //Add Teacher confirm button sample
    $('#confirmTeacherBtn').on('click', function () {
        bootstrapAlert('success', "Teacher Added");
        $('#addTeacherModal').modal('hide');
    });
})

function getTeachers() {
  $.ajax({
      url: `http://localhost:8080/api/moderator/teacherlist?classID=${classID}`,
      method: 'GET',
      dataType: 'json',
      success: function (data) {
          teachers = initializeDataTableWithFilters('#teacherTable', data.data, teacherColumns, [1, 'asc'], 10);
          console.log(data.data);
      },
      error: function (xhr, status, error) {
          console.error("Error fetching data from the API:", error);
      }
  });

}