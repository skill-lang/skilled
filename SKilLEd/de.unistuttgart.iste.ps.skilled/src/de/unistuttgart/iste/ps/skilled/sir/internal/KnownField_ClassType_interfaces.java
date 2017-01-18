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
 * set<interfacetype> ClassType.interfaces
 */
final class KnownField_ClassType_interfaces extends FieldDeclaration<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>, de.unistuttgart.iste.ps.skilled.sir.ClassType> implements
               KnownField<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>, de.unistuttgart.iste.ps.skilled.sir.ClassType> {

    public KnownField_ClassType_interfaces(FieldType<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>> type, int index, ClassTypeAccess owner) {
        super(type, "interfaces", index, owner);
            // TODO insert known restrictions?
    }

    @Override
    public void read(ChunkEntry ce) {
        final MappedInStream in = ce.in;
        final Chunk last = ce.c;
        final Iterator<de.unistuttgart.iste.ps.skilled.sir.ClassType> is;
        if (last instanceof SimpleChunk) {
            SimpleChunk c = (SimpleChunk) last;
            is = ((ClassTypeAccess) owner).dataViewIterator((int) c.bpo, (int) (c.bpo + c.count));
        } else
            is = owner.iterator();

        int count = (int) last.count;
        while (0 != count--) {
            is.next().setInterfaces(type.readSingleField(in));
        }
    }

    @Override
    public long offset() {
        final Block range = owner.lastBlock();
        final SingleArgumentType<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>, de.unistuttgart.iste.ps.skilled.sir.InterfaceType> t = (SingleArgumentType<java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>, de.unistuttgart.iste.ps.skilled.sir.InterfaceType>) type;

        final FieldType<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> baseType = t.groundType;
        final de.unistuttgart.iste.ps.skilled.sir.Type[] data = ((TypeAccess) owner.basePool()).data();
        long result = 0L;
        int i = null == range ? 0 : (int) range.bpo;
        final int high = null == range ? data.length : (int) (range.bpo + range.count);
        for (; i < high; i++) {
            final java.util.HashSet<? extends de.unistuttgart.iste.ps.skilled.sir.InterfaceType> v = (java.util.HashSet<? extends de.unistuttgart.iste.ps.skilled.sir.InterfaceType>)((de.unistuttgart.iste.ps.skilled.sir.ClassType)data[i]).getInterfaces();
            if(null==v || v.isEmpty())
                result++;
            else {
                result += V64.singleV64Offset(v.size());
                result += baseType.calculateOffset((java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType>)v);
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
            type.writeSingleField(((de.unistuttgart.iste.ps.skilled.sir.ClassType)data[i]).getInterfaces(), out);
        }
    }

    @Override
    public java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> getR(SkillObject ref) {
        return ((de.unistuttgart.iste.ps.skilled.sir.ClassType) ref).getInterfaces();
    }

    @Override
    public void setR(SkillObject ref, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> value) {
        ((de.unistuttgart.iste.ps.skilled.sir.ClassType) ref).setInterfaces(value);
    }

    @Override
    public java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> get(de.unistuttgart.iste.ps.skilled.sir.ClassType ref) {
        return ref.getInterfaces();
    }

    @Override
    public void set(de.unistuttgart.iste.ps.skilled.sir.ClassType ref, java.util.HashSet<de.unistuttgart.iste.ps.skilled.sir.InterfaceType> value) {
        ref.setInterfaces(value);
    }
}
