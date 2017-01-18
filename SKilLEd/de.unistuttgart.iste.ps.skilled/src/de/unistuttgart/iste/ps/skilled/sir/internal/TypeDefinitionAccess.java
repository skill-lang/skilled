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

public class TypeDefinitionAccess extends SubPool<de.unistuttgart.iste.ps.skilled.sir.TypeDefinition, de.unistuttgart.iste.ps.skilled.sir.Type> {

    /**
     * Can only be constructed by the SkillFile in this package.
     */
    TypeDefinitionAccess(int poolIndex, UserdefinedTypeAccess superPool) {
        super(poolIndex, "typedefinition", superPool, new HashSet<String>(Arrays.asList(new String[] { "target" })), noAutoFields());
    }

    @Override
    public void insertInstances() {
        de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess)basePool).data();
        final Block last = blocks().getLast();
        int i = (int) last.bpo;
        int high = (int) (last.bpo + last.count);
        while (i < high) {
            if (null != data[i])
                return;

            de.unistuttgart.iste.ps.skilled.sir.TypeDefinition r = new de.unistuttgart.iste.ps.skilled.sir.TypeDefinition(i + 1);
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

        final FieldDeclaration<?, de.unistuttgart.iste.ps.skilled.sir.TypeDefinition> f;
        switch (name) {
        case "target":
            f = new KnownField_TypeDefinition_target((FieldType<de.unistuttgart.iste.ps.skilled.sir.Type>)(owner().poolByName().get("type")), 1 + dataFields.size(), this);
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
    public <R> FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.TypeDefinition> addField(int ID, FieldType<R> type, String name,
            HashSet<FieldRestriction<?>> restrictions) {
        final FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.TypeDefinition> f;
        switch (name) {
        case "target":
            f = (FieldDeclaration<R, de.unistuttgart.iste.ps.skilled.sir.TypeDefinition>) new KnownField_TypeDefinition_target((FieldType<de.unistuttgart.iste.ps.skilled.sir.Type>) type, ID, this);
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
     * @return a new TypeDefinition instance with default field values
     */
    @Override
    public de.unistuttgart.iste.ps.skilled.sir.TypeDefinition make() {
        de.unistuttgart.iste.ps.skilled.sir.TypeDefinition rval = new de.unistuttgart.iste.ps.skilled.sir.TypeDefinition();
        add(rval);
        return rval;
    }

    /**
     * @return a new age instance with the argument field values
     */
    public de.unistuttgart.iste.ps.skilled.sir.TypeDefinition make(de.unistuttgart.iste.ps.skilled.sir.Type target, de.unistuttgart.iste.ps.skilled.sir.Comment comment, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Hint> hints, java.util.ArrayList<de.unistuttgart.iste.ps.skilled.sir.Restriction> restrictions, de.unistuttgart.iste.ps.skilled.sir.Identifier name) {
        de.unistuttgart.iste.ps.skilled.sir.TypeDefinition rval = new de.unistuttgart.iste.ps.skilled.sir.TypeDefinition(-1, target, comment, hints, restrictions, name);
        add(rval);
        return rval;
    }

    public TypeDefinitionBuilder build() {
        return new TypeDefinitionBuilder(this, new de.unistuttgart.iste.ps.skilled.sir.TypeDefinition());
    }

    /**
     * Builder for new TypeDefinition instances.
     *
     * @author Timm Felden
     */
    public static final class TypeDefinitionBuilder extends Builder<de.unistuttgart.iste.ps.skilled.sir.TypeDefinition> {

        protected TypeDefinitionBuilder(StoragePool<de.unistuttgart.iste.ps.skilled.sir.TypeDefinition, ? super de.unistuttgart.iste.ps.skilled.sir.TypeDefinition> pool, de.unistuttgart.iste.ps.skilled.sir.TypeDefinition instance) {
            super(pool, instance);
        }

        public TypeDefinitionBuilder target(de.unistuttgart.iste.ps.skilled.sir.Type target) {
            instance.setTarget(target);
            return this;
        }

        @Override
        public de.unistuttgart.iste.ps.skilled.sir.TypeDefinition make() {
            pool.add(instance);
            de.unistuttgart.iste.ps.skilled.sir.TypeDefinition rval = instance;
            instance = null;
            return rval;
        }
    }

    /**
     * used internally for type forest construction
     */
    @Override
    public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.TypeDefinition, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
        return new UnknownSubPool(index, name, this);
    }

    private static final class UnknownSubPool extends SubPool<de.unistuttgart.iste.ps.skilled.sir.TypeDefinition.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> {
        UnknownSubPool(int poolIndex, String name, StoragePool<? super de.unistuttgart.iste.ps.skilled.sir.TypeDefinition.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> superPool) {
            super(poolIndex, name, superPool, Collections.emptySet(), noAutoFields());
        }

        @Override
        public StoragePool<? extends de.unistuttgart.iste.ps.skilled.sir.TypeDefinition.SubType, de.unistuttgart.iste.ps.skilled.sir.Type> makeSubPool(int index, String name) {
            return new UnknownSubPool(index, name, this);
        }

        @Override
        public void insertInstances() {
            final Block last = lastBlock();
            int i = (int) last.bpo;
            int high = (int) (last.bpo + last.count);
            de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess) basePool).data();
            while (i < high) {
                if (null != data[i])
                    return;

                @SuppressWarnings("unchecked")
                de.unistuttgart.iste.ps.skilled.sir.TypeDefinition.SubType r = new de.unistuttgart.iste.ps.skilled.sir.TypeDefinition.SubType(this, i + 1);
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
