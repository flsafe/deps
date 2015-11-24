(ns deps.core
  (:require [com.stuartsierra.component :as component]
            [deps.database :as db]
            [deps.repo-importer :as repo])
  (:gen-class))

(defn deps-system [opts]
  (component/system-map
   :database (db/new-database opts)
   :repo-importer (component/using  (repo/new-repo-importer opts)
                                    [:database])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
