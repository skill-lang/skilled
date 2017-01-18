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
 *  a path that can be used in the description of a build process
 */
public class FilePath extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "filepath".hashCode()) << 32;

    @Override
    public String skillName() {
        return "filepath";
    }

    /**
     * Create a new unmanaged FilePath. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public FilePath() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public FilePath(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public FilePath(long skillID, boolean isAbsolut, java.util.ArrayList<java.lang.String> parts) {
        super(skillID);
        this.isAbsolut = isAbsolut;
        this.parts = parts;
    }

    protected boolean isAbsolut = false;

    /**
     *  true, iff starting from file system root
     */
    final public boolean getIsAbsolut() {
        return isAbsolut;
    }

    /**
     *  true, iff starting from file system root
     */
    final public void setIsAbsolut(boolean isAbsolut) {
        this.isAbsolut = isAbsolut;
    }

    protected java.util.ArrayList<java.lang.String> parts = null;

    /**
     *  parts of the path that will be assembled into a usable path
     */
    final public java.util.ArrayList<java.lang.String> getParts() {
        return parts;
    }

    /**
     *  parts of the path that will be assembled into a usable path
     */
    final public void setParts(java.util.ArrayList<java.lang.String> parts) {
        this.parts = parts;
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
        case "isabsolut":
            return (T) (java.lang.Boolean) isAbsolut;
        case "parts":
            return (T) parts;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("FilePath(this: ").append(this);
        sb.append(", isAbsolut: ").append(isAbsolut);
        sb.append(", parts: ").append(parts);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends FilePath implements NamedType {
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
