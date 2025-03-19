var playlistData = [
    {
        "name": "Classical 1",
        "class": "MUSIC 101",
        "playlistId": "0001"
    },
    {
        "name": "Jazz 1",
        "class": "MUSIC 102",
        "playlistId": "0002"
    }
];

const pathArr = location.href.split('/');
const userType = pathArr[pathArr.length - 2];

$(document).ready(function () {
    playlistTable = $('#playlistTable').DataTable({
        data: playlistData,
        // ajax:{
        //     // url: http::/localhost:8080/api/
        //     // type: 'GET',
        //     // dataSrc: '',
        //     // dataType: 'json',
        //     // data: playlistData,
        //     // timeout: '0',
        // },
        dom: "<'row'<'col-sm-12 col-md-12 text-end'B>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>><'#bottomLink'>",
        scrollCollapse: false,
        scrollY: '50vh',
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
                        '<a class="btn-sm btn btn-info" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" id="editPlaylist" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#editPlaylistModal">Edit Playlist</a>' + 
                        '<a class="dropdown-item" id="deletePlaylist" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#removeConfirmation">Delete Playlist</a>' + 
                        '</div></div>';
                    return dropdown;
                },
                orderable: false,
                width: "1 em"
            },
            {
                class: "viewColumn",
                data: null,
                render: function(data, type, row, meta) {
                    var playlistId = row.playlistId;
                    var viewButton = '<a class="btn-sm btn btn-info" href="./playlists/' + playlistId + '" role="button"><span class="fa fa-eye" aria-hidden="true"></span></a>';
                    return viewButton;
                },
                orderable: false,
                width: "1 em"
            },
            { data: "name", class: "charcolumn", width: "3 rem" },
            { data: "class", class: "charcolumn", width: "3 rem" }
        ] : [
            {
                class: "viewColumn",
                data: null,
                render: function(data, type, row, meta) {
                    var playlistId = row.playlistId;
                    var viewButton = '<a class="btn-sm btn btn-info" href="./playlists/' + playlistId + '" role="button"><span class="fa fa-eye" aria-hidden="true"></span></a>';
                    return viewButton;
                },
                orderable: false,
                width: "1 em"
            },
            { data: "name", class: "charcolumn", width: "3 rem" },
            { data: "class", class: "charcolumn", width: "3 rem" }
        ],
        drawCallback: function() {
            $('.dt-paging-button.current').attr('style', 'color: white !important'); //inline css styling used as last resort for highest priority selector
        }
    });

    //add a text input to each header cell
    $('#playlistsTable thead').append('<tr class="searchRow"></tr>');
    $('#playlistsTable').DataTable().columns().every(function (i) {
        if (i === 0) {
            $(this.header()).closest('thead').children('.searchRow').append('<th></th>');
        }
        else {
            let that = this;
            let title = $(this.header()).text();
            $(this.header()).closest('thead').children('.searchRow').append('<th><input class="form-control form-control-sm m-1 w-100 sorting_disabled" type="search" placeholder="Filter ' + title + '" /></th>');
            $('input', $('#playlistsTable thead tr.searchRow th').eq(i)).on('keyup change clear search', function() {
                if (that.search() !== this.value) {
                    that.search(this.value).draw();
                }
            });
        }
    });

    let rowRemove;
    
    $('#deletePlaylist').click(function() {
        const row = $(this).closest('tr');
        rowRemove = row;

        $('#removeConfirmation').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        if(rowRemove) {
            playlistTable.row(rowRemove).remove().draw();
            $('#removeConfirmation').modal('hide');
        }
    });

    let rowEdit;
    
    $('#editPlaylist').click(function() {
        const row = $(this).closest('tr');
        rowEdit = row;

        $('#editPlaylistModal').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        if(rowEdit) {
            playlistTable.row(rowEdit).remove().draw();
            $('#editPlaylistModal').modal('hide');
        }
    });

    //order table by status column
    playlistTable.order([[2, 'asc']]).draw();
})