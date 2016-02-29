/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.AutoField;
import de.ust.skill.common.java.internal.fieldTypes.*;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.restrictions.FieldRestriction;

public class HintParentAccess extends BasePool<de.unistuttgart.iste.ps.skillls.tools.HintParent> {

    @Override
    final protected de.unistuttgart.iste.ps.skillls.tools.HintParent[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skillls.tools.HintParent[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    HintParentAccess(int poolIndex) {
        super(poolIndex, "hintparent", new HashSet<String>(Arrays.asList(new String[] { "hints" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skillls.tools.HintParent[] data() {
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

            de.unistuttgart.iste.ps.skillls.tools.HintParent r = new de.unistuttgart.iste.ps.skillls.tools.HintParent(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skillls.tools.HintParent> f;
        switch (name) {
        case "hints":
            f = new KnownField_HintParent_hints(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skillls.tools.Hint>)(owner().poolByName().get("hint"))), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.HintParent> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.HintParent> f;
        switch (name) {
        case "hints":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skillls.tools.HintParent>) new KnownField_HintParent_hints((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint>>) type, ID, this);
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
     * @return a new HintParent instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skillls.tools.HintParent make() {
        de.unistuttgart.iste.ps.skillls.tools.HintParent rval = new de.unistuttgart.iste.ps.skillls.tools.HintParent();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skillls.tools.HintParent make(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
        de.unistuttgart.iste.ps.skillls.tools.HintParent rval = new de.unistuttgart.iste.ps.skillls.tools.HintParent(-1, hints);
        add(rval);
        return rval;
    }

    public HintParentBuilder build() {
        return new HintParentBuilder(this, new de.unistuttgart.iste.ps.skillls.tools.HintParent());
    }

    /**
     * Builder for new HintParent instances.
     *
     * @author Timm Felden
     */
    public static final class HintParentBuilder extends Builder<de.unistuttgart.iste.ps.skillls.tools.HintParent> {

        protected HintParentBuilder(StoragePool<de.unistuttgart.iste.ps.skillls.tools.HintParent, ? super de.unistuttgart.iste.ps.skillls.tools.HintParent> pool, de.unistuttgart.iste.ps.skillls.tools.HintParent instance) {
            super(pool, instance);
        }

        public HintParentBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Hint> hints) {
            instance.setHints(hints);
            return this;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.HintParent, de.unistuttgart.iste.ps.skillls.tools.HintParent> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skillls.tools.HintParent.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skillls.tools.HintParent.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skillls.tools.HintParent.SubType, de.unistuttgart.iste.ps.skillls.tools.HintParent> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skillls.tools.HintParent[] data = ((HintParentAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skillls.tools.HintParent.SubType r = new de.unistuttgart.iste.ps.skillls.tools.HintParent.SubType(this, i + 1);
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
