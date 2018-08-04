

const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.notifyWhenSend = functions.database.ref('/Comment/{message}').onWrite((change, context) => {
    const sender = change.after.child('messaageUser').val();
    const content = change.after.child('messageText').val();
    var topic = 'Comment';

    // See documentation on defining a message payload.
    var message = {
        notification: {
            title: sender,
            body: content
        },
        android: {
            collapse_key: 'BigChat',
            notification: {
                sound: 'default',
                tag: 'BigChat'
            }
        },
        topic: topic
    };

    // Send a message to devices subscribed to the provided topic.
    return admin.messaging().send(message)
        .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
            return Promise.all([]);
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
});

exports.notifyWhenPost = functions.database.ref('/Notifications/{post}').onCreate((snapshot, context) => {
    const topic = snapshot.child('topic').val();
    const rank = snapshot.child('rank').val();
    const title = snapshot.child('title').val();
    const content = snapshot.child('content').val();
    var priority;

    if (rank === 'high') {
        priority = 'HIGH';
    }
    else
    {
        priority = 'NORMAL';
    }

    var message = {
        notification: {
            title: title,
            body: content
        },
        android: {
            collapse_key: 'Post',
            priority: priority,
            notification: {
                sound: 'default',
                tag: 'Post'
            }
        },
        topic: topic
    };

    return admin.messaging().send(message)
        .then((response) => {
            // Response is a message ID string.
            console.log('Successfully sent message:', response);
            return Promise.all([]);
        })
        .catch((error) => {
            console.log('Error sending message:', error);
        });
})
