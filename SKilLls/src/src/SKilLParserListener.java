// Generated from ..\src\SKilLParser.g4 by ANTLR 4.5.1
package grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SKilLParser}.
 */
public interface SKilLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SKilLParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(SKilLParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(SKilLParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#header}.
	 * @param ctx the parse tree
	 */
	void enterHeader(SKilLParser.HeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#header}.
	 * @param ctx the parse tree
	 */
	void exitHeader(SKilLParser.HeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#include}.
	 * @param ctx the parse tree
	 */
	void enterInclude(SKilLParser.IncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#include}.
	 * @param ctx the parse tree
	 */
	void exitInclude(SKilLParser.IncludeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(SKilLParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(SKilLParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#usertype}.
	 * @param ctx the parse tree
	 */
	void enterUsertype(SKilLParser.UsertypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#usertype}.
	 * @param ctx the parse tree
	 */
	void exitUsertype(SKilLParser.UsertypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#enumtype}.
	 * @param ctx the parse tree
	 */
	void enterEnumtype(SKilLParser.EnumtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#enumtype}.
	 * @param ctx the parse tree
	 */
	void exitEnumtype(SKilLParser.EnumtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#interfacetype}.
	 * @param ctx the parse tree
	 */
	void enterInterfacetype(SKilLParser.InterfacetypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#interfacetype}.
	 * @param ctx the parse tree
	 */
	void exitInterfacetype(SKilLParser.InterfacetypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#typedef}.
	 * @param ctx the parse tree
	 */
	void enterTypedef(SKilLParser.TypedefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#typedef}.
	 * @param ctx the parse tree
	 */
	void exitTypedef(SKilLParser.TypedefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(SKilLParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(SKilLParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterConstant(SKilLParser.ConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitConstant(SKilLParser.ConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#data}.
	 * @param ctx the parse tree
	 */
	void enterData(SKilLParser.DataContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#data}.
	 * @param ctx the parse tree
	 */
	void exitData(SKilLParser.DataContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(SKilLParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(SKilLParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#typemulti}.
	 * @param ctx the parse tree
	 */
	void enterTypemulti(SKilLParser.TypemultiContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#typemulti}.
	 * @param ctx the parse tree
	 */
	void exitTypemulti(SKilLParser.TypemultiContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#typesingle}.
	 * @param ctx the parse tree
	 */
	void enterTypesingle(SKilLParser.TypesingleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#typesingle}.
	 * @param ctx the parse tree
	 */
	void exitTypesingle(SKilLParser.TypesingleContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#arraytype}.
	 * @param ctx the parse tree
	 */
	void enterArraytype(SKilLParser.ArraytypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#arraytype}.
	 * @param ctx the parse tree
	 */
	void exitArraytype(SKilLParser.ArraytypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#groundtype}.
	 * @param ctx the parse tree
	 */
	void enterGroundtype(SKilLParser.GroundtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#groundtype}.
	 * @param ctx the parse tree
	 */
	void exitGroundtype(SKilLParser.GroundtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(SKilLParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(SKilLParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#restriction}.
	 * @param ctx the parse tree
	 */
	void enterRestriction(SKilLParser.RestrictionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#restriction}.
	 * @param ctx the parse tree
	 */
	void exitRestriction(SKilLParser.RestrictionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#rarg}.
	 * @param ctx the parse tree
	 */
	void enterRarg(SKilLParser.RargContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#rarg}.
	 * @param ctx the parse tree
	 */
	void exitRarg(SKilLParser.RargContext ctx);
	/**
	 * Enter a parse tree produced by {@link SKilLParser#hint}.
	 * @param ctx the parse tree
	 */
	void enterHint(SKilLParser.HintContext ctx);
	/**
	 * Exit a parse tree produced by {@link SKilLParser#hint}.
	 * @param ctx the parse tree
	 */
	void exitHint(SKilLParser.HintContext ctx);
}