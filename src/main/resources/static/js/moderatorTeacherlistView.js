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
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ];

    $('#teacherTable tbody').on('click', '.remove-btn', function() {
        const row = $(this).closest('tr');
        const rowIndex = teacherTable.row(row).index();

        $('#removeConfirmation').data('rowindex', rowIndex)
        $('#removeConfirmation').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        let rowIndex = $('#removeConfirmation').data('rowindex');
        let row = teacherTable.row(rowIndex).data();

        let deleteForm = {
            "email": row.email,
            "classID": classID
        };
        removeTeacher(deleteForm);
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
          teacherTable = initializeDataTableWithFilters('#teacherTable', data.data, teacherColumns, [1, 'asc'], 10);
          console.log(data.data);
      },
      error: function (xhr, status, error) {
          console.error("Error fetching data from the API:", error);
      }
  });

}

function removeTeacher(deleteForm) {
    $.ajax({
        data: JSON.stringify(deleteForm),
        url: `http://localhost:8080/api/moderator/teacherlist?classID=${classID}`,
        type: 'PATCH',
        contentType: 'application/json',
        success: function() {
            console.log(deleteForm);
            bootstrapAlert('success', 'Removed teacher successfully.');
            $('#removeConfirmation').modal('hide');

            //refresh table data
            teacherTable.destroy();
            getTeachers();
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', 'Error while removing teacher: ' + error);
        }
    });
}