$(document).ready(async function() {
    let playlistData = {
        playlistName: "",
        classID: "",
        songData: []
    };

    //dummy class options JSON
    //let classOptions = ["Class A", "Class B", "Class C"];

    let classOptions = await getClassOptions();
    populateClassOptions(classOptions);

    //validate the last track card before adding a new one
    function validateLastTrack() {
        let lastTrack = $('#cardContainer .track-card:last');
        let isValid = true;

        lastTrack.find('input').each(function() {
            if ($(this).hasClass('track-timestamp')) {
                return;
            }
            if (!$(this).val().trim()) {
                isValid = false;
                $(this).addClass("is-invalid");
            } else {
                $(this).removeClass("is-invalid");
            }
        });

        if (!isValid) {
            bootstrapAlert('danger', 'Please fill all fields before adding a new track.');
        }

        return isValid;
    }

    //function to validate all track cards before saving
    function validateAllTracks() {
        let isValid = true;

        $('.track-card').each(function() {
            $(this).find('input').each(function() {
                if (!$(this).val().trim() && !$(this).hasClass('track-timestamp')) {
                    isValid = false;
                    $(this).addClass("is-invalid");
                } else {
                    $(this).removeClass("is-invalid");
                }
            });
        });

        if (!isValid) {
            bootstrapAlert('danger', 'Please fill all fields before saving the playlist.');
        }

        return isValid;
    }

    //function to add a new track card (only if last card is valid)
    function addTrackCard() {
        if (!validateLastTrack()) {
            return;
        }

        let newCard = `
            <div class="card shadow p-3 mb-3 track-card">
                <div class="card-body">
                    <h5 class="card-title">Track Details</h5>
                    <div class="mb-2">
                        <label class="form-label">Name</label>
                        <input type="text" class="form-control track-name" placeholder="Enter track name">
                    </div>
                    <div class="mb-2">
                        <label class="form-label">YouTube Link</label>
                        <input type="url" class="form-control track-link" placeholder="Enter YouTube link">
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Composer</label>
                        <input type="text" class="form-control track-composer" placeholder="Enter composer name">
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Year</label>
                        <input type="number" class="form-control track-year" min="1900" max="2099" placeholder="Enter year">
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Timestamp</label>
                        <input type="text" class="form-control track-timestamp" pattern="^(?:\d{1,2}:)?[0-5]?\d:[0-5]\d$" placeholder="Enter timestamp">
                        <small class="form-text text-muted">Optional. Please use the following timestamp format: HH:MM:SS or MM:SS</small>
                    </div>
                    <button class="btn btn-danger remove-card mt-2">Remove</button>
                </div>
            </div>`;

        $('#cardContainer').append(newCard);
        updateAddButton();
    }

    //function to update "Add Song" button position
    function updateAddButton() {
        $('#cardContainer .add-card').remove(); // Remove existing add buttons

        //add button to only the last card
        $('#cardContainer .track-card:last .card-body').append(`
            <button class="btn btn-secondary mt-2 add-card">Add Song</button>
        `);
    }

    $(document).on('click', '.add-card', function() {
        addTrackCard();
    });

    // Function to remove a track card (at least one must remain)
    $(document).on('click', '.remove-card', function() {
        if ($('.track-card').length > 1) {
            $(this).closest('.track-card').remove();
            updateAddButton();
        } else {
            bootstrapAlert('danger', 'At least one track must remain.');
        }
    });

    //function to save playlist data as JSON
    $('#savePlaylist').click(async function() {
        // Ensure all track fields are filled before saving
        if (!validateAllTracks()) {
            return;
        }

        $('.track-card').each(function() {
            let track = {
                name: $(this).find('.track-name').val().trim(),
                youtubeLink: $(this).find('.track-link').val().trim(),
                composer: $(this).find('.track-composer').val().trim(),
                year: $(this).find('.track-year').val().trim(),
                timestamp: $(this).find('.track-timestamp').val().trim()
            };

            playlistData.songData.push(track);
        });

        //store playlist name and class
        playlistData.playlistName = $('#playlistName').val().trim();
        playlistData.classID = $('#classSelect').val();

        //check if playlist is valid
        if (!playlistData.playlistName || !playlistData.classID) {
            bootstrapAlert('danger', 'Please enter a playlist name and select a class.');
            playlistData.songData = [];
            return;
        }

        await createPlaylist(playlistData);

        bootstrapAlert('success', 'Playlist saved successfully!');
    });

    //ensure the first card has the Add button on page load
    updateAddButton();
});

async function getClassOptions() {
    try {
        const response = await $.ajax({
            url: `http://localhost:8080/api/createplaylist`,
            type: 'GET',
            dataType: 'json'
        });
        return response.data;
    }
    catch (error) {
        console.error("Error fetching data from the API:", error);
        bootstrapAlert("danger", "Error fetching class data.");
        return [];
    }
}

//function to populate the class dropdown
function populateClassOptions(classOptions) {
    let classSelect = $("#classSelect");
    classSelect.empty(); // Clear existing options
    classSelect.append('<option value="" selected disabled>Select a class</option>'); // Default option
    
    for (let i = 0; i < classOptions.length; i++) {
        classSelect.append(`<option value="${classOptions[i].classID}">${classOptions[i].className}</option>`);
    }
}

async function createPlaylist(playlistData) {
    console.log(playlistData);
    try {
        const response = await $.ajax({
            data: JSON.stringify(playlistData),
            url: 'http://localhost:8080/api/createplaylist',
            type: 'POST',
            contentType: 'application/json'
        });
    }
    catch (error) {
        console.error("Error creating playlist:", error);
        bootstrapAlert("danger", "Error creating playlist");
        playlistData.songData = [];
    }
}