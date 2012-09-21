(ns watchtower.jenkins
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [watchtower.config :as config]))

(defn- jenkins-status []
  (json/parse-string
    (:body 
      (client/get 
        (str (config/value :jenkins.url) "/api/json?depth=5&tree=jobs[name,buildable,url,lastBuild[building,number,result,timestamp,culprit],lastCompletedBuild[building,number,result,timestamp,culprit]]")
        {:basic-auth [(config/value :jenkins.user) (config/value :jenkins.password)]}))
    true))
  
(defn jobs []
  (sort-by :name (:jobs (jenkins-status))))
