// var playlistData = [
//     {
//         "name": "Classical 1",
//         "class": "MUSIC 101",
//         "playlistId": "0001"
//     },
//     {
//         "name": "Jazz 1",
//         "class": "MUSIC 102",
//         "playlistId": "0002"
//     }
// ];

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
                    '<a class="dropdown-item" id="renamePlaylist" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#renamePlaylistModal">Rename Playlist</a>' + 
                    '<a class="dropdown-item text-danger fw-bold" id="deletePlaylist" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#removeConfirmation">Delete Playlist</a>' + 
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
                var playlistId = row.playlistID;
                var viewButton = '<a class="btn-sm btn btn-info" href="./playlists/' + playlistId + '" role="button"><span class="fa fa-eye" aria-hidden="true"></span></a>';
                return viewButton;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "playlistID", class: "charcolumn", width: "3 rem" },
        { data: "playlistName", class: "charcolumn", width: "3 rem" },
        { data: "className", class: "charcolumn", width: "3 rem" }
    ] : [
        {
            class: "viewColumn",
            data: null,
            render: function(data, type, row, meta) {
                var playlistId = row.playlistID;
                var viewButton = '<a class="btn-sm btn btn-info" href="./playlists/' + playlistId + '" role="button"><span class="fa fa-eye" aria-hidden="true"></span></a>';
                return viewButton;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "playlistID", class: "charcolumn", width: "3 rem" },
        { data: "playlistName", class: "charcolumn", width: "3 rem" },
        { data: "className", class: "charcolumn", width: "3 rem" }
    ];

    $.ajax({
        url: `http://localhost:8080/api/teacherLibrary`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {                        
            
            if (!playlistTable)
                playlistTable = initializeDataTableWithFilters('#playlistTable', data.data, playlistColumns, [1, 'asc'], 10);
        
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });

    let ignoredColumns = (userType == "teacher") ? [0,1] : [0];

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

    $('#editPlaylistModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = playlistTable.row(button.data('rowindex')).data();

        console.log(row);

        $('#songName').val(row.name);
    });

    let rowRename;

    $(document).on('click', '#renamePlaylist', function() {
        const row = $(this).closest('tr');
        rowRename = row;

        $('#renamePlaylistModal').modal('show');
    });

    $('#renamePlaylistModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = playlistTable.row(button.data('rowindex')).data();

        console.log("Renaming:", row);

        $('#newPlaylistName').val(row.playlistName);
    });

    $('#renamePlaylistForm').submit(function(event) {
        event.preventDefault();
    
        const newPlaylistName = $('#newPlaylistName').val();
        const playlistId = rowRename.find('td').eq(0).text(); 
    
        $.ajax({
            url: 'http://localhost:8080/api/teacherLibrary',
            type: 'POST',
            data: {
                playlistID: playlistId,
                newName: newPlaylistName
            },
            success: function(response) {
                console.log("Rename success:", response);
                $('#renamePlaylistModal').modal('hide');
                playlistTable.ajax.reload();
                
            },
            error: function(xhr, status, error) {
                console.error("Error renaming playlist:", error);
            }
        });
    });
    

    
});