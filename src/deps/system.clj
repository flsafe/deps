(ns deps.system
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :refer [<! go-loop chan close!]]
            [deps.database :as db]
            [deps.repo :as repo]))

(defrecord Channels [download-repos save-repos shutdown]
    component/Lifecycle
  (start [component]
    (assoc component
           :download-repos (chan)
           :save-repos (chan)
           :shutdown (chan)))
  (stop [component]
    (close! download-repos)
    (close! save-repos)
    (close! shutdown)
    (dissoc component
            :download-repos
            :save-repos
            :shutdown)))

(defrecord PrintSink [channel-key channels]
  component/Lifecycle
  (start [component]
    (go-loop [msg (<! (get channels channel-key))]
      (when msg 
        (println (str "-> downloaded " (count msg) " items"))
        (recur (<! (get channels channel-key))))))
  (stop [component]
    component))

(defn deps-system [opts]
  (component/system-map
   :channels (map->Channels {})
   :database (db/new-database opts)
   :repo-poller (component/using (repo/new-repo-poller opts)
                                 [:channels])
   :repo-downloader (component/using (repo/new-repo-downloader opts)
                                      [:channels])
   :print-sink (component/using
                (map->PrintSink {:channel-key :save-repos})
                [:channels])))
