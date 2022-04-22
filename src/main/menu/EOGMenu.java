package main.menu;

import java.util.Random;

import main.game_engine.PlayPanel;
import main.menu.shop.Shop;

public class EOGMenu implements Menu {

	private Shop shop;
	private int previousCoins;
	private int newCoins;

	/**
	 * @param shop            Shop parameter is passed so that the constructor does
	 *                        not initializes a new one
	 * @param metersTravelled The meters that the Character traveled during the game
	 */
	public EOGMenu(Shop shop, int metersTravelled) {
		this.shop = shop;
		this.previousCoins = shop.getCoins();
		this.newCoins = 0;
	}

	/**
	 * @return the coins owned before the last game
	 */
	public int getPreviousCoins() {
		return previousCoins;
	}

	/**
	 * @return the coins owned after the last game
	 */
	public int getNewCoins() {
		return newCoins;
	}

	/**
	 * The method updates the owned coins considering the meters traveled during the
	 * last game, the CoinsReducer malus and the CoinsIncrement booster
	 * 
	 * @param meters The meters that the Character traveled during the game
	 */
	private void updateCoins(int meters) {
		newCoins = this.previousCoins + ((int) Math.floor(meters / 5)) - this.updateCoinsReduce()
				+ this.updateCoinsIncrease();

		if (newCoins < 0) {
			newCoins = 0;
		}
		shop.setCoins(newCoins);
	}

	/**
	 * Calculates how many coins need to be subtracted to the total amount
	 * (previously owned coins + won coins in the last game). The coins subtracted
	 * are represented by an int randomly chosen from an ArrayList.
	 * 
	 * @return the sum of coins that need to be subtracted
	 */
	private int updateCoinsReduce(int times) {
		int sum = 0;
		for (int i = 0; i < times; i++) {
			sum += coinsReducer.changeState();
		}
		return sum;
	}

	/**
	 * Calculates how many coins need to be added to the total amount (previously
	 * owned coins + won coins in the last game). The coins added are represented by
	 * a random int that can go from 0 to 100.
	 * 
	 * @return the sum of coins that need to be added
	 */
	private int updateCoinsIncrease() {
		Random randomIncrease = new Random();

		return PlayPanel.incrementTimes * randomIncrease.nextInt(100);
	}
}
