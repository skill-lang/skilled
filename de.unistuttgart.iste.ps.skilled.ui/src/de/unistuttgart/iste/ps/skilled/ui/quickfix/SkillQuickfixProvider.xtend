/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.ui.quickfix

import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Fieldcontent
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.skill.Include
import de.unistuttgart.iste.ps.skilled.skill.IncludeFile
import de.unistuttgart.iste.ps.skilled.skill.SkillFactory
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.util.DependencyGraph.DependencyGraph
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import de.unistuttgart.iste.ps.skilled.validation.InheritenceValidator
import de.unistuttgart.iste.ps.skilled.validation.ScopingValidator
import de.unistuttgart.iste.ps.skilled.validation.UnusedDeclarationValidator
import java.util.ArrayList
import java.util.Iterator
import java.util.LinkedList
import java.util.List
import java.util.Set
import org.apache.log4j.Logger
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jface.text.BadLocationException
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Text
import org.eclipse.xtext.AbstractElement
import org.eclipse.xtext.CrossReference
import org.eclipse.xtext.EcoreUtil2
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
import org.eclipse.xtext.ui.editor.model.edit.IModification
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
import de.unistuttgart.iste.ps.skilled.formatting2.SkillImportOrganizer

/**
 * Custom quickfixes.
 * 
 * @author Jan Berberich
 * @author Marco Link
 * @author Tobias Heck
 * 
 */
public class SkillQuickfixProvider extends DefaultQuickfixProvider {

	@Inject private SKilLServices services

	@Fix("Imported resource could not be found.")
	def fixImport(Issue issue, IssueResolutionAcceptor acceptor) {
		var URI uri = URI.createURI(issue.data.head)
		uri = URI.createURI(uri.toString.replace("../", ""))
		val IWorkspaceRoot root = ResourcesPlugin.getWorkspace.getRoot
		val URI path = URI.createURI(root.locationURI.rawPath).appendSegments(uri.segmentsList)
		val java.io.File asdf = (new Path(path.toString)).toFile.absoluteFile.parentFile
		val java.io.File[] files = asdf.listFiles
		if (files == null) {
			return
		}
		val LinkedList<String> fileNames = new LinkedList
		for (file : files) {
			val distance = getLevenshteinDistance(file.absolutePath.replaceAll("/", "\\\\"),
				path.toString.substring(1).replaceAll("/", "\\\\"))
			if (3 > distance && 0 < distance && file.absolutePath.matches(".*\\.skill")) {
				fileNames.add(file.name)
			}
		}
		for (name : fileNames) {
			acceptor.accept(issue, "Change to " + name, "Change to " + name, "upcase.png",
				new ISemanticModification {
					override apply(EObject element, IModificationContext context) {
						var IncludeFile include = null
						if (element instanceof IncludeFile) {
							include = element
							include.importURI = include.importURI.replace(URI.createURI(include.importURI).lastSegment,
								name)
						}
					}
				})
		}
	}

	@Inject
	private ICaseInsensitivityHelper caseInsensitivityHelper;

	private static final Logger logger = Logger.getLogger(DefaultQuickfixProvider.getClass)

	@Inject
	private CrossRefResolutionConverter converter

	def private int getLevenshteinDistance(String s0, String s1) {
		val m = s0.length
		val n = s1.length
		var LinkedList<LinkedList<Integer>> d = new LinkedList
		for (var i = 0; i <= m; i++) {
			var LinkedList<Integer> temp = new LinkedList
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
					d.get(i).set(j, d.get(i - 1).get(j - 1))
				} else {
					d.get(i).set(j,
						Integer.min(d.get(i - 1).get(j), Integer.min(d.get(i).get(j - 1), d.get(i - 1).get(j - 1))) + 1)
				}
			}
		}
		return d.get(m).get(n)
	}

	def private boolean isSimilar(String s0, String s1) {
		if (Strings.isEmpty(s0) || Strings.isEmpty(s1)) {
			return false
		}
		return getLevenshteinDistance(s0, s1) <= 2
	}

	def private CrossReference findCrossReference(EObject context, INode node) {
		if (node == null || (node.hasDirectSemanticElement && context.equals(node.getSemanticElement))) {
			return null
		}
		var EObject grammarElement = node.getGrammarElement
		if (grammarElement instanceof CrossReference) {
			return grammarElement
		} else
			return findCrossReference(context, node.getParent)
	}

	@Override
	override public void createLinkingIssueResolutions(Issue issue, IssueResolutionAcceptor issueResolutionAcceptor) {
		val IModificationContext modificationContext = modificationContextFactory.createModificationContext(issue)
		val IXtextDocument xtextDocument = modificationContext.getXtextDocument
		if (xtextDocument == null) {
			return
		}
		xtextDocument.readOnly(
			new CancelableUnitOfWork<String, XtextResource> {

				IssueResolutionAcceptor myAcceptor = null

				@Override
				override public String exec(XtextResource state, CancelIndicator cancelIndicator) throws Exception {
					myAcceptor = getCancelableAcceptor(issueResolutionAcceptor, cancelIndicator)
					val EObject target = state.getEObject(issue.getUriToProblem.fragment)
					val EReference reference = getUnresolvedEReference(issue, target)
					if (reference == null) {
						return null
					}
					fixUnresolvedReference(issue, xtextDocument, target, reference)
					return null
				}

				def protected void fixUnresolvedReference(Issue issue, IXtextDocument xtextDocument, EObject target,
					EReference reference) throws BadLocationException {
					val boolean caseInsensitive = caseInsensitivityHelper.isIgnoreCase(reference)
					var EObject crossReferenceTerminal = getCrossReference(issue, target)
					var String ruleName = null
					var Keyword keyword = null
					if (crossReferenceTerminal instanceof RuleCall) {
						ruleName = crossReferenceTerminal.getRule.getName
					} else if (crossReferenceTerminal instanceof Keyword) {
						keyword = crossReferenceTerminal
					}
					var String issueString = xtextDocument.get(issue.getOffset, issue.getLength)
					var IScope scope = scopeProvider.getScope(target, reference)
					val List<IEObjectDescription> discardedDescriptions = Lists.newArrayList
					val Set<String> qualifiedNames = Sets.newHashSet
					var int addedDescriptions = 0
					var int checkedDescriptions = 0
					for (referableElement : queryScope(scope)) {
						if (checkedDescriptions <= 100) {
							var String referableElementQualifiedName = qualifiedNameConverter.toString(
								referableElement.getQualifiedName)
							if (isSimilar(issueString, qualifiedNameConverter.toString(referableElement.getName))) {
								addedDescriptions++
								createResolution(issueString, referableElement, ruleName, keyword, caseInsensitive)
								qualifiedNames.add(referableElementQualifiedName)
							} else {
								if (qualifiedNames.add(referableElementQualifiedName))
									discardedDescriptions.add(referableElement)
							}
							checkedDescriptions++
						}
					}
					if (discardedDescriptions.size + addedDescriptions <= 5) {
						for (referableElement : discardedDescriptions) {
							createResolution(issueString, referableElement, ruleName, keyword, caseInsensitive)
						}
					}
				}

				def protected AbstractElement getCrossReference(Issue issue, EObject target) {
					val ICompositeNode node = NodeModelUtils.getNode(target)
					if (node == null) {
						throw new IllegalStateException("Cannot happen since we found a reference")
					}
					var ICompositeNode rootNode = node.getRootNode
					var ILeafNode leaf = NodeModelUtils.findLeafNodeAtOffset(rootNode, issue.getOffset)
					var CrossReference crossReference = findCrossReference(target, leaf)
					return crossReference.getTerminal
				}

				def public void createResolution(String issueString, IEObjectDescription solution, String ruleName,
					Keyword keyword, boolean caseInsensitive) {
					var String replacement = qualifiedNameConverter.toString(solution.getName)
					var String replaceLabel = fixCrossReferenceLabel(issueString, replacement)
					if (keyword != null) {
						if (caseInsensitive && !replacement.equalsIgnoreCase(keyword.getValue)) {
							return
						}
						if (!caseInsensitive && !replacement.equals(keyword.getValue)) {
							return
						}
					} else if (ruleName != null) {
						replacement = converter.convertToString(replacement, ruleName)
						if (replacement == null) {
							return
						}
					} else {
						logger.error("either keyword or ruleName have to present", new IllegalStateException)
					}
					myAcceptor.accept(issue, replaceLabel, replaceLabel,
						fixCrossReferenceImage(issueString, replacement), new ReplaceModification(issue, replacement))
				}

			});
	}

	/** 
	 * Quickfix to change name if the Name has non-ASCII Characters
	 */
	@Fix("declarationNonASCII")
	def fixDeclarationName(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Change name", "Change the name of the Type.", "upcase.png", new ISemanticModification {
			override void apply(EObject element, IModificationContext context) {
				var TypeDeclaration td = element as TypeDeclaration
				// Create xtend class with a method to change the name to a new one
				var SetName name = new SetName(issue, acceptor)
				// Open name-change Window that will allow the User to enter a new name
				var ChangeNameField f = new ChangeNameField(name)
				f.oldName = td.name
				f.open
			}
		})
	}

	var String newName;

	def public String getNewName(String oldName) {
		var Display display
		var Shell shell = new Shell(display)
		shell.setText("Extract Type or Interface")
		shell.setLayout(new GridLayout(2, true))
		val Text text1 = new Text(shell, SWT.NONE)
		text1.text = oldName
		var GridData gridData = new GridData
		gridData.horizontalAlignment = SWT.CENTER
		gridData.horizontalSpan = 2
		text1.layoutData = gridData
		var GridData gridDataButton = new GridData
		gridDataButton.horizontalAlignment = SWT.CENTER
		gridDataButton.horizontalSpan = 2
		var Button continueButton = new Button(shell, SWT.NONE)
		continueButton.setText("OK")
		continueButton.setLayoutData(gridDataButton)
		continueButton.addListener(SWT.Selection, new Listener {
			override public void handleEvent(Event e) {
				if (e.type == SWT.Selection) {
					newName = text1.text
				}
			}
		})
		val Point newSize = shell.computeSize(150, 150, true)
		shell.setSize(newSize)
		shell.open()
		while (newName.equals("")) {
			Thread.sleep(5)
		}
		shell.close
		return newName

	}

	@Fix("fieldNonASCII")
	def fixFieldName(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(issue, "Change name", "Change the name of the Field.", "upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					var Fieldcontent field = element as Fieldcontent
					// Create xtend class with a method to change the name to a new one
					var SetName name = new SetName(issue, acceptor)
					// Open name-change Window that will allow the User to enter a new name
					var ChangeNameField f = new ChangeNameField(name)
					f.oldName = field.name
					f.open()
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
					var List<TypeDeclarationReference> remove = new ArrayList
					var Supertypes = e.supertypes
					// Remove Reference
					for (typeDeclarationReference : Supertypes) {
						if (typeDeclarationReference.type.equals(e)) {
							remove.add(typeDeclarationReference)
						}
					}
					Supertypes.removeAll(remove)
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
					var ArrayList<TypeDeclarationReference> remove = new ArrayList
					var Supertypes = e.supertypes
					var names = issue.data // Array with the Names of all Types of the Cyclic Component
					// Search all References that are in the Cyclic Component
					for (typeDeclarationReference : Supertypes) {
						for (name : names) {
							if (typeDeclarationReference.type.name.equals(name)) {
								remove.add(typeDeclarationReference)
							}
						}
					}
					// Remove the references that were found
					for (typeDeclarationReference : remove) {
						e.supertypes.remove(typeDeclarationReference)
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
					var file = element.eContainer
					while (file.eContainer != null) {
						file = file.eContainer
					}

					var Declaration referencedType

					if (element instanceof TypeDeclarationReference) {
						referencedType = element.type
					} else if (element instanceof DeclarationReference) {
						referencedType = element.type
					}

					// The URI which will be added.
					var URI referencedURI = referencedType?.eResource?.URI.deresolve(file.eResource.URI)

					if (referencedURI != null) {
						// Create new include.
						var Include include = SkillFactory.eINSTANCE.createInclude;
						var IncludeFile includeFile = SkillFactory.eINSTANCE.createIncludeFile
						includeFile.importURI = referencedURI?.path;
						include.includeFiles.add(includeFile);
						if (file instanceof File) {
							file.includes.add(include)
						}
					}
				}
			}
		)
	}

	@Fix(ScopingValidator::UNUSED_INCLUDED_FILE)
	def fixMissingFileInclude2(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Organize imports",
			"Organize imports",
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					organizeImports(element)
				}
			}
		)
	}

	@Fix(ScopingValidator::NOT_INCLUDED_FILE)
	def fixMissingFileInclude4(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Organize imports",
			"Organize imports",
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					organizeImports(element)
				}
			}
		)
	}

	@Fix(ScopingValidator::DUPLICATED_INCLUDED_FILE)
	def fixMissingFileInclude3(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Organize imports",
			"Organize imports",
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					organizeImports(element)
				}
			}
		)
	}

	@Fix(ScopingValidator::NOT_INCLUDED_FILE)
	def fixMissingFileWithOrganizeImportsProjectWide(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Organize imports project-wide",
			"Organize imports project-wide",
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					organizeImportsProjectWide(element)
				}
			}
		)
	}

	@Fix(ScopingValidator::DUPLICATED_INCLUDED_FILE)
	def fixDuplicatedIncludeWithOrganizeImportsProjectWide(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Organize imports project-wide",
			"Organize imports project-wide",
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					organizeImportsProjectWide(element)
				}
			}
		);
	}

	@Fix(ScopingValidator::UNUSED_INCLUDED_FILE)
	def fixUnusedIncludeWithOrganizeImportsProjectWide(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Organize imports project-wide",
			"Organize imports project-wide",
			"upcase.png",
			new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					organizeImportsProjectWide(element)
				}
			}
		)
	}

	/**
	 * Organizes the import of a whole project, for this there will be on main file which includes all includes 
	 * and every other skill file will include the main file.
	 * @param element The project in which this element is located will be organized.
	 */
	def organizeImportsProjectWide(EObject element) {
		var dependencyGraph = new DependencyGraph

		// The file in which the element is located.
		var file = EcoreUtil2.getRootContainer(element, true)

		if (!(file instanceof File) || services.isToolFile(EcoreUtil2.getURI(file))) {
			return
		}

		dependencyGraph.generateIgnoreOrigin((file as File), services.getAll(file as File, true))

		val org = new SkillOrganizeImportsHandler()
		org.organizeImportsWholeProject(file as File)
	}

	/**
	 * Organizes the imports of on skill file.
	 * @param element The file in which this element is located will be organized.
	 */
	def organizeImports(EObject element) {
		if (services == null) {
			services = new SKilLServices
		}
		var dependencyGraph = new DependencyGraph()

		// The file in which the element is located.
		var file = EcoreUtil2.getRootContainer(element, true)
		if (!(file instanceof File) || services.isToolFile(EcoreUtil2.getURI(file))) {
			return;
		}

		dependencyGraph.generateIgnoreOrigin((file as File), services.getAll(file as File, true))
		val org = new SkillImportOrganizer(dependencyGraph)
		val missing = dependencyGraph.getMissingIncludes((file as File).eResource)

		for (uri : missing) {
			val URI referencedURI = uri.deresolve(file.eResource.URI)
			if (referencedURI != null) {
				// Create new include.
				var Include include = SkillFactory.eINSTANCE.createInclude
				var IncludeFile includeFile = SkillFactory.eINSTANCE.createIncludeFile
				includeFile.importURI = referencedURI?.path
				include.includeFiles.add(includeFile)
				if (file instanceof File) {
					file.includes.add(include)
				}
				(file as File).includes.add(include)
			}
		}

		var unusedImports = org.getUnusedImports(file as File)
		unusedImports.addAll(de.unistuttgart.iste.ps.skilled.formatting2.SkillImportOrganizer.getDuplicateIncludes(file as File))

		for (delete : unusedImports) {
			val Iterator<Include> includeIterator = (file as File).includes.iterator;
			while (includeIterator.hasNext) {
				val include = includeIterator.next
				val Iterator<IncludeFile> includeFileIterator = include.includeFiles.iterator
				while (includeFileIterator.hasNext) {
					if (includeFileIterator.next.equals(delete)) {
						if (include.includeFiles.size > 1) {
							includeFileIterator.remove
						} else {
							includeIterator.remove
						}
					}
				}
			}
		}
	}

	@Fix(UnusedDeclarationValidator::UNUSED_TYPE)
	def removeUnusedType(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Remove unused Type",
			"Removes the unused Type.",
			"upcase.png",
			new IModification() {
				override void apply(IModificationContext context) throws BadLocationException {
					var IXtextDocument xtextDocument = context.getXtextDocument();
					if (issue.data.size >= 2) {
						xtextDocument.replace(Integer.parseInt(issue.data.get(0)), Integer.parseInt(issue.data.get(1)),
							"");
					}
				}
			}
		);
	}

	@Fix(UnusedDeclarationValidator::UNUSED_FIELD)
	def removeUnusedField(Issue issue, IssueResolutionAcceptor acceptor) {
		acceptor.accept(
			issue,
			"Remove unused Field",
			"Removes the unused Field.",
			"upcase.png",
			new IModification() {
				override void apply(IModificationContext context) throws BadLocationException {
					var IXtextDocument xtextDocument = context.getXtextDocument();
					if (issue.data.size >= 2) {
						xtextDocument.replace(Integer.parseInt(issue.data.get(0)), Integer.parseInt(issue.data.get(1)),
							"");
					}
				}
			}
		);
	}
}
