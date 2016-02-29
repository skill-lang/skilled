/*  ___ _  ___ _ _                                                            *\
 * / __| |/ (_) | |       Your SKilL Java 8 Binding                           *
 * \__ \ ' <| | | |__     generated: 29.02.2016                               *
 * |___/_|\_\_|_|____|    by: mpl                                             *
\*                                                                            */
package de.unistuttgart.iste.ps.skillls.tools.internal;

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
 * generator Tool.generator
 */
final class KnownField_Tool_generator extends FieldDeclaration<de.unistuttgart.iste.ps.skillls.tools.Generator, de.unistuttgart.iste.ps.skillls.tools.Tool> implements
               KnownField<de.unistuttgart.iste.ps.skillls.tools.Generator, de.unistuttgart.iste.ps.skillls.tools.Tool> {

    public KnownField_Tool_generator(FieldType<de.unistuttgart.iste.ps.skillls.tools.Generator> type, int index, ToolAccess owner) {
        super(type, "generator", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skillls.tools.Tool> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((ToolAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        final GeneratorAccess target = (GeneratorAccess)type;
        int count = (int) last.count;
        while (0 != count--) {
            is.next().setGenerator(target.getByID(in.v64()));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final de.unistuttgart.iste.ps.skillls.tools.Tool[] data = ((ToolAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final de.unistuttgart.iste.ps.skillls.tools.Generator instance = data[i].getGenerator();
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
        de.unistuttgart.iste.ps.skillls.tools.Tool[] data = ((ToolAccess) owner.basePool()).data();
        int i;
        final int high;

        final Chunk last = dataChunks.getLast().c;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            i = (int) c.bpo;
            high = (int) (c.bpo + c.count);
        } else {
            i = 0;
            high = owner.size();
        }

        for (; i < high; i++) {
            de.unistuttgart.iste.ps.skillls.tools.Generator v = data[i].getGenerator();
            if (null == v)
                out.i8((byte) 0);
            else
                out.v64(v.getSkillID());
        }
    }

    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Generator getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skillls.tools.Tool) ref).getGenerator();
    }

    @Override
    public void setR(SkillObject ref, de.unistuttgart.iste.ps.skillls.tools.Generator value) {
        ((de.unistuttgart.iste.ps.skillls.tools.Tool) ref).setGenerator(value);
    }

    @Override
    public de.unistuttgart.iste.ps.skillls.tools.Generator get(de.unistuttgart.iste.ps.skillls.tools.Tool ref) {
        return ref.getGenerator();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skillls.tools.Tool ref, de.unistuttgart.iste.ps.skillls.tools.Generator value) {
        ref.setGenerator(value);
    }
}
