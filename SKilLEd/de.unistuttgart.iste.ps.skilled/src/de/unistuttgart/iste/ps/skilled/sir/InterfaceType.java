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


public class InterfaceType extends UserdefinedType {
    private static final long serialVersionUID = 0x5c11L + ((long) "interfacetype".hashCode()) << 32;

    @Override
    public String skillName() {
        return "interfacetype";
    }

    /**
     * Create a new unmanaged InterfaceType. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public InterfaceType() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public InterfaceType(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public InterfaceType(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces, de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        super(skillID);
        this.fields = fields;
        this.interfaces = interfaces;
        this.Zsuper = Zsuper;
        this.comment = comment;
        this.hints = hints;
        this.restrictions = restrictions;
        this.name = name;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields = null;

    /**
     *  there might be multiple fields under the same name in this type, as the class can be a) part of configuration b)
     *  contain custom fields
     */
    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> getFields() {
        return fields;
    }

    /**
     *  there might be multiple fields under the same name in this type, as the class can be a) part of configuration b)
     *  contain custom fields
     */
    final public void setFields(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.FieldLike> fields) {
        this.fields = fields;
    }

    protected java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces = null;

    final public java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> getInterfaces() {
        return interfaces;
    }

    final public void setInterfaces(java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> interfaces) {
        this.interfaces = interfaces;
    }

    protected de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper = null;

    final public de.unistuttgart.iste.ps.skilled.sir.ClassType getSuper() {
        return Zsuper;
    }

    final public void setSuper(de.unistuttgart.iste.ps.skilled.sir.ClassType Zsuper) {
        this.Zsuper = Zsuper;
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
        case "fields":
            return (T) fields;
        case "interfaces":
            return (T) interfaces;
        case "super":
            return (T) Zsuper;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("InterfaceType(this: ").append(this);
        sb.append(", fields: ").append(fields);
        sb.append(", interfaces: ").append(interfaces);
        sb.append(", Zsuper: ").append(Zsuper);
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
    public static final class SubType extends InterfaceType implements NamedType {
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
