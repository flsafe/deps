(ns deps.migrate
  (:require [joplin.repl :as jrepl]
            [clojure.java.io :as io]))

(def mconfig
  (jrepl/load-config (io/resource "config.edn")))
