// Generated from ..\src\SKilLLexer.g4 by ANTLR 4.5.1
package grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SKilLLexer extends Lexer {
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
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"HEAD_COMMENT", "INCLUDE", "WITH", "EXTENDS", "DOUBLEPOINT", "BRACEO", 
		"BRACEC", "ENUM", "INTERFACE", "TYPEDEF", "CONST", "AUTO", "MAP", "SET", 
		"LIST", "ANNOTATION", "COMMA", "SEMICOLON", "PARENTHESEO", "PARENTHESEC", 
		"LESSTHAN", "GREATERTHAN", "BRACKETO", "BRACKETC", "EXCLAMATIONMARK", 
		"AT", "EQUAL", "COMMENT", "Identifier", "IdentifierNondigit", "Nondigit", 
		"Digit", "UniversalCharacterName", "HexQuad", "IntegerConstant", "DecimalConstant", 
		"OctalConstant", "HexadecimalConstant", "HexadecimalPrefix", "NonzeroDigit", 
		"OctalDigit", "HexadecimalDigit", "IntegerSuffix", "UnsignedSuffix", "LongSuffix", 
		"LongLongSuffix", "FloatingConstant", "DecimalFloatingConstant", "HexadecimalFloatingConstant", 
		"FractionalConstant", "ExponentPart", "Sign", "DigitSequence", "HexadecimalFractionalConstant", 
		"BinaryExponentPart", "HexadecimalDigitSequence", "FloatingSuffix", "CharacterConstant", 
		"CCharSequence", "CChar", "EscapeSequence", "SimpleEscapeSequence", "OctalEscapeSequence", 
		"HexadecimalEscapeSequence", "StringLiteral", "EncodingPrefix", "SCharSequence", 
		"SChar", "Whitespace", "Newline"
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


	public SKilLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SKilLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2$\u0236\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\3\2\3\2\7"+
		"\2\u0092\n\2\f\2\16\2\u0095\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7"+
		"\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r"+
		"\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3"+
		"\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3"+
		"\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\35\3\35\7\35\u0108\n\35"+
		"\f\35\16\35\u010b\13\35\3\35\3\35\3\35\3\36\3\36\3\36\7\36\u0113\n\36"+
		"\f\36\16\36\u0116\13\36\3\37\3\37\5\37\u011a\n\37\3 \3 \3!\3!\3\"\3\""+
		"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\5\"\u012a\n\"\3#\3#\3#\3#\3#\3$\3$\5"+
		"$\u0133\n$\3$\3$\5$\u0137\n$\3$\3$\5$\u013b\n$\5$\u013d\n$\3%\3%\7%\u0141"+
		"\n%\f%\16%\u0144\13%\3&\3&\7&\u0148\n&\f&\16&\u014b\13&\3\'\3\'\6\'\u014f"+
		"\n\'\r\'\16\'\u0150\3(\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\5,\u015e\n,\3,\3"+
		",\3,\3,\3,\5,\u0165\n,\3,\3,\5,\u0169\n,\5,\u016b\n,\3-\3-\3.\3.\3/\3"+
		"/\3/\3/\5/\u0175\n/\3\60\3\60\5\60\u0179\n\60\3\61\3\61\5\61\u017d\n\61"+
		"\3\61\5\61\u0180\n\61\3\61\3\61\3\61\5\61\u0185\n\61\5\61\u0187\n\61\3"+
		"\62\3\62\3\62\3\62\5\62\u018d\n\62\3\62\3\62\3\62\3\62\5\62\u0193\n\62"+
		"\5\62\u0195\n\62\3\63\5\63\u0198\n\63\3\63\3\63\3\63\3\63\3\63\5\63\u019f"+
		"\n\63\3\64\3\64\5\64\u01a3\n\64\3\64\3\64\3\64\5\64\u01a8\n\64\3\64\5"+
		"\64\u01ab\n\64\3\65\3\65\3\66\6\66\u01b0\n\66\r\66\16\66\u01b1\3\67\5"+
		"\67\u01b5\n\67\3\67\3\67\3\67\3\67\3\67\5\67\u01bc\n\67\38\38\58\u01c0"+
		"\n8\38\38\38\58\u01c5\n8\38\58\u01c8\n8\39\69\u01cb\n9\r9\169\u01cc\3"+
		":\3:\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3;\3"+
		";\5;\u01e7\n;\3<\6<\u01ea\n<\r<\16<\u01eb\3=\3=\5=\u01f0\n=\3>\3>\3>\3"+
		">\5>\u01f6\n>\3?\3?\3?\3@\3@\3@\3@\3@\3@\3@\3@\3@\3@\3@\5@\u0206\n@\3"+
		"A\3A\3A\3A\6A\u020c\nA\rA\16A\u020d\3B\5B\u0211\nB\3B\3B\5B\u0215\nB\3"+
		"B\3B\3C\3C\3C\5C\u021c\nC\3D\6D\u021f\nD\rD\16D\u0220\3E\3E\5E\u0225\n"+
		"E\3F\6F\u0228\nF\rF\16F\u0229\3F\3F\3G\3G\5G\u0230\nG\3G\5G\u0233\nG\3"+
		"G\3G\3\u0109\2H\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37=\2?\2A\2C\2E\2G I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_!"+
		"a\2c\2e\2g\2i\2k\2m\2o\2q\2s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\"\u0085"+
		"\2\u0087\2\u0089\2\u008b#\u008d$\3\2\22\3\2\f\f\5\2C\\aac|\3\2\62;\4\2"+
		"ZZzz\3\2\63;\3\2\629\5\2\62;CHch\4\2WWww\4\2NNnn\4\2--//\6\2HHNNhhnn\6"+
		"\2\f\f\17\17))^^\f\2$$))AA^^cdhhppttvvxx\5\2NNWWww\6\2\f\f\17\17$$^^\4"+
		"\2\13\13\"\"\u024d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\29\3\2\2\2\2;\3\2\2\2\2G\3\2\2\2\2_\3\2\2\2\2\u0083\3\2\2\2\2\u008b\3"+
		"\2\2\2\2\u008d\3\2\2\2\3\u008f\3\2\2\2\5\u0098\3\2\2\2\7\u00a0\3\2\2\2"+
		"\t\u00a5\3\2\2\2\13\u00ad\3\2\2\2\r\u00af\3\2\2\2\17\u00b1\3\2\2\2\21"+
		"\u00b3\3\2\2\2\23\u00b8\3\2\2\2\25\u00c2\3\2\2\2\27\u00ca\3\2\2\2\31\u00d0"+
		"\3\2\2\2\33\u00d5\3\2\2\2\35\u00d9\3\2\2\2\37\u00dd\3\2\2\2!\u00e2\3\2"+
		"\2\2#\u00ed\3\2\2\2%\u00ef\3\2\2\2\'\u00f1\3\2\2\2)\u00f3\3\2\2\2+\u00f5"+
		"\3\2\2\2-\u00f7\3\2\2\2/\u00f9\3\2\2\2\61\u00fb\3\2\2\2\63\u00fd\3\2\2"+
		"\2\65\u00ff\3\2\2\2\67\u0101\3\2\2\29\u0103\3\2\2\2;\u010f\3\2\2\2=\u0119"+
		"\3\2\2\2?\u011b\3\2\2\2A\u011d\3\2\2\2C\u0129\3\2\2\2E\u012b\3\2\2\2G"+
		"\u013c\3\2\2\2I\u013e\3\2\2\2K\u0145\3\2\2\2M\u014c\3\2\2\2O\u0152\3\2"+
		"\2\2Q\u0155\3\2\2\2S\u0157\3\2\2\2U\u0159\3\2\2\2W\u016a\3\2\2\2Y\u016c"+
		"\3\2\2\2[\u016e\3\2\2\2]\u0174\3\2\2\2_\u0178\3\2\2\2a\u0186\3\2\2\2c"+
		"\u0194\3\2\2\2e\u019e\3\2\2\2g\u01aa\3\2\2\2i\u01ac\3\2\2\2k\u01af\3\2"+
		"\2\2m\u01bb\3\2\2\2o\u01c7\3\2\2\2q\u01ca\3\2\2\2s\u01ce\3\2\2\2u\u01e6"+
		"\3\2\2\2w\u01e9\3\2\2\2y\u01ef\3\2\2\2{\u01f5\3\2\2\2}\u01f7\3\2\2\2\177"+
		"\u0205\3\2\2\2\u0081\u0207\3\2\2\2\u0083\u0210\3\2\2\2\u0085\u021b\3\2"+
		"\2\2\u0087\u021e\3\2\2\2\u0089\u0224\3\2\2\2\u008b\u0227\3\2\2\2\u008d"+
		"\u0232\3\2\2\2\u008f\u0093\7%\2\2\u0090\u0092\n\2\2\2\u0091\u0090\3\2"+
		"\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094"+
		"\u0096\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u0097\7\f\2\2\u0097\4\3\2\2\2"+
		"\u0098\u0099\7k\2\2\u0099\u009a\7p\2\2\u009a\u009b\7e\2\2\u009b\u009c"+
		"\7n\2\2\u009c\u009d\7w\2\2\u009d\u009e\7f\2\2\u009e\u009f\7g\2\2\u009f"+
		"\6\3\2\2\2\u00a0\u00a1\7y\2\2\u00a1\u00a2\7k\2\2\u00a2\u00a3\7v\2\2\u00a3"+
		"\u00a4\7j\2\2\u00a4\b\3\2\2\2\u00a5\u00a6\7g\2\2\u00a6\u00a7\7z\2\2\u00a7"+
		"\u00a8\7v\2\2\u00a8\u00a9\7g\2\2\u00a9\u00aa\7p\2\2\u00aa\u00ab\7f\2\2"+
		"\u00ab\u00ac\7u\2\2\u00ac\n\3\2\2\2\u00ad\u00ae\7<\2\2\u00ae\f\3\2\2\2"+
		"\u00af\u00b0\7}\2\2\u00b0\16\3\2\2\2\u00b1\u00b2\7\177\2\2\u00b2\20\3"+
		"\2\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7p\2\2\u00b5\u00b6\7w\2\2\u00b6"+
		"\u00b7\7o\2\2\u00b7\22\3\2\2\2\u00b8\u00b9\7k\2\2\u00b9\u00ba\7p\2\2\u00ba"+
		"\u00bb\7v\2\2\u00bb\u00bc\7g\2\2\u00bc\u00bd\7t\2\2\u00bd\u00be\7h\2\2"+
		"\u00be\u00bf\7c\2\2\u00bf\u00c0\7e\2\2\u00c0\u00c1\7g\2\2\u00c1\24\3\2"+
		"\2\2\u00c2\u00c3\7v\2\2\u00c3\u00c4\7{\2\2\u00c4\u00c5\7r\2\2\u00c5\u00c6"+
		"\7g\2\2\u00c6\u00c7\7f\2\2\u00c7\u00c8\7g\2\2\u00c8\u00c9\7h\2\2\u00c9"+
		"\26\3\2\2\2\u00ca\u00cb\7e\2\2\u00cb\u00cc\7q\2\2\u00cc\u00cd\7p\2\2\u00cd"+
		"\u00ce\7u\2\2\u00ce\u00cf\7v\2\2\u00cf\30\3\2\2\2\u00d0\u00d1\7c\2\2\u00d1"+
		"\u00d2\7w\2\2\u00d2\u00d3\7v\2\2\u00d3\u00d4\7q\2\2\u00d4\32\3\2\2\2\u00d5"+
		"\u00d6\7o\2\2\u00d6\u00d7\7c\2\2\u00d7\u00d8\7r\2\2\u00d8\34\3\2\2\2\u00d9"+
		"\u00da\7u\2\2\u00da\u00db\7g\2\2\u00db\u00dc\7v\2\2\u00dc\36\3\2\2\2\u00dd"+
		"\u00de\7n\2\2\u00de\u00df\7k\2\2\u00df\u00e0\7u\2\2\u00e0\u00e1\7v\2\2"+
		"\u00e1 \3\2\2\2\u00e2\u00e3\7c\2\2\u00e3\u00e4\7p\2\2\u00e4\u00e5\7p\2"+
		"\2\u00e5\u00e6\7q\2\2\u00e6\u00e7\7v\2\2\u00e7\u00e8\7c\2\2\u00e8\u00e9"+
		"\7v\2\2\u00e9\u00ea\7k\2\2\u00ea\u00eb\7q\2\2\u00eb\u00ec\7p\2\2\u00ec"+
		"\"\3\2\2\2\u00ed\u00ee\7.\2\2\u00ee$\3\2\2\2\u00ef\u00f0\7=\2\2\u00f0"+
		"&\3\2\2\2\u00f1\u00f2\7*\2\2\u00f2(\3\2\2\2\u00f3\u00f4\7+\2\2\u00f4*"+
		"\3\2\2\2\u00f5\u00f6\7>\2\2\u00f6,\3\2\2\2\u00f7\u00f8\7@\2\2\u00f8.\3"+
		"\2\2\2\u00f9\u00fa\7]\2\2\u00fa\60\3\2\2\2\u00fb\u00fc\7_\2\2\u00fc\62"+
		"\3\2\2\2\u00fd\u00fe\7#\2\2\u00fe\64\3\2\2\2\u00ff\u0100\7B\2\2\u0100"+
		"\66\3\2\2\2\u0101\u0102\7?\2\2\u01028\3\2\2\2\u0103\u0104\7\61\2\2\u0104"+
		"\u0105\7,\2\2\u0105\u0109\3\2\2\2\u0106\u0108\13\2\2\2\u0107\u0106\3\2"+
		"\2\2\u0108\u010b\3\2\2\2\u0109\u010a\3\2\2\2\u0109\u0107\3\2\2\2\u010a"+
		"\u010c\3\2\2\2\u010b\u0109\3\2\2\2\u010c\u010d\7,\2\2\u010d\u010e\7\61"+
		"\2\2\u010e:\3\2\2\2\u010f\u0114\5=\37\2\u0110\u0113\5=\37\2\u0111\u0113"+
		"\5A!\2\u0112\u0110\3\2\2\2\u0112\u0111\3\2\2\2\u0113\u0116\3\2\2\2\u0114"+
		"\u0112\3\2\2\2\u0114\u0115\3\2\2\2\u0115<\3\2\2\2\u0116\u0114\3\2\2\2"+
		"\u0117\u011a\5? \2\u0118\u011a\5C\"\2\u0119\u0117\3\2\2\2\u0119\u0118"+
		"\3\2\2\2\u011a>\3\2\2\2\u011b\u011c\t\3\2\2\u011c@\3\2\2\2\u011d\u011e"+
		"\t\4\2\2\u011eB\3\2\2\2\u011f\u0120\7^\2\2\u0120\u0121\7w\2\2\u0121\u0122"+
		"\3\2\2\2\u0122\u012a\5E#\2\u0123\u0124\7^\2\2\u0124\u0125\7W\2\2\u0125"+
		"\u0126\3\2\2\2\u0126\u0127\5E#\2\u0127\u0128\5E#\2\u0128\u012a\3\2\2\2"+
		"\u0129\u011f\3\2\2\2\u0129\u0123\3\2\2\2\u012aD\3\2\2\2\u012b\u012c\5"+
		"U+\2\u012c\u012d\5U+\2\u012d\u012e\5U+\2\u012e\u012f\5U+\2\u012fF\3\2"+
		"\2\2\u0130\u0132\5I%\2\u0131\u0133\5W,\2\u0132\u0131\3\2\2\2\u0132\u0133"+
		"\3\2\2\2\u0133\u013d\3\2\2\2\u0134\u0136\5K&\2\u0135\u0137\5W,\2\u0136"+
		"\u0135\3\2\2\2\u0136\u0137\3\2\2\2\u0137\u013d\3\2\2\2\u0138\u013a\5M"+
		"\'\2\u0139\u013b\5W,\2\u013a\u0139\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013d"+
		"\3\2\2\2\u013c\u0130\3\2\2\2\u013c\u0134\3\2\2\2\u013c\u0138\3\2\2\2\u013d"+
		"H\3\2\2\2\u013e\u0142\5Q)\2\u013f\u0141\5A!\2\u0140\u013f\3\2\2\2\u0141"+
		"\u0144\3\2\2\2\u0142\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143J\3\2\2\2"+
		"\u0144\u0142\3\2\2\2\u0145\u0149\7\62\2\2\u0146\u0148\5S*\2\u0147\u0146"+
		"\3\2\2\2\u0148\u014b\3\2\2\2\u0149\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a"+
		"L\3\2\2\2\u014b\u0149\3\2\2\2\u014c\u014e\5O(\2\u014d\u014f\5U+\2\u014e"+
		"\u014d\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2"+
		"\2\2\u0151N\3\2\2\2\u0152\u0153\7\62\2\2\u0153\u0154\t\5\2\2\u0154P\3"+
		"\2\2\2\u0155\u0156\t\6\2\2\u0156R\3\2\2\2\u0157\u0158\t\7\2\2\u0158T\3"+
		"\2\2\2\u0159\u015a\t\b\2\2\u015aV\3\2\2\2\u015b\u015d\5Y-\2\u015c\u015e"+
		"\5[.\2\u015d\u015c\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u016b\3\2\2\2\u015f"+
		"\u0160\5Y-\2\u0160\u0161\5]/\2\u0161\u016b\3\2\2\2\u0162\u0164\5[.\2\u0163"+
		"\u0165\5Y-\2\u0164\u0163\3\2\2\2\u0164\u0165\3\2\2\2\u0165\u016b\3\2\2"+
		"\2\u0166\u0168\5]/\2\u0167\u0169\5Y-\2\u0168\u0167\3\2\2\2\u0168\u0169"+
		"\3\2\2\2\u0169\u016b\3\2\2\2\u016a\u015b\3\2\2\2\u016a\u015f\3\2\2\2\u016a"+
		"\u0162\3\2\2\2\u016a\u0166\3\2\2\2\u016bX\3\2\2\2\u016c\u016d\t\t\2\2"+
		"\u016dZ\3\2\2\2\u016e\u016f\t\n\2\2\u016f\\\3\2\2\2\u0170\u0171\7n\2\2"+
		"\u0171\u0175\7n\2\2\u0172\u0173\7N\2\2\u0173\u0175\7N\2\2\u0174\u0170"+
		"\3\2\2\2\u0174\u0172\3\2\2\2\u0175^\3\2\2\2\u0176\u0179\5a\61\2\u0177"+
		"\u0179\5c\62\2\u0178\u0176\3\2\2\2\u0178\u0177\3\2\2\2\u0179`\3\2\2\2"+
		"\u017a\u017c\5e\63\2\u017b\u017d\5g\64\2\u017c\u017b\3\2\2\2\u017c\u017d"+
		"\3\2\2\2\u017d\u017f\3\2\2\2\u017e\u0180\5s:\2\u017f\u017e\3\2\2\2\u017f"+
		"\u0180\3\2\2\2\u0180\u0187\3\2\2\2\u0181\u0182\5k\66\2\u0182\u0184\5g"+
		"\64\2\u0183\u0185\5s:\2\u0184\u0183\3\2\2\2\u0184\u0185\3\2\2\2\u0185"+
		"\u0187\3\2\2\2\u0186\u017a\3\2\2\2\u0186\u0181\3\2\2\2\u0187b\3\2\2\2"+
		"\u0188\u0189\5O(\2\u0189\u018a\5m\67\2\u018a\u018c\5o8\2\u018b\u018d\5"+
		"s:\2\u018c\u018b\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u0195\3\2\2\2\u018e"+
		"\u018f\5O(\2\u018f\u0190\5q9\2\u0190\u0192\5o8\2\u0191\u0193\5s:\2\u0192"+
		"\u0191\3\2\2\2\u0192\u0193\3\2\2\2\u0193\u0195\3\2\2\2\u0194\u0188\3\2"+
		"\2\2\u0194\u018e\3\2\2\2\u0195d\3\2\2\2\u0196\u0198\5k\66\2\u0197\u0196"+
		"\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0199\3\2\2\2\u0199\u019a\7\60\2\2"+
		"\u019a\u019f\5k\66\2\u019b\u019c\5k\66\2\u019c\u019d\7\60\2\2\u019d\u019f"+
		"\3\2\2\2\u019e\u0197\3\2\2\2\u019e\u019b\3\2\2\2\u019ff\3\2\2\2\u01a0"+
		"\u01a2\7g\2\2\u01a1\u01a3\5i\65\2\u01a2\u01a1\3\2\2\2\u01a2\u01a3\3\2"+
		"\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01ab\5k\66\2\u01a5\u01a7\7G\2\2\u01a6"+
		"\u01a8\5i\65\2\u01a7\u01a6\3\2\2\2\u01a7\u01a8\3\2\2\2\u01a8\u01a9\3\2"+
		"\2\2\u01a9\u01ab\5k\66\2\u01aa\u01a0\3\2\2\2\u01aa\u01a5\3\2\2\2\u01ab"+
		"h\3\2\2\2\u01ac\u01ad\t\13\2\2\u01adj\3\2\2\2\u01ae\u01b0\5A!\2\u01af"+
		"\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01af\3\2\2\2\u01b1\u01b2\3\2"+
		"\2\2\u01b2l\3\2\2\2\u01b3\u01b5\5q9\2\u01b4\u01b3\3\2\2\2\u01b4\u01b5"+
		"\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b7\7\60\2\2\u01b7\u01bc\5q9\2\u01b8"+
		"\u01b9\5q9\2\u01b9\u01ba\7\60\2\2\u01ba\u01bc\3\2\2\2\u01bb\u01b4\3\2"+
		"\2\2\u01bb\u01b8\3\2\2\2\u01bcn\3\2\2\2\u01bd\u01bf\7r\2\2\u01be\u01c0"+
		"\5i\65\2\u01bf\u01be\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1"+
		"\u01c8\5k\66\2\u01c2\u01c4\7R\2\2\u01c3\u01c5\5i\65\2\u01c4\u01c3\3\2"+
		"\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c8\5k\66\2\u01c7"+
		"\u01bd\3\2\2\2\u01c7\u01c2\3\2\2\2\u01c8p\3\2\2\2\u01c9\u01cb\5U+\2\u01ca"+
		"\u01c9\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc\u01cd\3\2"+
		"\2\2\u01cdr\3\2\2\2\u01ce\u01cf\t\f\2\2\u01cft\3\2\2\2\u01d0\u01d1\7)"+
		"\2\2\u01d1\u01d2\5w<\2\u01d2\u01d3\7)\2\2\u01d3\u01e7\3\2\2\2\u01d4\u01d5"+
		"\7N\2\2\u01d5\u01d6\7)\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d8\5w<\2\u01d8"+
		"\u01d9\7)\2\2\u01d9\u01e7\3\2\2\2\u01da\u01db\7w\2\2\u01db\u01dc\7)\2"+
		"\2\u01dc\u01dd\3\2\2\2\u01dd\u01de\5w<\2\u01de\u01df\7)\2\2\u01df\u01e7"+
		"\3\2\2\2\u01e0\u01e1\7W\2\2\u01e1\u01e2\7)\2\2\u01e2\u01e3\3\2\2\2\u01e3"+
		"\u01e4\5w<\2\u01e4\u01e5\7)\2\2\u01e5\u01e7\3\2\2\2\u01e6\u01d0\3\2\2"+
		"\2\u01e6\u01d4\3\2\2\2\u01e6\u01da\3\2\2\2\u01e6\u01e0\3\2\2\2\u01e7v"+
		"\3\2\2\2\u01e8\u01ea\5y=\2\u01e9\u01e8\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb"+
		"\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ecx\3\2\2\2\u01ed\u01f0\n\r\2\2"+
		"\u01ee\u01f0\5{>\2\u01ef\u01ed\3\2\2\2\u01ef\u01ee\3\2\2\2\u01f0z\3\2"+
		"\2\2\u01f1\u01f6\5}?\2\u01f2\u01f6\5\177@\2\u01f3\u01f6\5\u0081A\2\u01f4"+
		"\u01f6\5C\"\2\u01f5\u01f1\3\2\2\2\u01f5\u01f2\3\2\2\2\u01f5\u01f3\3\2"+
		"\2\2\u01f5\u01f4\3\2\2\2\u01f6|\3\2\2\2\u01f7\u01f8\7^\2\2\u01f8\u01f9"+
		"\t\16\2\2\u01f9~\3\2\2\2\u01fa\u01fb\7^\2\2\u01fb\u0206\5S*\2\u01fc\u01fd"+
		"\7^\2\2\u01fd\u01fe\5S*\2\u01fe\u01ff\5S*\2\u01ff\u0206\3\2\2\2\u0200"+
		"\u0201\7^\2\2\u0201\u0202\5S*\2\u0202\u0203\5S*\2\u0203\u0204\5S*\2\u0204"+
		"\u0206\3\2\2\2\u0205\u01fa\3\2\2\2\u0205\u01fc\3\2\2\2\u0205\u0200\3\2"+
		"\2\2\u0206\u0080\3\2\2\2\u0207\u0208\7^\2\2\u0208\u0209\7z\2\2\u0209\u020b"+
		"\3\2\2\2\u020a\u020c\5U+\2\u020b\u020a\3\2\2\2\u020c\u020d\3\2\2\2\u020d"+
		"\u020b\3\2\2\2\u020d\u020e\3\2\2\2\u020e\u0082\3\2\2\2\u020f\u0211\5\u0085"+
		"C\2\u0210\u020f\3\2\2\2\u0210\u0211\3\2\2\2\u0211\u0212\3\2\2\2\u0212"+
		"\u0214\7$\2\2\u0213\u0215\5\u0087D\2\u0214\u0213\3\2\2\2\u0214\u0215\3"+
		"\2\2\2\u0215\u0216\3\2\2\2\u0216\u0217\7$\2\2\u0217\u0084\3\2\2\2\u0218"+
		"\u0219\7w\2\2\u0219\u021c\7:\2\2\u021a\u021c\t\17\2\2\u021b\u0218\3\2"+
		"\2\2\u021b\u021a\3\2\2\2\u021c\u0086\3\2\2\2\u021d\u021f\5\u0089E\2\u021e"+
		"\u021d\3\2\2\2\u021f\u0220\3\2\2\2\u0220\u021e\3\2\2\2\u0220\u0221\3\2"+
		"\2\2\u0221\u0088\3\2\2\2\u0222\u0225\n\20\2\2\u0223\u0225\5{>\2\u0224"+
		"\u0222\3\2\2\2\u0224\u0223\3\2\2\2\u0225\u008a\3\2\2\2\u0226\u0228\t\21"+
		"\2\2\u0227\u0226\3\2\2\2\u0228\u0229\3\2\2\2\u0229\u0227\3\2\2\2\u0229"+
		"\u022a\3\2\2\2\u022a\u022b\3\2\2\2\u022b\u022c\bF\2\2\u022c\u008c\3\2"+
		"\2\2\u022d\u022f\7\17\2\2\u022e\u0230\7\f\2\2\u022f\u022e\3\2\2\2\u022f"+
		"\u0230\3\2\2\2\u0230\u0233\3\2\2\2\u0231\u0233\7\f\2\2\u0232\u022d\3\2"+
		"\2\2\u0232\u0231\3\2\2\2\u0233\u0234\3\2\2\2\u0234\u0235\bG\2\2\u0235"+
		"\u008e\3\2\2\2\67\2\u0093\u0109\u0112\u0114\u0119\u0129\u0132\u0136\u013a"+
		"\u013c\u0142\u0149\u0150\u015d\u0164\u0168\u016a\u0174\u0178\u017c\u017f"+
		"\u0184\u0186\u018c\u0192\u0194\u0197\u019e\u01a2\u01a7\u01aa\u01b1\u01b4"+
		"\u01bb\u01bf\u01c4\u01c7\u01cc\u01e6\u01eb\u01ef\u01f5\u0205\u020d\u0210"+
		"\u0214\u021b\u0220\u0224\u0229\u022f\u0232\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}