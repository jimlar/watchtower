(ns watchtower.ring
  (:use clojure.java.io)
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
        [:span {:class "btn btn-large btn-success"} "OK"]
        [:span {:class "btn btn-large btn-danger"} "FAIL"])]
    [:td {:width "100%"}
      [:h4 (:name job)]]])

(defn- themes []
  (seq (.list (file "resources/public/bootstrap/themes"))))

(defn- theme-selector [theme]
  [:div {:class "btn-group"}
    [:a {:class "btn dropdown-toggle" :data-toggle "dropdown" :href "#"}
      "Change Theme"
      [:span {:class "caret"}]]
    [:ul {:class "dropdown-menu"}
      (map (fn [t] [:a {:href (str "?theme=" t)} t]) (themes))]])

;  [:span {:class "btn btn-large btn-success"} current])

(defn index [theme]
  (let [theme (if (empty? theme) "slate" theme)]
    (html5 {:lang "en"}
      [:head
        [:title "Watchtower"]
        (include-css (str "/bootstrap/themes/" theme "/bootstrap.min.css"))]
      [:body 
        [:div {:class "container-fluid"}
          [:div {:class "row-fluid"}
            [:div {:class "span10"}
              [:h1 "Watchtower"]]
            [:div {:class "span2"}
              (theme-selector theme)]]
          [:table {:class "table"}
            (map job-row (jenkins/jobs))]
          [:script {:src "http://code.jquery.com/jquery-latest.js"}]
          (include-js "/bootstrap/js/bootstrap.min.js")]])))


(defroutes app-routes
  (GET "/" [theme] (index theme))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
