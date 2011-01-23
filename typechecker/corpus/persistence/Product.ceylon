import orm.mapping { local pid = id, ... }
import orm.logical { ... }

entity table{ schema="orders"; name="product"; }
by "Gavin King"
   "Andrew Haley"
shared class Product(String id, String shortDescription, String longDescription, Decimal price) {
	
	pid
	shared String id = id;
	
	column { name="shortDesc"; }
	shared String shortDescription = shortDescription;
	
	column { name="longDesc"; }
	shared String longDescription = longDescription;
	
	shared variable Decimal price := price;
	
}