/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 16.01.2017                               *
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
 * fieldlike FieldView.target
 */
final class KnownField_FieldView_target extends FieldDeclaration<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.FieldView> implements
               KnownField<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.FieldView> {

    public KnownField_FieldView_target(FieldType<de.unistuttgart.iste.ps.skilled.sir.FieldLike> type, int index, FieldViewAccess owner) {
        super(type, "target", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.FieldView> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((FieldViewAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        final FieldLikeAccess target = (FieldLikeAccess)type;
        int count = (int) last.count;
        while (0 != count--) {
            is.next().setTarget(target.getByID(in.v64()));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final de.unistuttgart.iste.ps.skilled.sir.FieldLike instance = ((de.unistuttgart.iste.ps.skilled.sir.FieldView)data[i]).getTarget();
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
        de.unistuttgart.iste.ps.skilled.sir.FieldLike[] data = ((FieldLikeAccess) owner.basePool()).data();
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
            de.unistuttgart.iste.ps.skilled.sir.FieldLike v = ((de.unistuttgart.iste.ps.skilled.sir.FieldView)data[i]).getTarget();
            if (null == v)
                out.i8((byte) 0);
            else
                out.v64(v.getSkillID());
        }
    }

    @Override
    public de.unistuttgart.iste.ps.skilled.sir.FieldLike getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.FieldView) ref).getTarget();
    }

    @Override
    public void setR(SkillObject ref, de.unistuttgart.iste.ps.skilled.sir.FieldLike value) {
        ((de.unistuttgart.iste.ps.skilled.sir.FieldView) ref).setTarget(value);
    }

    @Override
    public de.unistuttgart.iste.ps.skilled.sir.FieldLike get(de.unistuttgart.iste.ps.skilled.sir.FieldView ref) {
        return ref.getTarget();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.FieldView ref, de.unistuttgart.iste.ps.skilled.sir.FieldLike value) {
        ref.setTarget(value);
    }
}
