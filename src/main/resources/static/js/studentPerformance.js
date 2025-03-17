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
        scrollY: '50vh',
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
        ] ,
        drawCallback: function() {
            $('.dt-paging-button.current').attr('style', 'color: white !important'); //inline css styling used as last resort for highest priority selector
        }
    });

    //add a text input to each header cell
    $('#songTable thead').append('<tr class="searchRow"></tr>');
    $('#songTable').DataTable().columns().every(function (i) {
        if (i === 0) {
            $(this.header()).closest('thead').children('.searchRow').append('<th></th>');
        }
        else {
            let that = this;
            let title = $(this.header()).text();
            $(this.header()).closest('thead').children('.searchRow').append('<th><input class="form-control form-control-sm m-1 w-100 sorting_disabled" type="search" placeholder="Filter ' + title + '" /></th>');
            $('input', $('#songTable thead tr.searchRow th').eq(i)).on('keyup change clear search', function() {
                if (that.search() !== this.value) {
                    that.search(this.value).draw();
                }
            });
        }
    });

    //order table by status column
    songTable.order([[4, 'asc']]).draw();

    $('#previewModal').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget);
        let row = songTable.row(button.data('rowindex')).data();

        $('#name').html(row.name);
        $('#composer').html(row.composer);
        $('#year').html(row.year);
    });
});