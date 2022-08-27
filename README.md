# VideoPlayer
A library and video player for the TV show "The Office". Rather than watching all of the episodes from the office straight from my file explorer, I created this application to give myself easier access to them, with a description for each of them and a nice looking UI. In addition to this, the application saves the episode and timestamp that you were currently on, so you can pick up right where you left off.

In order to do this, I had to create the menu for picking episodes as well as the entire video player portion (with play/pause buttons, duration bar, etc). To create the tooltips more efficiently, I wrote a python program that uses selenuim to scrape the data from the web much more quickly than doing it by hand.
Link to the repo for that:
https://github.com/BenTheurich/plot_summary_webscraper
