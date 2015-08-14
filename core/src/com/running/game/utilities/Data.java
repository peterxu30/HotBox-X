package com.running.game.utilities;

import java.util.ArrayList;

/**
 * Main class to be serialized to JSON
 * @author Peter
 *
 */
public class Data {
	
	/** User ID */
	String id;
	
	/** Game number */
	int game;
	
	/** All games data */
	private ArrayList<DataList> gameData;
	
	/**
	 * Class constructor
	 */
	public Data() {
		gameData = new ArrayList<DataList>();
	}
	
	/**
	 * Add dataList to current Data object
	 * @param dataList
	 */
	public void add(DataList dataList) {
		gameData.add(dataList);
	}
}