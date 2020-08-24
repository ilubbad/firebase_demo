Demo Features List 

Android(Kotlin)
1.	Firebase sign-up by email& password
2.	Firebase sign-in by email& password
3.	Firebase sign-out
4.	Remove user from firebase
5.	Change password
6.	Forget password
7.	API used in app
•	getListEntries
•	addNewEntry
•	updateEntry
•	deleteEntry

Back-End using Google Cloud Functions: (express.js/node) 
1.	Store database firebase firestore database 
2.	Create APIs:
•	API createEntry 
•	API  updateEntry
•	API deleteEntry
•	API getAllEntries
•	API getEntryById


You will need a Firebase project and firebase tools cli
npm install -g firebase-tools

log in to firebase in the terminal
firebase login
Deploy to firebase
For the first time, you have deploy the hosting and functions together
firebase deploy
After that, you just need to deploy functions only
firebase deploy --only functions
