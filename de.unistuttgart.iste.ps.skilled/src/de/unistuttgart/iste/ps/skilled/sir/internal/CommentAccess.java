/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.AutoField;
import de.ust.skill.common.java.internal.fieldTypes.*;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.restrictions.FieldRestriction;

/**
 *  A comment that explains types or fields.
 *  @author  Timm Felden
 */
public class CommentAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.Comment> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.Comment[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.Comment[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    CommentAccess(int poolIndex) {
        super(poolIndex, "comment", new HashSet<String>(Arrays.asList(new String[] { "tags", "text" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.Comment[] data() {
        return data;
    }

    @Override
    public void insertInstances() {
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skilled.sir.Comment r = new de.unistuttgart.iste.ps.skilled.sir.Comment(i + 1);
            data[i] = r;
            staticData.add(r);

            i += 1;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addKnownField(
        String name,
        de.ust.skill.common.java.internal.fieldTypes.StringType string,
        de.ust.skill.common.java.internal.fieldTypes.Annotation annotation) {

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.Comment> f;
        switch (name) {
        case "tags":
            f = new KnownField_Comment_tags(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.CommentTag>)(owner().poolByName().get("commenttag"))), 1 + dataFields.size(), this);
            break;

        case "text":
            f = new KnownField_Comment_text(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
            break;

        default:
            super.addKnownField(name, string, annotation);
            return;
        }
        if (!(f instanceof AutoField))
            dataFields.add(f);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Comment> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Comment> f;
        switch (name) {
        case "tags":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Comment>) new KnownField_Comment_tags((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag>>) type, ID, this);
            break;

        case "text":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Comment>) new KnownField_Comment_text((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
            break;

        default:
            return super.addField(ID, type, name, restrictions);
        }

        for (FieldRestriction<?> r : restrictions)
            f.addRestriction(r);
        dataFields.add(f);
        return f;
    }

    /**
     * @return a new Comment instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.Comment make() {
        de.unistuttgart.iste.ps.skilled.sir.Comment rval = new de.unistuttgart.iste.ps.skilled.sir.Comment();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.Comment make(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag> tags, java.util.ArrayList<java.lang.String> text) {
        de.unistuttgart.iste.ps.skilled.sir.Comment rval = new de.unistuttgart.iste.ps.skilled.sir.Comment(-1, tags, text);
        add(rval);
        return rval;
    }

    public CommentBuilder build() {
        return new CommentBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.Comment());
    }

    /**
     * Builder for new Comment instances.
     *
     * @author Timm Felden
     */
    public static final class CommentBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.Comment> {

        protected CommentBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.Comment, ? super de.unistuttgart.iste.ps.skilled.sir.Comment> pool, de.unistuttgart.iste.ps.skilled.sir.Comment instance) {
            super(pool, instance);
        }

        public CommentBuilder tags(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.CommentTag> tags) {
            instance.setTags(tags);
            return this;
        }

        public CommentBuilder text(java.util.ArrayList<java.lang.String> text) {
            instance.setText(text);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.Comment make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.Comment rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Comment, de.unistuttgart.iste.ps.skilled.sir.Comment> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.Comment.SubType, de.unistuttgart.iste.ps.skilled.sir.Comment> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.Comment.SubType, de.unistuttgart.iste.ps.skilled.sir.Comment> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Comment.SubType, de.unistuttgart.iste.ps.skilled.sir.Comment> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.Comment[] data = ((CommentAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.Comment.SubType r = new de.unistuttgart.iste.ps.skilled.sir.Comment.SubType(this, i + 1);
                data[i] = r;
                staticData.add(r);

                i += 1;
            }
        }
    }

    /**
     * punch a hole into the java type system :)
     */
    @SuppressWarnings("unchecked")
    static <T, U> FieldType<T> cast(FieldType<U> f) {
        return (FieldType<T>) f;
    }
}
