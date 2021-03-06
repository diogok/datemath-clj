# datemath

Basic datemath operation for relative time from string, in clojure based of java time.

## Usage

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.diogok/datemath.svg)](https://clojars.org/org.clojars.diogok/datemath)

Require and use:

```clojure
(require '[datemath.core :as dm])

(dm/calc "now/w +15d -1s") #_"Return a java.time.ZonedDateTime of the proper date"
(dm/calc "2019-01-02T00:10:30Z||/w +15d -1s") #_"Also support iso zoned date string"
```

Available operations:

- `+` add time units
- `-` remove time units
- `/` round to/truncate to time unit lower boundary

Available units:

- `s` for seconds
- `m` for minutes
- `h` for hours
- `d` for days
- `w` for weeks
- `M` for months
- `y` for years

You should always start with `now` or an ISO formated with timezone info such as `2019-01-02T00:10:30Z` or `2019-01-02T00:10:30+00:00`, it will use timezoned date time.

## License

MIT
