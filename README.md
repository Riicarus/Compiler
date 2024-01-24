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

## Lex

### Lexer

Lexer scans source code file by char and returns token list.

#### Token

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
new  sizeof  free
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
+  -  *  /  %  &  |  ^  ~  <<  >>
=  +=  -=  *=  /=  %=  &=  |=  ^=  <<=  >>=  ++  --
&&  ||  !
(  )  [  ]  {  }  ->  ,  .  ;  :  ?  //
```

#### Others

```text
EOF  ILLEGAL
```

## Syntax

### Syntax Define

As the syntax analysis algorithm we use is recursive descent analysis, so the syntax cannot contain left recursion or be ambiguous.

Tips:

We use *'"' quoted strings* like `"{"` to represent literal symbols(terminal), and *camel-case words with capital letters* like `Stmt` to represent syntax symbols(nonterminal). Specially, `e` represents to $\epsilon$.

*Statements* always represent executable actions while *Expressions* always represent a value. So *Statements* is of higher level comparing to *Expressions*. But there are some intersections like: `ASSIGN`, `INC`, `DEC`, `FuncCall`, they both can represent an action or a value, we put them in both levels.

#### Program

Statements do not end with right braces(`}`) need append a semicolon(`;`) in the end as a separator of statements.

```text
Program:        Stmts

CodeBlock:      "{" Stmts "}"

Stmt:           Decl
            |   Expr ";"
            |   Control
            |   CodeBlock

Stmts:          [ [ Stmt | ";" ] Stmts ]
```

#### Declare Statement

Declare statement contains variable and function declaration, and at the same time the init assignment is also acceptable.

```text
Decl:               [ "const" ] Type Id [ "=" Expr ] ";"
                |   Type "func" Id "(" FieldDecls ")" CodeBlock

Id:                 identifier

FieldDecls:         [ FieldDecl [ "," FieldDecls ] ]

FieldDecl:          Type Id [ "=" Expr ]

Type:               BaseType ExtType
ExtType:            "func" "(" ParamTypeDecls ")" ExtType
                |   "[" "]"
                |   e

BaseType:           "int"
                |   "float"
                |   "bool"
                |   "char"
                |   "string"
                |   "void"

ParamTypeDecls:     [ Type [ "," ParamTypeDecls ] ]
```

#### Expression Statement

Expression statement contains all kinks of statement which can return/construct a value.

```text
Expr:           BinaryExpr

BinaryExpr:     UnaryExpr
            |   BinaryExpr BinaryOp BinaryExpr

BinaryOp:       "==" | "!=" | "<" | "<=" | ">" | ">="
            |   "+"| "-" | "*" | "/" | "%" | "|" | "^" | "<<" | ">>"
            |   "=" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|=" | "^=" | "<<=" | ">>="
            |   "&&" | "||"

UnaryExpr:      PrimaryExpr
            |   UnaryOp UnaryExpr

UnaryOp:        "!" | "~"
            |   "++" | "--"

Operand:        Literal
            |   Id
            |   MethodExpr
            |   "(" Expr ")"
            |   NewArr

Literal:        BasicLit
            |   CompositeLit
            |   FunctionLit

BasicLit:       int_lit
            |   float_lit
            |   char_lit
            |   string_lit
            |   "true"
            |   "false"
            |   "null"

CompositeLit:   "{" Elements "}"

Elements:       [ Expr | CompositeLit ] [ "," Elements ]

FuncLit:        Type "(" FieldDecls ")" "->" Stmt

NewArr:         "new" Type "[" Expr "]" [ CompositeList ]

PrimaryExpr:    Operand
            |   Conversion
            |   PrimaryExpr Selector
            |   PrimaryExpr Index
            |   PrimaryExpr Slice
            |   PrimaryExpr TypeAssert
            |   PrimaryExpr Arguments
            |   PrimaryExpr [ "++" | "--" ]

Selector:       "." Id

Index:          "[" Expr "]"

Slice:          "[" Expr ":" Expr "]"

TypeAssert:     "." "(" Type ")"

Arguments:      "(" Exprs ")"

Exprs:          Expr [ "," Exprs ]
```

#### Control Statement

Control statement contains loops(`for`, `while`) and brach statements(`if`, `switch`) and simple control statements(`break`, `continue`, `return`).

```text
Control:    Break
        |   Continue
        |   Return
        |   If
        |   Switch
        |   For
        |   While

Break:      "break" ";"

Continue:   "continue" ";"

Return:     "return" [ Expr ] ";"
```

##### If Statement

```text
If:         "if" "(" Expr ")" [ Stmt | ";" ] Else

Else:       ElseIfs EndElse

ElseIfs:    [ ElseIf ElseIfs ]

ElseIf:     "elseif" "(" Expr ")" [ Stmt | ";" ]

EndElse:    [ "else" [ Stmt | ";" ] ]
```

##### Switch Statement

```text
Switch:         "switch" "(" Expr ")" "{" Cases "}"

Cases:          [ { "case" Expr ":" Stmt Cases } | { "default" ":" Stmt } ]
```

##### For Statement

```text
For:            "for" "(" VarAssigns ";" [ Expr ] ";" VarAssigns ")" [ Stmt | ";" ]

VarAssigns:     [ VarAssign [ "," VarAssigns ] ]

VarAssign:      FieldDecl
            |   Id AssignOp Expr
            |   PrimaryExpr [ "++" | "--" ]
            |   [ "++" | "--" ] PrimaryExpr

AssignOp:       "=" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|=" | "^=" | "<<=" | ">>="
```

##### While Statement

```text
While:  "while" "(" Expr ")" [ Stmt | ";" ]
```

### Syntaxer

Syntaxer scans the token list given by Lexer, completing following tasks:

- AST construction
- symbol table generation
- scope management

AST makes it better to deal with semantic analysis.

#### Parse Algorithm

Syntaxer mainly uses recursive descent analysis, and there may be some look-ahead optimization algorithm to help syntaxer define which production to use next.

Syntaxer does not get all lex tokens from lexer once, but gets one by one. When successfully consuming a token or ignore a token for error recovery, syntaxer gets next token from lexer through calling method `Lexer#next()`. Syntaxer will stop the token iteration when meeting the `EOF` token.

#### Error Handling

Currently the syntaxer will only scan and report the first occurred error, because when meeting one error, the syntaxer will throw an `IllegalStateException`. But we could add some more error recovery algorithms to scan more error and report better error messages.

We may also report warnings instead of only reporting errors. The warnings meaning the statement may be unnecessary or lead some runtime errors.

#### AST

For the above syntax, we designed AST nodes for it. The AST nodes is divided into kinds bellow:

- `Expr`: Expressions which can provide values.
  - `Operation`: Expressions like: `X Op Y`, either `X` or `Y` could be null.
  - `Lit`: Expressions which are directly a value.
    - `BasicLit`: Direct literal value but not identifier.
    - `FuncLit`: Function's prototype definition, like: `RetType (Params) -> CodeBlock`
    - `CompositeLit`: Lits which are compose by elements, like: `Type {Ele[0], Ele[1], ...}`, mainly used in array initializations.
  - `CondExpr`: Ternary expressions like: `X ? Y : X`.
  - `CallExpr`: Function call like: `FuncName(Params[0], Params[1], ...)`.
  - `IndexExpr`: Get element from array by index, like: `X[Index]`.
  - `SliceExpr`: Get elements(slice) from array by from and to index, like: `X[index[0], index[1]]`.
  - `CastExpr`: Cast variable into another type, like: `(Type) X`.
  - `NameExpr`: Identifiers.
  - `NewArrExpr`: Array created by keyword `new`, like: `new Type[] { Elements }`;
- `Stmt`: Executable actions.
  - `CodeBlock`: Representing a new scope.
  - `SimpleStmt`: Statements which can also provide values.
    - `AssignExpr`: Expressions like: `X AssignOp Y`, either `X` or `Y` could be null, representing self-assign expressions like: `X++`, `--X`.
    - `FieldDecl`: Declare *function params*, *variables* with optional initialization value like: `const Type FieldName = X`, if `X` is not null, there would be an `AssignExpr` as a field.
    - `CallExpr`
    - `NewExpr`
  - `DeclStmt`: Declarations.
    - `FieldDecl`
    - `FuncDecl`: Declare a function with function name(`NameExpr`) and prototype(`FuncLit`).
    - `TypeDecl`: Declare a type.
      - `ArrayType`: Declare an array, like: `BaseType []` or `BaseType [Size]`
      - `FuncType`: Declare a function type, like: `RetType func ( ParamTypes )`.
      - `BaseType`: Declare base types, like: `int`, `float`, .etc.
  - `ControlStmt`: Process control actions.
    - `BreakStmt`
    - `ContinueStmt`
    - `RetStmt`
    - `IfStmt`
    - `SwitchStmt`
    - `ForStmt`
    - `WhileStmt`
