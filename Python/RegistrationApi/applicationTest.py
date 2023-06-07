from application import application, users
import pytest
import json 

headers = {"Content-Type": "application/json"}

#Creates a test app to use based on the application
@pytest.fixture
def app():
    with application.test_client() as app:
        yield app 

#Clears the users list before each test
@pytest.fixture(autouse=True)
def clearUsers():
    users.clear()

#Tests a valid user registration 
def testValidRegistraion1(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers=headers)

    assert response.status_code == 201

#Tests a valid registration without cc 
def testValidRegistraion2(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    response = app.post("/users", data=json.dumps(user1), headers=headers)

    assert response.status_code == 201

#Test invalid username 
def testInvalidRegistration1(app):
    user1 = {"username":"user1.!@*", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400

#Test invalid password - under 8 chars
def testInvalidRegistration2(app):
    user1 = {"username":"user1", "password": "test", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400

#Test invalid password - over 8 char no uppercase no number
def testInvalidRegistration3(app):
    user1 = {"username":"user1", "password": "testpassword", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400

#Test invalid password - over 8 char uppercase but no number
def testInvalidRegistration4(app):
    user1 = {"username":"user1", "password": "testpassworD", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400

#Test invalid password under 8 char has number no uppercase 
def testInvalidRegistration5(app):
    user1 = {"username":"user1", "password": "test1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400
    
#Test invalid password under 8 char has number and uppercase
def testInvalidRegistration6(app):
    user1 = {"username":"user1", "password": "Test1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400
    
#Test invalid email - no @
def testInvalidRegistration7(app):
    user1 = {"username":"user1", "password": "Testpassword1", 
                "email": "testtest.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400
     
#Test invalid email - no .
def testInvalidRegistration8(app):
    user1 = {"username":"user1", "password": "Testpassword1", 
                "email": "test@testcom", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400
          
#Test invalid email - . before @ 
def testInvalidRegistration9(app):
    user1 = {"username":"user1", "password": "Testpassword1", 
                "email": ".test@testcom", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400

#Test invalid dob - not iso format 
def testInvalidRegistration10(app):
    user1 = {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "19-01-2000", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers = headers)

    assert response.status_code == 400

#Tests invalid CC number - under 16 numbers
def testInvalidRegistraion11(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "123"}
    
    response = app.post("/users", data=json.dumps(user1), headers=headers)

    assert response.status_code == 400

#Tests invalid CC number - over 16 numbers
def testInvalidRegistration12(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "12345678123456789"}
    
    response = app.post("/users", data=json.dumps(user1), headers=headers)

    assert response.status_code == 400

#Tests invalid CC number - not all numbers
def testInvalidRegistration13(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "abcdefghijklmnop"}
    
    response = app.post("/users", data=json.dumps(user1), headers=headers)

    assert response.status_code == 400

#Tests invalid age - under 18 
def testInvalidRegistration14(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2020-01-19", 
                "ccNum": "1234567812345678"}
    
    response = app.post("/users", data=json.dumps(user1), headers=headers)

    assert response.status_code == 403

#Tests username already taken
def testInvalidRegistration15(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user1", "password": "Testpassword1", 
            "email": "test@test.com", "dob": "2000-01-19", 
            "ccNum": "1234567812345678"}
    
    app.post("/users", data=json.dumps(user1), headers=headers)
    response = app.post("/users", data=json.dumps(user2), headers=headers)

    assert response.status_code == 409

#Tests getting users without any filter - 4 users
def testGetUsers1(app):

    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user3= {"username":"user3", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user4= {"username":"user4", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)
    app.post("/users", data=json.dumps(user4), headers=headers)

    response = app.get("/users")
    assert response.status_code == 200

    listOfUsers = response.get_json()
    assert len(listOfUsers) == 4

#Tests getting users with CC filter yes  - 2 users
def testGetUsers2(app):

    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user3= {"username":"user3", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user4= {"username":"user4", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user5= {"username":"user5", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567887654321"}
    
    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)
    app.post("/users", data=json.dumps(user4), headers=headers)
    app.post("/users", data=json.dumps(user5), headers=headers)

    response = app.get("/users?CreditCard=Yes")
    assert response.status_code == 200

    listOfUsers = response.get_json()
    assert len(listOfUsers) == 2

#Tests getting users with CC filter no  - 3 users
def testGetUsers3(app):

    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user3= {"username":"user3", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user4= {"username":"user4", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user5= {"username":"user5", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567887654321"}

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)
    app.post("/users", data=json.dumps(user4), headers=headers)
    app.post("/users", data=json.dumps(user5), headers=headers)

    response = app.get("/users?CreditCard=No")
    assert response.status_code == 200

    listOfUsers = response.get_json()
    assert len(listOfUsers) == 3

#Tests getting users with CC filter with incorrect param 
def testGetUsers4(app):

    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user3= {"username":"user3", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user4= {"username":"user4", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user5= {"username":"user5", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567887654321"}

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)
    app.post("/users", data=json.dumps(user4), headers=headers)
    app.post("/users", data=json.dumps(user5), headers=headers)

    response = app.get("/users?CreditCard=test")
    assert response.status_code == 400

#Test a valid payment with only a single user registered 
def testValidPayment1(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}

    paymentInfo = {"ccNum" : "1234567812345678", "amount" : "100"} 
    
    app.post("/users", data=json.dumps(user1), headers=headers)
    response = app.post("/payments", data=json.dumps(paymentInfo), headers = headers)

    assert response.status_code == 201

#Test a valid payment with multiple users registered
def testValidPayment2(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567887654321"}
    
    user3= {"username":"user3", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    paymentInfo = {"ccNum" : "1234567812345678", "amount" : "100"} 

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)

    response = app.post("/payments", data=json.dumps(paymentInfo), headers = headers)

    assert response.status_code == 201 

#Test a invalid payment - no cc is registered correct amount
def testInvalidPayment1(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user3= {"username":"user3", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    paymentInfo = {"ccNum" : "8765432187654321", "amount" : "100"} 

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)

    response = app.post("/payments", data=json.dumps(paymentInfo), headers = headers)

    assert response.status_code == 404

#Test a invalid payment - cc is registered incorrect amount
def testInvalidPayment2(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567812345678"}
    
    user2= {"username":"user2", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19", 
                "ccNum": "1234567887654321"}
    
    user3= {"username":"user3", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    paymentInfo = {"ccNum" : "1234567812345678", "amount" : "100000"} 

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)

    response = app.post("/payments", data=json.dumps(paymentInfo), headers = headers)

    assert response.status_code == 400

#Test a invalid payment - no cc are registered 
def testInvalidPayment3(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user2= {"username":"user2", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user3= {"username":"user3", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    paymentInfo = {"ccNum" : "1234567812345678", "amount" : "100"} 

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)

    response = app.post("/payments", data=json.dumps(paymentInfo), headers = headers)

    assert response.status_code == 404

#Test a invalid payment - invalid cc format 
def testInvalidPayment4(app):
    user1= {"username":"user1", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user2= {"username":"user2", "password": "Testpassword1", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    user3= {"username":"user3", "password": "Testpassword2", 
                "email": "test@test.com", "dob": "2000-01-19"}
    
    paymentInfo = {"ccNum" : "1234abcdef", "amount" : "100"} 

    app.post("/users", data=json.dumps(user1), headers=headers)
    app.post("/users", data=json.dumps(user2), headers=headers)
    app.post("/users", data=json.dumps(user3), headers=headers)

    response = app.post("/payments", data=json.dumps(paymentInfo), headers = headers)

    assert response.status_code == 400