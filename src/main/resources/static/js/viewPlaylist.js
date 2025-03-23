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

var songTable;

$(document).ready(function () {
    let songColumns = (userType == "teacher") ? [
        {
            class: "wrenchColumn",
            data: null,
            render: function(data, type, row, meta) {
                var dropdown = '<div class="dropdown show">' +
                    '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                    '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                    '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#editSongModal">Edit Song</a>' + 
                    '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#removeConfirmation">Delete Song</a>' + 
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
    ]
    
    songTable = initializeDataTableWithFilters('#songTable', songData, songColumns, [2, 'asc'], 10, [0,1]);

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
    });

    $('#confirmSongBtn').on('click', function () {
        bootstrapAlert('success', "Song Added");
        $('#addSongModal').modal('hide');
    });
});