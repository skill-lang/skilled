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
 * map<fieldlike,tooltypecustomization> Tool.customFieldAnnotations
 */
final class KnownField_Tool_customFieldAnnotations extends FieldDeclaration<java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>, de.unistuttgart.iste.ps.skilled.sir.Tool> implements
               KnownField<java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>, de.unistuttgart.iste.ps.skilled.sir.Tool> {

    public KnownField_Tool_customFieldAnnotations(FieldType<java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization>> type, int index, ToolAccess owner) {
        super(type, "customfieldannotations", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.Tool> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((ToolAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setCustomFieldAnnotations(type.readSingleField(in));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final MapType t = (MapType) type;
        final FieldType keyType = t.keyType;
        final FieldType valueType = t.valueType;
        final de.unistuttgart.iste.ps.skilled.sir.Tool[] data = ((ToolAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> v = (data[i]).getCustomFieldAnnotations();
            if(null==v)
                result++;
            else {
                result += V64.singleV64Offset(v.size());
                result += keyType.calculateOffset(v.keySet());
                result += valueType.calculateOffset(v.values());
            }
        }
        return result;
    }

    @Override
    public void write(MappedOutStream out) throws IOException {
        de.unistuttgart.iste.ps.skilled.sir.Tool[] data = ((ToolAccess) owner.basePool()).data();
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
            type.writeSingleField(data[i].getCustomFieldAnnotations(), out);
        }
    }

    @Override
    public java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.Tool) ref).getCustomFieldAnnotations();
    }

    @Override
    public void setR(SkillObject ref, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> value) {
        ((de.unistuttgart.iste.ps.skilled.sir.Tool) ref).setCustomFieldAnnotations(value);
    }

    @Override
    public java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> get(de.unistuttgart.iste.ps.skilled.sir.Tool ref) {
        return ref.getCustomFieldAnnotations();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.Tool ref, java.util.HashMap<de.unistuttgart.iste.ps.skilled.sir.FieldLike, de.unistuttgart.iste.ps.skilled.sir.ToolTypeCustomization> value) {
        ref.setCustomFieldAnnotations(value);
    }
}
