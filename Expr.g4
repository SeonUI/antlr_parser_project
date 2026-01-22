grammar Expr;

// parser rules
prog : decl_list expr ';';

decl_list : decl* ;

decl : 'def' var var_list '=' expr 'endef' ;

var_list : var* ;

expr : 'let' var '=' expr 'in' expr  # letinExpr
     | var '(' ')'                    # varfunExpr
     | var '(' expr_list ')'       # varfunparExpr     
     | expr ('*'|'/') expr         # infixExpr
     | expr ('+'|'-') expr         # infixExpr 
     | num                         # numberExpr
     | '(' expr ')'                # parensExpr
     | '~' '(' expr ')'            # negExpr
     | var                         # varExpr
     ;
     
expr_list : (expr ',')* expr ;

var : VAR  
    | FUN
    | PAR
    ;

num  : INT
     | REAL
     ;
     
// lexer rules                    
NEWLINE: [\r\n]+ ;
INT: '-'?[0-9]+ ;          // should handle negatives
REAL: '-'?[0-9]+'.'[0-9]* ; // should handle signs(+/-)
WS: [ \t\r\n]+ -> skip ;     
VAR: [a-zA-Z_-]+ ;
FUN: [a-zA-Z0-9]+ ;
PAR: [a-zA-Z_]+ ;
