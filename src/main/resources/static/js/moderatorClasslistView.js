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

  $(document).ready(function () {
    let studentTable = $('#studentTable').DataTable({
        data: students,
        // ajax:{
        //     // url: http::/localhost:8080/api/
        //     // type: 'GET',
        //     // dataSrc: '',
        //     // dataType: 'json',
        //     // data: classData,
        //     // timeout: '0',
        // },
        dom: "<'row'<'col-sm-12 col-md-12 text-end'B>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>><'#bottomLink'>",
        scrollCollapse: false,
        responsive: true,
        filter: true,
        info: false,
        lengthChange: false,
        columnDefs: [{
            orderable: false,
            targets: 0,
        }],
        columns: [
            { data: "studentId", class: "charcolumn", width: "2 rem"},
            { data: "firstName", class: "charcolumn", width: "3 rem"},
            { data: "lastName", class: "charcolumn", width: "3 rem"},
            { data: "email", class: "charcolumn", width: "1 rem"},
            { data: "null", class: "text-center", defaultContent: `
                <button class="btn btn-warning disable-btn">Disable</button>
                <button class ="btn btn-danger remove-btn">Remove</button>
            `},
        ],
        initComplete: function() {
            //create the search rows
            this.api().columns().every(function(index) {
                let column = this;
                let title = $(column.header()).text().trim();
                
                if ($('#studentTable thead tr.filters').length === 0) {
                    $('#studentTable thead').append('<tr class="filters"></tr>');
                }
                if (index === 0) {
                    $('#studentTable thead tr.filters').append('<th></th>');
                } 
                else {
                    let filterCell = $('<th><input type="text" class="form-control form-control-sm" placeholder="Filter ' + title + '" /></th>');
                    $('#studentTable thead tr.filters').append(filterCell);
                    $('input', filterCell).on('keyup change', function() {
                        if (column.search() !== this.value) {
                            column.search(this.value).draw();
                        }
                    });
                }
            });
            
            //disable sorting for search row
            $('#studentTable thead tr.filters th').addClass('sorting_disabled');
        }
    });

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

    //order table by status column
    studentTable.order([[4, 'asc']]).draw();

    //Add Student confirm button sample
    $('#confirmStudentBtn').on('click', function () {
        // Close the modal
        $('#addStudentModal').modal('hide');
    });
})

