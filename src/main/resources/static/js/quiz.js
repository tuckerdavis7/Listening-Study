let playlistData = [
    {
        "playlist": "Classical 1",
        "name": "Fur Elise",
        "composer": "Ludwig van Beethoven",
        "year": "1810",
        "url": "https://youtu.be/q9bU12gXUyM?si=gMz2qDgpwy6ZtolG",
        "timestamp": 160,
        "class": "MUSIC 101",
        "id": "0001"
    },
    {
        "playlist": "Classical 1",
        "name": "Symphony No. 40",
        "composer": "Wolfgang Amadeus Mozart",
        "year": "1788",
        "url": "https://youtu.be/JTc1mDieQI8?si=1ggnfrLLopftYWt7",
        "timestamp": 44,
        "class": "MUSIC 101",
        "id": "0002"
    }
];

let userAnswers = [];
let questionNumber = 0;
let numberCorrect = 0;
let playerReady = false;
let songLength = 60;
let secondsTimer = 0;
let songListens = 0;

$(document).ready(function() {
    document.addEventListener('playerReady', function() {
        playerReady = true;
        startQuiz();
    });

    document.addEventListener('endSong', function() {
        songListens++;
        $('#songListens').html(3 - songListens);

        if (songListens < 3) {
            loadCurrentSong();
        }
        else {
            document.dispatchEvent(new Event('attemptLimit'));
        }

    });

    $("#quizForm").submit(function(event) {
        event.preventDefault();
        let formData = $(this).serializeArray();
        userAnswers.push(formData);
        checkAnswers(formData);
        nextQuestion();
    });
});

function startQuiz() {
    if (playerReady) {
        loadCurrentSong();
    }
    else {
        console.warn("Player not ready yet.");
    }
}

function checkAnswers(formData) {
    let answerKey = playlistData[questionNumber];

    if (
        answerKey['name'].toLowerCase() === formData[0]['value'].toLowerCase() &&
        answerKey['composer'].toLowerCase() === formData[1]['value'].toLowerCase() &&
        answerKey['year'] === formData[2]['value']
    ) {
        numberCorrect++;
    }
}

function nextQuestion() {
    questionNumber++;
    songListens = 0;
    $('#questionAttempts').html(3);

    if (questionNumber < playlistData.length) {
        $('#questionNumber').text(questionNumber + 1);
        $('#quizForm')[0].reset();
        loadCurrentSong();
    }
    else {
        console.log("QUIZ COMPLETE!");
        alert(`Quiz complete! You got ${numberCorrect} out of ${playlistData.length} correct.`);
    }
}

function loadCurrentSong() {
    
    let videoId = getVideoId(playlistData[questionNumber]['url']);
    let timestamp = playlistData[questionNumber]['timestamp'];

    const setNewSong = new CustomEvent('setNewSong', {
        detail: {
            "url": videoId,
            "timestamp": timestamp,
            "songLength": songLength
        }
    });

    document.dispatchEvent(setNewSong);
}

function getVideoId(url) {
    let match = url.match(/(?:youtu\.be\/|youtube\.com\/(?:.*v=|.*\/|.*embed\/))([^?&]+)/);
    return match ? match[1] : null;
}
