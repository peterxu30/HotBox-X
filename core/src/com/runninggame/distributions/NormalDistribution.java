package com.runninggame.distributions;

import java.util.Random;

import com.badlogic.gdx.Gdx;

/**
 * Much of this code was borrowed from Apache Commons Math
 * Gutted to only return samples from the distribution.
 * @author Peter
 *
 */
public class NormalDistribution {
	
	public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;
	
	/** Mean of this distribution. */
	private final double mean;
	/** Standard deviation of this distribution. */
	private final double standardDeviation;
	private Random random;
	
	public NormalDistribution(double mean, double standardDeviation) {
		if (standardDeviation < 0) {
			Gdx.app.log("NormalDistribution", "Standard deviation must be positive");
		}

		this.mean = mean;
		this.standardDeviation = standardDeviation;
		random = new Random();
	}
	
    public double getMean() {
        return mean;
    }
    
    public double getStandardDeviation() {
        return standardDeviation;
    }
    
    public double sample()  {
        return standardDeviation * random.nextGaussian() + mean;
    }
	
}
