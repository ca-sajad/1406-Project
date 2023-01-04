# 1406-Project
## Final Code for 1405Z Project, Carleton University, Fall 2022

### This is a simple search engine that scrapes 1000 webpages containing a limited number of words (i.e. fruit names) and saves data such as term frequency, page rank, ... in text files. 
### Then, user can search for fruit names and receive the top 10 most reputable URLs (based on page rank calculations) with the most occurrences of those fruit names (based on Vector Space Model).
### This is similar to 1405Z Project (written in Python). However this project uses OOP principles and JavaFX.
### For more info see ["COMP1406Z-F22-Course Project.pdf"](/COMP1406Z-F22-Course%20Project.pdf) and ["Report.pdf"](/Report.pdf)

![GUI image](https://github.com/ca-sajad/1406-Project/blob/main/Screenshot%201406.png)

### How To Run the Code
#### Crawler
- All the files necessary to run the crawler are in the subpackage named [“model”](/src/fruits1/search_engine/model). 
- All the files storing the crawled data will be saved in a folder named “data”.
- ["fruits1"](/src/fruits1) contains files to test the crawler and search results for a set of 1000 webpages.

#### GUI
- In general, to run the GUI, one must first run the crawler so that the “data” folder is created (for instance in "fruits1" database). 
Then run the SearchApp class which can be found using the following pattern:
[src -> fruits1 -> search_engine —> gui —> SearchApp](/src/fruits1/search_engine/gui/SearchApp.java)
- However, currently, the "data" folder in the "search_engine" package contains a sample crawled data for a small dataset made of 10 pages whose GUI can be run using the [SearchApp](/src/search_engine/gui/SearchApp.java) class.
  

