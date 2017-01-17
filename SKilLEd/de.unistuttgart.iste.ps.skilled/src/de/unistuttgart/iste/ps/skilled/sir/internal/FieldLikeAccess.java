/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
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

public class FieldLikeAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.FieldLike> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.FieldLike[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.FieldLike[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    FieldLikeAccess(int poolIndex) {
        super(poolIndex, "fieldlike", new HashSet<String>(Arrays.asList(new String[] { "comment", "name", "type" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.FieldLike r = new de.unistuttgart.iste.ps.skilled.sir.FieldLike(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.FieldLike> f;
        switch (name) {
        case "comment":
            f = new KnownField_FieldLike_comment((FieldType<de.unistuttgart.iste.ps.skilled.sir.Comment>)(owner().poolByName().get("comment")), 1 + dataFields.size(), this);
            break;

        case "name":
            f = new KnownField_FieldLike_name((FieldType<de.unistuttgart.iste.ps.skilled.sir.Identifier>)(owner().poolByName().get("identifier")), 1 + dataFields.size(), this);
            break;

        case "type":
            f = new KnownField_FieldLike_type((FieldType<de.unistuttgart.iste.ps.skilled.sir.Type>)(owner().poolByName().get("type")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldLike> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldLike> f;
        switch (name) {
        case "comment":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldLike>) new KnownField_FieldLike_comment((FieldType<de.unistuttgart.iste.ps.skilled.sir.Comment>) type, ID, this);
            break;

        case "name":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldLike>) new KnownField_FieldLike_name((FieldType<de.unistuttgart.iste.ps.skilled.sir.Identifier>) type, ID, this);
            break;

        case "type":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldLike>) new KnownField_FieldLike_type((FieldType<de.unistuttgart.iste.ps.skilled.sir.Type>) type, ID, this);
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
     * @return a new FieldLike instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.FieldLike make() {
        de.unistuttgart.iste.ps.skilled.sir.FieldLike rval = new de.unistuttgart.iste.ps.skilled.sir.FieldLike();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.FieldLike make(de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        de.unistuttgart.iste.ps.skilled.sir.FieldLike rval = new de.unistuttgart.iste.ps.skilled.sir.FieldLike(-1, comment, name, type, hints, restrictions);
        add(rval);
        return rval;
    }

    public FieldLikeBuilder build() {
        return new FieldLikeBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.FieldLike());
    }

    /**
     * Builder for new FieldLike instances.
     *
     * @author Timm Felden
     */
    public static final class FieldLikeBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.FieldLike> {

        protected FieldLikeBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.FieldLike, ? super de.unistuttgart.iste.ps.skilled.sir.FieldLike> pool, de.unistuttgart.iste.ps.skilled.sir.FieldLike instance) {
            super(pool, instance);
        }

        public FieldLikeBuilder comment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
            instance.setComment(comment);
            return this;
        }

        public FieldLikeBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        public FieldLikeBuilder type(de.unistuttgart.iste.ps.skilled.sir.Type type) {
            instance.setType(type);
            return this;
        }

        public FieldLikeBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public FieldLikeBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.FieldLike make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.FieldLike rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.FieldLike.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.FieldLike.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.FieldLike.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.FieldLike.SubType r = new de.unistuttgart.iste.ps.skilled.sir.FieldLike.SubType(this, i + 1);
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
