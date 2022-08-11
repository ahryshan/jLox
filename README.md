# jLox 0.0.1

A pretty primitive custom scripting language implementation.  
Making it according to [Crafting Interpreters](https://craftinginterpreters.com/)

Made with Java to learn concepts.  
If I won't leave this idea, also there will be a cLox implementation in the future.

## Usage

Run script with (must be executed with java >= 18)

``` sh
java -jar jLox.jar [path/to/script]
```

Lox scripts must have .lox extension.

If there is no script provided to interpreter a prompt will be started, and a user can type in code in interactive mode.
All the variables will be stored for the time of interpreter's execution.

## Basic concepts

The Lox language is a c-style scripting language with an implementation in Java.
Lox is a dynamically typed language and at moment it supports the most basic toolset such as arithmetic, logic operations and global variables.

### Basic expressions

Lox supports expressions with literals. The most basic expressions in lox will be

```
"hello";
10;
```

Also an important thing to remember is that semicolon IS mandatory on the end of each statement.
Multiple statements can be placed on the same line making a

```
"hello"; "world";
```

a prefectly correct program.

Lox supports string, number, boolean literals and nil (instead of a null).
Lox has no destiction between integer and double values, it's all number to it.

A variable can be declared with a `var` keyword. There are examples of correct variable declaration and initialization: 

```
var a;
var a = 10;
var b = a;
var c = b = a = 12; // a, b and c all are equal to 12 at this point
```

Until variable is assigned a value it has nil as a value.

### Basic arithmetic operations

All the arithmetic operations are possible between any two numbers or expressions which are evaluated into a number.
Also if some of the oprands in addition is a string, the second one will be casted to a string and the values will be concatenated.
Negation is posible only with numbers.

### Logic operations

Comparison is possible only between two numbers.
Exception is != and == operators which can compare all types of expressions. There is no type casting in the process  

### Truthyness

Every expression with value different from nil, "", 0, false is truthy.

### Print operator

Lox has a print keyword. It will print into the console the expression to the reght of the print.

```
print a = 10; // Will print "10"
```

### Comments

Lox supports oneline comments and block comments. Block comments can be nested.

```
// it's a one line comment

var a; // comment can be also here

/*
  This is a block comment
*/

/**********************************
 *
 *  This is also a block comment.
 *
*********************************/

/* And this will be a nested comment block.
  /* Outer
    /* Inner */
  */
*/

```


