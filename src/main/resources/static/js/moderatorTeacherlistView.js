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
        { data: "null", class: "text-center", orderable: false, defaultContent: `
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
    $('#confirmTeacherButton').on('click', function (event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let teacherData = $('#addTeacherForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            }, {});
        
            //check if serialization maps incorrect key value pairs
            if (teacherData.name && teacherData.value) {
                teacherData[teacherData.name] = teacherData.value;
                delete teacherData.name;
                delete teacherData.value;
            }

            teacherData.classID = classID;
            console.log(teacherData);
            addTeacher(teacherData);
        }
    });
})

function getTeachers() {
  $.ajax({
      url: `http://localhost:8080/api/moderator/teacherlist?classID=${classID}`,
      method: 'GET',
      dataType: 'json',
      success: function (data) {
          teacherTable = initializeDataTableWithFilters('#teacherTable', data.data, teacherColumns, [1, 'asc'], 10, [4]);
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

function addTeacher(teacherData) {
    $.ajax({
        data: JSON.stringify(teacherData),
        url: `http://localhost:8080/api/moderator/teacherlist?classID=${classID}`,
        method: 'POST',
        contentType: 'application/json',
        success: function (data) {
            console.log("Teacher added");
            bootstrapAlert('success', "Teacher Added");
            $('#addTeacherModal').modal('hide');

            //refresh table data
            teacherTable.destroy();
            getTeachers();
        },
        error: function (xhr, status, error) {
            console.error("Teacher could not be added: ", error);
            bootstrapAlert('danger', 'Teacher could not be added: ' + error);
        }
    });
}

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('addTeacherModal').querySelectorAll("[required]").forEach(function (i) {
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