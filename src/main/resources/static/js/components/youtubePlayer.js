let player;
let playerLock = false;

$(document).ready(function () {
    $('#playButton').click(function (event) {
        event.preventDefault();
        if (player.getPlayerState() !== YT.PlayerState.PLAYING && !playerLock) {
            player.playVideo();
        }
        else {
            player.pauseVideo();
        }
    });

    $('#volumeRange').change(function () {
        let rangeValue = $(this).val();
        player.setVolume(rangeValue);
        $('#volumeLabel').html(`&#128266; ${rangeValue}`);
    });

    document.addEventListener('setNewSong', function (e) {
        playerLock = false;
        if (e.detail.songLength == 0) {
            player.cueVideoById(e.detail.url, e.detail.timestamp);
        }
        else {
            player.cueVideoById({
                videoId: e.detail.url,
                startSeconds: e.detail.timestamp,
                endSeconds: e.detail.timestamp + e.detail.songLength
            });
        }
    });

    document.addEventListener('attemptLimit', function() {
        playerLock = true;
    })

    loadYouTubeAPI();
    waitForYouTubeAPI();

});

function loadYouTubeAPI() {
    if (typeof YT === 'undefined' || typeof YT.Player === 'undefined') {
        console.log("Loading YouTube API...");
        var tag = document.createElement('script');
        tag.src = "https://www.youtube.com/iframe_api";
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
    } 
    else {
        initPlayer();
    }
}

function initPlayer() {
    console.log("YouTube API Ready");
    if (player) return;
    
    player = new YT.Player('player', {
        height: 0,
        width: 0,
        videoId: '89k1l3-Ruxg',
        playerVars: {
            playsinline: 1,
            autoplay: 0,
            controls: 0
        },
        events: {
            onReady: onPlayerReady,
            onStateChange: onPlayerStateChange
        }
    });
}

function onPlayerReady(event) {
    document.dispatchEvent(new Event('playerReady'));
}

function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.PLAYING) {
        $('#playSymbol').removeClass("fa-play").addClass("fa-pause");
    }
    else {
        $('#playSymbol').removeClass("fa-pause").addClass("fa-play");
    }

    if (event.data == YT.PlayerState.ENDED) {
        document.dispatchEvent(new Event('endSong'));
    }
}

function waitForYouTubeAPI() {
    if (typeof YT !== 'undefined' && typeof YT.Player !== 'undefined') {
        initPlayer();
    }
    else {
        console.log("Waiting for YouTube API...");
        setTimeout(waitForYouTubeAPI, 500);
    }
}
