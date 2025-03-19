$(document).ready(function() {
    let playlistData = {
        playlistName: "",
        class: "",
        tracks: []
    };

    // Function to add a new track card
    $('#addCard').click(function() {
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
    });

    // Function to remove a track card (at least one must remain)
    $(document).on('click', '.remove-card', function() {
        if ($('.track-card').length > 1) {
            $(this).closest('.track-card').remove();
        } else {
            alert('At least one track must remain.');
        }
    });

    // Function to save playlist data as JSON
    $('#savePlaylist').click(function() {
        let tracks = [];
        
        $('.track-card').each(function() {
            let track = {
                name: $(this).find('.track-name').val().trim(),
                youtubeLink: $(this).find('.track-link').val().trim(),
                composer: $(this).find('.track-composer').val().trim(),
                year: $(this).find('.track-year').val().trim()
            };

            // Ensure track has valid data before adding
            if (track.name && track.youtubeLink && track.composer && track.year) {
                tracks.push(track);
            }
        });

        // Store playlist name and class
        playlistData.playlistName = $('#playlistName').val().trim();
        playlistData.class = $('#classSelect').val();
        playlistData.tracks = tracks;

        // Check if playlist is valid
        if (!playlistData.playlistName || !playlistData.class) {
            alert("Please enter a playlist name and select a class.");
            return;
        }

        if (tracks.length === 0) {
            alert("Please add at least one valid track.");
            return;
        }

        // Convert to JSON and log
        let playlistJSON = JSON.stringify(playlistData, null, 4);
        console.log("Saved Playlist:", playlistJSON);

        // Store JSON in local storage (or send via AJAX later)
        localStorage.setItem("savedPlaylist", playlistJSON);

        alert("Playlist saved successfully!");
    });
});
