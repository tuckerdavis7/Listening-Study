let playlistData = [
    {
        "playlist": "Classical 1",
        "name": "Fur Elise",
        "composer": "Ludwig van Beethoven",
        "year": "1810",
        "url": "https://youtu.be/q9bU12gXUyM?si=gMz2qDgpwy6ZtolG",
        "timestamp": 20,
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

$(document).ready(function() {
    document.addEventListener('playerReady', function() {
        playerReady = true;
        startQuiz();
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

    const event = new CustomEvent('setNewURL', {
        detail: {
            "url": videoId,
            "timestamp": timestamp
        }
    });

    document.dispatchEvent(event);
}

function getVideoId(url) {
    let match = url.match(/(?:youtu\.be\/|youtube\.com\/(?:.*v=|.*\/|.*embed\/))([^?&]+)/);
    return match ? match[1] : null;
}
