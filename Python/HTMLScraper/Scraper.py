from bs4 import BeautifulSoup
import requests
import json

urls = []

def getLinks():
    URL = "https://ojs.aaai.org/index.php/ICWSM/issue/view/462"
    #Url of main page.
    page = requests.get(URL)
    soup = BeautifulSoup(page.content, "html.parser")
    article_summary = soup.find_all("div", {"class": "obj_article_summary"})

    #Gathers all of the links of the papers attached to the titles.
    for summary in article_summary:
        titles = summary.find("h3", {"class": "title"}).find_all("a")
        for link in titles:
            link_url = link["href"]
            urls.append(link_url)

    for url in urls:
        getMetaData(url)

    print("scraper no scraping")


def getMetaData(url):
    authors = []
    authorList = {}
    page = requests.get(url)
    soup = BeautifulSoup(page.content, "html.parser")

    main_entry = soup.find_all("div",{"class": "main_entry"})
    entry_details = soup.find_all("div", {"class": "entry_details"})

    #Get the tilte of the paper 
    title = soup.find("article", {"class": "obj_article_details"}).find("h1", {"class": "page_title"}).text.strip()

    #Get all authors and affiliation for each paper 
    for entry in main_entry:
        item_authors = entry.find("section", {"class": "item authors"}).find("ul", {"class": "authors"})
        listauthors = item_authors.find_all("li")
        for author in listauthors:
            name = author.find("span", {"class": "name"}).text.strip()
            if(author.find("span", {"class": "affiliation"})):
               affiliations = author.find("span", {"class": "affiliation"}).text.strip()
               authorList = {"name" : name, "affilition" : affiliations, "location" : " - "}
            else:
                authorList = {"name" : name, "affilition" : "N/A", "location" : "N/A"}

            authors.append(authorList)


    #Get abstract and keywords for each paper 
    #abstract = entry.find("section", {"class": "item abstract"}).find("h2",{"class": "label"}).next_sibling.strip()
    #For the year 2021 as the website changed HTML structure in that year
    abstract = entry.find("section", {"class": "item abstract"}).find("p").text.strip()
    #For the years before 2021 and the HTML structrue change

    #Get the keyword for each paper if they are available 
    if(entry.find("section", {"class": "item keywords"})):
        keywords = entry.find("section", {"class": "item keywords"}).find("span", {"class":"value"}).text.split()
    else:
        keywords = "N/A"


    #Get date of publication and paper type
    for detail in entry_details:
        date_published = detail.find("section", {"class": "sub_item"}).find("span").text
        paper_type = detail.find_all("section", {"class": "sub_item"})
        paper_type = paper_type[3].find("div", {"class": "value"}).text.strip()


    writeToJson(title, authors, abstract, keywords, date_published, paper_type)
    authorList.clear()
    authors.clear()


def writeToJson(title, authors, abstract, keywords, date_published, paper_type, filename='results8.json'):

    new_data = {"title" : title, "authors" : authors, "abstract" : abstract, "keywords" : keywords, "date" : date_published, "paper type" : paper_type}
    
    with open(filename, 'r+') as file:
        file_data = json.load(file)
        file_data["results"].append(new_data)
        file.seek(0)
        json.dump(file_data, file, indent = 4)


getLinks()
#getMetaData()


