let teachers = [
    {
      "teacherId": 1,
      "firstName": "John",
      "lastName": "Johnson",
      "email": "john.johnson@sru.edu",
    },
    {
      "teacherId": 2,
      "firstName": "Johnson",
      "lastName": "John",
      "email": "johnson.john@sru.edu",
    },
    {
      "teacherId": 3,
      "firstName": "Alice",
      "lastName": "Johnson",
      "email": "alice.johnson@example.com"
    },
    {
      "teacherId": 4,
      "firstName": "Bob",
      "lastName": "Brown",
      "email": "bob.brown@example.com"
    },
    {
      "teacherId": 5,
      "firstName": "Charlie",
      "lastName": "Davis",
      "email": "charlie.davis@example.com"
    }
  ]

  var teacherTable;

  $(document).ready(function () {
    let teacherColumns = [
        { data: "teacherId", class: "charcolumn", width: "2 rem"},
        { data: "firstName", class: "charcolumn", width: "3 rem"},
        { data: "lastName", class: "charcolumn", width: "3 rem"},
        { data: "email", class: "charcolumn", width: "1 rem"},
        { data: "null", class: "text-center", defaultContent: `
            <button class="btn btn-warning disable-btn">Disable</button>
            <button class ="btn btn-danger remove-btn">Remove</button>
        `},
    ];

    teacherTable = initializeDataTableWithFilters('#teacherTable', teachers, teacherColumns, [0, 'asc'], 5, [4]);

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
