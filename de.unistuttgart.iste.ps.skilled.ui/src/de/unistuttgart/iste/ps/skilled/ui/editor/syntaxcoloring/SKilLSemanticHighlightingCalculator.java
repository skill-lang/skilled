package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import de.unistuttgart.iste.ps.skilled.skill.Basetype;
import de.unistuttgart.iste.ps.skilled.skill.BuiltInType;
import de.unistuttgart.iste.ps.skilled.skill.Constant;
import de.unistuttgart.iste.ps.skilled.skill.Data;
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.skill.Enuminstance;
import de.unistuttgart.iste.ps.skilled.skill.Enumtype;
import de.unistuttgart.iste.ps.skilled.skill.FieldcontentReference;
import de.unistuttgart.iste.ps.skilled.skill.Hint;
import de.unistuttgart.iste.ps.skilled.skill.Interfacetype;
import de.unistuttgart.iste.ps.skilled.skill.Restriction;
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclarationReference;
import de.unistuttgart.iste.ps.skilled.skill.Typedef;
import de.unistuttgart.iste.ps.skilled.skill.Usertype;
import de.unistuttgart.iste.ps.skilled.skill.View;


/**
 * Custom Highlighting Calculator for SKilL. It is needed for custom syntax coloring. Semantic Highlighting
 * 
 * @author Marco Link
 */
public class SKilLSemanticHighlightingCalculator implements ISemanticHighlightingCalculator {

    /**
     * Checks the type of the nodes in the syntax tree and hands over the position of the individually node with its text
     * style.
     * 
     * @param resource
     *            the resource that will be highlighted. May be <code>null</code> in some rare cases.
     * @param acceptor
     *            used to announce the mapping from text-range to the style's id. The acceptor will never be
     *            <code>null</code>.
     */
    @Override
    public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {

        if (resource == null || resource.getParseResult() == null)
            return;

        INode root = resource.getParseResult().getRootNode();
        for (INode node : root.getAsTreeIterable()) {
            EObject grammarElement = node.getGrammarElement();
            EObject semanticElement = node.getSemanticElement();

            // Colors the cross references
            if ((grammarElement instanceof CrossReference)) {

                // Checks for base type cross references and for restriction
                // arguments cross references.
                if (semanticElement instanceof DeclarationReference) {
                    DeclarationReference dr = (DeclarationReference) semanticElement;

                    // Highlighting for usertype cross references
                    if (dr.getType() instanceof Usertype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.USERTYPE_ID);
                    }

                    // Highlighting for interface cross references
                    else if (dr.getType() instanceof Interfacetype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(),
                                SKilLHighlightingConfiguration.INTERFACE_ID);
                    }

                    // Highlighting for typedefs cross references
                    else if (dr.getType() instanceof Typedef) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.TYPEDEF_ID);
                    }

                    // Highlighting for enums cross references
                    else if (dr.getType() instanceof Enumtype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.ENUM_ID);
                    }

                }

                // Checks for supertype cross references.
                else if (semanticElement instanceof TypeDeclarationReference) {
                    TypeDeclarationReference tdr = (TypeDeclarationReference) node.getSemanticElement();

                    // Highlighting for usertype cross references
                    if (tdr.getType() instanceof Usertype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.USERTYPE_ID);
                    }

                    // Highlighting for interface cross references
                    else if (tdr.getType() instanceof Interfacetype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(),
                                SKilLHighlightingConfiguration.INTERFACE_ID);
                    }
                }

                // Checks for fieldcontent cross references in views.
                else if (semanticElement instanceof FieldcontentReference) {
                    FieldcontentReference fr = (FieldcontentReference) semanticElement;

                    if (fr.getFieldcontent() instanceof Data) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.DATA_ID);
                    } else if (fr.getFieldcontent() instanceof View) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.VIEW_ID);
                    } else if (fr.getFieldcontent() instanceof Constant) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.CONSTANT_ID);
                    }

                }

            }

            // Highlighting for the build in types.
            else if (semanticElement instanceof BuiltInType) {
                acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.BUILDINTYPE_ID);
            }

            // Checks for restrictions. Only the keyword and the name should be
            // colored as restriction, but not its
            // arguments.
            else if (semanticElement instanceof Restriction) {
                for (INode resNode : node.getAsTreeIterable()) {
                    // Highlighting for the keyword '@'
                    if (resNode.getText().equals("@")) {
                        acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
                                SKilLHighlightingConfiguration.RESTRICTION_ID);
                    }
                    // Highlighting for the restriction name.
                    else if (resNode.getGrammarElement() instanceof RuleCall) {
                        RuleCall rc = (RuleCall) resNode.getGrammarElement();
                        AbstractRule r = rc.getRule();
                        EObject c = resNode.getGrammarElement().eContainer();
                        if ((r.getName().equals("ID")) && (c instanceof Assignment)
                                && ((Assignment) c).getFeature().equals("restrictionName")) {
                            acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
                                    SKilLHighlightingConfiguration.RESTRICTION_ID);
                        }
                    }
                }
            }

            // Checks for hints. Only the keyword and the name should be colored
            // as hint, but not its arguments.
            else if (semanticElement instanceof Hint) {
                for (INode resNode : node.getAsTreeIterable()) {

                    // Highlighting for the keyword '!'
                    if (resNode.getText().equals("!")) {
                        acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
                                SKilLHighlightingConfiguration.HINT_ID);
                    }

                    // Highlighting for the keyword 'pragma'
                    else if (resNode.getText().equals("pragma")) {
                        acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
                                SKilLHighlightingConfiguration.HINT_ID);
                    }

                    // Highlighting for the hint name
                    else if (resNode.getGrammarElement() instanceof RuleCall) {
                        RuleCall rc = (RuleCall) resNode.getGrammarElement();
                        AbstractRule r = rc.getRule();
                        EObject c = resNode.getGrammarElement().eContainer();
                        if ((r.getName().equals("ID")) && (c instanceof Assignment)
                                && ((Assignment) c).getFeature().equals("hintName")) {
                            acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
                                    SKilLHighlightingConfiguration.HINT_ID);
                        }
                    }
                }
            }

            // Coloring for individually names.
            else if (grammarElement instanceof RuleCall) {
                RuleCall rc = (RuleCall) grammarElement;
                AbstractRule r = rc.getRule();
                EObject c = grammarElement.eContainer();

                // Checks whether the given element is a name.
                if ((c instanceof Assignment) && ((Assignment) c).getFeature().equals("name")) {

                    if (r.getName().equals("ID")) {

                        // Coloring for usertypes.
                        if (node.getSemanticElement() instanceof Usertype) {
                            acceptor.addPosition(node.getOffset(), node.getLength(),
                                    SKilLHighlightingConfiguration.USERTYPE_ID);

                        }

                        // Coloring for interfaces.
                        else if (node.getSemanticElement() instanceof Interfacetype) {
                            acceptor.addPosition(node.getOffset(), node.getLength(),
                                    SKilLHighlightingConfiguration.INTERFACE_ID);

                        }

                        // Coloring for enums.
                        else if (node.getSemanticElement() instanceof Enumtype) {
                            acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.ENUM_ID);
                        }

                        // Coloring for typedefs.
                        else if (node.getSemanticElement() instanceof Typedef) {
                            acceptor.addPosition(node.getOffset(), node.getLength(),
                                    SKilLHighlightingConfiguration.TYPEDEF_ID);
                        }
                    }

                    if ((r.getName().equals("XID"))) {

                        // Coloring for data fields.
                        if (node.getSemanticElement() instanceof Data) {
                            Data d = (Data) node.getSemanticElement();

                            // Coloring for basetypes.
                            if (d.getFieldtype() instanceof Basetype) {
                                acceptor.addPosition(node.getOffset(), node.getLength(),
                                        SKilLHighlightingConfiguration.DATA_ID);
                            }

                            // Coloring for compound types.
                            else {
                                acceptor.addPosition(node.getOffset(), node.getLength(),
                                        SKilLHighlightingConfiguration.COMPOUND_ID);
                            }
                        }

                        // Coloring for constant fields.
                        else if (node.getSemanticElement() instanceof Constant) {
                            acceptor.addPosition(node.getOffset(), node.getLength(),
                                    SKilLHighlightingConfiguration.CONSTANT_ID);
                        }

                        // Coloring for view fields.
                        else if (node.getSemanticElement() instanceof View) {
                            acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.VIEW_ID);
                        }

                        // Coloring for the instances of an Enum, not the enum
                        // name.
                        // enum Tag { --> Montag, Dienstag; <-- }
                        else if (node.getSemanticElement() instanceof Enuminstance) {
                            acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.ENUM_ID);
                        }
                    }
                }
            }
        }
    }
}
