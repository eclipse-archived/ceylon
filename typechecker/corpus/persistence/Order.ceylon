import orm.mapping { ... }
import orm.logical { ... }
import ceylon.math { Math }

entity table{ schema="orders"; name="order"; }
by "Gavin King"
   "Andrew Haley"
see (OrderSystem)
shared class Order() {
	
	//TODO: address, payment, user
	
	oneToMany{ mappedBy=Item.order; }
	cascade(persist, merge, remove) deleteOrphans
	OpenList<Item> itemList = ArrayList<Item>();
	
	shared variable Status status := draft;
	
	generated id column{ name="id"; }
	shared variable String? orderId := null;
	
	shared Item addItem(Product product, Natural quantity=1) {
		Item item = Item(this, product, quantity);
		itemList.add(item);
		return item;
	}
	
	transient
	shared List<Item> items {
		return itemList;
	}
	
	transient
	shared Float total {
		return Math.sum(items[].price);
	}
	
	
}