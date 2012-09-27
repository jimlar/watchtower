(ns watchtower.ring
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [watchtower.views :as views]
            [watchtower.config :as config]))

(defroutes app-routes
  (context config/app-root []
    (GET "/" [theme] (views/index theme))
    (GET "/jobs.json" [] (views/jobs))
    (route/resources "/")
    (route/not-found "Not Found"))
  (GET "/" [] {:status 302 :headers {"Location" config/app-root}})
  (ANY "*" [] {:status 404}))

(def app
  (handler/site app-routes))
