/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;

/**
 *  anything that can receive annotations
 */
public interface Annotations  {

    /**
     * cast to concrete type
     */
    public default de.ust.skill.common.java.internal.SkillObject self() {
        return (de.ust.skill.common.java.internal.SkillObject) this;
    }

    public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> getHints();

    public void setHints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints);

    public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> getRestrictions();

    public void setRestrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions);

}
