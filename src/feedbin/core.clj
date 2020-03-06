(ns feedbin.core
  (:require [clj-http.client :as client]
            [clj-time.format]
            [clj-time.core :refer [months ago before?]]
            [cheshire.core :as json]))

(use 'debugger.core)

; Filter list by all entries that are older than 2w
; since we'll just have the IDs, we'll want to pull each entry individually
; https://github.com/feedbin/feedbin-api/blob/master/content/entries.md
; GET /v2/entries.json?ids=4088,4089,4090,4091

; Now that we have a list of all entries that should be marked as read, let's
; mark each of them as unread
; https://github.com/feedbin/feedbin-api/blob/master/content/updated-entries.md#delete-updated-entries-mark-as-read
; DELETE /v2/updated_entries.json "updated_entries": [1,2,3]

; The API uses HTTP basic auth. FEEDBIN_AUTH should be "email:password"
(def feedbin-auth (System/getenv "FEEDBIN_AUTH"))

(def feedbin-base-url "https://api.feedbin.com/")

(defn one-month-old? [entry-id]
  (def entry
    (json/parse-string (:body
      (client/get (str feedbin-base-url "v2/entries/" entry-id ".json") {:basic-auth feedbin-auth}))))

  (before?
    (clj-time.format/parse (clj-time.format/formatters :date-time) (entry "published"))
    (-> 1 months ago)))

(defn mark-as-read [entry-ids])

(defn -main []
  ; Get all unread entries
  ; https://github.com/feedbin/feedbin-api/blob/master/content/unread-entries.md
  (def unread-entries (json/parse-string (:body
    (client/get (str feedbin-base-url "v2/unread_entries.json") {:basic-auth feedbin-auth}))))

  (println
    (mark-as-read (filter one-month-old? unread-entries))
  )
)