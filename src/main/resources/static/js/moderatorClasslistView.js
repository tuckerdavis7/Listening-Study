  const pathArr = location.href.split('/');
  const classID = pathArr[pathArr.length - 1];
  
  var studentTable, students, studentColumns;

  $(document).ready(function () {
    getStudents();

    studentColumns = [
        { data: "studentID", class: "charcolumn", width: "2 rem"},
        { data: "firstName", class: "charcolumn", width: "3 rem"},
        { data: "lastName", class: "charcolumn", width: "3 rem"},
        { data: "email", class: "charcolumn", width: "1 rem"},
        { data: "null", class: "text-center", defaultContent: `
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ]
    
    //studentTable = initializeDataTableWithFilters('#studentTable', students, studentColumns, [0, 'asc'], 5, [4]);

    $('#studentTable tbody').on('click', '.remove-btn', function() {
        const row = $(this).closest('tr');
        const rowIndex = studentTable.row(row).index();

        $('#removeConfirmation').data('rowindex', rowIndex)
        $('#removeConfirmation').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        let rowIndex = $('#removeConfirmation').data('rowindex');
        let row = studentTable.row(rowIndex).data();

        let deleteForm = {
            "studentID": row.studentID,
            "classID": classID
        };
        console.log(deleteForm);
        removeStudent(deleteForm);
    });

    //Add Student confirm button sample
    $('#confirmStudentBtn').on('click', function (event) {
        event.preventDefault();
        let valid = validateFormData();

        if (valid) {
            //gather form data
            let studentData = $('#addStudentForm').serializeArray().reduce(function (acc, item) {
                acc[item.name] = item.value;
                return acc;
            }, {});
        
            //check if serialization maps incorrect key value pairs
            if (studentData.name && studentData.value) {
                studentData[teacherData.name] = studentData.value;
                delete studentData.name;
                delete studentData.value;
            }

            studentData.classID = classID;
            console.log(studentData);
            addStudent(studentData);
        }
    });
})

function getStudents() {
  $.ajax({
      url: `http://localhost:8080/api/moderator/classlist?classID=${classID}`,
      method: 'GET',
      dataType: 'json',
      success: function (data) {
          studentTable = initializeDataTableWithFilters('#studentTable', data.data, studentColumns, [1, 'asc'], 10);
          console.log(data.data);
      },
      error: function (xhr, status, error) {
          console.error("Error fetching data from the API:", error);
      }
  });
}

function removeStudent(deleteForm) {
    $.ajax({
        data: JSON.stringify(deleteForm),
        url: `http://localhost:8080/api/moderator/classlist?classID=${classID}`,
        type: 'PATCH',
        contentType: 'application/json',
        success: function() {
            //console.log(deleteForm);
            bootstrapAlert('success', 'Removed student successfully.');
            $('#removeConfirmation').modal('hide');

            //refresh table data
            studentTable.destroy();
            getStudents();
        },
        error: function(xhr, status, error) {
            bootstrapAlert('danger', 'Error while removing student: ' + error);
        }
    });
}

function addStudent(studentData) {
    $.ajax({
        data: JSON.stringify(studentData),
        url: `http://localhost:8080/api/moderator/classlist?classID=${classID}`,
        method: 'POST',
        contentType: 'application/json',
        success: function (data) {
            console.log("Student added");
            bootstrapAlert('success', "Student Added");
            $('#addStudentModal').modal('hide');
;
            //refresh table data
            studentTable.destroy();
            getStudents();
        },
        error: function (xhr, status, error) {
            console.error("Student could not be added: ", error);
            bootstrapAlert('danger', 'Student could not be added: ' + error);
        }
    });
}

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('addStudentModal').querySelectorAll("[required]").forEach(function (i) {
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
