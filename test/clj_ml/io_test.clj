(ns clj-ml.io-test
  (:use [clj-ml io data] :reload-all)
  (:use clojure.test midje.sweet))

(deftest load-instances-iris-url
  (let [ds (load-instances :arff "http://repository.seasr.org/Datasets/UCI/arff/iris.arff")]
    (is (= 150 (dataset-count ds)))))
