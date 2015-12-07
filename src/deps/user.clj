(ns deps.user
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :as namespace]
            [deps.system :as system]
            [deps.migrate :as m]))

(def system nil)

(defn init []
  (alter-var-root #'system
                  (constantly
                   (system/deps-system {:database-uri (m/get-sql-url)
                                        :username "deps_frank"}))))

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
