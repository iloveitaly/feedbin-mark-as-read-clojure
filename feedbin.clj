(ns feedbin)

; The API uses HTTP basic auth. We'll need the username & password defined in a variable here

; Get all unread entries
; https://github.com/feedbin/feedbin-api/blob/master/content/unread-entries.md
; GET https://api.feedbin.com/v2/unread_entries.json

; Filter list by all entries that are older than 2w
; since we'll just have the IDs, we'll want to pull each entry individually
; https://github.com/feedbin/feedbin-api/blob/master/content/entries.md
; GET /v2/entries.json?ids=4088,4089,4090,4091

; Now that we have a list of all entries that should be marked as read, let's
; mark each of them as unread
; https://github.com/feedbin/feedbin-api/blob/master/content/updated-entries.md#delete-updated-entries-mark-as-read
; DELETE /v2/updated_entries.json "updated_entries": [1,2,3]