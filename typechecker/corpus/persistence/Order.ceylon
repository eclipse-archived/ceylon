import orm.mapping { ... }
import orm.logical { ... }
import ceylon.math { Math }

entity table{ schema="orders"; name="order"; }
by "Gavin King"
   "Andrew Haley"
see (OrderSystem)
shared class Order() {
	
	//TODO: address, payment, user
	
	shared class Status() 
	        of draft, submitted, shipped, closed 
	        extends Case() {}
	
    doc "A draft order being edited"
    charColumnValue "DR"
    shared object draft extends Status() {}
    
    doc "A submitted order ready for
         processing"
    charColumnValue "SU"
    shared object submitted extends Status() {}
    
    doc "An order that has been shipped"
    charColumnValue "SH"
    shared object shipped extends Status() {}
    
    doc "An order that has been delivered"
    charColumnValue "CL"
    shared object closed extends Status() {}
        
	oneToMany{ mappedBy=Item.order; }
	cascade(persist, merge, remove) deleteOrphans
	OpenList<Item> itemList = ArrayList<Item>();
	
	shared mutable Status status := Status.draft;
	
	generated id column{ name="id"; }
	public mutable String? orderId;
	
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