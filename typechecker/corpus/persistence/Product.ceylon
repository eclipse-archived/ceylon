import orm.mapping.*;
import orm.logical.*;

entity table{ schema="orders"; name="product"; }
by "Gavin King"
   "Andrew Haley"
public class Product(String id, String shortDescription, String longDescription, Decimal price) {
	
	id
	public String id = id;
	
	column { name="shortDesc"; }
	public String shortDescription = shortDescription;
	
	column { name="longDesc"; }
	public String longDescription = longDescription;
	
	public mutable Decimal price := price;
	
}