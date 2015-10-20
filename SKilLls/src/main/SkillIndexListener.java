package main;

import grammar.SKilLParser;
import grammar.SKilLParserBaseListener;
import org.antlr.v4.runtime.tree.TerminalNode;
import tools.Field;
import tools.Hint;
import tools.Type;
import tools.api.SkillFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Armin Hüneburg
 * Created on 15.09.15.
 */
@SuppressWarnings("Convert2streamapi")
class SkillIndexListener extends SKilLParserBaseListener {
    private final SkillFile skillFile;
    private tools.File file;

    public SkillIndexListener(SkillFile skillFile, File file) {
        super();
        this.skillFile = skillFile;
        byte[] bytes = new byte[4096];
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()))) {
                DigestInputStream dis = new DigestInputStream(is, md5);
                while (dis.read(bytes, 0, bytes.length) != -1) {
                    // digest message
                }
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
            bytes = md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        for (tools.File f : skillFile.Files()) {
            if (f.getPath().equals(file.getAbsolutePath())) {
                if (f.getMd5().equals(bytes) && f.getTimestamp().equals(String.valueOf(file.lastModified()))) {
                    this.file = null;
                    break;
                }
				this.file = f;
				this.file.setMd5(new String(bytes));
				this.file.setTimestamp("" + file.lastModified());
            }
        }
    }

    @Override
    public void exitFile(SKilLParser.FileContext ctx) {
    	ctx.declaration().forEach(this::processDeclaration);
        skillFile.flush();
    }

    private void processDeclaration(SKilLParser.DeclarationContext declarationContext) {
        for (Type type : skillFile.Types()) {
            int occurrence = declarationContext.getText().lastIndexOf(type.getName());
            int extOccurrence = Integer.MAX_VALUE;
            String text = declarationContext.getText();
            int o = text.indexOf(':');
            if (extOccurrence > o && o != -1) {
                extOccurrence = o;
            }
            o = text.indexOf("with");
            if (extOccurrence > o && o != -1) {
                extOccurrence = o;
            }
            o = text.indexOf("extends");
            if (extOccurrence > o && o != -1) {
                extOccurrence = o;
            }
            o = text.indexOf('{');
            if (extOccurrence > o && o != -1) {
                extOccurrence = o;
            }
            if (occurrence != -1 && occurrence < extOccurrence ) {
                return;
            }
        }
        if (declarationContext.interfacetype() != null) {
            processInterface(declarationContext.interfacetype());
        } else if (declarationContext.usertype() != null) {
            processUsertype(declarationContext.usertype());
        } else if (declarationContext.enumtype() != null) {
            processEnumtype(declarationContext.enumtype());
        } else if (declarationContext.typedef() != null) {
            processTypedef(declarationContext.typedef());
        }
    }

    private void processTypedef(SKilLParser.TypedefContext typedef) {
        String name = "typedef " + typedef.name.getText() + " " + typedef.type().getText();

        ArrayList<String> restrictions = new ArrayList<>();

        for (SKilLParser.RestrictionContext restrictionContext : typedef.restriction()) {
            skillFile.Strings().add(restrictionContext.getText());
            restrictions.add(restrictionContext.getText());
        }

        ArrayList<Hint> hints = new ArrayList<>();

        for (SKilLParser.HintContext hintContext : typedef.hint()) {
            Hint hint = skillFile.Hints().make(hintContext.getText(), null);
            hints.add(hint);
        }

        Type type = skillFile.Types().make(typedef.COMMENT() == null ? "" : typedef.COMMENT().getText(), new ArrayList<>(), new ArrayList<>(), file, name, restrictions, hints);
        for (Hint hint : hints) {
            hint.setParent(type);
        }
    }

    private void processEnumtype(SKilLParser.EnumtypeContext enumtype) {
        String name = "enum " + enumtype.name.getText();

        ArrayList<Field> fields = new ArrayList<>();

        fields.add(skillFile.Fields().make("", new ArrayList<>(), enumtype.enumvalues().getText(), new ArrayList<>(), null));

        fields.addAll(enumtype.field().stream().map(this::processField).collect(Collectors.toList()));

        Type type = skillFile.Types().make(enumtype.COMMENT() == null ? "" : enumtype.COMMENT().getText(), new ArrayList<>(), fields, file, name, new ArrayList<>(), new ArrayList<>());
        for (Field f : fields) {
            f.setType(type);
        }
    }

    private Field processField(SKilLParser.FieldContext fieldContext) {
        ArrayList<Hint> hints = new ArrayList<>();

        for (SKilLParser.HintContext h : fieldContext.description().hint()) {
            hints.add(skillFile.Hints().make(h.getText(), null));
        }

        String name;
        if (fieldContext.data() == null) {
            name = fieldContext.constant().getText();
        } else {
            name = fieldContext.data().type().getText() + " " + fieldContext.data().Identifier().getText();
        }

        ArrayList<String> restrictions = new ArrayList<>();

        for (SKilLParser.RestrictionContext r : fieldContext.description().restriction()) {
            skillFile.Strings().add(r.getText());
            restrictions.add(r.getText());
        }

        Field field = skillFile.Fields().make(fieldContext.description().COMMENT() == null ? "" : fieldContext.description().COMMENT().getText(), hints, name, restrictions, null);
        for (Hint h : hints) {
            h.setParent(field);
        }
        return field;
    }

    private void processUsertype(SKilLParser.UsertypeContext usertype) {
        ArrayList<Hint> hints = new ArrayList<>();

        for (SKilLParser.HintContext h : usertype.description().hint()) {
            hints.add(skillFile.Hints().make(h.getText(), null));
        }

        String name = usertype.name.getText();

        ArrayList<String> extensions = new ArrayList<>();

        for (SKilLParser.ExtensionContext ex : usertype.extension()) {
            skillFile.Strings().add(ex.Identifier().getSymbol().getText());
            extensions.add(ex.Identifier().getSymbol().getText());
        }

        ArrayList<String> restrictions = new ArrayList<>();

        for (SKilLParser.RestrictionContext r : usertype.description().restriction()) {
            skillFile.Strings().add(r.getText());
            restrictions.add(r.getText());
        }

        ArrayList<Field> fields = new ArrayList<>();

        for (SKilLParser.FieldContext f : usertype.field()) {
            fields.add(processField(f));
        }

        Type type = skillFile.Types().make(usertype.description().COMMENT() == null ? "" : usertype.description().COMMENT().getText(),
                extensions, fields, file, name, restrictions, hints);

        for (Field f : fields) {
            f.setType(type);
        }

        for (Hint h : hints) {
            h.setParent(type);
        }
    }

    private void processInterface(SKilLParser.InterfacetypeContext interfacetype) {
        ArrayList<Hint> hints = new ArrayList<>();

        String name = "interface " + interfacetype.name.getText();

        ArrayList<String> extensions = new ArrayList<>();

        for (SKilLParser.ExtensionContext ex : interfacetype.extension()) {
            ex.Identifier().getSymbol().getText();
        }

        ArrayList<String> restrictions = new ArrayList<>();

        ArrayList<Field> fields = new ArrayList<>();

        for (SKilLParser.FieldContext f : interfacetype.field()) {
            fields.add(processField(f));
        }

        Type type = skillFile.Types().make(interfacetype.COMMENT() == null ? "" : interfacetype.COMMENT().getText(),
                extensions, fields, file, name, restrictions, hints);

        for (Field f : fields) {
            f.setType(type);
        }

        for (Hint h : hints) {
            h.setParent(type);
        }
    }
}
