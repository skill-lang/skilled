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
 *  this type is used to allow tools to define their own non-standard set of hints and restrictions
 */
public class ToolTypeCustomizationAccess extends BasePool<de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> {

    @Override
    final protected de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization[] newArray(int size) {
        return new de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization[size];
    }

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    ToolTypeCustomizationAccess(int poolIndex) {
        super(poolIndex, "tooltypecustomization", new HashSet<String>(Arrays.asList(new String[] { "hints", "restrictions" })), noAutoFields());
    }

    final de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization[] data() {
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

            de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization r = new de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> f;
        switch (name) {
        case "hints":
            f = new KnownField_ToolTypeCustomization_hints(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.Hint>)(owner().poolByName().get("hint"))), 1 + dataFields.size(), this);
            break;

        case "restrictions":
            f = new KnownField_ToolTypeCustomization_restrictions(new VariableLengthArray<>((FieldType<de.unistuttgart.iste.ps.skilled.sir.Restriction>)(owner().poolByName().get("restriction"))), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> f;
        switch (name) {
        case "hints":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>) new KnownField_ToolTypeCustomization_hints((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint>>) type, ID, this);
            break;

        case "restrictions":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>) new KnownField_ToolTypeCustomization_restrictions((FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction>>) type, ID, this);
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
     * @return a new ToolTypeCustomization instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization make() {
        de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization rval = new de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization make(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
        de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization rval = new de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization(-1, hints, restrictions);
        add(rval);
        return rval;
    }

    public ToolTypeCustomizationBuilder build() {
        return new ToolTypeCustomizationBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization());
    }

    /**
     * Builder for new ToolTypeCustomization instances.
     *
     * @author Timm Felden
     */
    public static final class ToolTypeCustomizationBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> {

        protected ToolTypeCustomizationBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization, ? super de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> pool, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization instance) {
            super(pool, instance);
        }

        public ToolTypeCustomizationBuilder hints(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints) {
            instance.setHints(hints);
            return this;
        }

        public ToolTypeCustomizationBuilder restrictions(java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions) {
            instance.setRestrictions(restrictions);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization.SubType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization.SubType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization.SubType, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization[] data = ((ToolTypeCustomizationAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization.SubType r = new de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization.SubType(this, i + 1);
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
