(defproject cc.artifice/clj-ml "0.3.10"
  :description "Machine Learning library for Clojure built around Weka and friends"
  :java-source-paths ["src/java"]
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :url "https://github.com/joshuaeckroth/clj-ml"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [incanter/incanter-core "1.4.1"]
                 [incanter/incanter-charts "1.4.1"]
                 [nz.ac.waikato.cms.weka/weka-stable "3.6.9"]
                 [tw.edu.ntu.csie/libsvm "3.1"]
                 [org.jsoup/jsoup "1.7.2"]
                 [org.apache.lucene/lucene-analyzers-common "4.3.0"]
                 [org.clojars.chapmanb/fast-random-forest "0.98"]]
  :profiles {:dev
             {:plugins [[lein-midje "2.0.0"]]
              :dependencies [[midje "1.4.0"]]}})
