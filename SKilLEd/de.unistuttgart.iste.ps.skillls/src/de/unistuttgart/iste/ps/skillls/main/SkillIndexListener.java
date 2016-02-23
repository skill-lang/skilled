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
import java.util.Arrays;
import java.util.HashMap;


/**
 * Class for indexing all types, fields, hints and restrictions of a skill specification file
 *
 * @author Armin Hüneburg
 * @since 15.09.15
 */
@SuppressWarnings("unchecked")
class SkillIndexListener extends SKilLParserBaseListener {
    private final SkillFile skillFile;
    private de.unistuttgart.iste.ps.skillls.tools.File file;
    private final CleanUpAssistant cleanUpAssistant;
    private final HashMap<String, Type> origTypes;

    /**
     * Constructor. This object indexes all types, fields, and other attributes of a specification file.
     * 
     * @param skillFile
     *            the skillfile dedicated to saving the attributes of the specification.
     * @param file
     *            the file to be indexed.
     */
    public SkillIndexListener(SkillFile skillFile, File file, CleanUpAssistant cleanUpAssistant) {
        super();
        this.origTypes = new HashMap<>();
        skillFile.Types().stream().filter(t -> t.getOrig() == null).forEach(t -> origTypes.put(t.getName().toLowerCase(), t));
        this.cleanUpAssistant = cleanUpAssistant;
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
            if (new File(f.getPath()).getAbsolutePath().equals(file.getAbsolutePath())) {
                if (Arrays.equals(f.getMd5().getBytes(), bytes)
                        && f.getTimestamp().equals(String.valueOf(file.lastModified()))) {
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
     * 
     * @param ctx
     *            the content of the parsed file tokenized.
     */
    @Override
    public void exitFile(SKilLParser.FileContext ctx) {
        for (SKilLParser.DeclarationContext context : ctx.declaration()) {
            processDeclaration(context);
        }
        skillFile.flush();
    }

    /**
     * method for processing a typedeclaration.
     * 
     * @param declarationContext
     *            the content of the typedeclaration.
     */
    private void processDeclaration(SKilLParser.DeclarationContext declarationContext) {
        if (declarationContext.interfacetype() != null) {
            reindex(declarationContext.interfacetype(), cleanUpAssistant.findType(declarationContext.interfacetype().name.getText()));
        } else if (declarationContext.usertype() != null) {
            reindex(declarationContext.usertype(), cleanUpAssistant.findType(declarationContext.usertype().name.getText()));
        } else if (declarationContext.enumtype() != null) {
            reindex(declarationContext.enumtype(), cleanUpAssistant.findType(declarationContext.enumtype().name.getText()));
        } else if (declarationContext.typedef() != null) {
            reindex(declarationContext.typedef(), cleanUpAssistant.findType(declarationContext.typedef().name.getText()));
        }
    }

    private void reindex(SKilLParser.InterfacetypeContext interfacetype, Type type) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> extend = new ArrayList<>();
        Type newType;

        for (SKilLParser.FieldContext fieldContext : interfacetype.field()) {
            Field field;
            if (fieldContext.constant() != null) {
                field = indexFieldConstant(fieldContext);
            } else if (fieldContext.data() != null) {
                field = indexFieldData(fieldContext);
            } else {
                field = indexField(fieldContext.view(), fieldContext);
            }
            fields.add(field);
        }

        String comment;
        if (interfacetype.COMMENT() != null) {
            comment = interfacetype.COMMENT().getText();
        } else {
            comment = "";
        }

        newType = skillFile.Types().make(comment, extend, fields, file, "interface " + interfacetype.name.getText(), null, restrictions, hints);

        for (Field field : fields) {
            field.setType(newType);
        }
        for (Hint hint : hints) {
            hint.setParent(newType);
        }

        cleanUpAssistant.foundType(type, newType);
    }

    private void reindex(SKilLParser.TypedefContext typedef, Type type) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> extend = new ArrayList<>();
        Type newType;

        for (SKilLParser.RestrictionHintContext restrictionHintContext : typedef.restrictionHint()) {
            if (restrictionHintContext.restriction() == null) {
                SKilLParser.HintContext hintContext = restrictionHintContext.hint();
                hints.add(skillFile.Hints().make(hintContext.getText(), null));
            } else {
                restrictions.add(restrictionHintContext.restriction().getText());
            }
        }

        String comment;
        if (typedef.COMMENT() != null) {
            comment = typedef.COMMENT().getText();
        } else {
            comment = "";
        }

        extend.add(typedef.type().getText());
        newType = skillFile.Types().make(comment, extend, fields, file, "typedef " + typedef.name.getText() + " %s",
                null, restrictions, hints);

        for (Field field : fields) {
            field.setType(newType);
        }
        for (Hint hint : hints) {
            hint.setParent(newType);
        }

        cleanUpAssistant.foundType(type, newType);
    }

    private void reindex(SKilLParser.EnumtypeContext enumtype, Type type) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> extend = new ArrayList<>();
        Type newType;

        Field enumValues = skillFile.Fields().make("", enumtype.enumvalues().getText(), null, new ArrayList<>(), null, new ArrayList<>());
        fields.add(enumValues);

        for (SKilLParser.FieldContext fieldContext : enumtype.field()) {
            Field field;
            if (fieldContext.constant() != null) {
                field = indexFieldConstant(fieldContext);
            } else if (fieldContext.data() != null) {
                field = indexFieldData(fieldContext);
            } else {
                field = indexField(fieldContext.view(), fieldContext);
            }
            fields.add(field);
        }
        String comment;
        if (enumtype.COMMENT() != null) {
            comment = enumtype.COMMENT().getText();
        } else {
            comment = "";
        }
        newType = skillFile.Types().make(comment, extend, fields, file, "enum " + enumtype.name.getText(), null, restrictions, hints);
        for (Field field : fields) {
            field.setType(newType);
        }
        for (Hint hint : hints) {
            hint.setParent(newType);
        }
        cleanUpAssistant.foundType(type, newType);
    }

    private void reindex(SKilLParser.UsertypeContext usertypeContext, Type type) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<String> extend = new ArrayList<>();
        Type newType;

        for (SKilLParser.RestrictionHintContext restrictionHintContext : usertypeContext.description().restrictionHint()) {
            if (restrictionHintContext.restriction() == null) {
                SKilLParser.HintContext hintContext = restrictionHintContext.hint();
                hints.add(skillFile.Hints().make(hintContext.getText(), null));
            } else {
                restrictions.add(restrictionHintContext.restriction().getText());
            }
        }

        for (SKilLParser.ExtensionContext extensionContext : usertypeContext.extension()) {
            extend.add(extensionContext.Identifier().getText());
        }

        for (SKilLParser.FieldContext fieldContext : usertypeContext.field()) {
            Field field;
            if (fieldContext.constant() != null) {
                field = indexFieldConstant(fieldContext);
            } else if (fieldContext.data() != null) {
                field = indexFieldData(fieldContext);
            } else {
                field = indexField(fieldContext.view(), fieldContext);
            }
            fields.add(field);
        }
        String comment;
        if (usertypeContext.description().COMMENT() != null) {
            comment = usertypeContext.description().COMMENT().getText();
        } else {
            comment = "";
        }
        newType = skillFile.Types().make(comment, extend, fields, file, usertypeContext.name.getText(), null, restrictions, hints);
        for (Field field : fields) {
            field.setType(newType);
        }
        for (Hint hint : hints) {
            hint.setParent(newType);
        }
        cleanUpAssistant.foundType(type, newType);
    }

    private Field indexFieldConstant(SKilLParser.FieldContext fieldContext) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();

        for (SKilLParser.RestrictionHintContext restrictionHintContext : fieldContext.description().restrictionHint()) {
            if (restrictionHintContext.hint() == null) {
                restrictions.add(restrictionHintContext.restriction().getText());
            } else {
                hints.add(skillFile.Hints().make(restrictionHintContext.hint().getText(), null));
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("const ");
        builder.append(fieldContext.constant().type().getText());
        builder.append(' ');
        builder.append(fieldContext.constant().extendedIdentifer());
        builder.append(" = ");
        builder.append(fieldContext.constant().IntegerConstant().getText());
        String name = builder.toString();

        Field field = skillFile.Fields().make("", name, null, restrictions, null, hints);
        for (Hint hint : field.getHints()) {
            hint.setParent(field);
        }
        return field;
    }

    private Field indexFieldData(SKilLParser.FieldContext fieldContext) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();

        for (SKilLParser.RestrictionHintContext restrictionHintContext : fieldContext.description().restrictionHint()) {
            if (restrictionHintContext.hint() == null) {
                restrictions.add(restrictionHintContext.restriction().getText());
            } else {
                hints.add(skillFile.Hints().make(restrictionHintContext.hint().getText(), null));
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(fieldContext.data().AUTO() == null ? "" : (fieldContext.data().AUTO() + " "));
        builder.append(fieldContext.data().type().getText());
        builder.append(' ');
        builder.append(fieldContext.data().extendedIdentifer().getText());
        String name = builder.toString();

        Field field = skillFile.Fields().make("", name, null, restrictions, null, hints);
        for (Hint hint : field.getHints()) {
            hint.setParent(field);
        }
        return field;
    }

    private Field indexField(SKilLParser.ViewContext viewContext, SKilLParser.FieldContext fieldContext) {
        ArrayList<Hint> hints = new ArrayList<>();
        ArrayList<String> restrictions = new ArrayList<>();

        for (SKilLParser.RestrictionHintContext restrictionHintContext : fieldContext.description().restrictionHint()) {
            if (restrictionHintContext.hint() == null) {
                restrictions.add(restrictionHintContext.restriction().getText());
            } else {
                hints.add(skillFile.Hints().make(restrictionHintContext.hint().getText(), null));
            }
        }

        StringBuilder name = new StringBuilder();
        name.append("view ");
        for (SKilLParser.ExtendedIdentiferContext ex : viewContext.extendedIdentifer()) {
            name.append(ex.getText());
            name.append('.');
        }
        name.setLength(name.length() - 1);

        name.append(" as ");
        name.append(viewContext.data().type().getText());
        name.append(' ');
        name.append(viewContext.data().extendedIdentifer().getText());
        Field field = skillFile.Fields().make("", name.toString(), null, restrictions, null, hints);
        for (Hint hint : field.getHints()) {
            hint.setParent(field);
        }
        return field;
    }
}
