package de.unistuttgart.iste.ps.skillls.main;

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
import java.util.stream.Collectors;

import de.unistuttgart.iste.ps.skillls.grammar.SKilLParser;
import de.unistuttgart.iste.ps.skillls.grammar.SKilLParserBaseListener;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;


/**
 * Class for indexing all types, fields, hints and restrictions of a skill specification file
 *
 * @author Armin Hüneburg
 * @since 15.09.15
 */
class SkillIndexListener extends SKilLParserBaseListener {
    private final SkillFile skillFile;
    private de.unistuttgart.iste.ps.skillls.tools.File file;

    /**
     * Constructor. This object indexes all types, fields, and other attributes of a specification file.
     * 
     * @param skillFile
     *            the skillfile dedicated to saving the attributes of the specification.
     * @param file
     *            the file to be indexed.
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
     * 
     * @param typedef
     *            the context of the declaration.
     */
    private void processTypedef(SKilLParser.TypedefContext typedef) {
        String name = "typedef " + typedef.name.getText() + " %s " + typedef.type().getText();

        for (Type type : skillFile.Types()) {
            if (type.getOrig() == null && type.getName().toLowerCase().equals(name.toLowerCase())) {
                reindex(typedef, type);
                return;
            }
        }

        ArrayList<String> restrictions = new ArrayList<>();

        // Iterate over all restrictions of typedef
        for (SKilLParser.RestrictionContext restrictionContext : typedef.restrictionHint().stream()
                .filter(rh -> rh.restriction() != null).map(SKilLParser.RestrictionHintContext::restriction)
                .collect(Collectors.toList())) {
            skillFile.Strings().add(restrictionContext.getText());
            restrictions.add(restrictionContext.getText());
        }

        ArrayList<Hint> hints = new ArrayList<>();

        // Iterate over all hints of typedef
        for (SKilLParser.HintContext hintContext : typedef.restrictionHint().stream().filter(rh -> rh.hint() != null)
                .map(SKilLParser.RestrictionHintContext::hint).collect(Collectors.toList())) {
            Hint hint = skillFile.Hints().make(hintContext.getText(), null);
            skillFile.Strings().add(hintContext.getText());
            hints.add(hint);
        }

        Type type = skillFile.Types().make(typedef.COMMENT() == null ? "" : typedef.COMMENT().getText(), new ArrayList<>(),
                new ArrayList<>(), file, name, null, restrictions, hints);
        for (Hint hint : hints) {
            hint.setParent(type);
        }
    }

    private Hint indexHint(SKilLParser.HintContext hintContext, Type type, ArrayList<Hint> hints) {
        String newHintName = hintContext.getText();
        Hint hint = null;
        for (int i = 0; i < hints.size(); i++) {
            Hint h = hints.get(i);
            if (h.getName().toLowerCase().equals(newHintName.toLowerCase())) {
                hint = h;
                hints.remove(h);
            }
        }
        if (hint == null) {
            hint = skillFile.Hints().make(newHintName, type);
            type.getTypeHints().add(hint);
        }
        return hint;
    }

    /**
     * processes a enumtype typedeclaration.
     * 
     * @param enumtype
     *            the context of the declaration.
     */
    private void processEnumtype(SKilLParser.EnumtypeContext enumtype) {
        String name = "enum " + enumtype.name.getText();

        for (Type type : skillFile.Types()) {
            if (type.getOrig() == null && type.getName().toLowerCase().equals(name.toLowerCase())) {
                reindex(enumtype, type);
                return;
            }
        }

        ArrayList<Field> fields = new ArrayList<>();

        fields.add(skillFile.Fields().make("", new ArrayList<>(), enumtype.enumvalues().getText(), null, new ArrayList<>(), null));

        fields.addAll(enumtype.field().stream().map(this::processField).collect(Collectors.toList()));

        Type type = skillFile.Types().make(enumtype.COMMENT() == null ? "" : enumtype.COMMENT().getText(), new ArrayList<>(),
                fields, file, name, null, new ArrayList<>(), new ArrayList<>());
        for (Field f : fields) {
            f.setType(type);
        }
    }

    /**
     * processes a field in a typedeclaration
     * 
     * @param fieldContext
     *            the context of the declaration.
     * @return the found field.
     */
    private Field processField(SKilLParser.FieldContext fieldContext) {
        // Iterate over all hints
        ArrayList<Hint> hints = fieldContext.description().restrictionHint().stream().filter(rh -> rh.hint() != null)
                .map(SKilLParser.RestrictionHintContext::hint).collect(Collectors.toList()).stream()
                .map(h -> skillFile.Hints().make(h.getText(), null)).collect(Collectors.toCollection(ArrayList::new));

        String name;
        if (fieldContext.data() != null) {
            name = fieldContext.data().type().getText() + " " + fieldContext.data().extendedIdentifer().getText();
        } else if (fieldContext.constant() != null) {
            name = fieldContext.constant().getText();
        } else {
            name = fieldContext.view().data().extendedIdentifer().getText();
        }

        ArrayList<String> restrictions = new ArrayList<>();

        // Iterate over all restrictions
        for (SKilLParser.RestrictionContext r : fieldContext.description().restrictionHint().stream()
                .filter(rh -> rh.restriction() != null).map(SKilLParser.RestrictionHintContext::restriction)
                .collect(Collectors.toList())) {
            skillFile.Strings().add(r.getText());
            restrictions.add(r.getText());
        }

        Field field = skillFile.Fields().make(
                fieldContext.description().COMMENT() == null ? "" : fieldContext.description().COMMENT().getText(), hints,
                name, null, restrictions, null);
        for (Hint h : hints) {
            h.setParent(field);
        }
        return field;
    }

    /**
     * processes a usertype typedeclaration.
     * 
     * @param usertype
     *            the context of the declaration.
     */
    private void processUsertype(SKilLParser.UsertypeContext usertype) {
        String name = usertype.name.getText();

        for (Type type : skillFile.Types()) {
            if (type.getOrig() == null & type.getName().toLowerCase().equals(name.toLowerCase())) {
                reindex(usertype, type);
                return;
            }
        }

        ArrayList<Hint> hints = new ArrayList<>();

        // Iterate over all hints
        for (SKilLParser.HintContext h : usertype.description().restrictionHint().stream().filter(rh -> rh.hint() != null)
                .map(SKilLParser.RestrictionHintContext::hint).collect(Collectors.toList())) {
            hints.add(skillFile.Hints().make(h.getText(), null));
        }

        ArrayList<String> extensions = new ArrayList<>();

        for (SKilLParser.ExtensionContext ex : usertype.extension()) {
            skillFile.Strings().add(ex.Identifier().getSymbol().getText());
            extensions.add(ex.Identifier().getSymbol().getText());
        }

        ArrayList<String> restrictions = new ArrayList<>();

        // Iterate over all restrictions
        for (SKilLParser.RestrictionContext r : usertype.description().restrictionHint().stream()
                .filter(rh -> rh.restriction() != null).map(SKilLParser.RestrictionHintContext::restriction)
                .collect(Collectors.toList())) {
            skillFile.Strings().add(r.getText());
            restrictions.add(r.getText());
        }

        ArrayList<Field> fields = new ArrayList<>();

        for (SKilLParser.FieldContext f : usertype.field()) {
            fields.add(processField(f));
        }

        Type type = skillFile.Types().make(
                usertype.description().COMMENT() == null ? "" : usertype.description().COMMENT().getText(), extensions,
                fields, file, name, null, restrictions, hints);

        for (Field f : fields) {
            f.setType(type);
        }

        for (Hint h : hints) {
            h.setParent(type);
        }
    }

    /**
     * processes an interfacetype typedeclaration.
     * 
     * @param interfacetype
     *            the context of the declaration.
     */
    private void processInterface(SKilLParser.InterfacetypeContext interfacetype) {
        String name = "interface " + interfacetype.name.getText();

        for (Type type : skillFile.Types()) {
            if (type.getName().toLowerCase().equals(name.toLowerCase())) {
                reindex(interfacetype, type);
                return;
            }
        }

        ArrayList<Hint> hints = new ArrayList<>();

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
                extensions, fields, file, name, null, restrictions, hints);

        for (Field f : fields) {
            f.setType(type);
        }

        for (Hint h : hints) {
            h.setParent(type);
        }
    }

    private void reindex(SKilLParser.InterfacetypeContext interfacetype, Type type) {
        for (SKilLParser.FieldContext fieldContext : interfacetype.field()) {
            Field field;
            if (fieldContext.constant() != null) {
                String name = fieldContext.constant().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexFieldConstant(fieldContext);
                    field.setType(type);
                }
            } else if (fieldContext.data() != null) {
                String name = fieldContext.data().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexFieldData(fieldContext);
                    field.setType(type);
                }
            } else {
                String name = fieldContext.view().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexField(fieldContext.view(), fieldContext);
                    field.setType(type);
                }
            }
            if (fieldContext.description().COMMENT() == null) {
                field.setComment("");
            } else {
                field.setComment(fieldContext.description().COMMENT().getText());
            }
        }
        if (interfacetype.COMMENT() != null) {
            type.setComment(interfacetype.COMMENT().getText());
        } else {
            type.setComment("");
        }
    }

    private void reindex(SKilLParser.TypedefContext typedef, Type type) {
        ArrayList<Hint> hints = (ArrayList<Hint>) type.getTypeHints().clone();
        ArrayList<String> restrictions = (ArrayList<String>) type.getRestrictions().clone();

        for (SKilLParser.RestrictionHintContext restrictionHintContext : typedef.restrictionHint()) {
            if (restrictionHintContext.restriction() == null) {
                SKilLParser.HintContext hintContext = restrictionHintContext.hint();
                indexHint(hintContext, type, hints);
            } else {
                boolean found = false;
                String restriction = restrictionHintContext.restriction().getText();
                for (int i = 0; i < restrictions.size(); i++) {
                    if (restrictions.get(i).toLowerCase().equals(restriction.toLowerCase())) {
                        restrictions.remove(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    type.getRestrictions().add(restriction);
                }
            }
        }
        if (typedef.COMMENT() != null) {
            type.setComment(typedef.COMMENT().getText());
        } else {
            type.setComment("");
        }
    }

    private void reindex(SKilLParser.EnumtypeContext enumtype, Type type) {
        Field enumValues = null;
        for (Field field : type.getFields()) {
            if (!field.getName().contains(" ")) {
                enumValues = field;
                break;
            }
        }
        if (enumValues == null) {
            enumValues = skillFile.Fields().make("", new ArrayList<>(), enumtype.enumvalues().getText(), null, new ArrayList<>(), null);
            type.getFields().add(enumValues);
        } else {
            enumValues.setName(enumtype.enumvalues().getText());
        }
        for (SKilLParser.FieldContext fieldContext : enumtype.field()) {
            Field field;
            if (fieldContext.constant() != null) {
                String name = fieldContext.constant().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexFieldConstant(fieldContext);
                    field.setType(type);
                }
            } else if (fieldContext.data() != null) {
                String name = fieldContext.data().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexFieldData(fieldContext);
                    field.setType(type);
                }
            } else {
                String name = fieldContext.view().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexField(fieldContext.view(), fieldContext);
                    field.setType(type);
                }
            }
            if (fieldContext.description().COMMENT() == null) {
                field.setComment("");
            } else {
                field.setComment(fieldContext.description().COMMENT().getText());
            }
        }
        if (enumtype.COMMENT() != null) {
            type.setComment(enumtype.COMMENT().getText());
        } else {
            type.setComment("");
        }
    }

    private void reindex(SKilLParser.UsertypeContext usertypeContext, Type type) {
        ArrayList<Hint> hints = (ArrayList<Hint>) type.getTypeHints().clone();
        ArrayList<String> restrictions = (ArrayList<String>) type.getRestrictions().clone();
        ArrayList<String> extendList = (ArrayList<String>) type.getExtends().clone();

        for (SKilLParser.RestrictionHintContext restrictionHintContext : usertypeContext.description().restrictionHint()) {
            if (restrictionHintContext.restriction() == null) {
                SKilLParser.HintContext hintContext = restrictionHintContext.hint();
                indexHint(hintContext, type, hints);
            } else {
                boolean found = false;
                String restriction = restrictionHintContext.restriction().getText();
                for (int i = 0; i < restrictions.size(); i++) {
                    if (restrictions.get(i).toLowerCase().equals(restriction.toLowerCase())) {
                        restrictions.remove(i);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    type.getRestrictions().add(restriction);
                }
            }
        }

        for (SKilLParser.ExtensionContext extensionContext : usertypeContext.extension()) {
            String newExtend = extensionContext.Identifier().getText();
            for (int i = 0; i < extendList.size(); i++) {
                if (extendList.get(i).toLowerCase().equals(newExtend.toLowerCase())) {
                    extendList.remove(i);
                    break;
                }
            }
            type.getExtends().add(newExtend);
        }

        for (SKilLParser.FieldContext fieldContext : usertypeContext.field()) {
            Field field;
            if (fieldContext.constant() != null) {
                String name = fieldContext.constant().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexFieldConstant(fieldContext);
                    field.setType(type);
                }
            } else if (fieldContext.data() != null) {
                String name = fieldContext.data().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexFieldData(fieldContext);
                    field.setType(type);
                }
            } else {
                String name = fieldContext.view().getText();
                field = findField(type, name);
                if (field == null) {
                    field = indexField(fieldContext.view(), fieldContext);
                    field.setType(type);
                }
            }
            if (fieldContext.description().COMMENT() == null) {
                field.setComment("");
            } else {
                field.setComment(fieldContext.description().COMMENT().getText());
            }
        }
        if (usertypeContext.description().COMMENT() != null) {
            type.setComment(usertypeContext.description().COMMENT().getText());
        } else {
            type.setComment("");
        }
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

        String name = fieldContext.constant().getText();

        Field field = skillFile.Fields().make("", hints, name, null, restrictions, null);
        for (Hint hint : field.getFieldHints()) {
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

        String name = fieldContext.data().getText();

        Field field = skillFile.Fields().make("", hints, name, null, restrictions, null);
        for (Hint hint : field.getFieldHints()) {
            hint.setParent(field);
        }
        return field;
    }

    private Field findField(Type type, String name) {
        for (Field field : type.getFields()) {
            if (field.getName().toLowerCase().equals(name.toLowerCase())) {
                return field;
            }
        }
        return null;
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
        name.append(" view ");
        for (SKilLParser.ExtendedIdentiferContext ex : viewContext.extendedIdentifer()) {
            name.append(ex);
            name.append('.');
        }
        name.setLength(name.length() - 1);

        name.append(" as ");
        name.append(viewContext.data().getText());
        Field field = skillFile.Fields().make("", hints, name.toString(), null, restrictions, null);
        for (Hint hint : field.getFieldHints()) {
            hint.setParent(field);
        }
        return field;
    }
}
