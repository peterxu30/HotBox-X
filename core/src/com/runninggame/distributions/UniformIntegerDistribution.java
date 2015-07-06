package com.runninggame.distributions;

import java.util.Random;

/**
 * Much of this code was borrowed from Apache Commons Math's UniformIntegerDistribution class.
 * Gutted to only return samples from the distribution.
 * @author Peter
 *
 */
public class UniformIntegerDistribution {
	
	private final int lower;
	private final int upper;
	private final Random random;
	
	public UniformIntegerDistribution(int lower, int upper) {
		this.lower = lower;
		this.upper = upper;
		random = new Random();
	}
	
	public int getLower() {
		return lower;
	}
	
	public int getUpper() {
		return upper;
	}
	
	public int sample() {
		final int max = (upper - lower) + 1;
        if (max <= 0) {
            // The range is too wide to fit in a positive int (larger
            // than 2^31); as it covers more than half the integer range,
            // we use a simple rejection method.
            while (true) {
                final int r = random.nextInt();
                if (r >= lower &&
                    r <= upper) {
                    return r;
                }
            }
        } else {
            // We can shift the range and directly generate a positive int.
            return lower + random.nextInt(max);
        }
	}
	
}
