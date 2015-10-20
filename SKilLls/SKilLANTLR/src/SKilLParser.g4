//@author Marco Link
parser grammar SKilLParser;
options{tokenVocab = SKilLLexer;}

file: header declaration*;

header: HEAD_COMMENT* include*;

include: ('include' | 'with') StringLiteral+;

declaration: (usertype | enumtype | interfacetype | typedef);

usertype: description name=Identifier ((':' | 'with' | 'extends') Identifier)* '{' field* '}';

enumtype: COMMENT? 'enum' name=Identifier '{' Identifier (',' Identifier)* ';' field* '}';

interfacetype: COMMENT? 'interface' name=Identifier ((':' | 'with' | 'extends') Identifier)* '{' field* '}';

typedef: COMMENT? 'typedef' name=Identifier (restriction | hint)* type ';';

field: description (constant | data) ';' ;

constant: 'const' type Identifier '=' IntegerConstant;

data: 'auto'? type Identifier;

type: ('map' typemulti | 'set' typesingle | 'list' typesingle | arraytype);

typemulti: '<' groundtype (',' groundtype)+ '>';

typesingle: '<' groundtype '>';

arraytype: groundtype ('[' IntegerConstant? ']')?;

groundtype: ('annotation' | Identifier);

description: COMMENT? (restriction | hint)*;

restriction: '@' Identifier ('(' (rarg (',' rarg)*)?')')?;

rarg: (FloatingConstant | IntegerConstant | StringLiteral | (Identifier (',' Identifier)*));

hint: '!' Identifier ('(' (rarg (',' rarg)*)?')')?;