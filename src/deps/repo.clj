(ns deps.repo
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :refer [timeout alt!! >!! <!! thread]]
            [clojure.data.json :as json]
            [clj-http.client :as http]))

(defn download-repo [repo-url]
  (json/read-json (:body (http/get repo-url {:as :json}))))

(defn start-worker [download-repos save-repos shutdown]
  (thread
    (println "; Repo importer worker start")
    (loop []
      (alt!!
        shutdown 
        ([_]
         (println "; Repo importer Worker shutting down"))

        download-repos
        ([repo-url]
         (>!! save-repos (download-repo repo-url))
         (recur))))))

(defn start-workers [number-of-workers download-repos save-repos shutdown]
  (doall
   (map (fn [_] (start-worker download-repos save-repos shutdown))
        (range number-of-workers))))

(defrecord RepoDownloader [opts channels]
  component/Lifecycle
  (start [component]
    (println "; Starting repo importer")
    (assoc component
           :repo-download-workers 
           (start-workers 4
                          (:download-repos channels)
                          (:save-repos channels)
                          (:shutdown channels)))
    component)
  (stop [component]
    (println "; Stopping repo importer")
    (dissoc component :repo-download-workers)
    component))

(defn new-repo-downloader [opts]
  (map->RepoDownloader opts))

(defn start-poller [download-repos shutdown]
  (thread
    (println "; Poller start")
    (loop []
      (alt!!
        shutdown
        ([_]
         (println "; Repo poller shutdown"))

        (timeout 20)
        ([_]
         (>!! download-repos (rand-nth ["http://jsonplaceholder.typicode.com/photos"
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
