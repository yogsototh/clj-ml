(ns clj-ml.io-test
  (:require [clojure.string :as str])
  (:use [clj-ml io data] :reload-all)
  (:use clojure.test midje.sweet))

(deftest test-load-instances-iris-arff-url
  (let [ds (load-instances :arff "http://repository.seasr.org/Datasets/UCI/arff/iris.arff")]
    (is (= 150 (dataset-count ds)))))

(deftest test-load-instances-iris-csv-url
  (let [ds (load-instances :csv "https://raw.github.com/bigmlcom/bigmler/master/data/iris.csv")]
    (is (= 150 (dataset-count ds)))))

(deftest test-save-instances
  (let [ds (make-dataset "test" [:a :b {:c [:m :n]}] [[1 2 :m] [4 5 :m]])]
    (is (= (do (save-instances :csv "test.csv" ds)
               (str/replace (slurp "test.csv") #"\r\n" "\n"))
           "a,b,c\n1,2,m\n4,5,m\n"))
    (is (= (do (save-instances :arff "test.arff" ds)
               (str/replace (slurp "test.arff") #"\r\n" "\n"))
           "@relation test\n\n@attribute a numeric\n@attribute b numeric\n@attribute c {m,n}\n\n@data\n1,2,m\n4,5,m\n"))))

