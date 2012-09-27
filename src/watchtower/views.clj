(ns watchtower.views
  (:use clojure.java.io)
  (:use hiccup.core)
  (:use hiccup.page)
  (:require [watchtower.jenkins :as jenkins]
            [watchtower.config :as config]
            [cheshire.core :as json]))

(def app-root (config/value :root.path))

(defn- themes []
  (seq (.list (file "resources/public/bootstrap/themes"))))

(defn- job-row [job]
  [:tr {:id (:name job)}
    [:td
      (if (jenkins/successful? job)
        [:span.btn.btn-large.btn-success "OK"]
        [:span.btn.btn-large.btn-danger "FAIL"])]
    [:td {:width "100%"}
      [:h4 (:name job) " " 
        (if-not (jenkins/successful? job)
                (mapcat 
                  (fn [culprit] [[:span.label.label-info culprit] " "]) 
                  (jenkins/culprits job)))]]])


(defn- theme-selector [theme]
  [:li.dropdown
    [:a.dropdown-toggle {:data-toggle "dropdown" :href "#"} "Theme" [:span.caret]]
    [:ul.dropdown-menu
      (map (fn [t] [:a {:href (str "?theme=" t)} t]) (themes))]])

(defn index [theme]
  (let [theme (if (empty? theme) "amelia" theme)]
    (html5 {:lang "en"}
      [:head
        [:title "Watchtower"]
        (include-css (str app-root "/bootstrap/themes/" theme "/bootstrap.min.css"))]
      [:body 
        [:div.container-fluid
          [:div.navbar
            [:div.navbar-inner
              [:a.brand {:href "#"} "Watchtower"]
              [:ul.nav.pull-right
                (theme-selector theme)]]]
          [:table.table
            (map job-row (jenkins/jobs))]
          [:script {:src "http://code.jquery.com/jquery-latest.js"}]
          (include-js (str app-root "/bootstrap/js/bootstrap.min.js"))
          (include-js (str app-root "/js/watchtower.js"))]])))

(defn jobs [] 
  (defn filter [job]
    {:id (:name job) :name (:name job) :successful (jenkins/successful? job) :culprits (jenkins/culprits job)})
  (json/generate-string (map filter (jenkins/jobs))))