var reportTable, reportColumns, userTable, userColumns;

$(document).ready(function () {
    reportColumns = [
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
        { data: "username", class: "charcolumn", width: "3 rem" },
        {
            class: "charColumn",
            data: null,
            width: "5 rem",
            render: function(data, type, row, meta) {
                return row.modifiedDate + ' by ' + row.modifiedBy; 
            }
        },
        {
            class: "pillColumn",
            data: "status",
            width: "3 rem",
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
    ];

    userColumns = [
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
                return row.firstName + ' ' + row.lastName;
            }
        },
        { data: "username", class: "charcolumn", width: "5 rem" },
        {
            class: "charColumn",
            data: "role",
            width: "6 rem",
            render: function(data, type, row, meta) {
                return data.charAt(0).toUpperCase() + data.slice(1);
            }
        },
        { data: "email", class: "charcolumn", width: "2 rem" }
    ];

    $.ajax({
        url: 'http://localhost:8080/api/administrator/reports',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            reportTable = initializeDataTableWithFilters('#reportTable', data.data, reportColumns, [1, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });

    $.ajax({
        url: 'http://localhost:8080/api/administrator/users',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            userTable = initializeDataTableWithFilters('#userTable', data.data, userColumns, [1, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });
    
    showTab('bugReports'); //show bug reports tab first

    $('#reportModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = reportTable.row(button.data('rowindex')).data();

        $('#reportDate').html(row.initialDate);
        $('#reportUser').html(row.username);
        $('#reportEmail').html(row.email);
        $('#reportDate').html(row.initialDate);
        $('#reportDescription').html(row.description);
        $('#reportModified').html(row.modifiedDate + ' ' + row.modifiedBy);
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

        $('#userName').html(row.firstName + ' ' + row.lastName);
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