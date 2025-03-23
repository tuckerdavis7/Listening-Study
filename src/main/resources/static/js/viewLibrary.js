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

var playlistTable;

$(document).ready(function () {

    let playlistColumns = (userType == "teacher") ? [
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
    ];

    playlistTable = initializeDataTableWithFilters('#playlistTable', playlistData, playlistColumns, [2, 'asc'], 10, [0,1]);

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

    $('#createPlaylistButton').click(function() {
        window.location.href = "/teacher/createPlaylist";
    });
});