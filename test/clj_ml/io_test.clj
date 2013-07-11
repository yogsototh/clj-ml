(ns clj-ml.io-test
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
               (slurp "test.csv"))
           "a,b,c
1,2,m
4,5,m
"))
    (is (= (do (save-instances :arff "test.arff" ds)
               (slurp "test.arff"))
           "@relation test

@attribute a numeric
@attribute b numeric
@attribute c {m,n}

@data
1,2,m
4,5,m
"))))

