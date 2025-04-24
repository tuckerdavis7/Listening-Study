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

    //gather API data
    getUserData();
    getReportData();

    showTab('bugReports'); //show bug reports tab first

    $('#reportModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = reportTable.row(button.data('rowindex')).data();
        let resolution = (row.resolution === '') ? 'N/A' : row.resolution;

        $('#reportDate').html(row.initialDate);
        $('#reportEmail').html(row.email);
        $('#reportDate').html(row.initialDate);
        $('#reportDescription').html(row.description);
        $('#reportModified').html(row.modifiedDate + ' ' + row.modifiedBy);
        $('#reportStatus').html(row.status);
        $('#reportResolution').html(resolution);
        $('#reportID').val(row.ID);

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

    $('#acknowledgeReportButton, #saveReportButton').on('click', function() {
        let statusForm = {status: null, resolution: "", id: $('#reportID').val()};
        let isResolutionFieldEmpty = false;

        if ($('#acknowledgeReportButton').is(":visible")) {
            statusForm.status = "Acknowledged";
        }
        else {
            statusForm.status = "Resolved";
            statusForm.resolution = $('#resolution').val();

            let valid = validateFormData();
            if (!valid) {
                isResolutionFieldEmpty = true;
            }
        }

        if (!isResolutionFieldEmpty) {
            $.ajax({
                data: JSON.stringify(statusForm),
                url: 'http://localhost:8080/api/administrator/reports',
                type: 'PATCH',
                contentType: 'application/json',
                success: function(data) {
                    bootstrapAlert('success', 'Status updated.');
                    $('#reportModal, #reportSubModal').modal('hide');
    
                    //refresh table data
                    reportTable.destroy();
                    getReportData();                     
                },
                error: function(xhr, status, error) {
                    bootstrapAlert('danger', 'Error while updating status: ' + error);
                }
            });
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
        $(this).data('rowindex', button.data('rowindex')); //storing data for buttons
    });

    $('#addDesignationButton, #removeDesignationButton').on('click', function() {
        let rowIndex = $('#userModal').data('rowindex');
        let row = userTable.row(rowIndex).data();

        console.log(row);
        // change the role for API data
        let changedRole = (row.role === 'teacher') ? 'moderator' : 'teacher';
        let designationForm = {"role": changedRole, "email": row.email, "id": row.id};

        $.ajax({
            data: JSON.stringify(designationForm),
            url: 'http://localhost:8080/api/administrator/users',
            type: 'PATCH',
            contentType: 'application/json',
            success: function(data) {
                bootstrapAlert('success', 'Designation successful.');
                $('#userModal').modal('hide');

                //refresh table data
                userTable.destroy();
                getUserData();                     
            },
            error: function(xhr, status, error) {
                bootstrapAlert('danger', 'Error while updating designation: ' + error);
            }
        });
    });

    $('#deleteUserButton').on('click', function() {
        let rowIndex = $('#userModal').data('rowindex');
        let row = userTable.row(rowIndex).data();

        let deleteForm = {"email": row.email};
        $.ajax({
            data: JSON.stringify(deleteForm),
            url: 'http://localhost:8080/api/administrator/users',
            type: 'DELETE',
            contentType: 'application/json',
            success: function() {
                bootstrapAlert('success', 'Deleted user successfully.');
                $('#userModal').modal('hide');

                //refresh table data
                userTable.destroy();
                getUserData();
            },
            error: function(xhr, status, error) {
                bootstrapAlert('danger', 'Error while deleting user: ' + error);
            }
        });
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

//reusable function for ajax call to gather user data (Manage Users Screen)
function getUserData() {
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

}

//reusable function for ajax call to gather report data (Manage Bug Reports Screen)
function getReportData() {
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