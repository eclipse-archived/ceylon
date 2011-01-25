shared extension class Objects(Object this) {
    
    doc "Determine if this object belongs to the given |Category|.
         The binary |in| operator."
    see (Category)
    shared Boolean element(Category category) {
        return category.contains(this)
    }

}