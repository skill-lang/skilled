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

public class FieldAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.Field, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    FieldAccess(int poolIndex, FieldLikeAccess superPool) {
        super(poolIndex, "field", superPool, new HashSet<String>(Arrays.asList(new String[] { "isauto" })), noAutoFields());
    }

    @Override
    public void insertInstances() {
        de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess)basePool).data();
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skilled.sir.Field r = new de.unistuttgart.iste.ps.skilled.sir.Field(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.Field> f;
        switch (name) {
        case "isauto":
            f = new KnownField_Field_isAuto(BoolType.get(), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Field> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Field> f;
        switch (name) {
        case "isauto":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.Field>) new KnownField_Field_isAuto((FieldType<java.lang.Boolean>) type, ID, this);
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
     * @return a new Field instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.Field make() {
        de.unistuttgart.iste.ps.skilled.sir.Field rval = new de.unistuttgart.iste.ps.skilled.sir.Field();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.Field make(boolean isAuto, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        de.unistuttgart.iste.ps.skilled.sir.Field rval = new de.unistuttgart.iste.ps.skilled.sir.Field(-1, isAuto, comment, name, type, hints, restrictions);
        add(rval);
        return rval;
    }

    public FieldBuilder build() {
        return new FieldBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.Field());
    }

    /**
     * Builder for new Field instances.
     *
     * @author Timm Felden
     */
    public static final class FieldBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.Field> {

        protected FieldBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.Field, ? super de.unistuttgart.iste.ps.skilled.sir.Field> pool, de.unistuttgart.iste.ps.skilled.sir.Field instance) {
            super(pool, instance);
        }

        public FieldBuilder isAuto(boolean isAuto) {
            instance.setIsAuto(isAuto);
            return this;
        }

        public FieldBuilder comment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
            instance.setComment(comment);
            return this;
        }

        public FieldBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        public FieldBuilder type(de.unistuttgart.iste.ps.skilled.sir.Type type) {
            instance.setType(type);
            return this;
        }

        public FieldBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public FieldBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.Field make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.Field rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Field, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.Field.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.Field.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.Field.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.Field.SubType r = new de.unistuttgart.iste.ps.skilled.sir.Field.SubType(this, i + 1);
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
