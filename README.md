# fcprimes

Command-line util for multiplication table of prime numbers generation.

## Usage
To run with default parameters (10 prime numbers):
``` bash
lein run
```

To change number of primes:
``` bash
lein run -n 25
```

Additional options:
``` bash
lein run -j # Use Java implementation
lein run -d # Print debug information about time spent on different stages
lein run -q # Quiet mode, doesn't print multipliation table
```

## Notes
In spite of beeing a general purpose language, Clojure is not very good at this kind of task.
The resulted algorithm might be more expressive, but performance will certainly suffer. Out of
curiosity, I've also added Java implementation of the same algorithm. Here's the comparison
of running time for prime numbers generation:

``` Clojure
; Java
smaant.primes> (bench (.nPrimes (Primes.) 10000))
Evaluation count : 77220 in 60 samples of 1287 calls.
             Execution time mean : 780.383643 µs

; Clojure
smaant.primes> (bench (n-primes 10000))
Evaluation count : 240 in 60 samples of 4 calls.
             Execution time mean : 308.237249 ms
```

So basically, in this case, Java is 440 times faster than Clojure. No surprise, index
arithmetic and a big chunk of shared memory bit immutability and pure functions.

## Tests
To run tests:

``` bash
lein test
```
