doc "An animate thing that can move between
     locations of its own accord (if the
     player asks nicely)."
mutable package class Creature(String name, String description, Location location,
								Natural damage, Natural agility, Natural life) 
		extends Thing(name, description, location) {
		
	package Natural damage = damage;
	package Natural agility = agility;
	package mutable Natural life = life;
		
	package void go(Adventure game, Direction direction) {
		if ( cooperative(direction) ) {
			Location newLocation = location.connection(direction);
			location.remove(this);
			newLocation.put(this);
			game.currentLocation := newLocation;
			game.out("${name} went ${direction}.");
		}
		else {
		    game.out("${name} doesn't move an inch.");
		}
	}
	
	package void kill(Adventure game, Artifact weapon) {
		do {
			Natural attack = game.random();
			if ( attack>agility ) {
				life -= weapon.damage;
				game.out("You did ${weapon.damage} points of damage.");
				
			}
			else {
				game.out("You missed.");
			}
			Natural defense = game.random();
			if ( defense+agility > game.level ) {
				game.life -= damage;
				game.out("The ${name} did ${damage} points of damage.");
				if (game.life<=0) {
					game.out("You are dead.");
				}
			}
			else {
				game.out("The ${name} missed.");
			}
		}
		while (life>0)
		game.out("You killed the ${name}.");
		dead();
	}
		
	doc "Override this to implement special 
	     rules for commanding the creature"
	package Boolean cooperative(Direction direction) {
		return true;
	}
	
	doc "Override this to do special things
	     when the creature dies (drop items, 
	     end adventure)."
	package void dead() {}
		
}
