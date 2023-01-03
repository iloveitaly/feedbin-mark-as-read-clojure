# Automatically Mark Articles Older Than a Month as Read in Feedbin

This is a small side project to learn Clojure. Here's more information:

https://mikebian.co/learning-clojure-with-feed-automation

## Development

* `lein run` - run the app, useful for testing
* `http https://api.feedbin.com/v2/unread_entries.json --auth=$FEEDBIN_AUTH` - test the api via the cli
* clean system cache `rm -rf ~/.m2 ~/.lein`