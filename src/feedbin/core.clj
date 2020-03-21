(ns feedbin.core
  (:gen-class
      :implements [com.amazonaws.services.lambda.runtime.RequestHandler]
      :methods [^:static [handler [Object com.amazonaws.services.lambda.runtime.Context] String]])
  (:require [clj-http.client :as client]
            [clj-time.format]
            [clojure.string :as str]
            [clj-time.core :refer [months ago before?]]
            [cheshire.core :as json]))

(use 'debugger.core)

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
  ; let's mark each of the entries as read, which essentially deletes them
  ; https://github.com/feedbin/feedbin-api/blob/master/content/updated-entries.md#delete-updated-entries-mark-as-read
  (client/delete
    (str feedbin-base-url "v2/unread_entries.json")
    {:basic-auth feedbin-auth :debug true :debug-body true :form-params {:unread_entries entry-ids} :content-type :json }))

(defn -main []
  ; Get all unread entries
  ; https://github.com/feedbin/feedbin-api/blob/master/content/unread-entries.md
  (def unread-entries (json/parse-string (:body
    (client/get (str feedbin-base-url "v2/unread_entries.json") {:basic-auth feedbin-auth}))))

  (mark-as-read (filter one-month-old? (take 50 unread-entries)))
)

(defn -handler [o s]
  (-main)
  (println "feeds updated"))