//@author Marco Link
parser grammar SKilLParser;
options{tokenVocab = SKilLLexer;}

file: header declaration*;

header: HEAD_COMMENT* include*;

includeWord: (INCLUDE | WITH);

include: includeWord StringLiteral+;

declaration: (usertype | enumtype | interfacetype | typedef);

usertype: description name=Identifier extension* '{' field* '}';

enumtype: COMMENT? ENUM name=Identifier '{' enumvalues ';' field* '}';

interfacetype: COMMENT? INTERFACE name=Identifier extension* '{' field* '}';

extendWord: (':' | WITH | EXTENDS);

extension: extendWord Identifier;

typedef: COMMENT? TYPEDEF name=Identifier restrictionHint* type ';';

field: description (view | constant | data) ';' ;

view: VIEW (extendedIdentifer '.')? extendedIdentifer AS data;

constant: CONST type extendedIdentifer '=' IntegerConstant;

data: AUTO? type extendedIdentifer;

type: (MAP typemulti | SET typesingle | LIST typesingle | arraytype);

typemulti: '<' groundtype (',' groundtype)+ '>';

typesingle: '<' groundtype '>';

arraytype: groundtype ('[' IntegerConstant? ']')?;

groundtype: (ANNOTATION | Identifier);

description: COMMENT? restrictionHint*;

restriction: '@' Identifier ('(' (rarg (',' rarg)*)?')')?;

rarg: (FloatingConstant | IntegerConstant | StringLiteral | (extendedIdentifer ('.' | '::')?)+);

hint: '!' Identifier rarg? ('(' (rarg (',' rarg)*)?')')?;

enumvalues: Identifier (',' Identifier)*;

restrictionHint: (restriction | hint);

extendedIdentifer: Identifier
| (INCLUDE | WITH | EXTENDS | ENUM | INTERFACE | TYPEDEF | CONST | AUTO | MAP | SET | LIST | ANNOTATION | VIEW | AS)
;