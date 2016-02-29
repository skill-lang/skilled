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
 * type[] Tool.types
 */
final class KnownField_Tool_types extends FieldDeclaration<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type>, de.unistuttgart.iste.ps.skillls.tools.Tool> implements
               KnownField<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type>, de.unistuttgart.iste.ps.skillls.tools.Tool> {

    public KnownField_Tool_types(FieldType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type>> type, int index, ToolAccess owner) {
        super(type, "types", index, owner);
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

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setTypes(type.readSingleField(in));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final SingleArgumentType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type>, de.unistuttgart.iste.ps.skillls.tools.Type> t = (SingleArgumentType<java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type>, de.unistuttgart.iste.ps.skillls.tools.Type>) type;
        final FieldType<de.unistuttgart.iste.ps.skillls.tools.Type> baseType = t.groundType;
        final de.unistuttgart.iste.ps.skillls.tools.Tool[] data = ((ToolAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> v = (data[i]).getTypes();
            if(null==v)
                result++;
            else {
                result += V64.singleV64Offset(v.size());
                result += baseType.calculateOffset(v);
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
            type.writeSingleField(data[i].getTypes(), out);
        }
    }

    @Override
    public java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skillls.tools.Tool) ref).getTypes();
    }

    @Override
    public void setR(SkillObject ref, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> value) {
        ((de.unistuttgart.iste.ps.skillls.tools.Tool) ref).setTypes(value);
    }

    @Override
    public java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> get(de.unistuttgart.iste.ps.skillls.tools.Tool ref) {
        return ref.getTypes();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skillls.tools.Tool ref, java.util.ArrayList<de.unistuttgart.iste.ps.skillls.tools.Type> value) {
        ref.setTypes(value);
    }
}
