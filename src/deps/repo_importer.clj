(ns deps.repo-importer
  (:require [com.stuartsierra.component :as component]
            [com.core.aysnc :refer [>!! <!! thread]
             [clj-http.client :refer [get]]]))

(defn download-repo [repo-url]
  (println "; Repo importer worker is downloading..")
  (get "http://jsonplaceholder.typicode.com/photos"
       {:as :json}))

(defn start-worker
  [input output shutdown]
  (thread
    (println "; Repo importer worker start")
    (loop []
      (alt!!
        shutdown 
        ([_]
         (println "; Repo importer Worker shutting down"))

        input
        ([repo-url]
         (>!! output (download-repo repo-url))
         (recur))))))

(defn start-workers [n input output shutdown]
  (doall (map (partial start-worker input output shutdown) (range n))))

(defrecord RepoImporter [opts]
  component/Lifecycle
  (start [component]
    (println "; Starting repo-importer")
    
    component)
  (stop [component]
    (println "; Stoping repo-importer")
    component))

(defn new-repo-importer [opts]
  (map->RepoImporter opts))
