let userAnswers = [
    [
        {
            "name": "songName",
            "value": "Far elise"
        },
        {
            "name": "composer",
            "value": "Ludwig van Beethoven"
        },
        {
            "name": "year",
            "value": "1810"
        }
    ],
    [
        {
            "name": "songName",
            "value": "symphony no. 41"
        },
        {
            "name": "composer",
            "value": "Wolfgang Amadeus Mozart"
        },
        {
            "name": "year",
            "value": "1788"
        }
    ]
]
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

$(document).ready(function() {
    let wrongAnswerIndices = getWrongAnswers();
    let container = $('#wrongAnswersContainer');

    for (let i = 0; i < wrongAnswerIndices.length; i++) {
        let subcontainer = $('<div class="card m-4 shadow answerCard"></div>');
        container.append(subcontainer);
        
        subcontainer.append(`<h4 class="card-title">Question ${wrongAnswerIndices[i]+1}:`);
        subcontainer.append('<h6 class="card-subtitle"><b>Your Answer</b>:</h6>');
        subcontainer.append(`<p class="card-text"><u>Song Name</u>: ${userAnswers[wrongAnswerIndices[i]][0].value}`);
        subcontainer.append(`<p class="card-text"><u>Composer</u>: ${userAnswers[wrongAnswerIndices[i]][1].value}`);
        subcontainer.append(`<p class="card-text"><u>Year</u>: ${userAnswers[wrongAnswerIndices[i]][2].value}`);
        subcontainer.append('<h6 class="card-subtitle"><b>Answer</b>:</h6>');
        subcontainer.append(`<p class="card-text"><u>Song Name</u>: ${playlistData[wrongAnswerIndices[i]]['name']}`);
        subcontainer.append(`<p class="card-text"><u>Composer</u>: ${playlistData[wrongAnswerIndices[i]]['composer']}`);
        subcontainer.append(`<p class="card-text"><u>Year</u>: ${playlistData[wrongAnswerIndices[i]]['year']}`);
    }

    let numberCorrect = userAnswers.length - wrongAnswerIndices.length;
    let scorePercentage = (numberCorrect) / userAnswers.length * 100;
    $('#userScore').html(numberCorrect);
    $('#totalQuestions').html(userAnswers.length);
    $('#scorePercentage').html(scorePercentage);

    if (scorePercentage == 100) {
        console.log("good job")
        container.append('<img src="https://www.icegif.com/wp-content/uploads/2024/04/clapping-icegif-1.gif" width="100" height="100">');
    }
});

function getWrongAnswers() {
    let wrongAnswers = [];
    for (let i = 0; i < userAnswers.length; i++) {
        let answerKey = playlistData[i];

        if (
            answerKey['name'].toLowerCase() == userAnswers[i][0]['value'].toLowerCase() &&
            answerKey['composer'].toLowerCase() == userAnswers[i][1]['value'].toLowerCase() &&
            answerKey['year'] == userAnswers[i][2]['value']
        ) {
            //console.log(answerKey);
            //console.log(userAnswers[i]);
        }
        else {
            wrongAnswers.push(i);
        }
    }
    return wrongAnswers;

}