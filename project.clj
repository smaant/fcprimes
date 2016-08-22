(defproject fcprimes "0.1.0-SNAPSHOT"
  :description "Prime numbers generator"
  :url "https://github.com/smaant/fcprimes"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]]

  :profiles {:dev {:dependencies [[criterium "0.4.4"]
                                  [org.clojure/tools.namespace "0.3.0-alpha3"]]}

             :uberjar {:aot :all}}

  :main smaant.primes

  :source-paths ["src/clj"]
  :java-source-paths ["src/java"])
