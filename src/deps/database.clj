(ns deps.database
    (:require [com.stuartsierra.component :as component])
    (:import [com.zaxxer.hikari HikariConfig HikariDataSource]))

; https://github.com/weavejester/duct-hikaricp-component

(defn- make-config
  [{:keys [uri username password auto-commit? conn-timeout idle-timeout
           max-lifetime conn-test-query min-idle max-pool-size pool-name]}]
  (let [cfg (HikariConfig.)]
    (when uri                  (.setJdbcUrl cfg uri))
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

(defrecord Database []
  component/Lifecycle
  
  (start [component]
    (println "; Starting connection pool")
    (if (:db-spec component)
      component
      (assoc component :db-spec (make-spec component))))

  (stop [component]
    (println "; Stopping connection pool")
    (if-let [db-spec (:db-spec component)]
      (do (.close (:datasource db-spec))
          (dissoc component :db-spec))
      component)))

(defn new-database [opts]
  (map->Database opts))
