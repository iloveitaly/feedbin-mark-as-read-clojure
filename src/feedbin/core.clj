(ns feedbin.core
  (:require [clj-http.client :as client]
            [clj-time.format]
            [clojure.string :as str]
            [clj-time.core :refer [months ago before?]]
            [cheshire.core :as json]))

(use 'debugger.core)

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

  (if
    (before?
      (clj-time.format/parse (clj-time.format/formatters :date-time) (entry "published"))
      (-> 1 months ago))
    (do
      (println (entry "url"))
      true
    )
    false))

(defn mark-as-read [entry-ids]
  ; let's mark each of the entries as read
  ; https://github.com/feedbin/feedbin-api/blob/master/content/updated-entries.md#delete-updated-entries-mark-as-read
  (break true)
  (client/delete
    (str feedbin-base-url "v2/updated_entries.json")
    {:basic-auth feedbin-auth :debug true :debug-body true :form-params {:updated_entries entry-ids} :content-type :json }))

(defn -main []
  ; Get all unread entries
  ; https://github.com/feedbin/feedbin-api/blob/master/content/unread-entries.md
  (def unread-entries (json/parse-string (:body
    (client/get (str feedbin-base-url "v2/unread_entries.json") {:basic-auth feedbin-auth}))))

  (mark-as-read (filter one-month-old? (take 10 unread-entries)))
)