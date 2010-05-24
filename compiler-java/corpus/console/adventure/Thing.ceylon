doc "A thing which can move between locations."
mutable package abstract class Thing(String name, String description, Location location) {
	package String name = name;
	package String description = description;
	mutable package Location location := location;
}
