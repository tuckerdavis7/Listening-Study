var songData = [
    {
        "playlist": "Classical 1",
        "name": "Fur Elise",
        "composer": "Ludwig van Beethoven",
        "year": "1810",
        "url": "https://youtu.be/q9bU12gXUyM?si=gMz2qDgpwy6ZtolG",
        "timestamp": "0:20",
        "class": "MUSIC 101",
        "id": "0001"
    },
    {
        "playlist": "Classical 1",
        "name": "Symphony No. 40",
        "composer": "Wolfgang Amadeus Mozart",
        "year": "1788",
        "url": "https://youtu.be/JTc1mDieQI8?si=1ggnfrLLopftYWt7",
        "timestamp": "0:44",
        "class": "MUSIC 101",
        "id": "0002"
    }
];

const pathArr = location.href.split('/');
const userType = pathArr[pathArr.length - 3];

$(document).ready(function () {
    songTable = $('#songTable').DataTable({
        data: songData,
        // ajax:{
        //     // url: http::/localhost:8080/api/
        //     // type: 'GET',
        //     // dataSrc: '',
        //     // dataType: 'json',
        //     // data: songData,
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
        columns: (userType == "teacher") ? [
            {
                class: "wrenchColumn",
                data: null,
                render: function(data, type, row, meta) {
                    var dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="editModal">Edit Song</a>' + 
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="deleteModal">Delete Song</a>' + 
                        '</div></div>';
                    return dropdown;
                },
                orderable: false,
                width: "1 em"
            },
            {
                class: "previewColumn",
                data: null,
                render: function(data, type, row, meta) {
                    var previewButton = '<a class="btn-sm btn btn-info" href="#" role="button"><span class="fa fa-play" aria-hidden="true" data-bs-toggle="modal" data-bs-target="#previewModal"></span></a>';
                    return previewButton;
                },
                orderable: false,
                width: "1 em"
            },
            { data: "name", class: "charcolumn", width: "3 rem" },
            { data: "composer", class: "charcolumn", width: "3 rem" },
            { data: "year", class: "charcolumn", width: "3 rem" },
            { data: "url", class: "charcolumn", width: "3 rem" }
        ] : [
            {
                class: "previewColumn",
                data: null,
                render: function(data, type, row, meta) {
                    var previewButton = '<a class="btn-sm btn btn-info" href="#" role="button"><span class="fa fa-play" aria-hidden="true" data-bs-toggle="modal" data-bs-target="#previewModal"></span></a>';
                    return previewButton;
                },
                orderable: false,
                width: "1 em"
            },
            { data: "name", class: "charcolumn", width: "3 rem" },
            { data: "composer", class: "charcolumn", width: "3 rem" },
            { data: "year", class: "charcolumn", width: "3 rem" },
            { data: "url", class: "charcolumn", width: "3 rem" }
        ],
        initComplete: function() {
            //create the search rows
            this.api().columns().every(function(index) {
                let column = this;
                let title = $(column.header()).text().trim();
                
                if ($('#songTable thead tr.filters').length === 0) {
                    $('#songTable thead').append('<tr class="filters"></tr>');
                }
                if (index === 0) {
                    $('#songTable thead tr.filters').append('<th></th>');
                } 
                else {
                    let filterCell = $('<th><input type="text" class="form-control form-control-sm" placeholder="Filter ' + title + '" /></th>');
                    $('#songTable thead tr.filters').append(filterCell);
                    $('input', filterCell).on('keyup change', function() {
                        if (column.search() !== this.value) {
                            column.search(this.value).draw();
                        }
                    });
                }
            });
            
            //disable sorting for search row
            $('#songTable thead tr.filters th').addClass('sorting_disabled');
        }
    });

    //order table by name column
    songTable.order([[1, 'asc']]).draw();

    $('#previewModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();

        $('#name').html(row.name);
        $('#composer').html(row.composer);
        $('#year').html(row.year);
        $('#url').html(row.url);
    });

    $('#confirmSongBtn').on('click', function () {
        let songName = $('#songName').val().trim();
        let songURL = $('#songURL').val().trim();
        let composer = $('#composer').val().trim();
        let year = $('#year').val().trim();

        if(!songName && !songURL && !composer && !year) {
            alert("Please fill in all fields.");
            return;
        }

        let newSong = {
            name: songName,
            composer: composer,
            year: year,
            url: songURL
        };

        songData.push(newSong);
        songTable.row.add(newSong).draw();
        $('#songName, #composer, #year, #songURL').val('');

        // Close the modal
        $('#addSongModal').modal('hide');
    });
});