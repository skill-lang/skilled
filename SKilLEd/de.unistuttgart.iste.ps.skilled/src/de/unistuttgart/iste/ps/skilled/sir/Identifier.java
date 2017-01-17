/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir;

import de.ust.skill.common.java.api.FieldDeclaration;
import de.ust.skill.common.java.internal.NamedType;
import de.ust.skill.common.java.internal.SkillObject;
import de.ust.skill.common.java.internal.StoragePool;


/**
 *  A nicer version of a skill name that can be used to adapt to a target language naming convention.
 */
public class Identifier extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "identifier".hashCode()) << 32;

    @Override
    public String skillName() {
        return "identifier";
    }

    /**
     * Create a new unmanaged Identifier. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Identifier() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Identifier(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Identifier(long skillID, java.util.ArrayList<java.lang.String> parts, java.lang.String skillname) {
        super(skillID);
        this.parts = parts;
        this.skillname = skillname;
    }

    protected java.util.ArrayList<java.lang.String> parts = null;

    /**
     *  parts used to create the skill name
     */
    final public java.util.ArrayList<java.lang.String> getParts() {
        return parts;
    }

    /**
     *  parts used to create the skill name
     */
    final public void setParts(java.util.ArrayList<java.lang.String> parts) {
        this.parts = parts;
    }

    protected java.lang.String skillname = null;

    /**
     *  the plain skill name
     */
    final public java.lang.String getSkillname() {
        return skillname;
    }

    /**
     *  the plain skill name
     */
    final public void setSkillname(java.lang.String skillname) {
        this.skillname = skillname;
    }

    /**
     * unchecked conversions are required, because the Java type system known
     * nothing of our invariants
     * 
     * @note to self: Boxing bei primitiven beachten!
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(FieldDeclaration<T> field) {
        switch (field.name()) {
        case "parts":
            return (T) parts;
        case "skillname":
            return (T) skillname;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("Identifier(this: ").append(this);
        sb.append(", parts: ").append(parts);
        sb.append(", skillname: ").append(skillname);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Identifier implements NamedType {
        private final StoragePool<?, ?> τPool;

        /** internal use only!!! */
        public SubType(StoragePool<?, ?> τPool, long skillID) {
            super(skillID);
            this.τPool = τPool;
        }

        @Override
        public StoragePool<?, ?> τPool() {
          return τPool;
        }

        @Override
        public String skillName() {
            return τPool.name();
        }

        @Override
        public String toString() {
            return skillName() + "#" + skillID;
        }
    }
}
