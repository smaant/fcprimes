(ns smaant.primes
  (:gen-class)
  (:require [clojure.set :as set]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]])
  (:import [smaant Matrix Primes]))

(defn- next-prime
  "Returns a prime number next after `prev-prime` up to `limit`.
   `composites` should be a set of all known up composite numbers for all prime
   numbers from 2 up to `prev-prime`.
   Returns `nil` if there's no more primes and `2` if `prev-prime` is nil."
  [prev-prime composites limit]
  (if prev-prime
    (first (filter (complement composites) (range (inc prev-prime) (inc limit))))
    2))

(defn- eratosthenes-sieve
  "Generates a list of prime numbers up to `limit` number using Eratosthenes sieve.
   Can catch up from some existing point with given list of primes and composites."
  [primes composites limit]
  (loop [primes primes
         composites composites
         i (next-prime (last primes) composites limit)]
    (if (or (not i) (> i limit))
      primes
      (let [composites* (set/union composites (set (range (* i i) (inc limit) i)))
            i* (next-prime i composites* limit)]
        (if i*
          (recur (conj primes i) composites* i*)
          (conj primes i))))))

(defn- composites
  "Generates set of composite number for given prime numbers up to `limit`.
   Composites generates for all primes but only for numbers greater than a biggest prime."
  [primes limit]
  (->> (for [i primes] (range (* i i) (inc limit) i))
       flatten
       (filter (partial < (last primes)))
       set))

(defn- next-limit
  "Calculates next limit for prime numbers generator.
   Limit calculated based on assumption that prime numbers are pretty stable distributed
   per every 1000 of natural numbers. `per-thousand` - is expected number of primes per 1000."
  [cur-limit n per-thousand]
  (+ cur-limit (* 1000 (/ n per-thousand))))

(defn n-primes
  [n]
  "Returns a list of `n` prime numbers.
   Basic algorithm:
     1. Makes an assumption up to what limit it should build an Eratosthenes sieve to yield
        required number of primes
     2. Generates an Eratosthenes sieve
     3. If resulted yield is not enough - repeat the process with increased limit and
        updated up to new limit list of composite numbers."
  (loop [primes []
         limit (next-limit 0 n 80.)]
    (if (>= (count primes) n)
      (take n primes)
      (let [primes* (eratosthenes-sieve primes (composites primes limit) limit)]
        (recur primes* (next-limit limit (- n (count primes*)) 50.))))))

(defn product-table
  "Prints out a multiplication matrix for a given coll"
  [coll]
  (let [prod (for [x coll y coll] (* x y))
        max-width (count (str (* (last coll) (last coll))))
        lines (partition (count coll) prod)
        print-line (fn [width coll]
                     (-> (map (partial format (str "%" width "s ")) coll)
                         str/join
                         println))]

    (doseq [[i line] (partition 2 (interleave (cons "" coll) (cons coll lines)))]
      (print-line max-width (cons i line)))))

(def cli-options
  [["-n" "--number NUMBER" "Number of primes to find"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [pos? "Number should be greater than 0"]]

   ["-j" "--java" "Use Java implementation" :default false]
   ["-d" "--debug" "Print spent time" :default false]
   ["-q" "--quiet" "Don't print result" :default false]])

(defn -main
  [& args]
  (let [cli-args (parse-opts args cli-options)
        {:keys [number java debug quiet]} (:options cli-args)
        start (System/currentTimeMillis)
        primes (cond
                 java       (.nPrimes (Primes.) number)
                 (not java) (n-primes number) )
        primes-done (System/currentTimeMillis)]

    (when-let [[error] (:errors cli-args)]
      (println error)
      (System/exit 1))

    (when debug
      (println "Primes generation done"))

    (if-not java
      (binding [*out* (if quiet (io/writer "/dev/null") *out*)]
        (product-table primes))
      (Matrix/printProductMatrix primes quiet))

    (when debug
      (println (str "Primes generation: " (- primes-done start) "ms"))
      (println (str "Product matrix generation:" (- (System/currentTimeMillis) primes-done) "ms"))
      (println (str "Time spent total:" (- (System/currentTimeMillis) start) "ms")))))
