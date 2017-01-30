# twitterp: a Twitter library for Processing

Features:
* Tweet a status
* Tweet a status and image
* Stream statuses that match a keyword or set of keywords
* Stream statuses from a geographic region
* Stream statuses from a list of user IDs

## Installation
[Download the latest release](../../releases/latest), unzip it, and copy the twitterp folder to your Processing/libraries folder.

## Creating Twitter API credentials

1. Create a new Twitter account, or log in to an existing one.
2. If you created a new account, make sure to create at least one test tweet.
3. Make sure that your twitter account has a valid phone number attached to it, otherwise the below steps will fail. Go to “Settings -> Mobile” and enter your phone number if it’s not already there.
4. Go to http://apps.twitter.com, and click “Create New App”
5. Enter info for each field. Make sure that the optional Callback URL field has a value (can be any url, like http://blablahblah.blah), otherwise you won’t be able to log in. 
6. Scroll down to Application Settings after successfully creating your app. Find “Consumer Key” and click on “manage keys and access tokens.”
7. Find the “Your Access Token” section and click on the “Create my access token” button.
8.  You should now see your Consumer Key (API Key), Consumer Secret (API Secret), Access Token, and Access Token Secret values. Copy all four of these: you’ll need them to connect to the Twitter API.



