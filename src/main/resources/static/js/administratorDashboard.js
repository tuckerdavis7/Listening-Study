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
        "first": "John",
        "last": "Cena",
        "email": "cena@sru.edu",
        "description": "You can't see me?  No, I can't see the login page.  HELP!",
        "status": "Acknowledged",
        "modifiedDate": "03/15/2025 13:50:55",
        "modifiedUser": "reiner_13" 
    },
    {
        "initialDate": "03/10/2025 07:36:24",
        "first": "Tucker",
        "last": "Davis",
        "email": "davis@sru.edu",
        "description": "Ben fried the RAM on my laptop.  Who is surpised?",
        "status": "Resolved",
        "modifiedDate": "03/15/2025 13:47:56",
        "modifiedUser": "reiner_13" 
    }
]

var userData = [
    {
        "first": "Joe",
        "last": "Reiner",
        "role": "teacher",
        "email": "reiner@sru.edu",
        "classes": [
            {
                "name": "Music History 1-1"
            },
            {
                "name": "Music History 2-1"
            }
        ]
    },
    {
        "first": "Ben",
        "last": "Luzier",
        "role": "administrator",
        "email": "luzier@sru.edu",
        "classes": ""
    },
    {
        "first": "Brenden",
        "last": "Stilwell",
        "role": "moderator",
        "email": "stilwell@sru.edu",
        "classes": ""
    },
    {
        "first": "Tucker",
        "last": "Davis",
        "role": "student",
        "email": "davis@sru.edu",
        "classes": ""
    }
]

$(document).ready(function () {
    reportTable = $('#reportTable').DataTable({
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
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#reportModal">View Report Details</a>' + 
                        '</div></div>';
                    return dropdown;
                },
            },
            { data: "initialDate", class: "charcolumn", width: "2 rem" },
            {
                class: "charColumn",
                data: null,
                width: "6 rem",
                render: function(data, type, row, meta) {
                    return row.first + ' ' + row.last;
                }
            },
            {
                class: "charColumn",
                data: null,
                width: "6 rem",
                render: function(data, type, row, meta) {
                    return row.modifiedDate + ' by ' + row.modifiedUser; 
                }
            },
            {
                class: "charColumn pillColumn",
                data: "status",
                width: "6 rem",
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
                
                if ($('#reportTable thead tr.filters').length === 0) {
                    $('#reportTable thead').append('<tr class="filters"></tr>');
                }
                if (index === 0) {
                    $('#reportTable thead tr.filters').append('<th></th>');
                } 
                else {
                    let filterCell = $('<th><input type="text" class="form-control form-control-sm" placeholder="Filter ' + title + '" /></th>');
                    $('#reportTable thead tr.filters').append(filterCell);
                    $('input', filterCell).on('keyup change', function() {
                        if (column.search() !== this.value) {
                            column.search(this.value).draw();
                        }
                    });
                }
            });
            
            //disable sorting for search row
            $('#reportTable thead tr.filters th').addClass('sorting_disabled');
        }
    });

    //order table by date column
    reportTable.order([[1, 'asc']]).draw();

    userTable = $('#userTable').DataTable({
        data: userData,
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
                data: "role",
                orderable: false,
                width: "1 em",
                render: function(data, type, row, meta) {
                    let dropdown = "";
                    if (data === 'moderator'){
                        dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#userModal" data-type="moderator">Remove Moderator Designation</a>' + 
                        '<a class="dropdown-item text-danger fw-bold" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#userModal" data-type="delete">Delete User</a>' + 
                        '</div></div>';
                    }
                    else if (data === 'teacher') {
                        dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#userModal" data-type="teacher">Designate Moderator</a>' + 
                        '<a class="dropdown-item text-danger fw-bold" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#userModal" data-type="delete">Delete User</a>' + 
                        '</div></div>';
                    }
                    else if (data === 'student') {
                        dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item text-danger fw-bold" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#userModal" data-type="delete">Delete User</a>' + 
                        '</div></div>';
                    }
                    
                    return dropdown;
                },
            },
            {
                class: "charColumn",
                data: null,
                width: "6 rem",
                render: function(data, type, row, meta) {
                    return row.first + ' ' + row.last;
                }
            },
            {
                class: "charColumn",
                data: "role",
                width: "6 rem",
                render: function(data, type, row, meta) {
                    return data.charAt(0).toUpperCase() + data.slice(1);
                }
            },
            { data: "email", class: "charcolumn", width: "2 rem" }
        ],
        initComplete: function() {
            //create the search rows
            this.api().columns().every(function(index) {
                let column = this;
                let title = $(column.header()).text().trim();
                
                if ($('#userTable thead tr.filters').length === 0) {
                    $('#userTable thead').append('<tr class="filters"></tr>');
                }
                if (index === 0) {
                    $('#userTable thead tr.filters').append('<th></th>');
                } 
                else {
                    let filterCell = $('<th><input type="text" class="form-control form-control-sm" placeholder="Filter ' + title + '" /></th>');
                    $('#userTable thead tr.filters').append(filterCell);
                    $('input', filterCell).on('keyup change', function() {
                        if (column.search() !== this.value) {
                            column.search(this.value).draw();
                        }
                    });
                }
            });
            
            //disable sorting for search row
            $('#userTable thead tr.filters th').addClass('sorting_disabled');
        }
    });

    //order table by date column
    userTable.order([[1, 'asc']]).draw();

    showTab('bugReports'); //show bug reports tab first

    $('#reportModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = reportTable.row(button.data('rowindex')).data();

        $('#reportDate').html(row.initialDate);
        $('#reportName').html(row.first + ' ' + row.last);
        $('#reportEmail').html(row.email);
        $('#reportDate').html(row.initialDate);
        $('#reportDescription').html(row.description);
        $('#reportModified').html(row.modifiedDate + ' ' + row.modifiedUser);
        $('#reportStatus').html(row.status);

        //clearing comment field in submodal
        $('#resolution').val('');

        if (row.status == 'Open') {
            $('#resolveReportButton').hide();
            $('#acknowledgeReportButton').show();
        }

        else if (row.status == 'Acknowledged'){
            $('#acknowledgeReportButton').hide();
            $('#resolveReportButton').show();
        }

        else {
            $('#resolveReportButton, #acknowledgeReportButton').hide();
        }
    });

    $('#acknowledgeReportButton').on('click', function() {
        bootstrapAlert('info', 'Report acknowledged.');
        $('#reportModal').modal('hide');
    });

    $('#saveReportButton').on('click', function() {
        let valid = validateFormData();
        if (valid) {
            bootstrapAlert('success', 'Report resolved successfully.');
            $('#reportModal, #reportSubModal').modal('hide');
        }
    });

    $('#userModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = userTable.row(button.data('rowindex')).data();
        let type = button.data('type');

        if (type === 'moderator') {
            $('#userModalHeader').text('Remove Moderator Designation');
            $('#addDesignationButton, #deleteUserButton').hide();
            $('#removeDesignationButton').show();
        }
        else if (type === 'teacher'){
            $('#userModalHeader').text('Designate Moderator');
            $('#addDesignationButton').show();
            $('#removeDesignationButton, #deleteUserButton').hide();
        }

        else{
            $('#userModalHeader').text('Delete User');
            $('#removeDesignationButton, #addDesignationButton').hide();
            $('#deleteUserButton').show();
        }

        $('#userName').html(row.first + ' ' + row.last);
        $('#userEmail').html(row.email);
    });

    $('#addDesignationButton').on('click', function() {
        bootstrapAlert('success', 'Moderator designated successfully.');
        $('#userModal').modal('hide');
    });

    $('#removeDesignationButton').on('click', function() {
        bootstrapAlert('success', 'Removed moderator designation successfully.');
        $('#userModal').modal('hide');
    });

    $('#deleteUserButton').on('click', function() {
        bootstrapAlert('success', 'Deleted user successfully.');
        $('#userModal').modal('hide');
    });
})

//Function to toggle back and forth between tabs for tables
function showTab(tabName) {
    $('.tab-content').hide();
    $('.navbar-nav .nav-link').removeClass('active');
    $('#' + tabName).show();
    $('a[data-tab="' + tabName + '"]').addClass('active');

    if (tabName === 'bugReports' && typeof reportTable !== 'undefined') {
        reportTable.columns.adjust().draw();
    } 
    else if (tabName === 'manageUsers' && typeof userTable !== 'undefined') {
        userTable.columns.adjust().draw();
    }
}

function validateFormData() {
    let allAreFilled = true;
    document.getElementById('resolutionForm').querySelectorAll("[required]").forEach(function (i) {
        if (!allAreFilled) {
            return;
        }

        if (!i.value) {
            allAreFilled = false;
            return;
        }

    })
    if (!allAreFilled) {
        bootstrapAlert('danger', 'Resolution required.');
        return false;
    }

    return true;
}