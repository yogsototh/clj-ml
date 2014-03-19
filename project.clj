(defproject cc.artifice/clj-ml "0.5.0-SNAPSHOT"
  :description "Machine Learning library for Clojure built around Weka and friends"
  :java-source-paths ["src/java"]
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :url "https://github.com/joshuaeckroth/clj-ml"
  :dependencies [[nz.ac.waikato.cms.weka/weka-dev "3.7.10"]
		 [nz.ac.waikato.cms.weka/chiSquaredAttributeEval "1.0.3"]
		 [nz.ac.waikato.cms.weka/attributeSelectionSearchMethods "1.0.6"]
		 [nz.ac.waikato.cms.weka/linearForwardSelection "1.0.1"]
		 [nz.ac.waikato.cms.weka/rotationForest "1.0.3"]
		 [nz.ac.waikato.cms.weka/paceRegression "1.0.2"]
		 [nz.ac.waikato.cms.weka/SPegasos "1.0.2"]
		 [nz.ac.waikato.cms.weka/LibSVM "1.0.5"]
                 [org.clojars.remleduff/snowball "20051019-1"]
		 [junit/junit "4.11"]
                 [tw.edu.ntu.csie/libsvm "3.1"]
                 [org.clojure/data.xml "0.0.7"]
                 [org.apache.lucene/lucene-analyzers-common "4.3.0"]]
  :profiles {:dev
             {:plugins [[lein-midje "2.0.0"]]
              :dependencies [[midje "1.4.0"]]}}
  :codox {:output-dir "website/doc"
          :src-dir-uri "http://github.com/joshuaeckroth/clj-ml/blob/master"
          :src-linenum-anchor-prefix "L"})
