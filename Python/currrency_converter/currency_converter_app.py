import requests 
from flask import Flask, request, jsonify 

#creates instance of flask app
app = Flask(__name__)

#gets the echange rates from the open exchange rates api
def get_exchange_rates():
    url = 'https://openexchangerates.org/api/latest.json'
    params = {'app_id' : 'XXXX'} #replace with api key 

    response = requests.get(url, params)

    if response.status_code == 200:
        data = response.json()
        return data['rates']
    else:
        print('Failed to retrieve rates, code: ', response.status_code)


#endpoint for converting one or more currencies into another 
@app.route('/convert', methods=['POST'])
def calc_convert():

    conversions = request.get_json()
    exchange_rate = get_exchange_rates()

    results = []

    for x in conversions:
        amount = float(x.get('amount'))
        convert_from = x.get('from', '').upper()
        convert_to = x.get('to', '').upper()

        #checks for missing paramaters
        if not amount or not convert_from or not convert_to:
            return jsonify({'error': 'missing params'}), 400 

        #calculates the exchange rate and the converted amount 
        rate = rate = exchange_rate[convert_to] / exchange_rate[convert_from]
        converted = amount * rate

        if converted is None:
            return jsonify({'error', 'failed'}), 400

        result = { 'amount':amount, 'from': convert_from, 'to': convert_to, 'converted amount': converted}

        results.append(result)

    #returns the amount 
    return jsonify(results), 200

#endpoint for getting the conversion rate between two currencies 
@app.route('/exchangerate', methods=['GET'])
def get_exchange_rate():
    convert_from = request.args.get('from').upper()
    convert_to = request.args.get('to').upper()

    #checks for missing parameters
    if not convert_from or not convert_to:
        return jsonify({'error': 'missing params'}), 400
    
    exchange_rate = get_exchange_rates()

    #calculates the exchange rate and returns it 
    rate = exchange_rate[convert_to] / exchange_rate[convert_from]
    response = {'from':convert_from, 'to':convert_to, 'rate':"{:0.2f}".format(rate)}
    return jsonify(response), 200


if __name__ == '__main__':
    app.run()
