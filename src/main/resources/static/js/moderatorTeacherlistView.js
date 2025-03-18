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

  $(document).ready(function () {
    let teacherTable = $('#teacherTable').DataTable({
        data: teachers,
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
        scrollY: '50vh', // Use viewport percentage for responsive height
        responsive: true,
        filter: true,
        info: false,
        lengthChange: false,
        columnDefs: [{
            orderable: false,
            targets: 0,
        }],
        columns: [
            { data: "teacherId", class: "charcolumn", width: "2 rem"},
            { data: "firstName", class: "charcolumn", width: "3 rem"},
            { data: "lastName", class: "charcolumn", width: "3 rem"},
            { data: "email", class: "charcolumn", width: "1 rem"},
            { data: "null", class: "text-center", defaultContent: `
                <button class="btn btn-warning disable-btn">Disable</button>
                <button class ="btn btn-danger remove-btn">Remove</button>
            `},
        ],
        drawCallback: function() {
            $('.dt-paging-button.current').attr('style', 'color: white !important'); //inline css styling used as last resort for highest priority selector
        }
    });

    //add a text input to each header cell
    $('#teacherTable thead').append('<tr class="searchRow"></tr>');
    $('#teacherTable').DataTable().columns().every(function (i) {
        if (i === 0) {
            $(this.header()).closest('thead').children('.searchRow').append('<th></th>');
        }
        else {
            var that = this;
            let title = $(this.header()).text();
            $(this.header()).closest('thead').children('.searchRow').append('<th><input class="form-control form-control-sm m-1 w-100 sorting_disabled" type="search" placeholder="Filter ' + title + '" /></th>');
            $('input', $('#teacherTable thead tr.searchRow th').eq(i)).on('keyup change clear search', function() {
                if (that.search() !== this.value) {
                    that.search(this.value).draw();
                }
            });
        }
    });

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

    //order table by status column
    teacherTable.order([[4, 'asc']]).draw();

    //Add Teacher confirm button sample
    $('#confirmTeacherBtn').on('click', function () {
        // Close the modal
        $('#addTeacherModal').modal('hide');
    });
})
