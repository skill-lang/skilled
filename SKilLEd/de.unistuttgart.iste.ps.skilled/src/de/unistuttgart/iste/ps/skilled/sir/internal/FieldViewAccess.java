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

/**
 *  a view onto a field
 *  @note  some components such as annotations must not be set by views
 */
public class FieldViewAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.FieldView, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    FieldViewAccess(int poolIndex, FieldLikeAccess superPool) {
        super(poolIndex, "fieldview", superPool, new HashSet<String>(Arrays.asList(new String[] { "target" })), noAutoFields());
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

            de.unistuttgart.iste.ps.skilled.sir.FieldView r = new de.unistuttgart.iste.ps.skilled.sir.FieldView(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.FieldView> f;
        switch (name) {
        case "target":
            f = new KnownField_FieldView_target((FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>)(owner().poolByName().get("fieldlike")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldView> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldView> f;
        switch (name) {
        case "target":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.FieldView>) new KnownField_FieldView_target((FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike>) type, ID, this);
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
     * @return a new FieldView instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.FieldView make() {
        de.unistuttgart.iste.ps.skilled.sir.FieldView rval = new de.unistuttgart.iste.ps.skilled.sir.FieldView();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.FieldView make(de.unistuttgart.iste.ps.skilled.sir.FieldLike target, de.unistuttgart.iste.ps.skilled.sir.Comment comment, de.unistuttgart.iste.ps.skilled.sir.Identifier name, de.unistuttgart.iste.ps.skilled.sir.Type type, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        de.unistuttgart.iste.ps.skilled.sir.FieldView rval = new de.unistuttgart.iste.ps.skilled.sir.FieldView(-1, target, comment, name, type, hints, restrictions);
        add(rval);
        return rval;
    }

    public FieldViewBuilder build() {
        return new FieldViewBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.FieldView());
    }

    /**
     * Builder for new FieldView instances.
     *
     * @author Timm Felden
     */
    public static final class FieldViewBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.FieldView> {

        protected FieldViewBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.FieldView, ? super de.unistuttgart.iste.ps.skilled.sir.FieldView> pool, de.unistuttgart.iste.ps.skilled.sir.FieldView instance) {
            super(pool, instance);
        }

        public FieldViewBuilder target(de.unistuttgart.iste.ps.skilled.sir.FieldLike target) {
            instance.setTarget(target);
            return this;
        }

        public FieldViewBuilder comment(de.unistuttgart.iste.ps.skilled.sir.Comment comment) {
            instance.setComment(comment);
            return this;
        }

        public FieldViewBuilder name(de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
            instance.setName(name);
            return this;
        }

        public FieldViewBuilder type(de.unistuttgart.iste.ps.skilled.sir.Type type) {
            instance.setType(type);
            return this;
        }

        public FieldViewBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public FieldViewBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.FieldView make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.FieldView rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.FieldView, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.FieldView.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.FieldView.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.FieldView.SubType, de.unistuttgart.iste.ps.skilled.sir.FieldLike> makeSubPool(int index, String name) {
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
                de.unistuttgart.iste.ps.skilled.sir.FieldView.SubType r = new de.unistuttgart.iste.ps.skilled.sir.FieldView.SubType(this, i + 1);
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
