import "jest";
import {stub, SinonStub} from "sinon";
import * as admin from "firebase-admin";
import { HttpsFunction } from "firebase-functions";
import * as request from "supertest";

describe("API testing", function() {
  let webApi:HttpsFunction,
    adminInitStub:SinonStub,
    originalFirestore:(app: admin.app.App) => admin.firestore.Firestore;
  
  const collectionStub = stub();
  const firestoreStub = stub().returns({ collection: collectionStub});
    
  beforeAll(() => {
    adminInitStub = stub(admin, "initializeApp");
    originalFirestore = admin.firestore;
    Object.defineProperty(admin, "firestore", {get: () => firestoreStub});
    const myFunctions = require("../src/index");
    webApi = myFunctions.webApi;
  });

  afterAll(() => {
    adminInitStub.restore();
    Object.defineProperty(admin, "firestore", originalFirestore);
  });

  it("View All Contacts", function(done){
    const contacts = [
      {
        firstName: "ibraheem",
        lastName: "lubbad",
        email: "ilubbad93@gmail.com"
      }
    ].map((contact, id) => ({ data: () => contact, id}));

    collectionStub
      .withArgs("contacts")
      .returns({get: () => Promise.resolve(contacts)});

    request(webApi)
      .get("/api/v1/contacts")
      .expect(200)
      .end((err, res) => {
        const contacts = Object.entries(res);
        expect(contacts.length).toEqual(35);
        done()
      });
  });

  describe("Add new contact", function() {
    beforeAll(() => {
      collectionStub.resetBehavior();
      collectionStub
        .withArgs("contacts")
        .returns({add: (contact:any) => Promise.resolve({id: "k3iGsSDiZNJVZg195xHU", get: () => contact})});
    })

    it("Should OK when post with firstName, lastName and email.", function(done) {
      request(webApi)
        .post("/api/v1/contacts")
        .set('Content-Type', 'application/json')
        .send({firstName: "ibraheem",
        lastName: "lubbad",
        email: "ilubbad93@gmail.com"})
        .expect(201)
        .end((err, res) => {
          expect(res.text).toMatch('Created a new contact: k3iGsSDiZNJVZg195xHU');
          done();
        })
    });
  
    it("Should throw when sending wrong data", function(done) {

      request(webApi)
        .post("/api/v1/contacts")
        .set('Content-Type', 'application/json')
        .send({ name: 'salem', year: 2020})
        .expect(400)
        .end((err, res) => {
          expect(res.text).toMatch('Contact should only contains firstName, lastName and email!!!');
          done();
        })
    });
  });

  describe("View a contact", function() {

    beforeAll(() => {
      collectionStub.resetBehavior();
      const contacts = [
        {
          firstName: "ibraheem",
          lastName: "lubbad",
          email: "ilubbad93@gmail.com"
        }, {
          firstName: "salem",
          lastName: "salem",
          email: "salem@gmail.com"
        }
      ].map((contact, id) => ({ data: () => contact, id, exists: true}));
  
      collectionStub
        .withArgs("contacts")
        .returns({
          get: () => Promise.resolve(contacts),
          doc: (id: any) => ({
          
            get: () => Promise.resolve(contacts[id])
          
          })
        });
    });

    it("Should OK to get contact index: 0", function(done){
      
      request(webApi)
        .get("/api/v1/contacts/0")
        .expect(200)
        .end((err, res) => {
          const contact = res.body;
          expect(contact.firstName).toBe("ibraheem");
          expect(contact.lastName).toBe("lubbad");
          expect(contact.email).toBe("ilubbad93@gmail.com");
          done()
        });
    });

    it("Should Throw to get contact index: 2", function(done){
      
      request(webApi)
        .get("/api/v1/contacts/2")
        .expect(400)
        .end(() => done());
    });
  });

});
