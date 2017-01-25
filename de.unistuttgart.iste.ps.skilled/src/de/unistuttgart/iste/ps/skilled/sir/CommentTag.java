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
 *  The tag part of a comment. Can be used for easy inspection of comments.
 */
public class CommentTag extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "commenttag".hashCode()) << 32;

    @Override
    public String skillName() {
        return "commenttag";
    }

    /**
     * Create a new unmanaged CommentTag. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public CommentTag() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public CommentTag(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public CommentTag(long skillID, java.lang.String name, java.util.ArrayList<java.lang.String> text) {
        super(skillID);
        this.name = name;
        this.text = text;
    }

    protected java.lang.String name = null;

    final public java.lang.String getName() {
        return name;
    }

    final public void setName(java.lang.String name) {
        this.name = name;
    }

    protected java.util.ArrayList<java.lang.String> text = null;

    final public java.util.ArrayList<java.lang.String> getText() {
        return text;
    }

    final public void setText(java.util.ArrayList<java.lang.String> text) {
        this.text = text;
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
        case "text":
            return (T) text;
        default:
            return super.get(field);
        }
    }

    /**
     * potentially expensive but more pretty representation of this instance.
     */
    @Override
    public String prettyString() {
        StringBuilder sb = new StringBuilder("CommentTag(this: ").append(this);
        sb.append(", name: ").append(name);
        sb.append(", text: ").append(text);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends CommentTag implements NamedType {
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
