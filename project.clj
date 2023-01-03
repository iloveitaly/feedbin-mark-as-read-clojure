(defproject feedbin "1.0.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main ^:skip-aot feedbin.core
  :dependencies [[org.clojure/clojure "1.10.1"]
    [cheshire "5.10.0"]
    [debugger "0.2.1"]
    [clj-time "0.15.2"]
    ; this package can't be found on clojar, it's a java package loaded from the maven package management tool
    ; https://mvnrepository.com/artifact/com.amazonaws/aws-lambda-java-core
    [com.amazonaws/aws-lambda-java-core "1.2.0"]
    [clj-http "3.10.3"]]
  :repl-options {:init-ns feedbin.core}
  :aot :all)
