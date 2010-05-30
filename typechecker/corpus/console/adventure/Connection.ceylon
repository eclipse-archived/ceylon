doc "A connection (path, door, portal, etc) 
     between two locations, which the player
     can use to navigate between locations."
see #Location
package class Connection(String description, Direction direction, Location to) {

	package String description = description;
	package Direction direction = direction;
	package Location to = to;
		
	doc "Override this to implement special 
	     rules navigating the connection."
	package Boolean open() {
		return true;
	}
		
	package void go(Adventure game) {
		if ( open() ) {
			game.currentLocation = to;
			game.display("You went ${direction} and arrived at ${to.name}.");
		}
		else {
			game.display("You can't go that way right now.");
		}
	}

}
	
