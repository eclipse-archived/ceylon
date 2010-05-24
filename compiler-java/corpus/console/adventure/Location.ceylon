doc "A location which the player can visit.
     Locations may have connections to other
     locations. Locations can contain things."
see #Thing
see #Connection
package class Location(String name, String description, Connection... connections) {
			
	package String name = name;
	package String description = description;
		
	OpenMap<Direction, Connection> connectionByDirection = HashMap<Direction, Connection>();
	for (Connection conn in connections) {
		connectionByDirection[conn.direction] := conn;
	}
		
	OpenMap<String, Thing> thingByName = HashMap<String, Thing>();
		
	package Connection connection(Direction direction) {
		return connectionByDirection[direction];
	}
		
	package List<String> thingNames {
		return thingByName.keys;
	}
		
	package Bag<Thing> things {
		return thingByName.values;
	}
		
	package optional Thing thing(String name) {
		return thingByName.valueOrNull(name);
	}
		
	package void put(Thing thing) {
		thingByName[thing.name] := thing;
	}
		
	package void remove(Thing thing) {
		thingByName.remove(thing.name);
	}
		
}
	
