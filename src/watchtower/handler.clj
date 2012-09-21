(ns watchtower.handler
  (:use compojure.core)
  (:use hiccup.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [watchtower.jenkins :as jenkins]))

(defn- job-row [job]
  [:li (str (:name job) " " (:result (:lastBuild job)))])

(defn index []
  (html 
    [:html 
      [:body 
        [:h1 "Watchtower"]
        [:ul
          (map job-row (jenkins/jobs))
          ]]]))


(defroutes app-routes
  (GET "/" [] (index))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
