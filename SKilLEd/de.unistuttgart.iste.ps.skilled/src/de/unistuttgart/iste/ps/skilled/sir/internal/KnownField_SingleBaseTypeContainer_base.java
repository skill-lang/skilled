/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 18.01.2017                               *
 * |___/_|\_\_|_|____|    by: feldentm                                        *
\*                                                                            */
package de.unistuttgart.iste.ps.skilled.sir.internal;

import java.io.IOException;
import java.util.Iterator;

import de.ust.skill.common.java.internal.*;
import de.ust.skill.common.java.internal.fieldDeclarations.*;
import de.ust.skill.common.java.internal.fieldTypes.Annotation;
import de.ust.skill.common.java.internal.fieldTypes.MapType;
import de.ust.skill.common.java.internal.fieldTypes.SingleArgumentType;
import de.ust.skill.common.java.internal.fieldTypes.StringType;
import de.ust.skill.common.java.internal.fieldTypes.V64;
import de.ust.skill.common.java.internal.parts.Block;
import de.ust.skill.common.java.internal.parts.Chunk;
import de.ust.skill.common.java.internal.parts.SimpleChunk;
import de.ust.skill.common.java.iterators.IterableArrayView;
import de.ust.skill.common.jvm.streams.MappedInStream;
import de.ust.skill.common.jvm.streams.MappedOutStream;


/**
 * type SingleBaseTypeContainer.base
 */
final class KnownField_SingleBaseTypeContainer_base extends FieldDeclaration<de.unistuttgart.iste.ps.skilled.sir.GroundType, de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer> implements
               KnownField<de.unistuttgart.iste.ps.skilled.sir.GroundType, de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer> {

    public KnownField_SingleBaseTypeContainer_base(FieldType<de.unistuttgart.iste.ps.skilled.sir.GroundType> type, int index, SingleBaseTypeContainerAccess owner) {
        super(type, "base", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((SingleBaseTypeContainerAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        final TypeAccess target = (TypeAccess)TypeAccess
                .<de.unistuttgart.iste.ps.skilled.sir.Type,de.unistuttgart.iste.ps.skilled.sir.GroundType>cast(type);
        int count = (int) last.count;
        while (0 != count--) {
            is.next().setBase((de.unistuttgart.iste.ps.skilled.sir.GroundType)target.getByID(in.v64()));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final de.unistuttgart.iste.ps.skilled.sir.Type instance = ((de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer)data[i]).getBase().self();
            if (null == instance) {
                result += 1;
                continue;
            }
            long v = instance.getSkillID();

            if (0L == (v & 0xFFFFFFFFFFFFFF80L)) {
                result += 1;
            } else if (0L == (v & 0xFFFFFFFFFFFFC000L)) {
                result += 2;
            } else if (0L == (v & 0xFFFFFFFFFFE00000L)) {
                result += 3;
            } else if (0L == (v & 0xFFFFFFFFF0000000L)) {
                result += 4;
            } else if (0L == (v & 0xFFFFFFF800000000L)) {
                result += 5;
            } else if (0L == (v & 0xFFFFFC0000000000L)) {
                result += 6;
            } else if (0L == (v & 0xFFFE000000000000L)) {
                result += 7;
            } else if (0L == (v & 0xFF00000000000000L)) {
                result += 8;
            } else {
                result += 9;
            }
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess) owner.basePool()).data();
        int i;
        final int high;

        final Chunk last = dataChunks.getLast().c;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            i = (int) c.bpo;
            high = (int) (c.bpo + c.count);
        } else {
            i = owner.size() > 0 ? (int) owner.iterator().next().getSkillID() - 1 : 0;
            high = i + owner.size();
        }

        for (; i < high; i++) {
            type.writeSingleField(((de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer)data[i]).getBase(), out);
        }
    }

    @Override
    public de.unistuttgart.iste.ps.skilled.sir.GroundType getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer) ref).getBase();
    }

    @Override
    public void setR(SkillObject ref, de.unistuttgart.iste.ps.skilled.sir.GroundType value) {
        ((de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer) ref).setBase(value);
    }

    @Override
    public de.unistuttgart.iste.ps.skilled.sir.GroundType get(de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer ref) {
        return ref.getBase();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer ref, de.unistuttgart.iste.ps.skilled.sir.GroundType value) {
        ref.setBase(value);
    }
}
