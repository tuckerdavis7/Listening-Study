
let quizSettingsID = 19;

$(document).ready(async function() {
    let wrongAnswers = await getWrongAnswers();
    //console.log(wrongAnswers);
    let songIDs = getSongIDs(wrongAnswers);
    let correctAnswers = await getCorrectAnswers(songIDs);
    let numQuestions = 3;
    console.log(numQuestions);
    console.log(wrongAnswers);
    console.log(correctAnswers);

    let container = $('#wrongAnswersContainer');

    for (let i = 0; i < wrongAnswers.length; i++) {
        let subcontainer = $('<div class="card m-4 shadow answerCard"></div>');
        container.append(subcontainer);
        
        subcontainer.append('<h6 class="card-subtitle"><b>Your Answer</b>:</h6>');
        subcontainer.append(`<p class="card-text"><u>Song Name</u>: ${wrongAnswers[i].name}`);
        subcontainer.append(`<p class="card-text"><u>Composer</u>: ${wrongAnswers[i].composer}`);
        subcontainer.append(`<p class="card-text"><u>Year</u>: ${wrongAnswers[i].year}`);
        subcontainer.append('<h6 class="card-subtitle"><b>Answer</b>:</h6>');
        subcontainer.append(`<p class="card-text"><u>Song Name</u>: ${correctAnswers[i].name}`);
        subcontainer.append(`<p class="card-text"><u>Composer</u>: ${correctAnswers[i].composer}`);
        subcontainer.append(`<p class="card-text"><u>Year</u>: ${correctAnswers[i].year}`);
    }

    let numberCorrect = numQuestions - wrongAnswers.length;
    let scorePercentage = (numberCorrect / numQuestions) * 100;
    $('#userScore').html(numberCorrect);
    $('#totalQuestions').html(numQuestions);
    $('#scorePercentage').html(scorePercentage);

    if (scorePercentage == 100) {
        console.log("good job")
        container.append('<img src="https://www.icegif.com/wp-content/uploads/2024/04/clapping-icegif-1.gif" width="100" height="100">');
    }
});


async function getWrongAnswers() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `http://localhost:8080/api/quizResults?quizSettingsID=${quizSettingsID}`,
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                resolve(data.data);
            },
            error: function(xhr, status, error) {
                bootstrapAlert('danger', 'Error1 while updating designation: ' + error);
                reject(error);
            }
        });
    });
}

function getSongIDs(wrongAnswers) {
    let songIDs = [];
    wrongAnswers.forEach(element => {
        songIDs.push({"songID": element.songID});
    });
    console.log(songIDs);
    return songIDs;
    
}

async function getCorrectAnswers(songIDs) {
    console.log(songIDs);
    console.log("songIDs");
    return new Promise((resolve, reject) => {
        $.ajax({
            method: "POST",
            url: "http://localhost:8080/api/quizResults/correct",
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(songIDs),
            success: function(data) {
                resolve(data.data);
            },
            error: function(xhr, status, error) {
                bootstrapAlert('danger', 'Error2 while updating designation: ' + error);
                reject(error);
            }
        });
    });
}