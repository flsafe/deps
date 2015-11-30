(ns deps.repo
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :refer [timeout alt!! >!! <!! thread]]
            [clj-http.client :as http]))

(defn download-repo [repo-url]
  (println "; Repo importer worker is downloading..")
  (http/get repo-url {:as :json}))

(defn start-worker [input output shutdown]
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

(defn start-workers [number-of-workers input output shutdown]
  (doall
   (map (fn [_] (start-worker input output shutdown))
        (range number-of-workers))))

(defrecord RepoImporter [opts channels]
  component/Lifecycle
  (start [component]
    (println "; Starting repo importer")
    (assoc component
           :repo-download-workers 
           (start-workers 1
                          (:download-repos channels)
                          (:save-repos channels)
                          (:shutdown channels)))
    component)
  (stop [component]
    (println "; Stopping repo importer")
    (dissoc component :repo-download-workers)
    component))

(defn new-repo-importer [opts]
  (map->RepoImporter opts))

(defn start-poller [output shutdown]
  (thread
    (println "; Poller start")
    (loop []
      (alt!!
        shutdown
        ([_]
         (println "; Repo poller shutdown"))

        (timeout 50)
        ([_]
         (>!! output (rand-nth ["http://jsonplaceholder.typicode.com/photos"
                               "http://jsonplaceholder.typicode.com/comments"]))
         (recur))))))

(defrecord RepoPoller [opts channels]
  component/Lifecycle
  (start [component]
    (println "; Starting repo poller")
    (assoc component :repo-poller (start-poller (:download-repos channels)
                                                (:shutdown channels)))
    component)
  (stop [component]
    (println "; Stopping repo poller")
    (dissoc component :repo-poller)
    component))

(defn new-repo-poller [opts]
  (map->RepoPoller opts))
