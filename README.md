# clj-ml

A machine learning library for Clojure built on top of Weka and friends.

## Installation

### Installing from Clojars

    [cc.artifice/clj-ml "0.4.0-SNAPSHOT"]

### Installing from Maven

(add Clojars repository)

    <dependency>
      <groupId>cc.artifice</groupId>
      <artifactId>clj-ml</artifactId>
      <version>0.4.0-SNAPSHOT</version>
    </dependency>

## Supported algorithms

 * Filters
   * Discretization (supervised, unsupervised, PKI)
   * Nominal to binary (supervised, unsupervised)
   * Numeric to nominal
   * String to word vector
   * Attribute manipulation (reorder, add, remove range, remove percentage, etc.)
   * Resample (supervised, unsupervised)

 * Classifiers
   * k-Nearest neighbor
   * Decision trees: C4.5/J4.8, Boosted stump, Random forest, Rotation forest, M5P
   * Naive Bayes
   * Multilayer perceptrons
   * Support vector machines (grid-based training), SMO, Spegasos

* Regression
   * Linear
   * Logistic
   * Pace
   * Additive gradient boosting

* Clusterers
   * k-Means
   * Cobweb
   * Expectation-maximization

## Usage

API documenation can be found [here](http://clj-ml.artifice.cc/doc/index.html).

### I/O of data

```clojure
user> (use 'clj-ml.io)
nil

user> (def ds (load-instances :arff "file:///home/josh/git/clj-ml/iris.arff"))
#'user/ds
user> ds
#<Instances @relation iris

@attribute sepallength numeric
@attribute sepalwidth numeric
@attribute petallength numeric
@attribute petalwidth numeric
@attribute class {Iris-setosa,Iris-versicolor,Iris-virginica}

@data
5.1,3.5,1.4,0.2,Iris-setosa
4.9,3,1.4,0.2,Iris-setosa
4.7,3.2,1.3,0.2,Iris-setosa
4.6,3.1,1.5,0.2,Iris-setosa
5,3.6,1.4,0.2,Iris-setosa
5.4,3.9,1.7,0.4,Iris-setosa
4.6,3.4,1.4,0.3,Iris-setosa
...

user> (def ds (load-instances :arff "http://repository.seasr.org/Datasets/UCI/arff/iris.arff"))
#'user/ds

user> (save-instances :csv "iris.csv" ds)
nil
user> (println (slurp "iris.csv"))
sepallength,sepalwidth,petallength,petalwidth,class
5.1,3.5,1.4,0.2,Iris-setosa
4.9,3,1.4,0.2,Iris-setosa
4.7,3.2,1.3,0.2,Iris-setosa
4.6,3.1,1.5,0.2,Iris-setosa
5,3.6,1.4,0.2,Iris-setosa
5.4,3.9,1.7,0.4,Iris-setosa
4.6,3.4,1.4,0.3,Iris-setosa
5,3.4,1.5,0.2,Iris-setosa
4.4,2.9,1.4,0.2,Iris-setosa
4.9,3.1,1.5,0.1,Iris-setosa
5.4,3.7,1.5,0.2,Iris-setosa
...

user> (def ds (load-instances :csv "file:///home/josh/git/clj-ml/iris.csv"))
#'user/ds
user> ds
#<Instances @relation stream

@attribute sepallength numeric
@attribute sepalwidth numeric
@attribute petallength numeric
@attribute petalwidth numeric
@attribute class {Iris-setosa,Iris-versicolor,Iris-virginica}

@data
5.1,3.5,1.4,0.2,Iris-setosa
4.9,3,1.4,0.2,Iris-setosa
4.7,3.2,1.3,0.2,Iris-setosa
4.6,3.1,1.5,0.2,Iris-setosa
5,3.6,1.4,0.2,Iris-setosa
5.4,3.9,1.7,0.4,Iris-setosa
4.6,3.4,1.4,0.3,Iris-setosa
5,3.4,1.5,0.2,Iris-setosa
```

### Working with datasets

```clojure
user> (use 'clj-ml.data)
nil

user> (def ds (make-dataset"my-name" [:length :width {:style nil} {:kind [:good :bad]}]
                            [[12 24 "longish" :good]
                             [8 5 "shortish" :bad]]))
#'user/ds
user> ds
#<ClojureInstances @relation my-name

@attribute length numeric
@attribute width numeric
@attribute style string
@attribute kind {good,bad}

@data
12,24,longish,good
8,5,shortish,bad>

user> (dataset-seq ds)
(#<Instance 12,24,longish,good> #<Instance 8,5,shortish,bad>)

user> (map instance-to-map (dataset-seq ds))
({:kind :good, :style "longish", :width 24.0, :length 12.0}
{:kind :bad, :style "shortish", :width 5.0, :length 8.0})

user> (map instance-to-vector (dataset-seq ds))
([12.0 24.0 "longish" :good] [8.0 5.0 "shortish" :bad])
```

### Filtering datasets

```clojure
user> (use 'clj-ml.filters 'clj-ml.io)
nil

user> (def ds (load-instances :csv "file:///home/josh/git/clj-ml/iris.csv"))
#'user/ds

user> (def discretize (make-filter :unsupervised-discretize
                                   {:dataset-format ds
                                    :attributes [:sepallength :petallength]}))
#'user/discretize

user> (def filtered-ds (filter-apply discretize ds))
#'user/filtered-ds

user> (map instance-to-map (dataset-seq filtered-ds))
({:class :Iris-setosa, :petalwidth 0.2, :petallength :'(-inf-1.59]',
 :sepalwidth 3.5, :sepallength :'(5.02-5.38]'}
{:class :Iris-setosa, :petalwidth 0.2, :petallength :'(-inf-1.59]',
 :sepalwidth 3.0, :sepallength :'(4.66-5.02]'}
{:class :Iris-setosa, :petalwidth 0.2, :petallength :'(-inf-1.59]',
 :sepalwidth 3.2, :sepallength :'(4.66-5.02]'}
{:class :Iris-setosa, :petalwidth 0.2, :petallength :'(-inf-1.59]',
 :sepalwidth 3.1, :sepallength :'(-inf-4.66]'}
{:class :Iris-setosa, :petalwidth 0.2, :petallength :'(-inf-1.59]',
 :sepalwidth 3.6, :sepallength :'(4.66-5.02]'}
...) ;; the petallength and sepallength attributes are now nominal
```

Equivalently,

```clojure
user> (def filtered-ds (->> "file:///home/josh/git/clj-ml/iris.csv"
                            (load-instances :csv)
                            (make-apply-filter :unsupervised-discretize
                                               {:attributes [:sepallength :petallength]})))
```

### Using classifiers

```clojure
user> (use 'clj-ml.classifiers 'clj-ml.data 'clj-ml.utils)
nil

user> (def ds (-> (load-instances :arff "file:///home/josh/git/clj-ml/iris.arff")
                  (dataset-set-class :class)))
#'user/ds

user> (def classifier (-> (make-classifier :decision-tree :c45)
                          (classifier-train ds)))
#'user/classifier

user> (def instance (-> (first (dataset-seq ds))
                        (instance-set-class-missing)))

user> (classifier-classify classifier instance)
:Iris-setosa
```

Evaluation:

```clojure
user> (def evaluation (classifier-evaluate classifier :cross-validation ds 10))
#'user/evaluation

user> (clojure.pprint/pprint (dissoc evaluation :summary :confusion-matrix))
{:incorrect 7.0,
 :root-relative-squared-error 36.693518966642074,
 :sf-entropy-gain -4076.3670930399717,
 :recall
 {:Iris-setosa 0.9795918367346939,
  :Iris-versicolor 0.94,
  :Iris-virginica 0.94},
 :kb-information 217.7935138195151,
 :kb-relative-information 13741.240800360849,
 :false-positive-rate
 {:Iris-setosa 0.0,
  :Iris-versicolor 0.04040404040404041,
  :Iris-virginica 0.030303030303030304},
 :percentage-correct 95.30201342281879,
 :roc-area
 {:Iris-setosa 0.984845423317842,
  :Iris-versicolor 0.9456,
  :Iris-virginica 0.9496},
 :kb-mean-information 1.4617014350303028,
 :percentage-unclassified 0.0,
 :percentage-incorrect 4.697986577181208,
 :root-mean-squared-error 0.17297908222448935,
 :unclassified 0.0,
 :correlation-coefficient
 {:nan "Can't compute correlation coefficient: class is nominal!"},
 :correct 142.0,
 :sf-mean-entropy-gain -27.358168409664238,
 :mean-absolute-error 0.04083212821368881,
 :relative-absolute-error 9.187228848079984,
 :error-rate 0.04697986577181208,
 :kappa 0.9295222650179066,
 :f-measure
 {:Iris-setosa 0.9896907216494846,
  :Iris-versicolor 0.9306930693069307,
  :Iris-virginica 0.94},
 :false-negative-rate
 {:Iris-setosa 0.02040816326530612,
  :Iris-versicolor 0.06,
  :Iris-virginica 0.06},
 :evaluation-object #<Evaluation weka.classifiers.Evaluation@6a7272ca>,
 :average-cost 0.0,
 :precision
 {:Iris-setosa 1.0,
  :Iris-versicolor 0.9215686274509803,
  :Iris-virginica 0.94}}

user> (println (:summary evaluation))

Correctly Classified Instances         142               95.302  %
Incorrectly Classified Instances         7                4.698  %
Kappa statistic                          0.9295
Mean absolute error                      0.0408
Root mean squared error                  0.173 
Relative absolute error                  9.1872 %
Root relative squared error             36.6935 %
Total Number of Instances              149     
Ignored Class Unknown Instances                  1     

nil
user> (println (:confusion-matrix evaluation))
=== Confusion Matrix ===

  a  b  c   <-- classified as
 48  1  0 |  a = Iris-setosa
  0 47  3 |  b = Iris-versicolor
  0  3 47 |  c = Iris-virginica

nil
```
Saving and restoring (trained) classifiers:

```clojure

user> (serialize-to-file classifier "my-classifier.bin")
"my-classifier.bin"

user> (def classifier2 (deserialize-from-file "my-classifier.bin"))
#'user/classifier2

user> (classifier-classify classifier2 instance)
:Iris-setosa
```

Text document handling:

```clojure
user> (def docs [{:title "Document title 1"
                  :fulltext "This is the fulltext..."
                  :terms {"Topic" ["Sports"]}}
                 {:title "Another document title"
                  :fulltext "Some more \"fulltext\"; rabbit artificial machine bananas"
                  :terms {"Topic" ["Politics" "Food"]}}])
#'user/docs

user> (docs-to-dataset docs "Topic" "Sports" 1 "/tmp" :stemmer true :lowercase false)
#<Instances @relation 'docs-weka.filters.unsupervised.attribute.StringToWordVector...'

@attribute class {no,yes}
@attribute title-1 numeric
@attribute title-Another numeric
@attribute title-Document numeric
@attribute title-document numeric
@attribute title-titl numeric
@attribute fulltext-Some numeric
@attribute fulltext-This numeric
@attribute fulltext-artifici numeric
@attribute fulltext-banana numeric
@attribute fulltext-fulltext numeric
@attribute fulltext-is numeric
@attribute fulltext-machin numeric
@attribute fulltext-more numeric
@attribute fulltext-rabbit numeric
@attribute fulltext-the numeric

@data
{0 yes,1 0.480453,3 0.480453,7 0.480453,11 0.480453,15 0.480453}
{2 0.480453,4 0.480453,6 0.480453,8 0.480453,9 0.480453,12 0.480453,13 0.480453,14 0.480453}>
user>
```

Words appearing in the dataset will only be those appearing in the
documents (or a subset; by default, the most common 1000 words). This
presents a problem when new documents are loaded and used in a
classifier trained on other documents. The classifier will not know
how to handle word attributes that were not present in the training
set.

The `docs-to-dataset` function provides the ability to save the
training documents dataset and "filter" the testing documents through
this dataset to ensure the same word attributes are extracted for both
sets. The following example shows that the words "foo, bar, baz, quux"
are ignored in the new (testing) documents, and all the original
attributes in the training dataset are retained.

```clojure
user> (docs-to-dataset docs "Topic" "Sports" 1 "/tmp"
                       :stemmer true :lowercase false :training true)
#<Instances @relation 'docs-weka.filters.unsupervised.attribute.StringToWordVector...'

@attribute class {no,yes}
@attribute title-1 numeric
@attribute title-Another numeric
@attribute title-Document numeric
@attribute title-document numeric
@attribute title-titl numeric
@attribute fulltext-Some numeric
@attribute fulltext-This numeric
@attribute fulltext-artifici numeric
@attribute fulltext-banana numeric
@attribute fulltext-fulltext numeric
@attribute fulltext-is numeric
@attribute fulltext-machin numeric
@attribute fulltext-more numeric
@attribute fulltext-rabbit numeric
@attribute fulltext-the numeric

@data
{2 0.480453,4 0.480453,6 0.480453,8 0.480453,9 0.480453,12 0.480453,13 0.480453,14 0.480453}
{0 yes,1 0.480453,3 0.480453,7 0.480453,11 0.480453,15 0.480453}>

user> (def docs2 [{:title "Document title 1 foo bar"
                   :fulltext "baz rabbit quux"
                   :terms {"Topic" ["Sports"]}}])
#'user/docs2

user> (docs-to-dataset docs2 "Topic" "Sports" 1 "/tmp"
                       :stemmer true :lowercase false :testing true)
#<Instances @relation 'docs-weka.filters.unsupervised.attribute.StringToWordVector...'

@attribute class {no,yes}
@attribute title-1 numeric
@attribute title-Another numeric
@attribute title-Document numeric
@attribute title-document numeric
@attribute title-titl numeric
@attribute fulltext-Some numeric
@attribute fulltext-This numeric
@attribute fulltext-artifici numeric
@attribute fulltext-banana numeric
@attribute fulltext-fulltext numeric
@attribute fulltext-is numeric
@attribute fulltext-machin numeric
@attribute fulltext-more numeric
@attribute fulltext-rabbit numeric
@attribute fulltext-the numeric

@data
{0 yes,1 0.480453,3 0.480453,14 0.480453}>
user> 
```

### Using clusterers

```clojure
user> (use 'clj-ml.clusterers)
nil

user> (def ds (-> (load-instances :arff "file:///home/josh/git/clj-ml/iris.arff")
                  (dataset-remove-attribute-at 4)))
#'user/ds
user> ds
#<Instances @relation iris

@attribute sepallength numeric
@attribute sepalwidth numeric
@attribute petallength numeric
@attribute petalwidth numeric

@data
5.1,3.5,1.4,0.2
4.9,3,1.4,0.2
4.7,3.2,1.3,0.2
4.6,3.1,1.5,0.2
5,3.6,1.4,0.2
5.4,3.9,1.7,0.4
4.6,3.4,1.4,0.3
...

user> (def clusterer (make-clusterer :k-means {:number-clusters 3}))
#'user/clusterer

user> (clusterer-build clusterer ds)
nil

user> clusterer
#<SimpleKMeans 
kMeans
======

Number of iterations: 6
Within cluster sum of squared errors: 6.998114004826762
Missing values globally replaced with mean/mode

Cluster centroids:
                           Cluster#
Attribute      Full Data          0          1          2
                   (150)       (61)       (50)       (39)
=========================================================
sepallength       5.8433     5.8885      5.006     6.8462
sepalwidth         3.054     2.7377      3.418     3.0821
petallength       3.7587     4.3967      1.464     5.7026
petalwidth        1.1987      1.418      0.244     2.0795


>

user> (def clustered-ds (clusterer-cluster clusterer ds))
#'user/clustered-ds

user> clustered-ds
#<ClojureInstances @relation 'clustered iris'

@attribute sepallength numeric
@attribute sepalwidth numeric
@attribute petallength numeric
@attribute petalwidth numeric
@attribute class {0,1,2}

@data
5.1,3.5,1.4,0.2,1
4.9,3,1.4,0.2,1
4.7,3.2,1.3,0.2,1
4.6,3.1,1.5,0.2,1
5,3.6,1.4,0.2,1
5.4,3.9,1.7,0.4,1
4.6,3.4,1.4,0.3,1
5,3.4,1.5,0.2,1
4.4,2.9,1.4,0.2,1
...
```

## Thanks YourKit!

YourKit is kindly supporting open source projects with its
full-featured Java Profiler.  YourKit, LLC is the creator of
innovative and intelligent tools for profiling Java and .NET
applications. Take a look at YourKit's leading software products: <a
href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java
Profiler</a> and <a
href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET
Profiler</a>.

## License

MIT License

## Authors

* 2010: [Antonio Garrote](https://github.com/antoniogarrote)
* 2010-2013: [Ben Mabey](https://github.com/bmabey)
* 2013: [Joshua Eckroth](https://github.com/joshuaeckroth)
