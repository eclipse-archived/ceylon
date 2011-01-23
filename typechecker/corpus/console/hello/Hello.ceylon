doc "A more personalized greeting" 
void hello() {
	
	sayHello( process.args.first ? "World" );
	
	void sayHello(String name) {
		log.info("Hello, " name "!");
	}
	
}