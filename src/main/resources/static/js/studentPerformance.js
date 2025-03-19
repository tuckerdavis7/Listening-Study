var songData = [
    {
        "playlist": "Classical 1",
        "name": "Fur Elise",
        "timesCorrect": "2",
        "timesQuizzed": "2",
        "averageScore": "100%",
        "composer": "Ludwig van Beethoven",
        "year": "1810",
        "url": "https://youtu.be/q9bU12gXUyM?si=gMz2qDgpwy6ZtolG",
        "timestamp": "0:20",
        "class": "MUSIC 101",
        "id": "0001",
    },
    {
        "playlist": "Classical 1",
        "name": "Symphony No. 40",
        "timesCorrect": "2",
        "timesQuizzed": "2",
        "averageScore": "100%",
        "composer": "Wolgang Amadeus Mozart",
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
    let songColumns = [
        {
            class: "viewColumn",
            data: null,
            render: function(data, type, row, meta) {
                var previewButton = '<a class="btn-sm btn btn-info" href="#" role="button"><span class="fa fa-eye" aria-hidden="true" data-bs-toggle="modal" data-bs-target="#previewModal"></span></a>';
                return previewButton;
            },
            orderable: false,
            width: "1 em"
        },
        { data: "name", class: "charcolumn", width: "3 rem" },
        { data: "playlist", class: "charcolumn", width: "3 rem" },
        { data: "class", class: "charcolumn", width: "3 rem" },
        { data: "timesCorrect", class: "charcolumn", width: "3 rem" },
        { data: "timesQuizzed", class: "charcolumn", width: "3 rem" },
        { data: "averageScore", class: "charcolumn", width: "3 rem" }
    ]

    songTable = initializeDataTableWithFilters('#songTable', songData, songColumns, [6, 'asc']);

    $('#previewModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();

        $('#name').html(row.name);
        $('#composer').html(row.composer);
        $('#year').html(row.year);
    });
});