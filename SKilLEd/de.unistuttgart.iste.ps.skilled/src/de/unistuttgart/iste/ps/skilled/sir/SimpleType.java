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
 *  simple predefined types, such as i32
 */
public class SimpleType extends BuiltinType implements GroundType {
    private static final long serialVersionUID = 0x5c11L + ((long) "simpletype".hashCode()) << 32;

    @Override
    public String skillName() {
        return "simpletype";
    }

    /**
     * Create a new unmanaged SimpleType. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public SimpleType() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public SimpleType(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public SimpleType(long skillID, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        super(skillID);
        this.name = name;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.Identifier name = null;

    /**
     *  the skill name used to identify this type, e.g. i32.
     */
    final public de.unistuttgart.iste.ps.skilled.sir.Identifier getName() {
        return name;
    }

    /**
     *  the skill name used to identify this type, e.g. i32.
     */
    final public void setName(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        this.name = name;
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
        case "name":
            return (T) name;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("SimpleType(this: ").append(this);
        sb.append(", name: ").append(name);
        return sb.append(")").toString();
    }

    @Override
    public SimpleType self() {
        return this;
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends SimpleType implements NamedType {
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
