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

Basic regex definition:

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

Unquoted assistant symbols:

- `{}`: a group of symbols as an integration
- `[]`: optional symbols group
- `|`: choose one from connected symbols

*Statements* always represent executable actions while *Expressions* always represent a value. So *Statements* is of higher level comparing to *Expressions*. But there are some intersections like: `ASSIGN`, `INC`, `DEC`, `FuncCall`, they both can represent an action or a value, we put them in both levels.

#### Program

Statements do not end with right braces(`}`) need append a semicolon(`;`) in the end as a separator of statements.

```text
Program:        Stmts

CodeBlock:      "{" Stmts "}"

Stmts:          [ Stmt Stmts ]

Stmt:           Decl
            |   Expr ";"
            |   Control
            |   CodeBlock
            |   ";"

SimpleStmt:     FieldDecl
            |   Expr
```

#### Declare Statement

Declare statement contains variable and function declaration, and at the same time the init assignment is also acceptable.

Declarations are either const or normal, while the direct function declaration must be const.

```text
Decl:               [ "const" ] Type Id [ "=" Expr ] ";"
                |   Type "func" Id "(" FieldDecls ")" CodeBlock

Id:                 identifier

FieldDecls:         [ FieldDecl [ "," FieldDecls ] ]

FieldDecl:          [ "const" ] Type Id [ "=" Expr ]

Type:               BasicType ExtType

BasicType:          "int"
                |   "float"
                |   "bool"
                |   "char"
                |   "string"
                |   "void"

ExtType:            [ { { "func" "(" ParamTypeDecls ")" } | { "[" "]" } } ExtType ]

ParamTypeDecls:     [ Type [ "," ParamTypeDecls ] ]
```

#### Expression Statement

Expression statement contains all kinks of statements which can return/construct a value.

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

Operand:        Literal
            |   Id
            |   "(" Expr ")"
            |   NewArr

Literal:        BasicLit
            |   CompositeLit
            |   FuncLit

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

NewArr:         "new" Type Sizes [ CompositeLit ]

Sizes:          "[" int_lit "]" [ Sizes ]

PrimaryExpr:    Operand
            |   PrimaryExpr Index
            |   PrimaryExpr Slice
            |   PrimaryExpr TypeAssert
            |   PrimaryExpr Arguments
            |   PrimaryExpr { "++" | "--" }
            |   { "++" | "--" } PrimaryExpr
            |   "sizeof" "(" PrimaryExpr ")"

Index:          "[" Expr "]"

Slice:          "[" [ Expr ] ":" [ Expr ] "]"

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
If:         "if" "(" Expr ")" { Stmt | ";" } Else

Else:       ElseIfs EndElse

ElseIfs:    [ ElseIf ElseIfs ]

ElseIf:     "elseif" "(" Expr ")" { Stmt | ";" }

EndElse:    [ "else" { Stmt | ";" } ]
```

##### Switch Statement

```text
Switch:         "switch" "(" Expr ")" "{" Cases "}"

Cases:          [ { "case" Expr ":" Stmt Cases } | { "default" ":" Stmt } ]
```

##### For Statement

```text
For:            "for" "(" SimpleStmts ";" [ Expr ] ";" SimpleStmts ")" { Stmt | ";" }

AssignOp:       "=" | "+=" | "-=" | "*=" | "/=" | "%=" | "&=" | "|=" | "^=" | "<<=" | ">>="
```

##### While Statement

```text
While:  "while" "(" Expr ")" { Stmt | ";" }
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
    - `FuncLit`: Function's prototype definition, like: `RetType (Params) -> CodeBlock`.
    - `CompositeLit`: Lits which are compose by elements, like: `Type {Ele[0], Ele[1], ...}`, mainly used in array initializations.
  - `CallExpr`: Function call like: `FuncName(Params[0], Params[1], ...)`.
  - `IndexExpr`: Get element from array by index, like: `X[Index]`.
  - `SliceExpr`: Get elements(slice) from array by from and to index, like: `X[index[0], index[1]]`.
  - `CastExpr`: Cast variable into another type, like: `(Type) X`.
  - `NameExpr`: Identifiers.
  - `ArrExpr`: Create array by keyword `new`, like: `new Type Sizes { Elements }`.
  - `SizeExpr`: Get array size like: `sizeof(X)`.
  - `Inc/DecExpr`: Variables pre/post self increase/decrease expression, like: `i++`, `--i`.
- `Stmt`: Executable actions.
  - `CodeBlock`: Representing a new scope.
  - `SimpleStmt`: Statements which can also provide values.
    - `Expr`
    - `FieldDecl`: Declare *function params*, *variables* with optional initialization value like: `const Type FieldName = X`. If `X` is not null, there would be an `AssignExpr` as a field.
  - `DeclStmt`: Declarations.
    - `FieldDecl`
    - `FuncDecl`: Declare a function with function name(`NameExpr`) and prototype(`FuncLit`).
    - `TypeDecl`: Declare a type.
      - `ArrayType`: Declare an array, like: `BasicType []` or `BasicType [Size]`.
      - `FuncType`: Declare a function type, like: `RetType func ( ParamTypes )`.
      - `BasicType`: Declare base types, like: `int`, `float`, .etc.
  - `ControlStmt`: Process control actions.
    - `BreakStmt`
    - `ContinueStmt`
    - `RetStmt`
    - `IfStmt`
    - `SwitchStmt`
    - `ForStmt`
    - `WhileStmt`
- `CodeFile`: Code in a single file, contains all AST nodes in the file.

## Semantic

The semantic checker based on AST nodes generated by Syntaxer.

Semantic checker will do jobs bellow:

- Scope and element management
- Type check and type inference
- Static computation optimization
- Closure scope increment
- Clean unnecessary code

### Scope and element manage

We create scopes and collect elements during *recursive descent analyze* process, and eventually the root scope in `CodeFile` contains all scopes and elements in one code file.

The scope is distinguished by name and parent scope's name, but statements like `If`, `For` do not have distinguishable names, so we add a suffix to the name, formatted as: `Name#${Id}`, `${Id}` is the size of current scope's element set.

We use `Scope#enter(String name)` to enter or create(only in elements collecting phase), but we can not remember the id of scope's name, so we simply bind each scope to the leading statement which leads to the new scope, thus we use `Stmt#getScope()` to get the binding scope and enter with its name with `Scope#getName()`. Scope's full name is `(parent == null ? "" : parent.getFullName() + ".") + name`.

If we bind a function lit to a variable, the `FuncLit` will be firstly created as an anonymous function and its scope is also anonymous, in `decl()` we update its name to the bound variable's name.

Some complex statements have a code block like body with only one statement, but not quoted by `{}`, we bind the scope to the statement. Otherwise, the scope will bind to code block, or the outer size of statements like `ForStmt`.

> Note that the scope will set its `collecting` field to `false` immediately after the first invocation of `Scope#exit()` method.

### Type check

Type check checks through the AST.
