$(document).ready(function() {
    let playlistData = {
        playlistName: "",
        class: "",
        tracks: []
    };

    // Dummy class options JSON
    let classOptions = ["Class A", "Class B", "Class C"];

    // Function to populate the class dropdown
    function loadClassOptions() {
        let classSelect = $("#classSelect");
        classSelect.empty(); // Clear existing options
        classSelect.append('<option value="" selected disabled>Select a class</option>'); // Default option
        
        classOptions.forEach(className => {
            classSelect.append(`<option value="${className}">${className}</option>`);
        });
    }

    // Call function to load class options on page load
    loadClassOptions();

    // Function to validate the last track card before adding a new one
    function validateLastTrack() {
        let lastTrack = $('#cardContainer .track-card:last');
        let isValid = true;

        lastTrack.find('input').each(function() {
            if (!$(this).val().trim()) {
                isValid = false;
                $(this).addClass("is-invalid"); // Add red border if empty
            } else {
                $(this).removeClass("is-invalid"); // Remove red border if filled
            }
        });

        if (!isValid) {
            bootstrapAlert('danger', 'Please fill all fields before adding a new track.');
        }

        return isValid;
    }

    // Function to validate all track cards before saving
    function validateAllTracks() {
        let isValid = true;

        $('.track-card').each(function() {
            $(this).find('input').each(function() {
                if (!$(this).val().trim()) {
                    isValid = false;
                    $(this).addClass("is-invalid"); // Add red border if empty
                } else {
                    $(this).removeClass("is-invalid"); // Remove red border if filled
                }
            });
        });

        if (!isValid) {
            bootstrapAlert('danger', 'Please fill all fields before saving the playlist.');
        }

        return isValid;
    }

    // Function to add a new track card (only if last card is valid)
    function addTrackCard() {
        if (!validateLastTrack()) {
            return; // Stop adding if the last track is not valid
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
                    <button class="btn btn-danger remove-card mt-2">Remove</button>
                </div>
            </div>`;

        $('#cardContainer').append(newCard);
        updateAddButton();
    }

    // Function to update "Add Track" button position
    function updateAddButton() {
        $('#cardContainer .add-card').remove(); // Remove existing add buttons

        // Add button to only the last card
        $('#cardContainer .track-card:last .card-body').append(`
            <button class="btn btn-primary mt-2 add-card">Add Track</button>
        `);
    }

    // Event listener for dynamically added "Add Track" button
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

    // Function to save playlist data as JSON
    $('#savePlaylist').click(function() {
        // Ensure all track fields are filled before saving
        if (!validateAllTracks()) {
            return;
        }

        let tracks = [];

        $('.track-card').each(function() {
            let track = {
                name: $(this).find('.track-name').val().trim(),
                youtubeLink: $(this).find('.track-link').val().trim(),
                composer: $(this).find('.track-composer').val().trim(),
                year: $(this).find('.track-year').val().trim()
            };

            tracks.push(track);
        });

        // Store playlist name and class
        playlistData.playlistName = $('#playlistName').val().trim();
        playlistData.class = $('#classSelect').val();
        playlistData.tracks = tracks;

        // Check if playlist is valid
        if (!playlistData.playlistName || !playlistData.class) {
            bootstrapAlert('danger', 'Please enter a playlist name and select a class.');
            return;
        }

        // Convert to JSON and log
        let playlistJSON = JSON.stringify(playlistData, null, 4);
        console.log("Saved Playlist:", playlistJSON);

        // Store JSON in local storage (or send via AJAX later)
        localStorage.setItem("savedPlaylist", playlistJSON);

        bootstrapAlert('success', 'Playlist saved successfully!');
        window.location.href = '/teacher/Playlists';
    });

    // Ensure the first card has the Add button on page load
    updateAddButton();
});
