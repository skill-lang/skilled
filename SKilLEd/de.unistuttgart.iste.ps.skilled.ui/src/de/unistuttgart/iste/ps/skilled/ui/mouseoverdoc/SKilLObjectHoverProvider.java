package de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc;

import java.lang.reflect.Field;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.hover.html.IXtextBrowserInformationControl;
import org.eclipse.xtext.ui.editor.hover.html.XtextBrowserInformationControl;

import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;

/**
 * This Class defines the HoverProvider for mouseover documentation in SKilL and
 * creates the title string for the documentation
 * 
 * @author Jan Berberich
 * @author Tobias Heck
 */
public class SKilLObjectHoverProvider extends DefaultEObjectHoverProvider {

    private IInformationControlCreator presenterControlCreator;
    private IInformationControlCreator hoverControlCreator;

    @Override
    protected String getFirstLine(EObject o) {
        String comment = "";
        if (o instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) o;
            if (o instanceof Usertype) {
                comment = comment + "Type ";
            }
            if (o instanceof Interfacetype) {
                comment = comment + "Interface ";
            }
            comment = comment + td.getName();
        } else if (o instanceof Typedef) {
            Typedef td = (Typedef) o;
            comment = comment + "Typedef: " + td.getName();
        } else if (o instanceof Enumtype) {
            Enumtype en = (Enumtype) o;
            comment = comment + "Enum: " + en.getName();
            return comment;
        }
        return comment;
    }

    @Override
    public IInformationControlCreator getInformationPresenterControlCreator() {
        if (presenterControlCreator == null)
            presenterControlCreator = new PresenterControlCreator() {
                @Override
                public IInformationControl doCreateInformationControl(Shell parent) {
                    if (BrowserInformationControl.isAvailable(parent)) {
                        ToolBarManager tbm = new ToolBarManager(SWT.FLAT);
                        String font = "org.eclipse.jdt.ui.javadocfont";
                        IXtextBrowserInformationControl control = new XtextBrowserInformationControl(parent, font, tbm) {
                            private long changeSize = 0;
                            private long time = 0;

                            @Override
                            public void setSize(int width, int height) {
                                if (changeSize == 0 || (changeSize > 1 && (time + 100 > System.currentTimeMillis()))) {
                                    super.setSize(width, height);
                                }
                                changeSize++;
                                time = System.currentTimeMillis();
                            }
                        };
                        configureControl(control, tbm, font);
                        control.setSize(350, 250);
                        return control;
                    } else {
                        return new DefaultInformationControl(parent, true);
                    }
                }
            };
        return presenterControlCreator;
    }

    @Override
    public IInformationControlCreator getHoverControlCreator() {
        if (hoverControlCreator == null)
            hoverControlCreator = new HoverControlCreator(getInformationPresenterControlCreator()) {

                @Override
                public IInformationControl doCreateInformationControl(Shell parent) {
                    String tooltipAffordanceString = EditorsUI.getTooltipAffordanceString();
                    if (BrowserInformationControl.isAvailable(parent)) {
                        String font = "org.eclipse.jdt.ui.javadocfont";
                        IXtextBrowserInformationControl iControl = new XtextBrowserInformationControl(parent, font,
                                tooltipAffordanceString) {
                            private long changeSize = 0;
                            private long time = 0;

                            @Override
                            public void setSize(int width, int height) {
                                if (changeSize == 0 || (changeSize > 1 && (time + 100 > System.currentTimeMillis()))) {
                                    super.setSize(width, height);
                                }
                                changeSize++;
                                time = System.currentTimeMillis();
                            }

                            @Override
                            public IInformationControlCreator getInformationPresenterControlCreator() {
                                IInformationControlCreator fInformationPresenterControlCreator = null;
                                try {
                                    Field f = (new HoverControlCreator(null)).getClass()
                                            .getDeclaredField("fInformationPresenterControlCreator");
                                    f.setAccessible(true);
                                    fInformationPresenterControlCreator = (IInformationControlCreator) f
                                            .get(hoverControlCreator);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // not gonna happen
                                }
                                return fInformationPresenterControlCreator;
                            }
                        };
                        addLinkListener(iControl);
                        iControl.setSize(350, 250);
                        return iControl;
                    } else {
                        return new DefaultInformationControl(parent, tooltipAffordanceString);
                    }
                }
            };
        return hoverControlCreator;
    }

}
