(ns datemath.core
  (:require [java-time :as time])
  (:require [clojure.string :as str]))

(defn now
  "Return now, zoned date and time."
  [] (time/zoned-date-time))

(defn from-string
  "Parse a ISO datestring to zoned date time."
  [date-string] (time/zoned-date-time (time/formatter :iso-offset-date-time) date-string))

(defn to-string
  "Return an ISO string of the given date"
  [date-time] (time/format (time/formatter :iso-offset-date-time) date-time))

(def unit-to-unit
  {"s" time/seconds
   "m" time/minutes
   "h" time/hours
   "d" time/days
   "w" time/weeks
   "M" time/months
   "y" time/years})

(def first-day-of-week-adjuster
  (. java.time.temporal.TemporalAdjusters (previousOrSame java.time.DayOfWeek/SUNDAY)))

(def unit-to-truncate
    {"s" [time/truncate-to :seconds]
     "m" [time/truncate-to :minutes]
     "h" [time/truncate-to :hours]
     "d" [time/truncate-to :days]
     "w" [first-day-of-week-adjuster]
     "M" [:first-day-of-month]
     "y" [:first-day-of-year]})

(defn make-op
  "Make a function to operate on some unit. 
Expect input like '1d' and a function.
Return a function that take an instant and apply the operation."
  [part op]
  (let [[_ value unit] (re-find #"([0-9]+)([a-zA-Z])" part)]
    (fn [current-time]
      (op current-time ((unit-to-unit unit) (Integer/valueOf value))))))

(defn make-div
  "Make a function to round to some unit. 
Expect input unit like 'W'.
Return a function that take an instant and return it rounded."
  [unit]
  (fn [current-time] 
    (apply time/adjust current-time (unit-to-truncate unit))))

(defn parts-to-operations-0
  [part] 
  (let [op-key (str (first part))
        op-value (apply str (rest part))]
    (condp = op-key
      "/" (make-div op-value)
      "+" (make-op op-value time/plus)
      "-" (make-op op-value time/minus))))

(defn parts-to-operations
  "Transform text operation in a function"
  [part]
  (if (= "now" part) (now)
      (if (str/includes? part "T")
        (from-string part)
        (parts-to-operations-0 part))))

(defn operations-to-instant
  "Reduce instant operations"
  [acc op] 
  (if 
   (nil? acc) (op)
   (op acc)))

(defn extract-operations
  "Split a full text to operations text parts. Ignore unexpected parts."
  [text] (re-seq #"[+\-][1-9]+[a-zA-Z]|[/][a-zA-Z]" text))

(defn text-to-parts
  "Get initial date and operations"
  [text] 
  (if (= "now" (apply str (take 3 text)))
    (cons "now" (extract-operations (apply str (drop 3 text))))
    (let [parts (str/split text #"\|\|")]
      (when (= 2 (count parts))
        (cons (first parts) (extract-operations (last parts)))))))

(defn calc-date
  "Transform a date math in an date calculated"
  [text] 
  (->> text
       (text-to-parts)
       (map parts-to-operations)
       (reduce operations-to-instant)))
