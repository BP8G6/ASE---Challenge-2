/**
 * @author: Ramesh Kuthala
 * 28-Sep-2014
 */
package com.challenge.estore;

import java.util.ArrayList;

import com.challenge.utilities.ItemDetails;
import com.challenge.utilities.Stores;

public class CartData {

	private static CartData instance;
	
	private Stores selectedStore;
	private ArrayList<ItemDetails> selectedItemList;
	
	private CartData()
	{
		selectedItemList = new ArrayList<ItemDetails>();
	}
	
	public static CartData getInstance()
	{
		if(instance == null)
			instance = new CartData();
		
		return instance;
	}
	
	public Stores getSelectedStore() {
		return selectedStore;
	}

	public void setSelectedStore(Stores selectedStore) {
		this.selectedStore = selectedStore;
	}

	public ArrayList<ItemDetails> getSelectedItemList() {
		return selectedItemList;
	}

	public void addSelectedItem(ItemDetails selectedItem) {
		selectedItemList.add(selectedItem);
	}

	public void removeSelectedItem(ItemDetails selectedItem)
	{
		selectedItemList.remove(selectedItem);
	}
	
	public double getTotalPrice()
	{
		double totalPrice = 0.0;
		
		for(int i = 0; i < selectedItemList.size(); i++)
			totalPrice += selectedItemList.get(i).getPricing();	
		
		return totalPrice;
	}
	public void clearCart()
	{
		selectedItemList.clear();
	}
	
	
}
