import javax.persistence{entity, id, generatedValue, column}

shared entity class Employee(name, year = null) {
    
    generatedValue id
    shared late Integer id;
    
    column { length = 50; }
    shared String name;
    
    column
    shared variable Integer? year;
    
}