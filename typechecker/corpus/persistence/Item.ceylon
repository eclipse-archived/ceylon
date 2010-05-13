import orm.mapping.*;
import orm.logical.*;

entity table{ schema="orders"; name="item"; }
by "Gavin King"
   "Andrew Haley"
public class Item(Order order, Product product, Natural quantity) {
	
	manyToOne id column { name="orderId"; }
	public Order order = order;
	
	manyToOne id column { name="productId"; }
	public Product product = product;
	
	public Natural quantity = quantity;
	
	transient
	public Float price {
		return product.price*quantity;
	}
	
}