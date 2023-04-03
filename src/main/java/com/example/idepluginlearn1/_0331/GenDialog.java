package com.example.idepluginlearn1._0331;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.ui.*;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class GenDialog extends DialogWrapper {

    private final Project project;
    private final PsiJavaFile psiJavaFile;
    private final List<CheckedTreeNode> nodes = new ArrayList<>();
    private EditorTextFieldWithBrowseButton pkgChooseBtn;
    private JBTextField textField;

    public GenDialog(@Nullable Project project, PsiJavaFile psiJavaFile) {
        super(project, true);
        this.project = project;
        this.psiJavaFile = psiJavaFile;
        setTitle("Generate POJO");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        BorderLayoutPanel borderLayoutPanel = new BorderLayoutPanel().withMinimumWidth(580);

        JPanel topPanel = new JPanel();

        GridLayout gridLayout = new GridLayout(2, 1, 5, 5);
        topPanel.setLayout(gridLayout);
        LabeledComponent<EditorTextFieldWithBrowseButton> pkgChooseBtn = new LabeledComponent<>();
        pkgChooseBtn.add(createPackageChooseButton());
        pkgChooseBtn.setText("Choose package");
        topPanel.add(pkgChooseBtn);

        LabeledComponent<JBTextField> labeledComponent = new LabeledComponent<>();
        labeledComponent.setText("Class name");
        textField = new JBTextField(psiJavaFile.getClasses()[0].getName());
        labeledComponent.setComponent(textField);
        topPanel.add(labeledComponent);

        borderLayoutPanel.addToTop(topPanel);

        borderLayoutPanel.addToCenter(createFieldsSelectorPanel());
        return borderLayoutPanel;
    }

    @NotNull
    private EditorTextFieldWithBrowseButton createPackageChooseButton() {
        pkgChooseBtn = new EditorTextFieldWithBrowseButton(project, false);
        PackageChooserDialog chooserDialog = new PackageChooserDialog("Choose Package", project);
        pkgChooseBtn.setText(psiJavaFile.getPackageName());
        pkgChooseBtn.addActionListener(e -> {
            chooserDialog.show();
            final PsiPackage aPackage = chooserDialog.getSelectedPackage();
            if (aPackage != null) {
                pkgChooseBtn.setText(aPackage.getQualifiedName());
            }
        });
        return pkgChooseBtn;
    }

    public void doGenerate() {

        Module[] modules = ModuleManager.getInstance(project).getModules();
        if (modules.length == 0) return;


        String packageName = pkgChooseBtn.getText();
        PsiDirectory psiDirectory = PackageUtil.findOrCreateDirectoryForPackage(modules[0], packageName, null, true);
        if (psiDirectory == null) return;

        String className = textField.getText();

        WriteCommandAction.runWriteCommandAction(project, "Create Java File", "psi", () -> {
            JavaDirectoryService.getInstance().checkCreateClass(psiDirectory, className);
            PsiClass destClass = JavaDirectoryService.getInstance().createClass(psiDirectory, className);
            PsiClass srcClass = psiJavaFile.getClasses()[0];
            // 添加 lombok 注解
            PsiModifierList psiModifierList = Objects.requireNonNull(destClass.getModifierList());
            PsiAnnotation annotation = psiModifierList.addAnnotation("lombok.Data");
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(annotation);

            // 把原先类的字段复制到当前类的字段上，跳过没有选择的类
            Set<Integer> checkedIndexList = nodes.stream().filter(CheckedTreeNode::isChecked)
                    .map(e -> ((Pair<Integer, PsiField>) e.getUserObject()).getFirst()).collect(Collectors.toSet());
            PsiField[] srcFields = srcClass.getAllFields();

            for (int i = 0, srcFieldsLength = srcFields.length; i < srcFieldsLength; i++) {
                if (checkedIndexList.contains(i)) {
                    PsiField srcField = srcFields[i];
                    destClass.add(srcField);
                    // TODO 怎么添加空行
                }
            }
        });

        WriteCommandAction.runWriteCommandAction(project, "Update Java File", "psi", () -> {
            PsiClass[] classes = JavaDirectoryService.getInstance().getClasses(psiDirectory);
            PsiClass targetClass = null;
            for (PsiClass aClass : classes) {
                if (Objects.equals(aClass.getName(), className)) {
                    targetClass = aClass;
                    break;
                }
            }
            if (targetClass == null) return;
            PsiField[] targetFields = targetClass.getAllFields();
            for (PsiField targetField : targetFields) {
                for (PsiAnnotation annotation : targetField.getAnnotations()) {
                    // 保留 ApiModelProperty 注解
                    if ("io.swagger.annotations.ApiModelProperty".equals(annotation.getQualifiedName())) {
                        break;
                    }
                    annotation.delete();
                }
            }

            Map<Integer, PsiField> index2PsiField = nodes.stream().filter(CheckedTreeNode::isChecked)
                    .map(e -> (Pair<Integer, PsiField>) e.getUserObject())
                    .collect(Collectors.toMap(e -> e.getFirst(), e -> e.getSecond()));

            // 如果字段没有 ApiModelProperty 注解，提取注释中的
            for (PsiField targetField : targetFields) {
                if (Arrays.stream(targetField.getAnnotations())
                        .noneMatch(an -> "io.swagger.annotations.ApiModelProperty".equals(an.getQualifiedName()))) {

                    PsiDocComment docComment = targetField.getDocComment();
                    String annotationText = "@ApiModelProperty";
                    if (docComment != null) {
                        String comment = Arrays.stream(docComment.getDescriptionElements())
                                .filter(ele -> ele instanceof PsiDocTokenImpl)
                                .map(ele -> (PsiDocTokenImpl) ele)
                                .map(LeafElement::getText)
                                .collect(Collectors.joining(" "));
                        annotationText += "(\"" + comment + "\")";
                    }
                    PsiAnnotation annotation = PsiElementFactory.getInstance(project)
                            .createAnnotationFromText(annotationText, targetField);
                    targetField.addAfter(annotation, targetField.getDocComment());
                    JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(project);
                    codeStyleManager.shortenClassReferences(annotation);
                }
            }
            // TODO import annotations
            // 格式化
            new ReformatCodeProcessor(targetClass.getContainingFile(), false).run();
            // 优化 import 报错？
            // new OptimizeImportsProcessor(project, targetClass.getContainingFile()).run();
        });
    }

    private Component createFieldsSelectorPanel() {
        PsiField[] allFields = psiJavaFile.getClasses()[0].getAllFields();

        for (int i = 0; i < allFields.length; i++) {
            PsiField field = allFields[i];
            if (field.hasModifierProperty(PsiModifier.STATIC)) continue;
            nodes.add(new CheckedTreeNode(Pair.create(i, field)));
        }

        final CheckedTreeNode root = new CheckedTreeNode();
        TreeUtil.addChildrenTo(root, nodes);

        CheckboxTree myTree = new CheckboxTree(new CheckboxTree.CheckboxTreeCellRenderer(true, false) {
            @Override
            public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (!(value instanceof CheckedTreeNode)) {
                    return;
                }
                Object userObject = ((CheckedTreeNode) value).getUserObject();
                if (!(userObject instanceof Pair)) return;
                ColoredTreeCellRenderer renderer = getTextRenderer();
                Pair<?, ?> pair = (Pair<?, ?>) userObject;
                PsiField field = (PsiField) pair.second;
                renderer.append(field.getName());
                PsiDocComment docComment = field.getDocComment();
                if (docComment != null) {
                    String comment = Arrays.stream(docComment.getDescriptionElements())
                            .filter(ele -> ele instanceof PsiDocTokenImpl)
                            .map(ele -> (PsiDocTokenImpl) ele)
                            .map(LeafElement::getText)
                            .collect(Collectors.joining(" "));

                    renderer.append("(").append(comment).append(")");
                }
                renderer.setIcon(AllIcons.Nodes.Field);
            }
        }, root);

        TreeUtil.expand(myTree, 1);

        // TODO selectAll unselectAll
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(myTree)
                .setToolbarPosition(ActionToolbarPosition.BOTTOM);

        return decorator.createPanel();
    }

}
