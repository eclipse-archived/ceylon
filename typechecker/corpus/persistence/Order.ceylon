import orm.mapping.*;
import orm.logical.*;
import math.Math;

entity table{ schema="orders"; name="order"; }
by "Gavin King"
   "Andrew Haley"
see #OrderSystem
public class Order() {
	
	//TODO: address, payment, user
	
	public class Status() {
	
		doc "A draft order being edited"
		charColumnValue "DR"
		case draft,
		
		doc "A submitted order ready for
		     processing"
		charColumnValue "SU"
		case submitted,
		
		doc "An order that has been shipped"
		charColumnValue "SH"
		case shipped,
		
		doc "An order that has been delivered"
		charColumnValue "CL"
		case closed;
		
	}
	
	oneToMany{ mappedBy=#Item.order; }
	cascade(persist, merge, remove) deleteOrphans
	OpenList<Item> itemList = ArrayList<Item>();
	
	public mutable Status status := Status.draft;
	
	generated id column{ name="id"; }
	public mutable optional orderId;
	
	public Item addItem(Product product, Natural quantity=1) {
		Item item = Item(this, product, quantity);
		itemList.add(item);
		return item;
	}
	
	transient
	public List<Item> items {
		return itemList;
	}
	
	transient
	public Float total {
		return Math.sum(items*.price);
	}
	
	
}