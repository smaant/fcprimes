package smaant;

import java.util.ArrayList;
import java.util.Arrays;

public class Primes {
    private boolean[] sieve = {};
    private ArrayList<Integer> primes = new ArrayList<>();

    private int nextPrime(int start, int limit) {
        int i = start;
        while(i <= limit && this.sieve[i-1]) { i++; }
        return i;
    }

    private void eratosthenesSieve(int start, int limit) {
        final double stopPoint = Math.sqrt(limit);
        for(int i = start; i <= limit; i = nextPrime(i+1, limit)) {
            this.primes.add(i);
            for(int j = i*2; i <= stopPoint && j <= limit; j += i) {
                this.sieve[j-1] = true;
            }
        }
    }

    private void refillSieve(int newSize, int startFrom) {
        final double stopPoint = Math.sqrt(newSize);
        this.sieve = Arrays.copyOf(this.sieve, newSize);
        for(int i: this.primes) {
            for(int j = i*2; i <= stopPoint && j <= newSize; j += i) {
                // we don't need to fill the sieve from the very begining
                if (j >= startFrom) {
                    this.sieve[j-1] = true;
                }
            }
        }
    }

    public Integer[] nPrimes(int n) {
        int limit = Math.round(1000 * (n / 90.0f));
        this.primes.ensureCapacity(n);
        this.primes.add(2);

        while(this.primes.size() < n) {
            final int lastPrime = this.primes.get(this.primes.size() - 1);
            refillSieve(limit, lastPrime);
            eratosthenesSieve(nextPrime(lastPrime+1, limit), limit);
            limit += 1000 * (n - this.primes.size()) / 50;
        }
        return this.primes.subList(0, n).toArray(new Integer[0]);
    }
}
