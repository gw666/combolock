(defproject combolock "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :main combolock.core

  :dependencies [[org.clojure/clojure "1.3.0"]
                 ]
  :dev-dependencies [[midje "1.3.1"]
                     [clojure-source "1.3.0"]
                     [swank-clojure "1.4.0"]]
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n"])