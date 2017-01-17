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


public class TypeDefinition extends UserdefinedType {
    private static final long serialVersionUID = 0x5c11L + ((long) "typedefinition".hashCode()) << 32;

    @Override
    public String skillName() {
        return "typedefinition";
    }

    /**
     * Create a new unmanaged TypeDefinition. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public TypeDefinition() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public TypeDefinition(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public TypeDefinition(long skillID, de.unistuttgart.iste.ps.skilled.sir.Type target, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        super(skillID);
        this.target = target;
        this.comment = comment;
        this.hints = hints;
        this.restrictions = restrictions;
        this.name = name;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.Type target = null;

    /**
     *  the target of this definition
     */
    final public de.unistuttgart.iste.ps.skilled.sir.Type getTarget() {
        return target;
    }

    /**
     *  the target of this definition
     */
    final public void setTarget(de.unistuttgart.iste.ps.skilled.sir.Type target) {
        this.target = target;
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
        case "target":
            return (T) target;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("TypeDefinition(this: ").append(this);
        sb.append(", target: ").append(target);
        sb.append(", comment: ").append(comment);
        sb.append(", hints: ").append(hints);
        sb.append(", restrictions: ").append(restrictions);
        sb.append(", name: ").append(name);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends TypeDefinition implements NamedType {
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
