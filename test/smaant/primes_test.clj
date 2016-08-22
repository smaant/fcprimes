(ns smaant.primes-test
  (:require [clojure.test :refer :all]
            [smaant.primes :as primes]))

(deftest next-prime-test
  (testing "next prime is a few number away from prev prime"
    (is (= 5 (#'primes/next-prime 3 #{4 6 8 9 10} 10))))

  (testing "empty prev prime"
    (is (= 2 (#'primes/next-prime nil #{} 10))))

  (testing "next prime is a next number"
    (is (= 3 (#'primes/next-prime 2 #{4 6 8 10} 10))))

  (testing "there's no next prime within a limit"
    (is (nil? (#'primes/next-prime 7 #{8 9 10} 10)))))

(defn- is-prime?
  [x]
  (loop [[y & others] (range 2 x)]
    (if-not y
      (not= x 1)
      (if (= 0 (mod x y))
        (= x y)
        (recur others)))))

(deftest eratosthenes-sieve-test
  (testing "generating from scratch"
    (is (= [2 3 5 7] (#'primes/eratosthenes-sieve [] #{} 10))))

  (testing "if numbers are actually prime"
    (doseq [x (#'primes/eratosthenes-sieve [] #{} 1000)]
      (is (is-prime? x))))

  (testing "generating from some position"
    (is (= [2 3 5 7] (#'primes/eratosthenes-sieve [2 3] #{2 4 6 8 9 10} 10))))

  (testing "generating from the point where there's no more primes"
    (is (= [2 3 5 7] (#'primes/eratosthenes-sieve [2 3 5 7] #{8 9 10} 10))))

  (testing "limit is less than 2"
    (is (= [] (#'primes/eratosthenes-sieve [] #{} 0))))

  (testing "limit is 2"
    (is (= [2] (#'primes/eratosthenes-sieve [] #{} 2)))))

(deftest composites-test
  (testing "composites for empty primes"
    (is (= #{} (#'primes/composites [] 10))))

  (testing "composites for given primes"
    (is (= #{4 6 8 9 10} (#'primes/composites [2 3] 10)))))

(deftest next-limit-test
  (testing "initial limit"
    (is (= 1000 (#'primes/next-limit 0 1 1))))

  (testing "next limit"
    (is (= 2000 (#'primes/next-limit 1000 1 1)))))

(deftest n-primes-test
  (testing "1 prime generation"
    (is (= [2] (primes/n-primes 1))))

  (testing "5 primes generation"
    (is (= [2 3 5 7 11] (primes/n-primes 5)))))

(deftest product-table-test
  (testing "table of 1 number"
    (is (= (str "  1 \n"
                "1 1 \n") (with-out-str (primes/product-table [1])))))

  (testing "table of 3 numbers"
    (is (= (str "  1 2 3 \n"
                "1 1 2 3 \n"
                "2 2 4 6 \n"
                "3 3 6 9 \n") (with-out-str (primes/product-table [1 2 3]))))))
