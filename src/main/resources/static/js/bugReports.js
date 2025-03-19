var reportData = [
    {
        "initialDate": "03/14/2025 13:35:37",
        "first": "Jane",
        "last": "Doe",
        "email": "doe@sru.edu",
        "description": "A long text-field explaining the issue at hand",
        "status": "Open",
        "modifiedDate": "03/14/2025 13:35:37",
        "modifiedUser": "jdoe" 
    },
    {
        "initialDate": "03/15/2025 09:45:00",
        "first": "Jane",
        "last": "Doe",
        "email": "doe@sru.edu",
        "description": "You can't see me?  No, I can't see the login page.  HELP!",
        "status": "Acknowledged",
        "modifiedDate": "03/15/2025 13:50:55",
        "modifiedUser": "reiner_13" 
    },
    {
        "initialDate": "03/10/2025 07:36:24",
        "first": "Jane",
        "last": "Doe",
        "email": "doe@sru.edu",
        "description": "Ben fried the RAM on my laptop.  Who is surpised?",
        "status": "Resolved",
        "modifiedDate": "03/15/2025 13:47:56",
        "modifiedUser": "reiner_13" 
    }
]

var userData = [
    {
        "role": "administrator"
    }
    
]

$(document).ready(function () {
    bugsTable = $('#bugsTable').DataTable({
        data: reportData,
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
            {
                class: "wrenchColumn",
                data: null,
                orderable: false,
                width: "1 em",
                render: function(data, type, row, meta) {
                    let dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#resolveModal">Resolve Bug Report</a>' + 
                        '</div></div>';
                    return dropdown;
                },
            },
            { data: "initialDate", class: "charcolumn", width: "2 rem" },
            { data: "description", class: "charcolumn", width: "8 rem" },
            {
                class: "charColumn",
                data: null,
                width: "5 rem",
                render: function(data, type, row, meta) {
                    return row.modifiedDate + ' by ' + row.modifiedUser; 
                }
            },
            {
                class: "charColumn pillColumn",
                data: "status",
                width: "4 rem",
                render: function(data, type, row, meta) {
                    let pillClass;
                    if (data === "Open") {
                        pillClass = "bg-danger";
                    }
                    else if (data === "Acknowledged") {
                        pillClass = "bg-warning";
                    }
                    else {
                        pillClass = "bg-success";
                    }

                    return '<span class="badge rounded-pill ' + pillClass + ' text-center d-inline-block">' + data + '</span>';
                }
            },
           
        ],
        initComplete: function() {
            //create the search rows
            this.api().columns().every(function(index) {
                let column = this;
                let title = $(column.header()).text().trim();
                
                if ($('#bugsTable thead tr.filters').length === 0) {
                    $('#bugsTable thead').append('<tr class="filters"></tr>');
                }
                if (index === 0) {
                    $('#bugsTable thead tr.filters').append('<th></th>');
                } 
                else {
                    let filterCell = $('<th><input type="text" class="form-control form-control-sm" placeholder="Filter ' + title + '" /></th>');
                    $('#bugsTable thead tr.filters').append(filterCell);
                    $('input', filterCell).on('keyup change', function() {
                        if (column.search() !== this.value) {
                            column.search(this.value).draw();
                        }
                    });
                }
            });
            
            //disable sorting for search row
            $('#bugsTable thead tr.filters th').addClass('sorting_disabled');
        }
    });

    //order table by date column
    bugsTable.order([[1, 'asc']]).draw();


    $('#bugModal').on('show.bs.modal', function (event) {
        $('#description').val('');
    });

    $('#saveBugButton').on('click', function() {
        let valid = validateFormData();
        if (valid) {
            bootstrapAlert('success', 'Report created successfully.');
            $('#bugModal').modal('hide');
        }
    });

    $('#resolveModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = bugsTable.row(button.data('rowindex')).data();

        $('#resolveDate').html(row.initialDate);
        $('#resolveName').html(row.first + ' ' + row.last);
        $('#resolveEmail').html(row.email);
        $('#resolveDate').html(row.initialDate);
        $('#resolveDescription').html(row.description);
        $('#resolveModified').html(row.modifiedDate + ' ' + row.modifiedUser);
        $('#resolveStatus').html(row.status);
    });

    $('#resolveReportButton').on('click', function() {
        bootstrapAlert('success', 'Report resolved successfully.');
        $('#resolveModal').modal('hide');
    });
});

function navigateHome(){
    switch(userData[0].role) {
        case "administrator":
            window.location.href = '/administrator/dashboard';
            break;
        
        case "moderator":
            window.location.href='/moderator/dashboard'
            break;

        case "teacher":
            window.location.href='/teacher/dashboard'
            break;
        
        case "student":
        window.location.href='/student/dashboard'
        break;
    }
}

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('bugForm').querySelectorAll("[required]").forEach(function (i) {
        if (!allAreFilled) {
            return;
        }

        if (!i.value) {
            allAreFilled = false;
            return;
        }

    })
    if (!allAreFilled) {
        bootstrapAlert('danger', 'Description required.');
        return false;
    }

    return true;
}