doc "A connection (path, door, portal, etc) 
     between two locations, which the player
     can use to navigate between locations."
see (Location)
shared class Connection(String description, Direction direction, Location to) {

	shared String description = description;
	shared Direction direction = direction;
	shared Location to = to;
		
	doc "Override this to implement special 
	     rules navigating the connection."
	shared Boolean open() {
		return true;
	}
		
	shared void go(Adventure game) {
		if ( open() ) {
			game.currentLocation = to;
			game.display("You went " direction " and arrived at " to.name ".");
		}
		else {
			game.display("You can't go that way right now.");
		}
	}

}
	
