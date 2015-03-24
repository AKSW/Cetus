// Generated from CetusPatterns.g4 by ANTLR 4.5
package org.aksw.simba.cetus.parser.antlr4;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CetusPatternsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CetusPatternsVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#sentence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentence(CetusPatternsParser.SentenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#type_after_entity_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_after_entity_pattern(CetusPatternsParser.Type_after_entity_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#direct_following_type_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirect_following_type_pattern(CetusPatternsParser.Direct_following_type_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#is_a_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIs_a_pattern(CetusPatternsParser.Is_a_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#is_a_type_of_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIs_a_type_of_pattern(CetusPatternsParser.Is_a_type_of_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#type_in_front_of_entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_in_front_of_entity(CetusPatternsParser.Type_in_front_of_entityContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#type_with_dt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_with_dt(CetusPatternsParser.Type_with_dtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(CetusPatternsParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CetusPatternsParser#nr_or_crd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNr_or_crd(CetusPatternsParser.Nr_or_crdContext ctx);
}