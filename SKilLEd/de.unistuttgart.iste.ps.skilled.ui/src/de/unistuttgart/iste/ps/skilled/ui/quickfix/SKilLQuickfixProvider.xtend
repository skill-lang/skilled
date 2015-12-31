/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.ui.quickfix

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.Include
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLFactory
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.validation.InheritenceValidator
import de.unistuttgart.iste.ps.skilled.validation.ScopingValidator
import java.util.ArrayList
import java.util.LinkedList
import java.util.List
import java.util.Set
import org.apache.log4j.Logger
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jface.text.BadLocationException
import org.eclipse.xtext.AbstractElement
import org.eclipse.xtext.CrossReference
import org.eclipse.xtext.Keyword
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.nodemodel.ICompositeNode
import org.eclipse.xtext.nodemodel.ILeafNode
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.scoping.ICaseInsensitivityHelper
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider
import org.eclipse.xtext.ui.editor.quickfix.Fix
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.ui.editor.quickfix.ReplaceModification
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.util.Strings
import org.eclipse.xtext.util.concurrent.CancelableUnitOfWork
import org.eclipse.xtext.validation.Issue

import static de.unistuttgart.iste.ps.skilled.ui.quickfix.SKilLQuickfixProvider.*

/**
 * Custom quickfixes.
 * 
 * @author Jan Berberich
 * @author Marco Link
 * @author Tobias Heck
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#quick-fixes
 */
public class SKilLQuickfixProvider extends DefaultQuickfixProvider {

	@Inject
	private ICaseInsensitivityHelper caseInsensitivityHelper;

	private static final Logger logger = Logger.getLogger(DefaultQuickfixProvider.getClass());

	@Inject
	private CrossRefResolutionConverter converter;
	
	def private int getLevenshteinDistance(String s0, String s1) {
		val m = s0.length
		val n = s1.length
		var LinkedList<LinkedList<Integer>> d = new LinkedList()
		for (var i = 0; i <= m; i++) {
			var LinkedList<Integer> temp = new LinkedList()
			for (var j = 0; j <= n; j++) {
				temp.add(0)
			}
			d.add(temp)
		}
		for (var i = 1; i <= m; i++) {
			d.get(i).set(0, i)
		}
		for (var j = 1; j <= n; j++) {
			d.get(0).set(j, j)
		}
		for (var j = 1; j <= n; j++) {
			for (var i = 1; i <= m; i++) {
				if (s0.charAt(i - 1) == s1.charAt(j - 1)) {
					d.get(i).set(j, d.get(i-1).get(j-1))
				} else {
					d.get(i).set(j, Integer.min(d.get(i-1).get(j), Integer.min(d.get(i).get(j-1), d.get(i-1).get(j-1))) + 1)
				}
			}
		}
		return d.get(m).get(n)
	}
	
	def private boolean isSimilar(String s0, String s1) {
		if(Strings.isEmpty(s0) || Strings.isEmpty(s1)) {
			return false;
		}
		var double levenshteinDistance = getLevenshteinDistance(s0, s1);
		return levenshteinDistance <= 2;
	}

	def private CrossReference findCrossReference(EObject context, INode node) {
		if (node == null || (node.hasDirectSemanticElement() && context.equals(node.getSemanticElement())))
			return null;

		var EObject grammarElement = node.getGrammarElement();
		if (grammarElement instanceof CrossReference) {
			return grammarElement as CrossReference;
		} else
			return findCrossReference(context, node.getParent());
	}

	@Override
	override public void createLinkingIssueResolutions(Issue issue, IssueResolutionAcceptor issueResolutionAcceptor) {
		val IModificationContext modificationContext = modificationContextFactory.createModificationContext(issue);
		val IXtextDocument xtextDocument = modificationContext.getXtextDocument();
		if (xtextDocument == null)
			return;
		xtextDocument.readOnly(
			new CancelableUnitOfWork<String, XtextResource>() {

				IssueResolutionAcceptor myAcceptor = null;

				@Override
				override public String exec(XtextResource state,
					CancelIndicator cancelIndicator) throws Exception {
					myAcceptor = getCancelableAcceptor(issueResolutionAcceptor, cancelIndicator);
					val EObject target = state.getEObject(issue.getUriToProblem().fragment());
					val EReference reference = getUnresolvedEReference(issue, target);
					if (reference == null)
						return null;
					fixUnresolvedReference(issue, xtextDocument, target, reference);
					return null;
				}

				def protected void fixUnresolvedReference(Issue issue, IXtextDocument xtextDocument, EObject target,
					EReference reference) throws BadLocationException {
					val boolean caseInsensitive = caseInsensitivityHelper.isIgnoreCase(reference);
					var EObject crossReferenceTerminal = getCrossReference(issue, target);
					var String ruleName = null;
					var Keyword keyword = null;
					if (crossReferenceTerminal instanceof RuleCall) {
						var RuleCall ruleCall = crossReferenceTerminal as RuleCall;
						ruleName = ruleCall.getRule().getName();
					} else if (crossReferenceTerminal instanceof Keyword) {
						keyword = crossReferenceTerminal as Keyword;
					}
					var String issueString = xtextDocument.get(issue.getOffset(), issue.getLength());
					var IScope scope = scopeProvider.getScope(target, reference);
					var List<IEObjectDescription> discardedDescriptions = Lists.newArrayList();
					var Set<String> qualifiedNames = Sets.newHashSet();
					var int addedDescriptions = 0;
					var int checkedDescriptions = 0;
					for (IEObjectDescription referableElement : queryScope(scope)) {
						if (checkedDescriptions <= 100) {
							var String referableElementQualifiedName = qualifiedNameConverter.toString(
								referableElement.getQualifiedName());
							if (isSimilar(issueString,
								qualifiedNameConverter.toString(referableElement.getName()))) {
								addedDescriptions++;
								createResolution(issueString, referableElement, ruleName, keyword, caseInsensitive);
								qualifiedNames.add(referableElementQualifiedName);
							} else {
								if (qualifiedNames.add(referableElementQualifiedName))
									discardedDescriptions.add(referableElement);
							}
							checkedDescriptions++;
						}
					}
					if (discardedDescriptions.size() + addedDescriptions <= 5) {
						for (IEObjectDescription referableElement : discardedDescriptions) {
							createResolution(issueString, referableElement, ruleName, keyword, caseInsensitive);
						}
					}
				}

				def protected AbstractElement getCrossReference(Issue issue, EObject target) {
					val ICompositeNode node = NodeModelUtils.getNode(target);
					if (node == null)
						throw new IllegalStateException("Cannot happen since we found a reference");
					var ICompositeNode rootNode = node.getRootNode();
					var ILeafNode leaf = NodeModelUtils.findLeafNodeAtOffset(rootNode, issue.getOffset());
					var CrossReference crossReference = findCrossReference(target, leaf);
					return crossReference.getTerminal();
				}

				def public void createResolution(String issueString, IEObjectDescription solution, String ruleName,
					Keyword keyword, boolean caseInsensitive) {
					var String replacement = qualifiedNameConverter.toString(solution.getName());
					var String replaceLabel = fixCrossReferenceLabel(issueString, replacement);
					if (keyword != null) {
						if (caseInsensitive && !replacement.equalsIgnoreCase(keyword.getValue()))
							return;
						if (!caseInsensitive && !replacement.equals(keyword.getValue()))
							return;
					} else if (ruleName != null) {
						replacement = converter.convertToString(replacement, ruleName);
						if (replacement == null) {
							return;
						}
					} else {
						logger.error("either keyword or ruleName have to present", new IllegalStateException());
					}
					myAcceptor.accept(issue, replaceLabel, replaceLabel,
						fixCrossReferenceImage(issueString, replacement), new ReplaceModification(issue, replacement));
				}

			});
	}

	// Quickfix to remove the Parent that is the Type
	@Fix(InheritenceValidator::TYPE_IS_HIS_OWN_PARENT)
	def fixSupertype(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Remove Type", "Removes the supertype " + issue.data.get(0) + ".", "upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					var e = element as TypeDeclaration
					var TypeDeclarationReference remove
					var Supertypes = e.supertypes
					// Remove Reference
					for (TypeDeclarationReference tdr : Supertypes) {
						if (tdr.type.equals(e)) {
							remove = tdr
						}
					}
					Supertypes.remove(remove)
				}
			})
	}

	// Quickfix to remove Cyclic Types
	@Fix(InheritenceValidator::CYCLIC_TYPES)
	def fixCyclicType(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Remove Type", "Removes the supertype " + issue.data.get(0) + ".", "upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					var e = element as TypeDeclaration
					var ArrayList<TypeDeclarationReference> remove = new ArrayList<TypeDeclarationReference>
					var Supertypes = e.supertypes
					var names = issue.data // Array with the Names of all Types of the Cyclic Component
					// Search all References that are in the Cyclic Component
					for (TypeDeclarationReference tdr : Supertypes) {
						for (String name : names) {
							if (tdr.type.name.equals(name)) {
								remove.add(tdr)
							}
						}
					}
					// Remove the references that were found
					for (TypeDeclarationReference tdr : remove) {
						e.supertypes.remove(tdr)
					}
				}
			})
	}

	/**
	 * Quickfix to add the missing included file.
	 */
	@Fix(ScopingValidator::NOT_INCLUDED_FILE)
	def fixMissingFileInclude(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Add missing File",
			"Adds the missing file: " + issue.data.get(0),
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					// The file in which the element is located.
					var file = element.eContainer;
					while (file.eContainer != null) {
						file = file.eContainer;
					}

					var Declaration referencedType;

					if (element instanceof TypeDeclarationReference) {
						referencedType = element.type;
					} else if (element instanceof DeclarationReference) {
						referencedType = element.type;
					}

					// The URI which will be added.
					var URI referencedURI = referencedType?.eResource?.URI.deresolve(file.eResource.URI);

					if (referencedURI != null) {
						// Create new include.
						var Include include = SKilLFactory.eINSTANCE.createInclude;
						var IncludeFile includeFile = SKilLFactory.eINSTANCE.createIncludeFile;
						includeFile.importURI = referencedURI?.path;
						include.includeFiles.add(includeFile);
						if (file instanceof File) {
							file.includes.add(include);
						}
					}
				}
			}
		);
	}
}
