let player;
let playing = false;

$(document).ready(function() {
    $('#playButton').click(function(event) {
        event.preventDefault();
        if (!playing)
            player.playVideo()
        else
            player.pauseVideo()
    })

    $('#volumeRange').change(function(event) {
        let rangeValue = $(this).val();
        player.setVolume(rangeValue);
        $('#volumeLabel').html(`&#128266; ${rangeValue}`);
    })
});

function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
        height: 0,
        width: 0,
        videoId: '89k1l3-Ruxg',
        playerVars: {
            playsinline: 1,
            autoPlay: 0,
            controls: 0
        },
        events: {
            onReady: onPlayerReady,
            onStateChange: onPlayerStateChange
        }
    });
}

function onPlayerReady(event) {
    // might need later
}

function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING) {
        playing = true;
        $('#playButton').html('&#9208;'); // pause symbol
    }
    else {
        playing = false;
        $('#playButton').html('&#9654;'); // play symbol
    }
}