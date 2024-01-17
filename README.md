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
new
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

### Syntax Define

As the syntax analysis algorithm we use is recursive descent analysis, so the syntax cannot contain left recursion or be ambiguous.

Tips:

We use *'"' quoted strings* like `"{"` to represent literal symbols(terminal), and *camel-case words with capital letters* like `Stmt` to represent syntax symbols(nonterminal). Specially, `e` represents to $\epsilon$.

#### Program

Statements do not end with right braces(`}`) need append a semicolon(`;`) in the end as a separator of statements.

```text
Program:    Stmts

CodeBlock:  "{" Stmts "}"

Stmt:   Decl
    |   Expr ";"
    |   Control
    |   CodeBlock

Stmts:  NullableStmt Stmts
    |   eps

NullableStmt:   Stmt
            |   ";"
```

#### Declare Statement

Declare statement contains variable and function declaration, and at the same time the init assignment is also acceptable.

```text
Decl:   Type Id ";"
    |   Type Id "=" Expr ";"
    |   Type "func" Id "(" ParamDecls ")" CodeBlock

Id:     identifier

ParamDecls:     ParamDecl ParamDecls'
            |   e
ParamDecls':    "," ParamDecl ParamDecls'
            |   e
ParamDecl:      Type Id

Type:   BaseType Type'
    |   TypeName Type'
Type':  "func" "(" ParamTypeDecls ")" Type'
    |   "[" "]"
    |   e

BaseType:   "int"
        |   "float"
        |   "bool"
        |   "char"
        |   "string"
        |   "void"

ParamTypeDecls:     Type ParamTypeDecls'
                |   e
ParamTypeDecls':    "," Type ParamTypeDecls'
                |   e

StructDecl: "type" TypeName "struct" "{" FieldDecls "}"
TypeName:   identifier

FieldDecls:     FieldDecl FieldDecls
            |   e
FieldDecl:      Type Id ";"
```

#### Expression Statement

Expression statement contains all kinks of statement which can return/construct a value.

```text
Expr:   AssignExpr

AssignExpr: CondExpr
        |   UnaryExpr AssignOp AssignExpr

AssignOp:   "="
        |   "+="
        |   "-="
        |   "*="
        |   "/="
        |   "%="
        |   "&="
        |   "|="
        |   "^="
        |   "<<="
        |   ">>="

CondExpr:   LorExpr
        |   LorExpr "?" Expr : Expr

LorExpr:    LandExpr LorExpr'
LorExpr':   "||" LandExpr LorExpr'
        |   e

LandExpr:   OrExpr LandExpr'
LandExpr':  "&&" OrExpr LandExpr'
        |   e

OrExpr:     XorExpr OrExpr'
OrExpr':    "|" XorExpr OrExpr'
        |   e

XorExpr:    AndExpr XorExpr'
XorExpr':   "^" AndExpr XorExpr'
        |   e

AndExpr:    EqExpr AndExpr'
AndExpr':   "&" EqExpr AndExpr'
        |   e

EqExpr:     RelExpr EqExpr'
EqExpr':    "==" RelExpr EqExpr'
        |   "!=" RelExpr EqExpr'
        |   e

RelExpr:    ShiftExpr RelExpr'
RelExpr':   RelOp ShiftExpr RelExpr'
        |   e

RelOp:  ">"
    |   ">="
    |   "<"
    |   "<="

ShiftExpr:  AddExpr ShiftExpr'
ShiftExpr': "<<" AddExpr ShiftExpr'
        |   ">>" AddExpr ShiftExpr'
        |   e

AddExpr:    MulExpr AddExpr'
AddExpr':   "+" MulExpr AddExpr'
        |   "-" MulExpr AddExpr'
        |   e

MulExpr:    CastExpr MulExpr'
MulExpr':   "*" CastExpr MulExpr'
        |   "-" CastExpr MulExpr'
        |   "%" CastExpr MulExpr'
        |   e

CastExpr:   UnaryExpr
        |   "(" Type ")" CastExpr

UnaryExpr:  PostfixExpr
        |   UnaryOp CastExpr

UnaryOp:    "!"
        |   "~"
        |   "&"
        |   "*"
        |   "++"
        |   "--"

PostfixExpr:    PrimExpr PostfixExpr'
PostfixExpr':   "[" Expr "]" PostfixExpr'
            |   "[" Expr ":" Expr "]" PostfixExpr'
            |   "(" Params ")" PostfixExpr'
            |   "." Id PostfixExpr'
            |   "++"
            |   "--"

Params:     Expr Params'
        |   e
Params':    "," Expr Params'
        |   e

PrimExpr:   Id
        |   Const
        |   "(" Expr ")"
        |   FuncInlineDecl
        |   NewExpr

Const:  int_lit
    |   float_lit
    |   char_lit
    |   string_lit
    |   "true"
    |   "false"
    |   "null"

FuncInlineDecl: Type "(" ParamDecls ")" "->" Stmt

NewExpr:    "new" Type "(" Params ")"
        |   "new" Type "[" Expr "]"
        |   "new" Type "[" Expr  "]" "{" Elements "}"

Elements:   Expr Elements'
Elements':  ", " Expr Elements'
        |   e
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

Return:     "return" ";"
        |   "return" Expr ";"
```

##### If Statement

```text
If:         "if" "(" Expr ")" NullableStmt Else

Else:       ElseIfs EndElse

ElseIfs:    ElseIf ElseIfs
        |   e

ElseIf:     "elseif" "(" Expr ")" NullableStmt

EndElse:    "else" NullableStmt
        |   e
```

##### Switch Statement

```text
Switch:         "switch" "(" Expr ")" "{" Cases "}"

Cases:          Case Cases
            |   DefaultCase
            |   e

Case:           "case" Expr ":" Stmt
DefaultCase:    "default" ":" Stmt
```

##### For Statement

```text
For:            "for" "(" ForInits ";" ForCond ";" ForUpdate ")" NullableStmt

ForInits:       VarAssigns

ForCond:        Expr
            |   e

ForUpdate:      VarAssigns

VarAssigns:     VarAssign VarAssigns'
            |   e
VarAssigns':    "," VarAssign VarAssigns'
            |   e
VarAssign:      Type Id ":=" Expr
            |   Id ":=" Expr
```

##### While Statement

```text
While:  "while" "(" Expr ")" NullableStmt
```
