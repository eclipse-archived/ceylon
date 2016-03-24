"""Abstraction over [[callable constructors|CallableConstructorDeclaration]]
   and [[value constructors|ValueConstructorDeclaration]]
"""
shared interface ConstructorDeclaration
        of CallableConstructorDeclaration|ValueConstructorDeclaration 
        satisfies NestableDeclaration {
    
}

