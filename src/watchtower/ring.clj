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
  [:tr 
    [:td
      (if (successful? job)
        [:span {:class "btn btn-success"} "OK"]
        [:span {:class "label label-important"} "FAIL"])]
    [:td {:width "100%"}
      (:name job)]])

(defn index []
  (html5 {:lang "en"}
    [:head
      [:title "Watchtower"]
      (include-css "/bootstrap/css/bootstrap.min.css")]
    [:body 
      [:div {:class "container-fluid"}
        [:h1 "Watchtower"]
        [:table {:class "table"}
          (map job-row (jenkins/jobs))]
        [:script {:src "http://code.jquery.com/jquery-latest.js"}]
        (include-js "/bootstrap/js/bootstrap.min.js")]]))


(defroutes app-routes
  (GET "/" [] (index))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
