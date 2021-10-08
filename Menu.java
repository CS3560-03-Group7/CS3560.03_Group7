package orderSystem;

import java.util.ArrayList;

public class Menu {
	ArrayList<Item> menuList = new ArrayList<Item>();
	
	//This method falls under the Update Menu use case. It adds new items to the menu
	void addToMenu(Item item){ 
		menuList.add(item);
		
		return;		
	}
	
	//This method falls under the Update Menu use case. It removes items from the menu
	void removeFromMenu(Item item){ 
		menuList.remove(item);
		
		return;
	}
	
	//This method allows the user to look up an item using its name
	Item searchItem(String searchedName) { 
		Item item = new Item();
		
		for(int i = 0; i < menuList.size(); i++) {
			if (menuList.get(i).itemName == searchedName) {
				item = menuList.get(i);
				break;
			}
		}
		
		return item;
	}
	
	//This method determines whether or not an item on the menu is available for purchase
	boolean itemAvailable(Item item) { 
		return item.isAvailable;
	}
	
	public Menu(ArrayList<Item> menuItems) { //This is the constructor for the Menu class
		this.menuList = menuItems;
	}

}
