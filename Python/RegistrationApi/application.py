from flask import Flask, request, jsonify
from datetime import datetime 
import re

#creates instance of flask app 
application = Flask(__name__)

#stores the users as we arent using a database
users = []

#registers the users 
@application.route('/users', methods=['POST'])
def registerUsers():
    user = request.get_json()
    
    #Checks valid username - alphanumeric and no spaces 
    if not re.match("^[a-zA-Z0-9]+$", user['username']):
        return jsonify({'error': 'Invalid username'}), 400
    
    #Checks valid password, over 8 chars and one uppercase, lower and numebr
    if not re.match("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$", user['password']):
        return jsonify({'error': 'Invalid password'}), 400
    
    #Checks valid email needs a . and @ and matches symbols that aren't @
    if not re.match("[^@]+@[^@]+\\.[^@]+", user['email']):
        return jsonify({'error': 'Invalid email'}), 400
    
    #Checks see if the dob is not in the correct format using isoformat
    try:
        dob = datetime.fromisoformat(user['dob'])
    except ValueError:
        return jsonify({'error': 'Invalid Date of Birth format'}), 400
    
    #Chekcs to see if the dob is over 18 by calculating the number of days and comparing that to 18*365 
    if (datetime.now() - dob).days < 18 * 365:
        return jsonify({'error': 'User should be over 18.'}), 403
    
    #Chekcs to see if the ccNum is present and is 16chars if not ignores
    if 'ccNum' in user and user['ccNum'] and not re.match("^\\d{16}$", user['ccNum']):
        return jsonify({'error':'Invalid CC Number'}), 400
    
    #Checks to see if username already exists
    if any(u['username'] == user['username'] for u in users):
        return jsonify({'error': 'Username taken'}), 409
    
    #Adds user to list and returns successful message
    users.append(user)
    return jsonify({'message': 'Successful Registration'}), 201 

#function to return the registerd users using get 
@application.route('/users', methods=['GET'])
def getUsers():
    #Checks to see if the request have CreditCard Yes or No
    creditCard = request.args.get('CreditCard')
    #If its yes go through users and get all the ones where there is 
    #a cc num for that entry if not then get the opposite
    if creditCard:
        if creditCard.lower() == 'yes':
            response = [u for u in users if u.get('ccNum')]
        elif creditCard.lower() == 'no':
            response = [u for u in users if not u.get('ccNum')]
        else:
            return jsonify({'error': 'Invalid request'}), 400
    else:
        response = users 
    #returns those users
    return jsonify(response), 200

#function to make a payment given a cc num.
@application.route('/payments', methods = ['POST'])
def makePayment():
    payment = request.get_json()

    #Checks to see if the CC num is not 16 chars
    if not re.match("^\\d{16}$", payment['ccNum']):
        return jsonify({'error': 'Invalid CC number'}), 400
    
    #Checks to see if the amount is over 3 chars
    if not re.match("^\\d{3}$", payment['amount']):
        return jsonify({'error': 'Invalid amount'}), 400
    
    #Searches through users and finds if the cc num is registerd 
    user = next((u for u in users if 'ccNum' in u and u['ccNum'] == payment['ccNum']), None)
    if not user:
        return jsonify({'error': 'CC not registered'}), 404 
    
    #returns successful payment if everything is correct
    return jsonify({'message': 'successful payment'}), 201


if __name__ == '__main__':
    application.run(debug=False)