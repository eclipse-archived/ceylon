class StatementsWithOperators() {
	
	++count;
	Integer j = i++;
	if ( x > 100 || x < 0 ) { }
	User? gavin = users["Gavin"];
	List<Item> firstPage = list[0..20];
	for (Natural n in 1..10) { }
	if (char in `A`..`Z`) { }
	List<Day> nonworkDays = days[{0,7}];
	Natural lastIndex = getLastIndex() ? sequence.lastIndex;
	log.info("Hello " + $person + "!");
	List<String> names = {person1, person2, person3}[].name;
	String? name = person?.name;
	this.total += item.price;
	if ( nonempty args[i] && args[i]?.first != `-` ) { }
	Float vol = length**3;
	map.define(person.name->person);
	order.lineItems[index] := LineItem { product = prod; quantity = 1; };
	if (!document.internal || user is Employee) { }
	
	
	log.info("Hello world!");
	log.info { message = "Hello world!"; };
	printer.print { join = ", "; "Gavin", "Emmanuel", "Max", "Steve" };
	printer.print { "Names: ", from (Person p in people) select (p.name) };
	set(person.name)("Gavin");
	get(process.args)();
	amounts.sort() by (Float x, Float y) ( x<=>y );
	people.each() perform (Person p) { log.info(p.name); };
	hash(default, firstName, initial, lastName);
	hash { algorithm=default; firstName, initial, lastName };
	from (people) where (Person p) (p.age>18) select (Person p) (p.name);
	iterate (map) 
		perform (String name->Object value) {
			log.info("Entry: " name "->" value "");
		};
	
	log.info("Hello, " person.firstName " " person.lastName ", the time is " Time() ".");
	log.info("1 + 1 = " 1 + 1 "");
	
	assert ("x must be zero") that (x==0.0);
	y := when (x>100.0) then (100.0) otherwise (x);
	repeat (3) times { stream.writeLine("Hello!"); };
	Natural[] cubes = list (30) containing (Natural i) (i**3);
	String[] names = from (people) select (Person p) (p.name);
	Boolean adults = forAll (people) every (Person p) (p.age>=18);
	Float total = fold (items, 0.0) 
					using (Float sum, Item item)
						(sum + item.quantity*item.product.price);
	
}