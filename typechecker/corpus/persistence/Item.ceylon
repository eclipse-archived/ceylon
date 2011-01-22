import orm.mapping { ... }
import orm.logical { ... }

entity table{ schema="orders"; name="item"; }
by "Gavin King"
   "Andrew Haley"
shared class Item(Order order, Product product, Natural quantity) {
	
	manyToOne id column { name="orderId"; }
	shared Order order = order;
	
	manyToOne id column { name="productId"; }
	shared Product product = product;
	
	shared Natural quantity = quantity;
	
	transient
	shared Float price {
		return product.price*quantity;
	}
	
}