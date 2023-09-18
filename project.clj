(defproject genqref-web "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/clojurescript "1.11.60"]
                 [aerial.hanami "0.18.0"]]
  :plugins [[lein-figwheel "0.5.18"]
            [lein-cljsbuild "1.1.8" :exclusions [[org.clojure/clojure]]]]
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]
                :figwheel {:on-jsload "genqref-web.core/on-js-reload"
                           :open-urls ["http://localhost:3450/index.html"]}
                :compiler {:main genqref-web.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/genqref-web.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}]}
  :figwheel {:server-port 3450
             :repl-eval-timeout 30000
             :css-dirs ["resources/public/css"]}
  :main ^:skip-aot genqref-web.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
