(ns genqref-web.core
  (:gen-class)
  (:require [clojure.string :as str]
            [aerial.hanami.core :as hmi]
            [aerial.hanami.common :as hc]
            [aerial.hanami.templates :as ht]
            [taoensso.carmine :as car]
            [genqref-web.fingerprint :refer [fingerprint]]))

(defn start
  "Initialize a basic system and start server"
  [port]

  ;; Some defaults that are useful in most any basic system
  (hc/update-defaults
   :USERDATA {:tab {:id :TID, :label :TLBL, :opts :TOPTS}
              :frame {:top :TOP, :bottom :BOTTOM, :left :LEFT, :right :RIGHT
                      :fid :FID}
              :opts :OPTS
              :vid :VID,
              :msgop :MSGOP,
              :session-name :SESSION-NAME}
   :MSGOP :tabs, :SESSION-NAME "Exploring"
   :TID :expl1, :TLBL #(-> :TID % name str/capitalize)
   :OPTS (hc/default-opts :vgl), :TOPTS (hc/default-opts :tab))

  (hmi/start-server
   port
   :idfn (constantly "Exploring")))

(defn stop []
  (hmi/stop-server))

(defn -main [& args]
  (start 5000))

;; -------- UNTESTED

(defonce state (atom {}))

(defn deep-merge [& maps]
  (apply merge-with (fn [& args]
                      (if (every? map? args)
                        (apply deep-merge args)
                        (last args)))
         maps))

(defn flip [f]
  (fn [& xs]
    (apply f (reverse xs))))

#_((flip /) 84 2)

(defn nest [& args]
  (let [[val & path] (reverse args)]
    (reduce (flip hash-map) val path)))

#_(nest :1 :2 :3 :hello)

(defn store! [& args]
  (->> (apply nest args)
       (swap! state deep-merge))
  ;; swap! returns the whole state
  (last args))

#_(store! :this :is :a :test)

(def ^:private sym (comp keyword :symbol))

(defn handle
  ([scope] (handle scope sym))
  ([scope id-fn]
   (fn [[_subscribe _pattern _channel resource]]
     (store! scope (id-fn resource) resource))))

;; TODO: unhardcode
(def redis-spec {:uri "redis://localhost:6379/"})

(defn create-listener []
  (car/with-new-pubsub-listener redis-spec
    {"state/markets" (handle :markets)
     "state/transactions" (handle :transactions fingerprint)
     "state/ships" (handle :ships)}
    (car/psubscribe "state/*")))

#_(create-listener)

(defn go []
  (start 5000)
  (create-listener)


  (-> (hc/xform ht/point-chart
                :DATA (-> @state :transactions vals)
                :TITLE (format "What's being traded (%s)" (-> @state :transactions count))
                ;; :X "timestamp"
                ;; :XTYPE "temporal"
                :X "units"
                :Y "totalPrice"
                :DFMT {:parse {"timestamp" "utc:'%Y-%m-%dT%H:%M:%SZ'"}}
                ;;:COLOR "shipSymbol"
                :COLOR "tradeSymbol"
                ;;:TRANSFORM [{:loess :Y :on :X}]
                )
      hmi/sv!)

  )
#_(-> @state :transactions vals first)

"done"
