# datemath

Basic datemath operation for relative time from string, in clojure based of java time.

## Usage

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.diogok/datemath.svg)](https://clojars.org/org.clojars.diogok/datemath)

Require and use:

```clojure
(require '[datemath.core :as dm])

(dm/calc "now/w +15d +1s") #_"Return a java.time.LocalDateTime of the proper date"
```

Available operations:

- `+` 
- `-`
- `/`

Available units:

- `s` for seconds
- `m` for minutes
- `h` for hours
- `d` for days
- `w` for weeks
- `M` for months
- `y` for years

You should always start with `now`.

## License

MIT
