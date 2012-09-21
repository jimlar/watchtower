(ns watchtower.ring
  (:use compojure.core)
  (:use hiccup.core)
  (:use hiccup.page)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [watchtower.jenkins :as jenkins]))

(defn- successful? [job]
  (= "SUCCESS" (:result (:lastBuild job))))

(defn- job-row [job]
  [:li 
    (if (successful? job)
      [:span {:class "label label-success"} "OK"]
      [:span {:class "label label-important"} "FAIL"])
    " " 
    (:name job)])

(defn index []
  (html5 {:lang "en"}
    [:head
      [:title "Watchtower"]
      (include-css "/bootstrap/css/bootstrap.min.css")]
    [:body 
      [:h1 "Watchtower"]
      [:ul {:class "unstyled"} (map job-row (jenkins/jobs))]
      [:script {:src "http://code.jquery.com/jquery-latest.js"}]
      (include-js "/bootstrap/js/bootstrap.min.js")]))


(defroutes app-routes
  (GET "/" [] (index))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
