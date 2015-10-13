package main;

import grammar.SKilLParser;
import grammar.SKilLParserBaseListener;
import tools.Field;
import tools.Hint;
import tools.Type;
import tools.api.SkillFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by armin on 15.09.15.
 */
public class SkillIndexListener extends SKilLParserBaseListener {
    private final SkillFile skillFile;
    private final File file;

    public SkillIndexListener(SkillFile skillFile, File file) {
        super();
        this.skillFile = skillFile;
        this.file = file;
        for (tools.File f : skillFile.Files()) {
            if (f.getPath().equals(file.getAbsolutePath())) {
                return;
            }
        }
        skillFile.Files().make("", file.getAbsolutePath(), "");
    }

    @Override
    public void exitUsertype(SKilLParser.UsertypeContext ctx) {
        Type type = skillFile.Types().make(new ArrayList<>(), skillFile.Files().make("", file.getAbsolutePath(), "") ,ctx.name.getText(), new ArrayList<>());
        for (Type t : skillFile.Types()) {
            if (type.getName().equals(t.getName()) && type.getFile().getPath().equals(t.getFile().getPath())) {
                return;
            }
        }
        skillFile.Types().add(type);
    }

    @Override
    public void exitEnumtype(SKilLParser.EnumtypeContext ctx) {
        Type type = skillFile.Types().make(new ArrayList<>(), skillFile.Files().make("", file.getAbsolutePath(), "") ,ctx.name.getText(), new ArrayList<>());
        for (Type t : skillFile.Types()) {
            if (type.getName().equals(t.getName()) && type.getFile().getPath().equals(t.getFile().getPath())) {
                return;
            }
        }
        skillFile.Types().add(type);
    }

    @Override
    public void exitInterfacetype(SKilLParser.InterfacetypeContext ctx) {
        Type type = skillFile.Types().make(new ArrayList<>(), skillFile.Files().make("", file.getAbsolutePath(), "") ,ctx.name.getText(), new ArrayList<>());
        for (Type t : skillFile.Types()) {
            if (type.getName().equals(t.getName()) && type.getFile().getPath().equals(t.getFile().getPath())) {
                return;
            }
        }
        skillFile.Types().add(type);
    }

    @Override
    public void exitTypedef(SKilLParser.TypedefContext ctx) {
        Type type = skillFile.Types().make(new ArrayList<>(), skillFile.Files().make("", file.getAbsolutePath(), "") ,ctx.name.getText(), new ArrayList<>());
        for (Type t : skillFile.Types()) {
            if (type.getName().equals(t.getName()) && type.getFile().getPath().equals(t.getFile().getPath())) {
                return;
            }
        }
        skillFile.Types().add(type);
    }

    //@Override
    //public void exit
}
