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
        "description": "A long text-field explaining the issue at hand",
        "status": "Closed",
        "modifiedDate": "03/15/2025 13:50:55",
        "modifiedUser": "reiner_13" 
    },
    {
        "initialDate": "03/10/2025 07:36:24",
        "first": "Tucker",
        "last": "Davis",
        "email": "davis@sru.edu",
        "description": "A long text-field explaining the issue at hand",
        "status": "Resolved",
        "modifiedDate": "03/15/2025 13:47:56",
        "modifiedUser": "reiner_13" 
    }
]

var moderatorData = [
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
        "role": "teacher",
        "email": "luzier@sru.edu",
        "classes": ""
    },
    {
        "first": "Brenden",
        "last": "Stilwell",
        "role": "moderator",
        "email": "luzier@sru.edu",
        "classes": "N/A"
    },
]

$(document).ready(function () {
    renderReportTable();
    renderModeratorTable();
    renderUserTable();

    showTab('bugReports'); //show bug reports tab first

    $('#reportModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = reportTable.row(button.data('rowindex')).data();

        $('#date').html(row.initialDate);
        $('#name').html(row.first + ' ' + row.last);
        $('#email').html(row.email);
        $('#date').html(row.initialDate);
        $('#description').html(row.description);
        $('#modified').html(row.modifiedDate + ' ' + row.modifiedUser);
        $('#status').html(row.status);

        //clearing comment field in submodal
        $('#resolution').val('');

        if (row.status == 'Closed') {
            $('#closeReportButton').hide();
            $('#commentReportButton').show();
        }

        else if (row.status == 'Open'){
            $('#commentReportButton').hide();
            $('#closeReportButton').show();
        }

        else {
            $('#closeReportButton, #commentReportButton').hide();
        }
    });

    $('#closeReportButton').on('click', function() {
        bootstrapAlert('success', 'Report closed successfully');
        $('#reportModal').modal('hide');
    });

    $('#resolveReportButton').on('click', function() {
        bootstrapAlert('success', 'Report resolved successfully');
        $('#reportModal, #reportSubModal').modal('hide');
    });
})

function renderReportTable() {
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
                    var dropdown = '<div class="dropdown show">' +
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
                    else if (data === "Closed") {
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
}

function renderModeratorTable() {
    moderatorTable = $('#moderatorTable').DataTable({
        data: moderatorData,
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
                    var dropdown = '<div class="dropdown show">' +
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
                    else if (data === "Closed") {
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
                
                if ($('#moderatorTable thead tr.filters').length === 0) {
                    $('#moderatorTable thead').append('<tr class="filters"></tr>');
                }
                if (index === 0) {
                    $('#moderatorTable thead tr.filters').append('<th></th>');
                } 
                else {
                    let filterCell = $('<th><input type="text" class="form-control form-control-sm" placeholder="Filter ' + title + '" /></th>');
                    $('#moderatorTable thead tr.filters').append(filterCell);
                    $('input', filterCell).on('keyup change', function() {
                        if (column.search() !== this.value) {
                            column.search(this.value).draw();
                        }
                    });
                }
            });
            
            //disable sorting for search row
            $('#moderatorTable thead tr.filters th').addClass('sorting_disabled');
        }
    });

    //order table by date column
    moderatorTable.order([[1, 'asc']]).draw();
}

function showTab(tabName) {
    $('.tab-content').hide();
    $('.navbar-nav .nav-link').removeClass('active');
    $('#' + tabName).show();
    $('a[data-tab="' + tabName + '"]').addClass('active');
}