package com.running.game.utilities;

import com.badlogic.gdx.utils.TimeUtils;

public class TimeManager {

	/** In-game time */
	private static long time;
	
	/** 
	 * Reset baseline time to current time 
	 */
	public static void resetTime() {
		time = TimeUtils.millis();
	}
	
	/**
	 * Get game time in current round in seconds
	 * @return double time value
	 */
	public static double getSecondsTime() {
		return (TimeUtils.millis() - time) / 1000.0;
	}
	
}
