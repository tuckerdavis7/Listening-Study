/*
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
*/
let playlistID = 1;
let studentID = 1;

let quizQuestions = [];
let quizSettings = {};
let playbackMethod = "Random";
let playbackDuration = 60;
let activeQuizID = 0;

let userAnswers = [];
let questionNumber = 0;
let numberCorrect = 0;
let playerReady = false;
let secondsTimer = 0;
let songListens = 0;
let songDuration = 0;
let videoId = "";
let playbackTimestamp = 0;

$(document).ready(async function() {
    await updateQuizQuestions();
    await updateQuizSettings();
    playbackMethod = quizSettings[0]['playbackMethod'];
    playbackDuration = quizSettings[0]['playbackDuration'];
    activeQuizID = quizSettings[0]['quizSettingsID'];


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
        let isFormValid = true;
        let answer = {};

        formData.forEach(field => {
            if (!field.value.trim()) {
                isFormValid = false;
            }
        });

        if (!isFormValid) {
            bootstrapAlert('danger', 'Please fill out all fields before submitting.')
            return;
        }
        answer['name'] = formData[0]['value'];
        answer['composer'] = formData[1]['value'];
        answer['year'] = formData[2]['value'];
        answer['songID'] = quizQuestions[questionNumber]['songID'];
        answer['playlistID'] = quizQuestions[questionNumber]['playlistID'];

        userAnswers.push(answer);
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

async function nextQuestion() {
    questionNumber++;
    songListens = 0;
    $('#songListens').html(3);
    $('#elapsedTime').html('0:00');

    if (questionNumber < quizQuestions.length) {
        $('#questionNumber').text(questionNumber + 1);
        $('#quizForm')[0].reset();
        loadCurrentSong();
    }
    else {
        
        let wrongAnswers = await submitAnswers();
        wrongAnswers.forEach(element => {
            element.quizSettingsID = activeQuizID;
        });

        await forwardAnswers(wrongAnswers);


        window.location.href = "./quizResults";
    }
}

async function loadCurrentSong() {
    if (songListens == 0) {
        videoId = quizQuestions[questionNumber]['youtubeLink'];
        if (playbackMethod == "MostReplayed")
            playbackTimestamp = quizQuestions[questionNumber]['mrTimestamp']
        else if (playbackMethod == "UserDefined")
            playbackTimestamp = quizQuestions[questionNumber]['udTimestamp'];
        else
            playbackTimestamp = 0;
    }
    
    songDuration = await getVideoDuration();
    await setNewSong(videoId, playbackTimestamp, playbackDuration);

    if (songListens == 0) {
        const newSongWait = setTimeout(async function () {
            if (playbackMethod == "Random")
                playbackTimestamp = -1;
            let fixedTimestamp = await getCorrectTimestamp(playbackTimestamp);
            if (fixedTimestamp != playbackTimestamp) {
                playbackTimestamp = fixedTimestamp;
                await setNewSong(videoId, fixedTimestamp, playbackDuration);
            }
        }, 1000);
    }
}

async function updateQuizQuestions() {
    try {
        const response = await $.ajax({
            type: "GET",
            url: `http://localhost:8080/api/takequiz/songs?playlistID=${playlistID}&studentID=${studentID}`,
            dataType: 'json',
        });
        quizQuestions = response.data;
    }
    catch (error) {
        console.log("Error fetching data from the API:", error);
    }
    
}

async function updateQuizSettings() {
    try {
        const response = await $.ajax({
            type: "GET",
            url: `http://localhost:8080/api/takequiz/settings?playlistID=${playlistID}`,
            dataType: 'json'
        });
        quizSettings = response.data;
    }
    catch (error) {
        console.log("Error fetching data from the API:", error)
    }
}

async function getCorrectTimestamp(timestamp) {
    let newTimestamp = -1;
    try {
        const response = await $.ajax({
            type: "GET",
            url: `http://localhost:8080/api/snippet?playbackMethod=${playbackMethod}&playbackDuration=${playbackDuration}&songDuration=${songDuration}&timestamp=${timestamp}`,
            dataType: 'json'
        });
        newTimestamp = parseInt(response);
    }
    catch (error) {
        console.log("Error fetching data from the API:", error)
    }
    return newTimestamp;
}

async function getVideoDuration() {
    return new Promise((resolve) => {
        document.addEventListener('returnVideoDuration', function handler(e) {
            document.removeEventListener('returnVideoDuration', handler);
            resolve(e.detail.videoDuration);
        });
        document.dispatchEvent(new Event('getVideoDuration'));
    });
}

async function setNewSong(videoId, timestamp, playbackDuration) {
    let setNewSong = new CustomEvent('setNewSong', {
        detail: {
            "url": videoId,
            "timestamp": timestamp,
            "songLength": playbackDuration
        }
    });
    document.dispatchEvent(setNewSong);
}

async function submitAnswers() {
    try {
        const response = await $.ajax({
            method: "POST",
            url: "http://localhost:8080/api/quizResults/submit",
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(userAnswers)
        });
        return response.data;
    }
    catch (error) {
        console.log("Error fetching data from the API:", error);
        return null;
    }
}

async function forwardAnswers(wrongAnswers) {
    try {
        const response = await $.ajax({
            method: "POST",
            url: "http://localhost:8080/api/quizResults/forward",
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(wrongAnswers)
        });
        return response.data;
    }
    catch (error) {
        console.log("Error fetching data from the API:", error);
        return null;
    }
}