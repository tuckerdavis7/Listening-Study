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
        "status": "Open",
        "modifiedDate": "03/15/2025 13:50:55",
        "modifiedUser": "reiner_13" 
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
                    var dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#reportModal">View/Edit Report Details</a>' + 
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#resolveModal">Resolve Report</a>' + 
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
                    let pillClass = (data === "Open") ? "bg-warning" : "bg-success";
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
        },
        drawCallback: function() {
            $('.dt-paging-button.current').attr('style', 'color: white !important'); //inline css styling used as last resort for highest priority selector
        }
    });

    //order table by status column
    reportTable.order([[4, 'asc']]).draw();

    $('#reportModal').on('show.bs.modal', function (event) {
        console.log('hello');
    });
})