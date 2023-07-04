# Currency converter application 

This is a currency converter application built in Python, it has two endpoints /convert and /exchangerate. /convert can take one or more queries in JSON format and will return the converted amount. 
The JSON body has the structure {"amount" : val, "from" : val, "to" : val} where from and to are the currencies you wish to convert to and from. 

/exchangerate will take two params 'from' and 'to' and will provide the exchange rate between those two currencies. 
