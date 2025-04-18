const pathArr = location.href.split('/');
const userType = pathArr[pathArr.length - 3];
const playlistID = pathArr[pathArr.length - 1];

var songTable;
let songColumns;

$(document).ready(function () {
    getSongData();
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
            url: 'http://localhost:8080/api/teacher/songs',
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

        console.log(row);

        $('#editSongName').val(row.name);
        $('#editComposer').val(row.composer);
        $('#editYear').val(row.year);
        $('#editURL').val(row.url);
        $('#editTimestamp').val(row.udTimestamp);

    });
});

function getSongData() {
    $.ajax({
        url: `http://localhost:8080/api/teacher/songs?playlistID=${playlistID}`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            data.data.forEach(element => {
                element.url = "https://youtu.be/" + element.url;
            });
            songTable = initializeDataTableWithFilters('#songTable', data.data, songColumns, [2, 'asc'], 10);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching data from the API:", error);
        }
    });

}