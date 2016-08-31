"""Abstraction over [[callable constructors|CallableConstructorDeclaration]]
   and [[value constructors|ValueConstructorDeclaration]]
"""
since("1.2.0")
shared interface ConstructorDeclaration
        of CallableConstructorDeclaration|ValueConstructorDeclaration 
        satisfies NestableDeclaration {
    
}

