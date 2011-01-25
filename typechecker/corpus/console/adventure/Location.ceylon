doc "A location which the player can visit.
     Locations may have connections to other
     locations. Locations can contain things."
see (Thing)
see (Connection)
shared class Location(String name, String description, Connection... connections) {
			
	shared String name = name;
	shared String description = description;
		
	OpenMap<Direction, Connection> connectionByDirection = HashMap<Direction, Connection>();
	for (Connection conn in connections) {
		connectionByDirection[conn.direction] := conn
	}
		
	OpenMap<String, Thing> thingByName = HashMap<String, Thing>();
		
	shared Connection connection(Direction direction) {
		return connectionByDirection[direction]
	}
		
	shared List<String> thingNames {
		return thingByName.keys
	}
		
	shared Bag<Thing> things {
		return thingByName.values
	}
		
	shared Thing? thing(String name) {
		return thingByName.valueOrNull(name)
	}
		
	shared void put(Thing thing) {
		thingByName[thing.name] := thing
	}
		
	shared void remove(Thing thing) {
		thingByName.remove(thing.name)
	}
		
}
	
