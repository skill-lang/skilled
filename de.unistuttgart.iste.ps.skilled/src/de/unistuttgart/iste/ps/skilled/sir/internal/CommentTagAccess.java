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
 *  The tag part of a comment. Can be used for easy inspection of comments.
 */
public class CommentTagAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.CommentTag> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.CommentTag[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.CommentTag[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    CommentTagAccess(int poolIndex) {
        super(poolIndex, "commenttag", new HashSet<String>(Arrays.asList(new String[] { "name", "text" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.CommentTag[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.CommentTag r = new de.unistuttgart.iste.ps.skilled.sir.CommentTag(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.CommentTag> f;
        switch (name) {
        case "name":
            f = new KnownField_CommentTag_name(string, 1 + dataFields.size(), this);
            break;

        case "text":
            f = new KnownField_CommentTag_text(new VariableLengthArray<>(string), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CommentTag> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CommentTag> f;
        switch (name) {
        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CommentTag>) new KnownField_CommentTag_name((FieldType<java.lang.String>) type, ID, this);
            break;

        case "text":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.CommentTag>) new KnownField_CommentTag_text((FieldType<java.util.ArrayList<java.lang.String>>) type, ID, this);
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
     * @return a new CommentTag instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.CommentTag make() {
        de.unistuttgart.iste.ps.skilled.sir.CommentTag rval = new de.unistuttgart.iste.ps.skilled.sir.CommentTag();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.CommentTag make(java.lang.String name, java.util.ArrayList<java.lang.String> text) {
        de.unistuttgart.iste.ps.skilled.sir.CommentTag rval = new de.unistuttgart.iste.ps.skilled.sir.CommentTag(-1, name, text);
        add(rval);
        return rval;
    }

    public CommentTagBuilder build() {
        return new CommentTagBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.CommentTag());
    }

    /**
     * Builder for new CommentTag instances.
     *
     * @author Timm Felden
     */
    public static final class CommentTagBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.CommentTag> {

        protected CommentTagBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.CommentTag, ? super de.unistuttgart.iste.ps.skilled.sir.CommentTag> pool, de.unistuttgart.iste.ps.skilled.sir.CommentTag instance) {
            super(pool, instance);
        }

        public CommentTagBuilder name(java.lang.String name) {
            instance.setName(name);
            return this;
        }

        public CommentTagBuilder text(java.util.ArrayList<java.lang.String> text) {
            instance.setText(text);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.CommentTag make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.CommentTag rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.CommentTag, de.unistuttgart.iste.ps.skilled.sir.CommentTag> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.CommentTag.SubType, de.unistuttgart.iste.ps.skilled.sir.CommentTag> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.CommentTag.SubType, de.unistuttgart.iste.ps.skilled.sir.CommentTag> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.CommentTag.SubType, de.unistuttgart.iste.ps.skilled.sir.CommentTag> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.CommentTag[] data = ((CommentTagAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.CommentTag.SubType r = new de.unistuttgart.iste.ps.skilled.sir.CommentTag.SubType(this, i + 1);
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
