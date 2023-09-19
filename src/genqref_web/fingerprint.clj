(ns genqref-web.fingerprint
  (:require [clojure.walk :as walk]))

(defn sorted
  "Convert a data structure into an equivalent with a fixed ordering."
  [data]
  (walk/postwalk
   (fn [x]
     (cond
      (map? x) (apply sorted-map (apply concat x))
      (set? x) (apply sorted-set x)
      :else    x))
   data))

(defn sha1 [s]
  (->> (.getBytes s)
       (.digest (java.security.MessageDigest/getInstance "SHA1"))
       java.math.BigInteger.
       (format "%x")))

(defn- fingerprint* [data]
  (sha1 (pr-str (sorted data))))

(defn- weak-hash-map []
  (java.util.Collections/synchronizedMap (java.util.WeakHashMap.)))

(def fingerprint
  "Return a unique ID for the data structure. Equivalent data structures will
  always produce the same ID."
  (let [cache (weak-hash-map)]
    (fn [data]
      (or (.get cache data)
          (let [print (fingerprint* data)]
            (.put cache data print)
            print)))))
