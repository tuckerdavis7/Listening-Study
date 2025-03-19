let students = [
    {
      "studentId": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com"
    },
    {
      "studentId": 2,
      "firstName": "Jane",
      "lastName": "Smith",
      "email": "jane.smith@example.com"
    },
    {
      "studentId": 3,
      "firstName": "Alice",
      "lastName": "Johnson",
      "email": "alice.johnson@example.com"
    },
    {
      "studentId": 4,
      "firstName": "Bob",
      "lastName": "Brown",
      "email": "bob.brown@example.com"
    },
    {
      "studentId": 5,
      "firstName": "Charlie",
      "lastName": "Davis",
      "email": "charlie.davis@example.com"
    }
  ]

  var studentTable;

  $(document).ready(function () {
    let studentColumns = [
        { data: "studentId", class: "charcolumn", width: "2 rem"},
        { data: "firstName", class: "charcolumn", width: "3 rem"},
        { data: "lastName", class: "charcolumn", width: "3 rem"},
        { data: "email", class: "charcolumn", width: "1 rem"},
        { data: "null", class: "text-center", defaultContent: `
            <button class="btn btn-warning disable-btn">Disable</button>
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ]
    
    studentTable = initializeDataTableWithFilters('#studentTable', students, studentColumns, [0, 'asc'], [4]);

    //event listener for disable button
    $('#studentTable tbody').on('click', '.disable-btn', function() {
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

    $('#studentTable tbody').on('click', '.remove-btn', function() {
        const row = $(this).closest('tr');
        rowRemove = row;

        $('#removeConfirmation').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        if(rowRemove) {
            studentTable.row(rowRemove).remove().draw();
            $('#removeConfirmation').modal('hide');
        }
    });

    //Add Student confirm button sample
    $('#confirmStudentBtn').on('click', function () {
        // Close the modal
        $('#addStudentModal').modal('hide');
    });
})

