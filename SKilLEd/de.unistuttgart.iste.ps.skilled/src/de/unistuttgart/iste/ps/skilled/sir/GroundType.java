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
 *  Types without type arguments.
 */
public interface GroundType  {

    /**
     * cast to concrete type
     */
    public default de.unistuttgart.iste.ps.skilled.sir.Type self() {
        return (de.unistuttgart.iste.ps.skilled.sir.Type) this;
    }

    /**
     *  the skill name used to identify this type, e.g. i32.
     */
    public de.unistuttgart.iste.ps.skilled.sir.Identifier getName();

    /**
     *  the skill name used to identify this type, e.g. i32.
     */
    public void setName(de.unistuttgart.iste.ps.skilled.sir.Identifier name);

}
