(ns deps.migrate
  (:require [joplin.repl :as jrepl]
            [clojure.java.io :as io]))

(def mconfig
  (jrepl/load-config (io/resource "config.edn")))

(defn get-clj-env []
  (or (System/getenv "CLJ_ENV")
      "dev"))

(defn get-sql-url []
  (let [db (keyword (str "sql-" (get-clj-env)))]
    (get-in mconfig [:databases db :url])))
