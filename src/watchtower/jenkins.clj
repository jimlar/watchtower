(ns watchtower.jenkins
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [watchtower.config :as config]))

(defn- jenkins-status []
  (json/parse-string
    (:body 
      (client/get 
        (str (config/value :jenkins.url) "/api/json?tree=jobs[name,buildable,url,lastBuild[building,number,result,timestamp,culprits[fullName]],lastCompletedBuild[building,number,result,timestamp,culprits[fullName]]]")
        {:basic-auth [(config/value :jenkins.user) (config/value :jenkins.password)]}))
    true))
  
(defn jobs []
  (defn add-id [job]
    (assoc job :id (apply str (filter (fn [c] (Character/isLetter c)) (:name job)))))
  (map add-id (sort-by :name (:jobs (jenkins-status)))))

(defn successful? [job]
  (= "SUCCESS" (:result (:lastCompletedBuild job))))

(defn culprits [job]
  (map :fullName (:culprits (:lastCompletedBuild job))))
