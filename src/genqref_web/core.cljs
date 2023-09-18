(ns genqref-web.core
  (:require [clojure.string :as cljstr]
            [aerial.hanami.core :as hmi]
            [aerial.hanami.common :as hc]))

(when-let [elem (js/document.querySelector "#app")]
  (hc/update-defaults
   :USERDATA {:tab {:id :TID, :label :TLBL, :opts :TOPTS}
              :frame {:top :TOP, :bottom :BOTTOM,
                      :left :LEFT, :right :RIGHT
                      :fid :FID :at :AT :pos :POS}
              :opts :OPTS
              :vid :VID,
              :msgop :MSGOP,
              :session-name :SESSION-NAME}
   :AT :end :POS :after
   :MSGOP :tabs, :SESSION-NAME "Exploring"
   :TID #(hmi/get-cur-tab :id)
   :TLBL #(-> (let [tid (% :TID)] (if (fn? tid) (tid) tid))
              name cljstr/capitalize)
   :OPTS (hc/default-opts :vgl), :TOPTS (hc/default-opts :tab))
  (hmi/start :elem elem
             :port js/location.port
             :host js/location.hostname
             ;;:header-fn bbh-header-fn
             ))
