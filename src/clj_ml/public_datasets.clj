(ns clj-ml.public-datasets
  (:require [clojure.data.xml :as xml])
  (:use [clojure.java.io :only [reader file]]))

(defn reuters21578-docs
  "Unzip the file from http://modnlp.berlios.de/reuters21578.html into reuters-xml-dir"
  [reuters-xml-dir]
  (let [files (filter (fn [f] (re-matches #"reut2-\d+.xml" (.getName f)))
                      (file-seq (file reuters-xml-dir)))]
    (for [f files
          xmldoc (:content (xml/parse (reader f)))]
      {:id (:NEWID (:attrs xmldoc))
       :topics (for [topic (:content (second (:content xmldoc)))]
                 (first (:content topic)))
       :fulltext (first (:content (last (:content (last (:content xmldoc))))))})))
