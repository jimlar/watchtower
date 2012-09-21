(ns watchtower.config
  (:require [clojure.java.io :as javaio]))

(defn- load-props [file]
  (with-open [^java.io.Reader reader (javaio/reader file)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) v])))))

; Local properties is an optional config file
(def config-props
  (let [file (java.io.File. "config.properties")]
    (if (.exists file)
      (load-props "config.properties")
      {})))

; Search for config in local.properties fallback to system env (i.e heroku)
(defn value [key]
  (get config-props key (get (System/getenv) (name key))))

(defn int-value [key]
  (Integer. (value key)))
