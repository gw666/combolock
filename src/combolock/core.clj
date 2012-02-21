(ns combolock.core
  (:gen-class)
  (:use clojure.test)
  (:use midje.sweet))

(defn initialized-counts
  ""
  []
  (atom {0 0, 1 0, 2 0, 3 0, 4 0, 5 0, 6 0, 7 0, 8 0, 9 0}))

(defn nasinc
  "In the atom-NAS, increment the value associated with the key-value pair
found by following the vector of indices. Works for multiple levels of
nesting.

An atom-NAS is an atom that points to a NAS (nested associative structure).
Examples:  (atom {k1 v1, k2 v2}), (atom [{k1 v1, k2 v2} {k1 v3, k2 v4}])

Given (def a (atom [{:k1 1, :k2 2} {:k1 3, :k2 4}])),
then (nasinc a [1 :k2]) sets a = [{:k1 1, :k2 2} {:k1 3, :k2 5}]

Note that the first index chooses the particular map to target. Also, if the
atom contains only one map, then the surrounding brackets are not needed
around the map and vec-of-indices will have only a single value in it."
  [atom-NAS & indices]
  (swap! atom-NAS update-in (vec indices) inc))

(defn process-index-list
  "For all indexes in index-list, increment the value associated with
that index in atom-NAS. Returns nil."
  [atom-NAS index-list]
  (let [mycount     (fresh-count)]
    (doseq [index   index-list]
      (nasinc atom-NAS index))))

(defn n1<n2<n3? [vec]
  (let [n1 (nth vec 0)
        n2 (nth vec 1)
        n3 (nth vec 2)]
    (and (< n1 n2)
         (< n2 n3))))

(defn in-order
  "For every (n1 n2 n3) in seq, retain only when n1 < n2 < n3."
  [seq]
  (filter n1<n2<n3? seq))

(defn separate-digits
  "Breaks a two-digit number into a list: (e.g., 31 -> (3 1))."
  [n]
  (list (quot n 10) (rem n 10)))

;; (defn process-primesOLD
;;   "Given list of primes, returns list with all digits (0..9) obtained by
;; 'exploding' each prime into two digits (order of digits not important)."
;;   [prime-list]
;;   (let [digit-list   ()]
;;     (doseq [prime    prime-list]
;;       (let [digits (separate-digits prime)]
;;         (into digit-list digits)
;;         (println "\n\nAfter processing" prime ":")
;;         (println "digit-list" (coll? digit-list) digit-list)
;;         (println (coll? digits) digits)))
;;     digit-list))

(defn process-primes-helper
  "Given list of primes, returns list with all digits (0..9) obtained by
'exploding' each prime into two digits (order of digits not important)."
  [digit-list primes-list]
  (if (empty? primes-list)
    digit-list
    (let [prime (first primes-list)]
      (process-primes-helper
       (into digit-list (separate-digits prime)) (rest primes-list)))))

(defn process-primes [primes-list]
  (process-primes-helper '() primes-list))

(defn valid-soln?
  ""
  [primes-list]
  (let [mycounts      (initialized-counts)
        all-digits    (process-primes primes-list)
        IGNORE        (process-index-list mycounts all-digits)
        ; this sorts the counts for digits 1-9; 0 is ignored (so as not to
        ; count leading zeros, and no number ending in 0 is prime, anyway)
        sorted-counts (sort (rest (vals @mycounts)))]
    (= sorted-counts
       '(0 0 0 0 0 0 2 2 2))))

(defn run[]
  (let [primes [2 3 5 7 11 13 17 19 23 29 31 37]
        n      (count primes)
        candidates (for [n1 primes, n2 primes, n3 primes]
                     [n1 n2 n3])        
        ordered-cands   (in-order candidates)]
    (doseq [a-trio ordered-cands]
 ;     (println a-trio)
      (if (valid-soln? a-trio)
        (println a-trio "WORKS!")))))