(defproject deps "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.async "0.2.374"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/data.json "0.2.6"]
                 [joplin.core "0.3.4"]
                 [joplin.jdbc "0.3.4"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.postgresql/postgresql "9.4-1205-jdbc41"]
                 [com.stuartsierra/component "0.3.0"]
                 [com.zaxxer/HikariCP "2.4.1"]
                 [clj-http "2.0.0"]]
  :main ^:skip-aot deps.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
