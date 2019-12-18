(ns datemath.core-test
  (:use datemath.core)
  (:use clojure.test)
  (:require [java-time :as t]))

(deftest test-parser
  (is (= (text-to-parts "now +1d/d-25s /M")
         ["now" "+1d" "/d"  "-25s" "/M"])))

(deftest basic-convertions
  (let [fixed-now (now)]
    (binding [*fixed-time* fixed-now]
      (is (= (calc-date "now") 
             fixed-now))
      (is (= (calc-date "now+1d")
             (t/plus fixed-now (t/days 1))))
      (is (= (calc-date "now+23d")
             (t/plus fixed-now (t/days 23))))
      (is (= (calc-date "now+23d -2s -3d +1w")
             (-> fixed-now
                 (t/plus  (t/days 23))
                 (t/minus (t/seconds 2))
                 (t/minus (t/days 3))
                 (t/plus  (t/weeks 1)))))
      (is (= (calc-date "now +1y")
             (t/plus fixed-now (t/years 1))))
      (is (= (calc-date "now/m")
             (t/truncate-to fixed-now :minutes)))
      (is (= (calc-date "now/h")
             (t/truncate-to fixed-now :hours)))
      (is (= (calc-date "now/d")
             (t/truncate-to fixed-now :days)))
      (is (= (calc-date "now/w")
             (t/adjust fixed-now first-day-of-week-adjuster)))
      (is (= (calc-date "now/M")
             (t/adjust fixed-now :first-day-of-month)))
      (is (= (calc-date "now/y")
             (t/adjust fixed-now :first-day-of-year)))
      (is (= (calc-date "now +23d/w +1w")
             (-> fixed-now
                 (t/plus (t/days 23))
                 (t/adjust first-day-of-week-adjuster)
                 (t/plus (t/weeks 1)))))
      )))