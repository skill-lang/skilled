//@author Marco Link
parser grammar SKilLParser;
options{tokenVocab = SKilLLexer;}

file: header declaration*;

header: HEAD_COMMENT* include*;

includeWord: ('include' | 'with');

include: includeWord StringLiteral+;

declaration: (usertype | enumtype | interfacetype | typedef);

usertype: description name=Identifier extension* '{' field* '}';

enumtype: COMMENT? 'enum' name=Identifier '{' enumvalues ';' field* '}';

interfacetype: COMMENT? 'interface' name=Identifier extension* '{' field* '}';

extendWord: (':' | 'with' | 'extends');

extension: extendWord Identifier;

typedef: COMMENT? 'typedef' name=Identifier restrictionHint* type ';';

field: description (constant | data) ';' ;

constant: 'const' type Identifier '=' IntegerConstant;

data: 'auto'? type Identifier;

type: ('map' typemulti | 'set' typesingle | 'list' typesingle | arraytype);

typemulti: '<' groundtype (',' groundtype)+ '>';

typesingle: '<' groundtype '>';

arraytype: groundtype ('[' IntegerConstant? ']')?;

groundtype: ('annotation' | Identifier);

description: COMMENT? restrictionHint*;

restriction: '@' Identifier ('(' (rarg (',' rarg)*)?')')?;

rarg: (FloatingConstant | IntegerConstant | StringLiteral | (Identifier (',' Identifier)*));

hint: '!' Identifier ('(' (rarg (',' rarg)*)?')')?;

enumvalues: Identifier (',' Identifier)*;

restrictionHint: (restriction | hint);
