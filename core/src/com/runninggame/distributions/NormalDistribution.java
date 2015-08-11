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
	
	/** Mean of this distribution. */
	private final double mean;
	
	/** Standard deviation of this distribution. */
	private final double standardDeviation;
	
	/** Random number generator */
	private Random random;
	
	/**
	 * Class constructor
	 * @param mean: Mean of new distribution
	 * @param standardDeviation: Standard deviation of new distribution
	 */
	public NormalDistribution(double mean, double standardDeviation) {
		if (standardDeviation < 0) {
			Gdx.app.log("NormalDistribution", "Standard deviation must be positive");
		}

		this.mean = mean;
		this.standardDeviation = standardDeviation;
		random = new Random();
	}
	
	/**
	 * Return mean of distribution
	 * @return mean of distribution
	 */
    public double getMean() {
        return mean;
    }
    
    /**
     * Return standard deviation of distribution
     * @return standard deviation of distribution
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }
    
    /**
     * Sample the distribution
     * @return a sample of the distribution
     */
    public double sample()  {
        return standardDeviation * random.nextGaussian() + mean;
    }
	
}
