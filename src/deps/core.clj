(ns deps.core
  (:require [com.stuartsierra.component :as component]
            [deps.database :as db]
            [deps.repo-importer :as repo])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
