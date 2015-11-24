(ns deps.repo-importer
  (:require [com.stuartsierra.component :as component]))

(defrecord RepoImporter [opts database]
  component/Lifecycle

  (start [component]
    (println "; Starting repo-importer")
    component)

  (stop [component]
    (println "; Stoping repo-importer")
    component))

(defn new-repo-importer [opts]
  (map->RepoImporter opts))
