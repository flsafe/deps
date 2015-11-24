(ns deps.user
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :as namespace]
            [deps.core :as core]))

(def system nil)

(defn init []
  (alter-var-root #'system
                  (constantly (core/deps-system {}))))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (namespace/refresh :after 'deps.user/go))
