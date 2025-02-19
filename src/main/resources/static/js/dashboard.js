window.onload = function() {
    let storedMessage = localStorage.getItem('loginMessage');
    if (storedMessage) {
        let { color, message } = JSON.parse(storedMessage);
        bootstrapAlert(color, message);
                        
        localStorage.removeItem('loginMessage'); //Clear the message after displaying it
    }
};