import nltk 
from nltk.sentiment import SentimentIntensityAnalyzer
from nltk.corpus import stopwords 
from nltk.stem import WordNetLemmatizer
import re
from flask import Flask, request, jsonify
import requests
from bs4 import BeautifulSoup

app = Flask(__name__)


#cleans up the inputted text
def process_text(text):

    #removes HTML tags if text is scraped from web
    processed_text = re.sub('<.*?', '', text)

    #removes punctuation and chars 
    processed_text = re.sub('[^\w\s]', '', processed_text)

    #tokenization
    tokens = nltk.word_tokenize(processed_text)

    #removes stopwords 
    stopword = stopwords.words('english')
    tokens = [token for token in tokens if token.lower() not in stopword]

    #lemmatization
    lemmatizer = WordNetLemmatizer()
    tokens = [lemmatizer.lemmatize(token) for token in tokens]

    #joins all tokens back together to a piece of text and returns 
    processed_text = ' '.join(tokens)
    return processed_text

#scrapes all text from the <p> tags from the html given
def scrape(url):

    try:
        response = requests.get(url)
        response.raise_for_status()
        html = response.text 

        soup = BeautifulSoup(html, 'html.parser')
        para_tags = soup.find_all('p')
        text = ' '.join(tag.get_text() for tag in para_tags)
        return text
    except requests.exceptions.RequestException as e:
        return None 

#performs sentiment analysis on text
@app.route('/sentiment', methods=['POST'])
def sentiment_analysis():
    input = request.get_json()

    if 'text' in input:
        text = input['text']

        cleaned_text = process_text(text)

    elif 'url' in input:
        url = input['url']
       
        extracted = scrape(url)

        if extracted is None:
            return jsonify({'error' : 'failed to get text from the html'}), 400
        
        cleaned_text = process_text(extracted)

    else:
        return jsonify({'error' : 'invalid params'}), 400
    
    analyser = SentimentIntensityAnalyzer()

    sentiment_score = analyser.polarity_scores(cleaned_text)

    sentiment = sentiment_score['compound']
    neg_sentiment = sentiment_score['neg']
    neut_sentiment = sentiment_score['neu']
    pos_sentiment = sentiment_score['pos']

    if sentiment > 0:
        sentiment_label = 'Positive'
    elif sentiment < 0:
        sentiment_label = 'Negative'
    else:
        sentiment_label = 'Neutral'

    results = {'sentiment score': sentiment, 
               'sentiment label': sentiment_label, 
               'negative sentiment score' : neg_sentiment, 
               'neutral sentiment score' : neut_sentiment, 
               'positive sentiment score' : pos_sentiment}
    
    return jsonify(results), 200


# @app.route('/language', method=['GET'])
# def find_language():


if __name__ == '__main__':
    app.run(debug=False)