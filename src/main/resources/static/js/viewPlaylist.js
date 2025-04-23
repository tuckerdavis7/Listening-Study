const pathArr = location.href.split('/');
const userType = pathArr[pathArr.length - 3];
const playlistID = pathArr[pathArr.length - 1];

let songTable;
let songColumns;
let masterSongData;
let deleteSongID = -1;

$(document).ready(function () {
    updateSongTable();
    songColumns = (userType == "teacher") ? [
        {
            class: "wrenchColumn",
            data: null,
            render: function(data, type, row, meta) {
                var dropdown = '<div class="dropdown show">' +
                    '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                    '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                    '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#editSongModal">Edit Song</a>' + 
                    '<a class="dropdown-item text-danger fw-bold" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#removeConfirmation">Delete Song</a>' + 
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
                var previewButton = '<a class="btn-sm btn btn-info" href="#" role="button"><span class="fa fa-play" aria-hidden="true" data-bs-toggle="modal" data-bs-target="#previewModal" data-rowindex="' + meta.row + '"></span></a>';
                return previewButton;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "name", class: "charcolumn", width: "3 rem" },
        { data: "composer", class: "charcolumn", width: "3 rem" },
        { data: "year", class: "charcolumn", width: "3 rem" },
        { data: "url", class: "charcolumn", width: "3 rem" },
        { data: "mrTimestamp", class: "charcolumn", width: "1 rem" },
        { data: "udTimestamp", class: "charcolumn", width: "1 rem" }
    ] : [
        {
            class: "previewColumn",
            data: null,
            render: function(data, type, row, meta) {
                var previewButton = '<a class="btn-sm btn btn-info" href="#" role="button"><span class="fa fa-play" aria-hidden="true" data-bs-toggle="modal" data-bs-target="#previewModal" data-rowindex="' + meta.row + '"></span></a>';
                return previewButton;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "name", class: "charcolumn", width: "3 rem" },
        { data: "composer", class: "charcolumn", width: "3 rem" },
        { data: "year", class: "charcolumn", width: "3 rem" },
        { data: "url", class: "charcolumn", width: "3 rem" }
    ]
    

    let rowRemove;
    
    $('#deleteSong').click(function() {
        const row = $(this).closest('tr');
        rowRemove = row;

        $('#removeConfirmation').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        if(rowRemove) {
            songTable.row(rowRemove).remove().draw();
            $('#removeConfirmation').modal('hide');
        }
    });

    let rowEdit;
    
    $('#editSong').click(function() {
        const row = $(this).closest('tr');
        rowEdit = row;

        $('#editSongModal').modal('show');
    });

    $('#removeConfirmationButton').on('click', function() {
        if(rowEdit) {
            songTable.row(rowEdit).remove().draw();
            $('#editSongModal').modal('hide');
        }
    });

    $('#previewModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();

        $('#name').html(row.name);
        $('#composer').html(row.composer);
        $('#year').html(row.year);
        $('#url').html(row.url);
        
        let splitURL = row.url.split("/");
        
        let setNewSong = new CustomEvent('setNewSong', {
            detail: {
                "url": splitURL[splitURL.length - 1],
                "timestamp": 0,
                "songLength": 60
            }
        });
        document.dispatchEvent(setNewSong);
    });

    $('#addSongForm').submit(function(event) {
        event.preventDefault();
        $("#confirmAddSongBtn").prop("disabled", true);
        $("#addSongSpinner").removeClass("d-none");

        let addSongForm = $('#addSongForm').serializeArray();
        let addSongData = {};
        addSongForm.forEach(element => {
            addSongData[element.name] = element.value;
        });
        addSongData['playlistID'] = playlistID;

        $.ajax({
            data: JSON.stringify(addSongData),
            url: 'http://localhost:8080/api/teachersong',
            type: 'POST',
            contentType: 'application/json',
            success: function(data) {
                let responseData = JSON.parse(data);
                console.log(responseData);
                if (responseData == "success") {
                    console.log("Success for add song");
                    bootstrapAlert('success', "Song Added");

                    $('#addSongModal').modal('hide'); 
                    $('#addSongForm')[0].reset();
                    
                    updateSongTable();
                    window.location.reload();
                }
                else {
                    console.log("Failed to add song")
                    bootstrapAlert('danger', 'Invalid song information.');
                }
                $("#addSongSpinner").addClass("d-none");
                $("#confirmAddSongBtn").prop("disabled", false);
            },
            error: function(xhr, status, error) {
                bootstrapAlert('danger', 'Error adding song: ' + error);
            }
        });
        
    });

    $('#editSongModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();

        $('#editSongName').val(row.name);
        $('#editComposer').val(row.composer);
        $('#editYear').val(row.year);
        $('#editURL').val(row.url);
        if (row.udTimestamp == "None")
            $('#editTimestamp').val('');
        else {
            let timestamp = row.udTimestamp;
            $('#editTimestamp').val(timestamp);
        }

    });

    $('#editSongForm').submit(function (event) {
        event.preventDefault();
        $("#confirmEditSongBtn").prop("disabled", true);
        $("#editSongSpinner").removeClass("d-none");

        let editSongForm = $('#editSongForm').serializeArray();
        let editSongData = {};
        editSongForm.forEach(element => {
            editSongData[element.name] = element.value;
        });
        editSongData['playlistID'] = parseInt(playlistID);
        editSongData['songID'] = getSongID(editSongData['url']);
        console.log(editSongData);

        $.ajax({
            data: JSON.stringify(editSongData),
            url: 'http://localhost:8080/api/teachersong',
            type: 'PATCH',
            contentType: 'application/json',
            success: function(data) {
                let responseData = JSON.parse(data);
                console.log(responseData);
                if (responseData == "success") {
                    console.log("Success for edit song");
                    bootstrapAlert('success', "Song Edited");

                    $('#editSongModal').modal('hide'); 
                    $('#editSongForm')[0].reset();
                    
                    updateSongTable();
                    window.location.reload();
                }
                else {
                    console.log("Failed to edit song")
                    bootstrapAlert('danger', 'Invalid song information.');
                }
                $("#editSongSpinner").addClass("d-none");
                $("#confirmEditSongBtn").prop("disabled", false);
            },
            error: function(xhr, status, error) {
                console.log(error);
                bootstrapAlert('danger', 'Error editing song: ' + error);
                $("#editSongSpinner").addClass("d-none");
                $("#confirmEditSongBtn").prop("disabled", false);
            }
        });
    });

    $('#removeConfirmation').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();
        deleteSongID = row["songID"];
    });

    $('#removeConfirmation').on('hide.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();
        deleteSongID = -1;
    });

    $('#removeConfirmationButton').click(function () {
        let deleteSongData = {};
        deleteSongData['playlistID'] = parseInt(playlistID);
        deleteSongData['songID'] = deleteSongID;
        console.log(deleteSongData);

        $.ajax({
            data: JSON.stringify(deleteSongData),
            url: 'http://localhost:8080/api/teachersong',
            type: 'DELETE',
            contentType: 'application/json',
            success: function(data) {
                let responseData = JSON.parse(data);
                console.log(responseData);
                if (responseData == "success") {
                    console.log("Success for delete song");
                    bootstrapAlert('success', "Song Deleted");

                    $('#removeConfirmation').modal('hide'); 
                    
                    updateSongTable();
                    window.location.reload();
                }
                else {
                    console.log("Failed to delete song")
                    bootstrapAlert('danger', 'Failed to delete song.');
                }
            },
            error: function(xhr, status, error) {
                console.log(error);
                bootstrapAlert('danger', 'Error deleting song: ' + error);
                $("#removeConfirmation").addClass("d-none");
            }
        });
    });
});

function updateSongTable() {
    $.ajax({
        url: `http://localhost:8080/api/teachersong?playlistID=${playlistID}`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            data.data.forEach(element => {
                element.url = "https://youtu.be/" + element.url;
                element.mrTimestamp = element.mrTimestamp == -1 ? "None" : getTimestamp(element.mrTimestamp);
                element.udTimestamp = element.udTimestamp == -1 ? "None" : getTimestamp(element.udTimestamp);
            });
            masterSongData = data.data;
            console.log(masterSongData);
            
            if (!songTable)
                songTable = initializeDataTableWithFilters('#songTable', masterSongData, songColumns, [2, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });

}

function getTimestamp(totalSeconds) {
    let hours = Math.floor(totalSeconds / 3600);
    totalSeconds %= 3600;
    let minutes = Math.floor(totalSeconds / 60);
    let seconds = totalSeconds % 60;
    let timestamp;
    if (hours != 0)
        timestamp = `${hours}:${minutes}:${seconds}`;
    else
        timestamp = `${minutes}:${seconds}`;
    return timestamp;
}

function getSongID(url) {
    console.log(masterSongData);
    for (let i = 0; i < masterSongData.length; i++) {
        if (masterSongData[i].url == url)
            return masterSongData[i].songID;
    }
    return -1;
}