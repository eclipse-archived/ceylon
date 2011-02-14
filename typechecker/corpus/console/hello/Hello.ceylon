doc "A more personalized greeting" 
void hello() {
	
	void sayHello(String name) {
		log.info("Hello, " name "!");
	}
	
    sayHello( process.args.first ? "World" );
    
}