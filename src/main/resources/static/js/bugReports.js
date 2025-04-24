var bugTable, bugColumns;

$(document).ready(function () {
    bugColumns = [
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
                return row.modifiedDate + ' by ' + row.modifiedBy; 
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
       
    ];

    getBugData();

    $('#bugModal').on('show.bs.modal', function (event) {
        $('#description').val('');
    });

    $('#saveBugButton').on('click', function() {
        let valid = validateFormData();
        if (valid) {
            let bugForm = {"description": $('#description').val()};

            console.log(bugForm);

            $.ajax({
                data: JSON.stringify(bugForm),
                url: 'http://localhost:8080/api/bugReports',
                type: 'POST',
                contentType: 'application/json',
                success: function(data) {
                    bootstrapAlert('success', 'Report created successfully');
                    $('#bugModal').modal('hide');
    
                    //refresh table data
                    bugTable.destroy();
                    getBugData();                     
                },
                error: function(xhr, status, error) {
                    bootstrapAlert('danger', 'Error while adding report: ' + error);
                }
            });
        }
    });

    $('#resolveModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = bugTable.row(button.data('rowindex')).data();
        let resolution = (row.resolution === '') ? 'N/A' : row.resolution;

        $('#resolveDate').html(row.initialDate);
        $('#resolveEmail').html(row.email);
        $('#resolveDescription').html(row.description);
        $('#resolveModified').html(row.modifiedDate + ' by ' + row.modifiedBy);
        $('#resolveStatus').html(row.status);
        $('#resolveResolution').html(resolution);
        $('#reportID').val(row.ID);

        if (row.status === 'Resolved') {
            $('#resolveReportButton').hide();
        }
        else {
            $('#resolveReportButton').show();
        }
    });

    $('#resolveReportButton').on('click', function() {
        let bugForm = {id: $('#reportID').val()};

        $.ajax({
            data: JSON.stringify(bugForm),
            url: 'http://localhost:8080/api/bugReports',
            type: 'PATCH',
            contentType: 'application/json',
            success: function(data) {
                bootstrapAlert('success', 'Report resolved.');
                $('#resolveModal').modal('hide');

                //refresh table data
                bugTable.destroy();
                getBugData();                     
            },
            error: function(xhr, status, error) {
                bootstrapAlert('danger', 'Error while updating report: ' + error);
            }
        });
    });
});

//api call to get data for page
function getBugData() {
    $.ajax({
        url: 'http://localhost:8080/api/bugReports',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            bugTable = initializeDataTableWithFilters('#bugTable', data.data, bugColumns, [1, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });
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