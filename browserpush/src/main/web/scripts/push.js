//see https://github.com/mdn/to-do-notifications/blob/gh-pages/scripts/todo.js

const notificationBtn = document.getElementById('notificationBtn');
const notificationHint = document.getElementById('notificationHint');


// Do an initial check to see what the notification permission state is
handlePushPermission();


function askNotificationPermission() {
    // function to actually ask the permissions

    // Let's check if the browser supports notifications
    if (!('Notification' in window)) {
        console.log("This browser does not support notifications.");
        notificationHint.textContent = "This browser does not support notifications."
        notificationBtn.style.display = 'none';
        notificationHint.style.display = 'block';
    } else {
        Notification.requestPermission().then(() => {
            handlePushPermission();
        })
    }
}

function handlePushPermission() {
    // set the button to shown or hidden, depending on what the user answers
    if (Notification.permission === 'denied' || Notification.permission === 'default') {
        notificationBtn.style.display = 'block';
        notificationHint.style.display = 'none';
    } else {
        notificationBtn.style.display = 'none';
        notificationHint.style.display = 'block';
    }
}


notificationBtn.addEventListener('click', askNotificationPermission);