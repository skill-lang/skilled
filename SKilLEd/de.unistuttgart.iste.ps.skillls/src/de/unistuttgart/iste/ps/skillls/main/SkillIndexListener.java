package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.grammar.SKilLParser;
import de.unistuttgart.iste.ps.skillls.grammar.SKilLParserBaseListener;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

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
 * @author Armin Hüneburg Created on 15.09.15.
 */
class SkillIndexListener extends SKilLParserBaseListener {
    private final SkillFile skillFile;
    private de.unistuttgart.iste.ps.skillls.tools.File file;

    /**
     * Constructor.
     * This object indexes all types, fields, and other attributes of a specification file.
     * @param skillFile the skillfile dedicated to saving the attributes of the specification.
     * @param file the file to be indexed.
     */
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
        for (de.unistuttgart.iste.ps.skillls.tools.File f : skillFile.Files()) {
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

    /**
     * Method for the end of parsing a file.
     * @param ctx the content of the parsed file tokenized.
     */
    @Override
    public void exitFile(SKilLParser.FileContext ctx) {
        ctx.declaration().forEach(this::processDeclaration);
        skillFile.flush();
    }

    /**
     * method for processing a typedeclaration.
     * @param declarationContext the content of the typedeclaration.
     */
    private void processDeclaration(SKilLParser.DeclarationContext declarationContext) {
        for (Type type : skillFile.Types()) {
            int occurrence = declarationContext.getText().indexOf(type.getName());
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
            if (declarationContext.typedef() == null && occurrence != -1
                    && occurrence + type.getName().length() == extOccurrence
                    || declarationContext.typedef() != null && occurrence == 7) {
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

    /**
     * processes a typedef typedeclaration.
     * @param typedef the context of the declaration.
     */
    private void processTypedef(SKilLParser.TypedefContext typedef) {
        String name = "typedef " + typedef.name.getText() + " %s " + typedef.type().getText();

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

        Type type = skillFile.Types().make(typedef.COMMENT() == null ? "" : typedef.COMMENT().getText(), new ArrayList<>(),
                new ArrayList<>(), file, name, restrictions, hints);
        for (Hint hint : hints) {
            hint.setParent(type);
        }
    }

    /**
     * processes a enumtype typedeclaration.
     * @param enumtype the context of the declaration.
     */
    private void processEnumtype(SKilLParser.EnumtypeContext enumtype) {
        String name = "enum " + enumtype.name.getText();

        ArrayList<Field> fields = new ArrayList<>();

        fields.add(skillFile.Fields().make("", new ArrayList<>(), enumtype.enumvalues().getText(), new ArrayList<>(), null));

        fields.addAll(enumtype.field().stream().map(this::processField).collect(Collectors.toList()));

        Type type = skillFile.Types().make(enumtype.COMMENT() == null ? "" : enumtype.COMMENT().getText(), new ArrayList<>(),
                fields, file, name, new ArrayList<>(), new ArrayList<>());
        for (Field f : fields) {
            f.setType(type);
        }
    }

    /**
     * processes a field in a typedeclaration
     * @param fieldContext the context of the declaration.
     * @return the found field.
     */
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

        Field field = skillFile.Fields().make(
                fieldContext.description().COMMENT() == null ? "" : fieldContext.description().COMMENT().getText(), hints,
                name, restrictions, null);
        for (Hint h : hints) {
            h.setParent(field);
        }
        return field;
    }

    /**
     * processes a usertype typedeclaration.
     * @param usertype the context of the declaration.
     */
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

        Type type = skillFile.Types().make(
                usertype.description().COMMENT() == null ? "" : usertype.description().COMMENT().getText(), extensions,
                fields, file, name, restrictions, hints);

        for (Field f : fields) {
            f.setType(type);
        }

        for (Hint h : hints) {
            h.setParent(type);
        }
    }

    /**
     * processes an interfacetype typedeclaration.
     * @param interfacetype the context of the declaration.
     */
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