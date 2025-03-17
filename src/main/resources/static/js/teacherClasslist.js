let classData = [
    {
        "classID": "01",
        "className": "Music Theory Basics",
        "studentCount": "12",
        "playlistCount": "3"
    },
    {
        "classID": "02",
        "className": "Jazz Improvisation",
        "studentCount": "18",
        "playlistCount": "5"
    },
    {
        "classID": "03",
        "className": "Classical Compositions",
        "studentCount": "20",
        "playlistCount": "4"
    },
    {
        "classID": "04",
        "className": "Rock History 101",
        "studentCount": "25",
        "playlistCount": "6"
    },
    {
        "classID": "05",
        "className": "Electronic Music Production",
        "studentCount": "15",
        "playlistCount": "8"
    },
    {
        "classID": "06",
        "className": "Hip-Hop & Rap Culture",
        "studentCount": "22",
        "playlistCount": "7"
    },
    {
        "classID": "07",
        "className": "Orchestration Techniques",
        "studentCount": "14",
        "playlistCount": "3"
    },
    {
        "classID": "08",
        "className": "Film Score Analysis",
        "studentCount": "19",
        "playlistCount": "5"
    },
    {
        "classID": "09",
        "className": "Choir & Vocal Harmony",
        "studentCount": "30",
        "playlistCount": "6"
    },
    {
        "classID": "10",
        "className": "Blues Guitar Essentials",
        "studentCount": "17",
        "playlistCount": "4"
    },
    {
        "classID": "11",
        "className": "Music Business & Marketing",
        "studentCount": "21",
        "playlistCount": "9"
    },
    {
        "classID": "12",
        "className": "Pop Songwriting",
        "studentCount": "16",
        "playlistCount": "5"
    },
    {
        "classID": "13",
        "className": "Sound Engineering 101",
        "studentCount": "23",
        "playlistCount": "8"
    },
    {
        "classID": "14",
        "className": "Folk & Acoustic Styles",
        "studentCount": "13",
        "playlistCount": "4"
    },
    {
        "classID": "15",
        "className": "Reggae Rhythms & Culture",
        "studentCount": "20",
        "playlistCount": "5"
    }
];

$(document).ready(function () {
    let classTable = $('#classTable').DataTable({
        data: classData,
        // ajax:{
        //     // url: http::/localhost:8080/api/
        //     // type: 'GET',
        //     // dataSrc: '',
        //     // dataType: 'json',
        //     // data: classData,
        //     // timeout: '0',
        // },
        dom: "<'row'<'col-sm-12 col-md-12 text-end'B>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>><'#bottomLink'>",
        scrollCollapse: false,
        scrollY: '50vh', // Use viewport percentage for responsive height
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
                render: function(data, type, row, meta) {
                    var dropdown = '<div class="dropdown show">' +
                        '<a class="btn-sm btn btn-info" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="fa fa-wrench" aria-hidden="true"></span></a>' +
                        '<div class="dropdown-menu aria-labelledby="dropdownMenuLink">' +
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#classStudentsModal">View Class Students</a>' + 
                        '<a class="dropdown-item" href="#" data-rowindex ="' + meta.row + '" data-bs-toggle="modal" data-bs-target="#classPlaylistsModal">View Class Playlists</a>' + 
                        '</div></div>';
                    return dropdown;
                },
                orderable: false,
                width: "1 em"
            },
            { data: "classID", class: "charcolumn", width: "2 rem"},
            { data: "className", class: "charcolumn", width: "3 rem"},
            { data: "studentCount", class: "charcolumn", width: "3 rem"},
            { data: "playlistCount", class: "charcolumn", width: "1 rem"},
        ],
        drawCallback: function() {
            $('.dt-paging-button.current').attr('style', 'color: white !important'); //inline css styling used as last resort for highest priority selector
        }
    });

    //add a text input to each header cell
    $('#classTable thead').append('<tr class="searchRow"></tr>');
    $('#classTable').DataTable().columns().every(function (i) {
        if (i === 0) {
            $(this.header()).closest('thead').children('.searchRow').append('<th></th>');
        }
        else {
            var that = this;
            let title = $(this.header()).text();
            $(this.header()).closest('thead').children('.searchRow').append('<th><input class="form-control form-control-sm m-1 w-100 sorting_disabled" type="search" placeholder="Filter ' + title + '" /></th>');
            $('input', $('#classTable thead tr.searchRow th').eq(i)).on('keyup change clear search', function() {
                if (that.search() !== this.value) {
                    that.search(this.value).draw();
                }
            });
        }
    });

    //order table by status column
    classTable.order([[4, 'asc']]).draw();
})