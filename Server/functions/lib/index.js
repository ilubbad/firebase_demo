"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.app = exports.webApi = void 0;
const functions = require("firebase-functions");
const admin = require("firebase-admin");
const firebaseHelper = require("firebase-functions-helper/dist");
const express = require("express");
const bodyParser = require("body-parser");
admin.initializeApp(functions.config().firebase);
const db = admin.firestore();
const app = express();
exports.app = app;
const main = express();
main.use(bodyParser.json());
main.use(bodyParser.urlencoded({ extended: false }));
main.use('/api/v1', app);
const contactsCollection = 'contacts';
exports.webApi = functions.https.onRequest(main);
// Add new contact
app.post('/contacts', async (req, res) => {
    try {
        const contact = {
            firstName: req.body['firstName'],
            lastName: req.body['lastName'],
            email: req.body['email']
        };
        const newDoc = await firebaseHelper.firestore
            .createNewDocument(db, contactsCollection, contact);
        res.status(201).send(`Created a new contact: ${newDoc.id}`);
    }
    catch (error) {
        res.status(400).send(`Contact should only contains firstName, lastName and email!!!`);
    }
});
// Update new contact
app.patch('/contacts/:contactId', async (req, res) => {
    const updatedDoc = await firebaseHelper.firestore.updateDocument(db, contactsCollection, req.params.contactId, req.body);
    res.status(204).send(`Update a new contact:${updatedDoc} `);
});
// View a contact
app.get('/contacts/:contactId', (req, res) => {
    firebaseHelper.firestore
        .getDocument(db, contactsCollection, req.params.contactId)
        .then(doc => res.status(200).send(doc))
        .catch(error => res.status(400).send(`Cannot get contact: ${error}`));
});
// View all contacts
app.get('/contacts', (req, res) => {
    firebaseHelper.firestore
        .backup(db, contactsCollection)
        .then(data => {
        const res_data = JSON.parse(JSON.stringify(data));
        var keys = Object.keys(res_data.contacts);
        for (var i = 0; i < keys.length; i++) {
            res_data.contacts[keys[i]].id = keys[i];
        }
        res.status(200).send(Object.values((res_data.contacts)));
    })
        .catch(error => res.status(400).send(`Cannot get contacts: ${error}`));
});
// Delete a contact 
app.delete('/contacts/:contactId', async (req, res) => {
    const deletedContact = await firebaseHelper.firestore
        .deleteDocument(db, contactsCollection, req.params.contactId);
    res.status(204).send(`Contact is deleted: ${deletedContact}`);
});
//# sourceMappingURL=index.js.map