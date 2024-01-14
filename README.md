# Compiler

## Overview

Compiler project contains the entire compilation process, including:

- lex
- syntax
- semantic(with AST)
- IR generation
- IR optimization
- target code generation

Logic of Lexer and Syntaxer is written manually. Comparing to use automatons like NFA or DFA, manually written logic brings finer granularity, thus bringing more customized options.

### Target

This is my first integral compiler project and its purpose is to have a clear understanding of all compilation process. And there might be a more complex compiler project base on this, aiming to create my own program language(Mostly with c/cpp).

### Complaints

This idea comes from project: [C4](https://github.com/rswier/c4).

Pointer is a good thing, but Java does not hug it, so sad.

It actually leads many creative implementations!

## Lexer

Lexer scans source code file by char and returns token list.

### Token

`Token` is one lexical item, mainly used in syntaxer's logic. It's defined as bellow:

```java
public class Token {
    private final LexSymbol symbol;
    private final String lexeme;
    private final Position position;
}

public enum LexSymbol {
    private final String name;
}

public record Position(String filename, int offset, int row, int col) {}
```

### Symbols

Lexer's scan depends on lexical symbols, they can be divided into types:

- reserved words
- value lit
- op symbols

#### Reserved Words

```text
int  float  bool  char  string  func  void  type  struct
true  false  null
for  while  if  else  elseif  switch  case  default  continue  break  return
package  import  const
```

#### Literal Words

基础组件定义:

```text
letter  ->  [A-Za-z_]
digit  ->  [0-9]
digits  ->  digit digit*
```

```text
identifier  ->  letter_ (letter_ | digit_)*
int_lit  ->  digits
float_lit  ->  digits.digits
char_lit  ->  ' (charset exclude ') '
string_lit  ->  " (charset exclude ")* "
```

#### Op Symbols

```text
==  !=  <  <=  >  >=
+  -  *  /  %  &  |  ^  <<  >>
=  +=  -=  *=  /=  %=  &=  |=  ^=  <<=  >>=  ++  --
&&  ||  !
(  )  [  ]  {  }  ->  ,  .  ;  :  //
```

#### Others

```text
EOF  ILLEGAL
```
