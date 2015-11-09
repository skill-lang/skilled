// Generated from ..\src\SKilLParser.g4 by ANTLR 4.5.1
package grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SKilLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		HEAD_COMMENT=1, INCLUDE=2, WITH=3, EXTENDS=4, DOUBLEPOINT=5, BRACEO=6, 
		BRACEC=7, ENUM=8, INTERFACE=9, TYPEDEF=10, CONST=11, AUTO=12, MAP=13, 
		SET=14, LIST=15, ANNOTATION=16, COMMA=17, SEMICOLON=18, PARENTHESEO=19, 
		PARENTHESEC=20, LESSTHAN=21, GREATERTHAN=22, BRACKETO=23, BRACKETC=24, 
		EXCLAMATIONMARK=25, AT=26, EQUAL=27, COMMENT=28, Identifier=29, IntegerConstant=30, 
		FloatingConstant=31, StringLiteral=32, Whitespace=33, Newline=34;
	public static final int
		RULE_file = 0, RULE_header = 1, RULE_include = 2, RULE_declaration = 3, 
		RULE_usertype = 4, RULE_enumtype = 5, RULE_interfacetype = 6, RULE_typedef = 7, 
		RULE_field = 8, RULE_constant = 9, RULE_data = 10, RULE_type = 11, RULE_typemulti = 12, 
		RULE_typesingle = 13, RULE_arraytype = 14, RULE_groundtype = 15, RULE_description = 16, 
		RULE_restriction = 17, RULE_rarg = 18, RULE_hint = 19;
	public static final String[] ruleNames = {
		"file", "header", "include", "declaration", "usertype", "enumtype", "interfacetype", 
		"typedef", "field", "constant", "data", "type", "typemulti", "typesingle", 
		"arraytype", "groundtype", "description", "restriction", "rarg", "hint"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'include'", "'with'", "'extends'", "':'", "'{'", "'}'", "'enum'", 
		"'interface'", "'typedef'", "'const'", "'auto'", "'map'", "'set'", "'list'", 
		"'annotation'", "','", "';'", "'('", "')'", "'<'", "'>'", "'['", "']'", 
		"'!'", "'@'", "'='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "HEAD_COMMENT", "INCLUDE", "WITH", "EXTENDS", "DOUBLEPOINT", "BRACEO", 
		"BRACEC", "ENUM", "INTERFACE", "TYPEDEF", "CONST", "AUTO", "MAP", "SET", 
		"LIST", "ANNOTATION", "COMMA", "SEMICOLON", "PARENTHESEO", "PARENTHESEC", 
		"LESSTHAN", "GREATERTHAN", "BRACKETO", "BRACKETC", "EXCLAMATIONMARK", 
		"AT", "EQUAL", "COMMENT", "Identifier", "IntegerConstant", "FloatingConstant", 
		"StringLiteral", "Whitespace", "Newline"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SKilLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SKilLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FileContext extends ParserRuleContext {
		public HeaderContext header() {
			return getRuleContext(HeaderContext.class,0);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public FileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitFile(this);
		}
	}

	public final FileContext file() throws RecognitionException {
		FileContext _localctx = new FileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_file);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			header();
			setState(44);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ENUM) | (1L << INTERFACE) | (1L << TYPEDEF) | (1L << EXCLAMATIONMARK) | (1L << AT) | (1L << COMMENT) | (1L << Identifier))) != 0)) {
				{
				{
				setState(41);
				declaration();
				}
				}
				setState(46);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeaderContext extends ParserRuleContext {
		public List<TerminalNode> HEAD_COMMENT() { return getTokens(SKilLParser.HEAD_COMMENT); }
		public TerminalNode HEAD_COMMENT(int i) {
			return getToken(SKilLParser.HEAD_COMMENT, i);
		}
		public List<IncludeContext> include() {
			return getRuleContexts(IncludeContext.class);
		}
		public IncludeContext include(int i) {
			return getRuleContext(IncludeContext.class,i);
		}
		public HeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitHeader(this);
		}
	}

	public final HeaderContext header() throws RecognitionException {
		HeaderContext _localctx = new HeaderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_header);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==HEAD_COMMENT) {
				{
				{
				setState(47);
				match(HEAD_COMMENT);
				}
				}
				setState(52);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==INCLUDE || _la==WITH) {
				{
				{
				setState(53);
				include();
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncludeContext extends ParserRuleContext {
		public List<TerminalNode> StringLiteral() { return getTokens(SKilLParser.StringLiteral); }
		public TerminalNode StringLiteral(int i) {
			return getToken(SKilLParser.StringLiteral, i);
		}
		public IncludeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_include; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterInclude(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitInclude(this);
		}
	}

	public final IncludeContext include() throws RecognitionException {
		IncludeContext _localctx = new IncludeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_include);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			_la = _input.LA(1);
			if ( !(_la==INCLUDE || _la==WITH) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			setState(61); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(60);
				match(StringLiteral);
				}
				}
				setState(63); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==StringLiteral );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public UsertypeContext usertype() {
			return getRuleContext(UsertypeContext.class,0);
		}
		public EnumtypeContext enumtype() {
			return getRuleContext(EnumtypeContext.class,0);
		}
		public InterfacetypeContext interfacetype() {
			return getRuleContext(InterfacetypeContext.class,0);
		}
		public TypedefContext typedef() {
			return getRuleContext(TypedefContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitDeclaration(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(65);
				usertype();
				}
				break;
			case 2:
				{
				setState(66);
				enumtype();
				}
				break;
			case 3:
				{
				setState(67);
				interfacetype();
				}
				break;
			case 4:
				{
				setState(68);
				typedef();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UsertypeContext extends ParserRuleContext {
		public Token name;
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public List<TerminalNode> Identifier() { return getTokens(SKilLParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(SKilLParser.Identifier, i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public UsertypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usertype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterUsertype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitUsertype(this);
		}
	}

	public final UsertypeContext usertype() throws RecognitionException {
		UsertypeContext _localctx = new UsertypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_usertype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			description();
			setState(72);
			((UsertypeContext)_localctx).name = match(Identifier);
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << WITH) | (1L << EXTENDS) | (1L << DOUBLEPOINT))) != 0)) {
				{
				{
				setState(73);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << WITH) | (1L << EXTENDS) | (1L << DOUBLEPOINT))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(74);
				match(Identifier);
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(80);
			match(BRACEO);
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONST) | (1L << AUTO) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << ANNOTATION) | (1L << EXCLAMATIONMARK) | (1L << AT) | (1L << COMMENT) | (1L << Identifier))) != 0)) {
				{
				{
				setState(81);
				field();
				}
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(87);
			match(BRACEC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumtypeContext extends ParserRuleContext {
		public Token name;
		public List<TerminalNode> Identifier() { return getTokens(SKilLParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(SKilLParser.Identifier, i);
		}
		public TerminalNode COMMENT() { return getToken(SKilLParser.COMMENT, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public EnumtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumtype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterEnumtype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitEnumtype(this);
		}
	}

	public final EnumtypeContext enumtype() throws RecognitionException {
		EnumtypeContext _localctx = new EnumtypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_enumtype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(89);
				match(COMMENT);
				}
			}

			setState(92);
			match(ENUM);
			setState(93);
			((EnumtypeContext)_localctx).name = match(Identifier);
			setState(94);
			match(BRACEO);
			setState(95);
			match(Identifier);
			setState(100);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(96);
				match(COMMA);
				setState(97);
				match(Identifier);
				}
				}
				setState(102);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(103);
			match(SEMICOLON);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONST) | (1L << AUTO) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << ANNOTATION) | (1L << EXCLAMATIONMARK) | (1L << AT) | (1L << COMMENT) | (1L << Identifier))) != 0)) {
				{
				{
				setState(104);
				field();
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
			match(BRACEC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InterfacetypeContext extends ParserRuleContext {
		public Token name;
		public List<TerminalNode> Identifier() { return getTokens(SKilLParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(SKilLParser.Identifier, i);
		}
		public TerminalNode COMMENT() { return getToken(SKilLParser.COMMENT, 0); }
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public InterfacetypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfacetype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterInterfacetype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitInterfacetype(this);
		}
	}

	public final InterfacetypeContext interfacetype() throws RecognitionException {
		InterfacetypeContext _localctx = new InterfacetypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_interfacetype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(112);
				match(COMMENT);
				}
			}

			setState(115);
			match(INTERFACE);
			setState(116);
			((InterfacetypeContext)_localctx).name = match(Identifier);
			setState(121);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << WITH) | (1L << EXTENDS) | (1L << DOUBLEPOINT))) != 0)) {
				{
				{
				setState(117);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << WITH) | (1L << EXTENDS) | (1L << DOUBLEPOINT))) != 0)) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(118);
				match(Identifier);
				}
				}
				setState(123);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(124);
			match(BRACEO);
			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONST) | (1L << AUTO) | (1L << MAP) | (1L << SET) | (1L << LIST) | (1L << ANNOTATION) | (1L << EXCLAMATIONMARK) | (1L << AT) | (1L << COMMENT) | (1L << Identifier))) != 0)) {
				{
				{
				setState(125);
				field();
				}
				}
				setState(130);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(131);
			match(BRACEC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypedefContext extends ParserRuleContext {
		public Token name;
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(SKilLParser.Identifier, 0); }
		public TerminalNode COMMENT() { return getToken(SKilLParser.COMMENT, 0); }
		public List<RestrictionContext> restriction() {
			return getRuleContexts(RestrictionContext.class);
		}
		public RestrictionContext restriction(int i) {
			return getRuleContext(RestrictionContext.class,i);
		}
		public List<HintContext> hint() {
			return getRuleContexts(HintContext.class);
		}
		public HintContext hint(int i) {
			return getRuleContext(HintContext.class,i);
		}
		public TypedefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterTypedef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitTypedef(this);
		}
	}

	public final TypedefContext typedef() throws RecognitionException {
		TypedefContext _localctx = new TypedefContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_typedef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(133);
				match(COMMENT);
				}
			}

			setState(136);
			match(TYPEDEF);
			setState(137);
			((TypedefContext)_localctx).name = match(Identifier);
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EXCLAMATIONMARK || _la==AT) {
				{
				setState(140);
				switch (_input.LA(1)) {
				case AT:
					{
					setState(138);
					restriction();
					}
					break;
				case EXCLAMATIONMARK:
					{
					setState(139);
					hint();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(144);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(145);
			type();
			setState(146);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldContext extends ParserRuleContext {
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public DataContext data() {
			return getRuleContext(DataContext.class,0);
		}
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitField(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_field);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			description();
			setState(151);
			switch (_input.LA(1)) {
			case CONST:
				{
				setState(149);
				constant();
				}
				break;
			case AUTO:
			case MAP:
			case SET:
			case LIST:
			case ANNOTATION:
			case Identifier:
				{
				setState(150);
				data();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(153);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(SKilLParser.Identifier, 0); }
		public TerminalNode IntegerConstant() { return getToken(SKilLParser.IntegerConstant, 0); }
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitConstant(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_constant);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(CONST);
			setState(156);
			type();
			setState(157);
			match(Identifier);
			setState(158);
			match(EQUAL);
			setState(159);
			match(IntegerConstant);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(SKilLParser.Identifier, 0); }
		public DataContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_data; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterData(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitData(this);
		}
	}

	public final DataContext data() throws RecognitionException {
		DataContext _localctx = new DataContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_data);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			_la = _input.LA(1);
			if (_la==AUTO) {
				{
				setState(161);
				match(AUTO);
				}
			}

			setState(164);
			type();
			setState(165);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypemultiContext typemulti() {
			return getRuleContext(TypemultiContext.class,0);
		}
		public TypesingleContext typesingle() {
			return getRuleContext(TypesingleContext.class,0);
		}
		public ArraytypeContext arraytype() {
			return getRuleContext(ArraytypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			switch (_input.LA(1)) {
			case MAP:
				{
				setState(167);
				match(MAP);
				setState(168);
				typemulti();
				}
				break;
			case SET:
				{
				setState(169);
				match(SET);
				setState(170);
				typesingle();
				}
				break;
			case LIST:
				{
				setState(171);
				match(LIST);
				setState(172);
				typesingle();
				}
				break;
			case ANNOTATION:
			case Identifier:
				{
				setState(173);
				arraytype();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypemultiContext extends ParserRuleContext {
		public List<GroundtypeContext> groundtype() {
			return getRuleContexts(GroundtypeContext.class);
		}
		public GroundtypeContext groundtype(int i) {
			return getRuleContext(GroundtypeContext.class,i);
		}
		public TypemultiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typemulti; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterTypemulti(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitTypemulti(this);
		}
	}

	public final TypemultiContext typemulti() throws RecognitionException {
		TypemultiContext _localctx = new TypemultiContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_typemulti);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(LESSTHAN);
			setState(177);
			groundtype();
			setState(180); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(178);
				match(COMMA);
				setState(179);
				groundtype();
				}
				}
				setState(182); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			setState(184);
			match(GREATERTHAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypesingleContext extends ParserRuleContext {
		public GroundtypeContext groundtype() {
			return getRuleContext(GroundtypeContext.class,0);
		}
		public TypesingleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typesingle; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterTypesingle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitTypesingle(this);
		}
	}

	public final TypesingleContext typesingle() throws RecognitionException {
		TypesingleContext _localctx = new TypesingleContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_typesingle);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(LESSTHAN);
			setState(187);
			groundtype();
			setState(188);
			match(GREATERTHAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArraytypeContext extends ParserRuleContext {
		public GroundtypeContext groundtype() {
			return getRuleContext(GroundtypeContext.class,0);
		}
		public TerminalNode IntegerConstant() { return getToken(SKilLParser.IntegerConstant, 0); }
		public ArraytypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arraytype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterArraytype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitArraytype(this);
		}
	}

	public final ArraytypeContext arraytype() throws RecognitionException {
		ArraytypeContext _localctx = new ArraytypeContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_arraytype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			groundtype();
			setState(196);
			_la = _input.LA(1);
			if (_la==BRACKETO) {
				{
				setState(191);
				match(BRACKETO);
				setState(193);
				_la = _input.LA(1);
				if (_la==IntegerConstant) {
					{
					setState(192);
					match(IntegerConstant);
					}
				}

				setState(195);
				match(BRACKETC);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroundtypeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(SKilLParser.Identifier, 0); }
		public GroundtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groundtype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterGroundtype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitGroundtype(this);
		}
	}

	public final GroundtypeContext groundtype() throws RecognitionException {
		GroundtypeContext _localctx = new GroundtypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_groundtype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			_la = _input.LA(1);
			if ( !(_la==ANNOTATION || _la==Identifier) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DescriptionContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(SKilLParser.COMMENT, 0); }
		public List<RestrictionContext> restriction() {
			return getRuleContexts(RestrictionContext.class);
		}
		public RestrictionContext restriction(int i) {
			return getRuleContext(RestrictionContext.class,i);
		}
		public List<HintContext> hint() {
			return getRuleContexts(HintContext.class);
		}
		public HintContext hint(int i) {
			return getRuleContext(HintContext.class,i);
		}
		public DescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_description; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitDescription(this);
		}
	}

	public final DescriptionContext description() throws RecognitionException {
		DescriptionContext _localctx = new DescriptionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_description);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			_la = _input.LA(1);
			if (_la==COMMENT) {
				{
				setState(200);
				match(COMMENT);
				}
			}

			setState(207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EXCLAMATIONMARK || _la==AT) {
				{
				setState(205);
				switch (_input.LA(1)) {
				case AT:
					{
					setState(203);
					restriction();
					}
					break;
				case EXCLAMATIONMARK:
					{
					setState(204);
					hint();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(209);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RestrictionContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(SKilLParser.Identifier, 0); }
		public List<RargContext> rarg() {
			return getRuleContexts(RargContext.class);
		}
		public RargContext rarg(int i) {
			return getRuleContext(RargContext.class,i);
		}
		public RestrictionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_restriction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterRestriction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitRestriction(this);
		}
	}

	public final RestrictionContext restriction() throws RecognitionException {
		RestrictionContext _localctx = new RestrictionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_restriction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(AT);
			setState(211);
			match(Identifier);
			setState(224);
			_la = _input.LA(1);
			if (_la==PARENTHESEO) {
				{
				setState(212);
				match(PARENTHESEO);
				setState(221);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Identifier) | (1L << IntegerConstant) | (1L << FloatingConstant) | (1L << StringLiteral))) != 0)) {
					{
					setState(213);
					rarg();
					setState(218);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(214);
						match(COMMA);
						setState(215);
						rarg();
						}
						}
						setState(220);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(223);
				match(PARENTHESEC);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RargContext extends ParserRuleContext {
		public TerminalNode FloatingConstant() { return getToken(SKilLParser.FloatingConstant, 0); }
		public TerminalNode IntegerConstant() { return getToken(SKilLParser.IntegerConstant, 0); }
		public TerminalNode StringLiteral() { return getToken(SKilLParser.StringLiteral, 0); }
		public List<TerminalNode> Identifier() { return getTokens(SKilLParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(SKilLParser.Identifier, i);
		}
		public RargContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rarg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterRarg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitRarg(this);
		}
	}

	public final RargContext rarg() throws RecognitionException {
		RargContext _localctx = new RargContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_rarg);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			switch (_input.LA(1)) {
			case FloatingConstant:
				{
				setState(226);
				match(FloatingConstant);
				}
				break;
			case IntegerConstant:
				{
				setState(227);
				match(IntegerConstant);
				}
				break;
			case StringLiteral:
				{
				setState(228);
				match(StringLiteral);
				}
				break;
			case Identifier:
				{
				{
				setState(229);
				match(Identifier);
				setState(234);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(230);
						match(COMMA);
						setState(231);
						match(Identifier);
						}
						} 
					}
					setState(236);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
				}
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HintContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(SKilLParser.Identifier, 0); }
		public List<RargContext> rarg() {
			return getRuleContexts(RargContext.class);
		}
		public RargContext rarg(int i) {
			return getRuleContext(RargContext.class,i);
		}
		public HintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).enterHint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SKilLParserListener ) ((SKilLParserListener)listener).exitHint(this);
		}
	}

	public final HintContext hint() throws RecognitionException {
		HintContext _localctx = new HintContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_hint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(EXCLAMATIONMARK);
			setState(240);
			match(Identifier);
			setState(253);
			_la = _input.LA(1);
			if (_la==PARENTHESEO) {
				{
				setState(241);
				match(PARENTHESEO);
				setState(250);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Identifier) | (1L << IntegerConstant) | (1L << FloatingConstant) | (1L << StringLiteral))) != 0)) {
					{
					setState(242);
					rarg();
					setState(247);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(243);
						match(COMMA);
						setState(244);
						rarg();
						}
						}
						setState(249);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(252);
				match(PARENTHESEC);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3$\u0102\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\7\2-\n\2\f\2\16\2\60\13\2\3\3\7"+
		"\3\63\n\3\f\3\16\3\66\13\3\3\3\7\39\n\3\f\3\16\3<\13\3\3\4\3\4\6\4@\n"+
		"\4\r\4\16\4A\3\5\3\5\3\5\3\5\5\5H\n\5\3\6\3\6\3\6\3\6\7\6N\n\6\f\6\16"+
		"\6Q\13\6\3\6\3\6\7\6U\n\6\f\6\16\6X\13\6\3\6\3\6\3\7\5\7]\n\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\7\7e\n\7\f\7\16\7h\13\7\3\7\3\7\7\7l\n\7\f\7\16\7o\13"+
		"\7\3\7\3\7\3\b\5\bt\n\b\3\b\3\b\3\b\3\b\7\bz\n\b\f\b\16\b}\13\b\3\b\3"+
		"\b\7\b\u0081\n\b\f\b\16\b\u0084\13\b\3\b\3\b\3\t\5\t\u0089\n\t\3\t\3\t"+
		"\3\t\3\t\7\t\u008f\n\t\f\t\16\t\u0092\13\t\3\t\3\t\3\t\3\n\3\n\3\n\5\n"+
		"\u009a\n\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\5\f\u00a5\n\f\3\f"+
		"\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00b1\n\r\3\16\3\16\3\16\3\16"+
		"\6\16\u00b7\n\16\r\16\16\16\u00b8\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3"+
		"\20\3\20\5\20\u00c4\n\20\3\20\5\20\u00c7\n\20\3\21\3\21\3\22\5\22\u00cc"+
		"\n\22\3\22\3\22\7\22\u00d0\n\22\f\22\16\22\u00d3\13\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\7\23\u00db\n\23\f\23\16\23\u00de\13\23\5\23\u00e0\n\23"+
		"\3\23\5\23\u00e3\n\23\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u00eb\n\24\f"+
		"\24\16\24\u00ee\13\24\5\24\u00f0\n\24\3\25\3\25\3\25\3\25\3\25\3\25\7"+
		"\25\u00f8\n\25\f\25\16\25\u00fb\13\25\5\25\u00fd\n\25\3\25\5\25\u0100"+
		"\n\25\3\25\2\2\26\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(\2\5\3\2"+
		"\4\5\3\2\5\7\4\2\22\22\37\37\u0114\2*\3\2\2\2\4\64\3\2\2\2\6=\3\2\2\2"+
		"\bG\3\2\2\2\nI\3\2\2\2\f\\\3\2\2\2\16s\3\2\2\2\20\u0088\3\2\2\2\22\u0096"+
		"\3\2\2\2\24\u009d\3\2\2\2\26\u00a4\3\2\2\2\30\u00b0\3\2\2\2\32\u00b2\3"+
		"\2\2\2\34\u00bc\3\2\2\2\36\u00c0\3\2\2\2 \u00c8\3\2\2\2\"\u00cb\3\2\2"+
		"\2$\u00d4\3\2\2\2&\u00ef\3\2\2\2(\u00f1\3\2\2\2*.\5\4\3\2+-\5\b\5\2,+"+
		"\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\3\3\2\2\2\60.\3\2\2\2\61\63"+
		"\7\3\2\2\62\61\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65:\3"+
		"\2\2\2\66\64\3\2\2\2\679\5\6\4\28\67\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2"+
		"\2\2;\5\3\2\2\2<:\3\2\2\2=?\t\2\2\2>@\7\"\2\2?>\3\2\2\2@A\3\2\2\2A?\3"+
		"\2\2\2AB\3\2\2\2B\7\3\2\2\2CH\5\n\6\2DH\5\f\7\2EH\5\16\b\2FH\5\20\t\2"+
		"GC\3\2\2\2GD\3\2\2\2GE\3\2\2\2GF\3\2\2\2H\t\3\2\2\2IJ\5\"\22\2JO\7\37"+
		"\2\2KL\t\3\2\2LN\7\37\2\2MK\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3"+
		"\2\2\2QO\3\2\2\2RV\7\b\2\2SU\5\22\n\2TS\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW"+
		"\3\2\2\2WY\3\2\2\2XV\3\2\2\2YZ\7\t\2\2Z\13\3\2\2\2[]\7\36\2\2\\[\3\2\2"+
		"\2\\]\3\2\2\2]^\3\2\2\2^_\7\n\2\2_`\7\37\2\2`a\7\b\2\2af\7\37\2\2bc\7"+
		"\23\2\2ce\7\37\2\2db\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2gi\3\2\2\2h"+
		"f\3\2\2\2im\7\24\2\2jl\5\22\n\2kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2"+
		"\2np\3\2\2\2om\3\2\2\2pq\7\t\2\2q\r\3\2\2\2rt\7\36\2\2sr\3\2\2\2st\3\2"+
		"\2\2tu\3\2\2\2uv\7\13\2\2v{\7\37\2\2wx\t\3\2\2xz\7\37\2\2yw\3\2\2\2z}"+
		"\3\2\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}{\3\2\2\2~\u0082\7\b\2\2\177\u0081"+
		"\5\22\n\2\u0080\177\3\2\2\2\u0081\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0082\3\2\2\2\u0085\u0086\7\t"+
		"\2\2\u0086\17\3\2\2\2\u0087\u0089\7\36\2\2\u0088\u0087\3\2\2\2\u0088\u0089"+
		"\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008b\7\f\2\2\u008b\u0090\7\37\2\2"+
		"\u008c\u008f\5$\23\2\u008d\u008f\5(\25\2\u008e\u008c\3\2\2\2\u008e\u008d"+
		"\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091"+
		"\u0093\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0094\5\30\r\2\u0094\u0095\7"+
		"\24\2\2\u0095\21\3\2\2\2\u0096\u0099\5\"\22\2\u0097\u009a\5\24\13\2\u0098"+
		"\u009a\5\26\f\2\u0099\u0097\3\2\2\2\u0099\u0098\3\2\2\2\u009a\u009b\3"+
		"\2\2\2\u009b\u009c\7\24\2\2\u009c\23\3\2\2\2\u009d\u009e\7\r\2\2\u009e"+
		"\u009f\5\30\r\2\u009f\u00a0\7\37\2\2\u00a0\u00a1\7\35\2\2\u00a1\u00a2"+
		"\7 \2\2\u00a2\25\3\2\2\2\u00a3\u00a5\7\16\2\2\u00a4\u00a3\3\2\2\2\u00a4"+
		"\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\5\30\r\2\u00a7\u00a8\7"+
		"\37\2\2\u00a8\27\3\2\2\2\u00a9\u00aa\7\17\2\2\u00aa\u00b1\5\32\16\2\u00ab"+
		"\u00ac\7\20\2\2\u00ac\u00b1\5\34\17\2\u00ad\u00ae\7\21\2\2\u00ae\u00b1"+
		"\5\34\17\2\u00af\u00b1\5\36\20\2\u00b0\u00a9\3\2\2\2\u00b0\u00ab\3\2\2"+
		"\2\u00b0\u00ad\3\2\2\2\u00b0\u00af\3\2\2\2\u00b1\31\3\2\2\2\u00b2\u00b3"+
		"\7\27\2\2\u00b3\u00b6\5 \21\2\u00b4\u00b5\7\23\2\2\u00b5\u00b7\5 \21\2"+
		"\u00b6\u00b4\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9"+
		"\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00bb\7\30\2\2\u00bb\33\3\2\2\2\u00bc"+
		"\u00bd\7\27\2\2\u00bd\u00be\5 \21\2\u00be\u00bf\7\30\2\2\u00bf\35\3\2"+
		"\2\2\u00c0\u00c6\5 \21\2\u00c1\u00c3\7\31\2\2\u00c2\u00c4\7 \2\2\u00c3"+
		"\u00c2\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\7\32"+
		"\2\2\u00c6\u00c1\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\37\3\2\2\2\u00c8\u00c9"+
		"\t\4\2\2\u00c9!\3\2\2\2\u00ca\u00cc\7\36\2\2\u00cb\u00ca\3\2\2\2\u00cb"+
		"\u00cc\3\2\2\2\u00cc\u00d1\3\2\2\2\u00cd\u00d0\5$\23\2\u00ce\u00d0\5("+
		"\25\2\u00cf\u00cd\3\2\2\2\u00cf\u00ce\3\2\2\2\u00d0\u00d3\3\2\2\2\u00d1"+
		"\u00cf\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2#\3\2\2\2\u00d3\u00d1\3\2\2\2"+
		"\u00d4\u00d5\7\34\2\2\u00d5\u00e2\7\37\2\2\u00d6\u00df\7\25\2\2\u00d7"+
		"\u00dc\5&\24\2\u00d8\u00d9\7\23\2\2\u00d9\u00db\5&\24\2\u00da\u00d8\3"+
		"\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd"+
		"\u00e0\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00d7\3\2\2\2\u00df\u00e0\3\2"+
		"\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e3\7\26\2\2\u00e2\u00d6\3\2\2\2\u00e2"+
		"\u00e3\3\2\2\2\u00e3%\3\2\2\2\u00e4\u00f0\7!\2\2\u00e5\u00f0\7 \2\2\u00e6"+
		"\u00f0\7\"\2\2\u00e7\u00ec\7\37\2\2\u00e8\u00e9\7\23\2\2\u00e9\u00eb\7"+
		"\37\2\2\u00ea\u00e8\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec"+
		"\u00ed\3\2\2\2\u00ed\u00f0\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00e4\3\2"+
		"\2\2\u00ef\u00e5\3\2\2\2\u00ef\u00e6\3\2\2\2\u00ef\u00e7\3\2\2\2\u00f0"+
		"\'\3\2\2\2\u00f1\u00f2\7\33\2\2\u00f2\u00ff\7\37\2\2\u00f3\u00fc\7\25"+
		"\2\2\u00f4\u00f9\5&\24\2\u00f5\u00f6\7\23\2\2\u00f6\u00f8\5&\24\2\u00f7"+
		"\u00f5\3\2\2\2\u00f8\u00fb\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2"+
		"\2\2\u00fa\u00fd\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fc\u00f4\3\2\2\2\u00fc"+
		"\u00fd\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u0100\7\26\2\2\u00ff\u00f3\3"+
		"\2\2\2\u00ff\u0100\3\2\2\2\u0100)\3\2\2\2#.\64:AGOV\\fms{\u0082\u0088"+
		"\u008e\u0090\u0099\u00a4\u00b0\u00b8\u00c3\u00c6\u00cb\u00cf\u00d1\u00dc"+
		"\u00df\u00e2\u00ec\u00ef\u00f9\u00fc\u00ff";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}