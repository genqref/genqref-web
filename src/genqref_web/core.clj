(ns genqref-web.core
  (:gen-class)
  (:require [clojure.string :as str]
            [aerial.hanami.core :as hmi]
            [aerial.hanami.common :as hc]))

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
