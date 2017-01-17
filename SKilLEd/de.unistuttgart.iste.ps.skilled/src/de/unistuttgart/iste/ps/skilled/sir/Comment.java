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
 *  A comment that explains types or fields.
 *  @author  Timm Felden
 */
public class Comment extends SkillObject {
    private static final long serialVersionUID = 0x5c11L + ((long) "comment".hashCode()) << 32;

    @Override
    public String skillName() {
        return "comment";
    }

    /**
     * Create a new unmanaged Comment. Allocation of objects without using the
     * access factory method is discouraged.
     */
    public Comment() {
        super(-1);
    }

    /**
     * Used for internal construction only!
     * 
     * @param skillID
     */
    public Comment(long skillID) {
        super(skillID);
    }

    /**
     * Used for internal construction, full allocation.
     */
    public Comment(long skillID, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag> tags, java.util.ArrayList<java.lang.String> text) {
        super(skillID);
        this.tags = tags;
        this.text = text;
    }

    protected java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag> tags = null;

    final public java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag> getTags() {
        return tags;
    }

    final public void setTags(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag> tags) {
        this.tags = tags;
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
        case "tags":
            return (T) tags;
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
        StringBuilder sb = new StringBuilder("Comment(this: ").append(this);
        sb.append(", tags: ").append(tags);
        sb.append(", text: ").append(text);
        return sb.append(")").toString();
    }

    /**
     * Generic sub types of this type.
     * 
     * @author Timm Felden
     */
    public static final class SubType extends Comment implements NamedType {
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
