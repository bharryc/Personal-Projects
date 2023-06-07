#Python API for user registration

This project was part of an assessment that I completed that mimicked a video streaming platforms sign up service. The API had two endpoints /users and /payments to be implemented.

The /users being responsible for taking a JSON body with details relating to the customer such as username, password, email, dob and credit card number. There were constraints to the data entry that were outlined in the comments in the code. The program should return status codes depending on the response.

The endpoint /payments was responsible for taking a JSON body with details such as a credit card number and an amount, and would return status codes depeding on the response of the request.

There was no need to implement any front end code, nor was there any need to use any databases instead language provided data structures could be used.

Unit tests were also expected to be provided which tested all aspects of the program, including accepted and erroneous data.
