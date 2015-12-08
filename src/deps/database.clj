(ns deps.database
    (:require [com.stuartsierra.component :as component])
    (:import [com.zaxxer.hikari HikariConfig HikariDataSource]))

; https://github.com/weavejester/duct-hikaricp-component

(defn- make-config
  [{:keys [url username password auto-commit? conn-timeout idle-timeout
           max-lifetime conn-test-query min-idle max-pool-size pool-name]}]
  (let [cfg (HikariConfig.)]
    (when url                  (.setJdbcUrl cfg url))
    (when username             (.setUsername cfg username))
    (when password             (.setPassword cfg password))
    (when (some? auto-commit?) (.setAutoCommit cfg auto-commit?))
    (when conn-timeout         (.setConnectionTimeout cfg conn-timeout))
    (when idle-timeout         (.setIdleTimeout cfg conn-timeout))
    (when max-lifetime         (.setMaxLifetime cfg max-lifetime))
    (when max-pool-size        (.setMaximumPoolSize cfg max-pool-size))
    (when min-idle             (.setMinimumIdle cfg min-idle))
    (when pool-name            (.setPoolName cfg pool-name))
    (.setDataSourceClassName cfg "org.postgresql.ds.PGSimpleDataSource")
    cfg))

(defn- make-spec [component]
  {:datasource (HikariDataSource. (make-config component))})

(defrecord Database [url]
  component/Lifecycle
  
  (start [component]
    (println "; Starting connection pool")
    (if (:pool component)
      component
      (assoc component :pool (make-spec component))))

  (stop [component]
    (println "; Stopping connection pool")
    (if-let [pool (:pool component)]
      (do (.close (:datasource pool))
          (dissoc component :pool))
      component)))

(defn new-database [opts]
  {:pre [(:url opts)]}
  (map->Database opts))
