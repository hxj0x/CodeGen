package com.example.idepluginlearn1._0331;

import com.example.idepluginlearn1.model.JavaGenModel;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.impl.file.PsiJavaDirectoryFactory;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.ui.*;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SampleDialogWrapper extends DialogWrapper {

    private final Project project;
    private final PsiDirectory psiDirectory;
    private final JavaGenModel javaGenModel;

    private CheckboxTree myTree;
    private final List<CheckedTreeNode> nodes = new ArrayList<>();

    public SampleDialogWrapper(Project project, PsiDirectory psiDirectory, JavaGenModel javaGenModel) {
        super(project, true); // use current window as parent
        setTitle("Test DialogWrapper");
        this.project = project;
        this.psiDirectory = psiDirectory;
        this.javaGenModel = javaGenModel;
        init();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        BorderLayoutPanel borderLayoutPanel = new BorderLayoutPanel().withMinimumWidth(580);
        EditorTextFieldWithBrowseButton justPackage = justPackage();
        borderLayoutPanel.addToTop(justPackage);

        // 树
        ToolbarDecorator decorator = getToolbarDecorator(javaGenModel);
        borderLayoutPanel.addToCenter(decorator.createPanel());

        return borderLayoutPanel;

        // JPanel dialogPanel = new JPanel(new BorderLayout());
        //
        // JLabel label = new JLabel("testing");
        // label.setPreferredSize(new Dimension(100, 100));
        // dialogPanel.add(label, BorderLayout.CENTER);
        //
        // return dialogPanel;
    }

    public EditorTextFieldWithBrowseButton justPackage() {
        EditorTextFieldWithBrowseButton component = new EditorTextFieldWithBrowseButton(project, false);
        PackageChooserDialog chooserDialog = new PackageChooserDialog("选择包位置", project);
        component.setText(javaGenModel.getPackageName());
        component.addActionListener(e -> {
            chooserDialog.show();
            final PsiPackage aPackage = chooserDialog.getSelectedPackage();
            if (aPackage != null) {
                component.setText(aPackage.getQualifiedName());
            }
        });
        // component.addActionListener(e -> {
        //     PackageChooserDialog chooserDialog = new PackageChooserDialog("选择包位置", project);
        //     chooserDialog.selectPackage("");
        //     PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
        //     if (selectedPackage != null) {
        //         component.setText(selectedPackage.getQualifiedName());
        //     }
        // });
        return component;
    }

    @NotNull
    public ToolbarDecorator getToolbarDecorator(JavaGenModel javaGenModel) {
        final CheckedTreeNode root = new CheckedTreeNode();


        List<JavaGenModel.JavaGenField> fields = javaGenModel.getFields();
        for (int i = 0; i < fields.size(); i++) {
            nodes.add(new CheckedTreeNode(Pair.create(i, fields.get(i))));
        }

        TreeUtil.addChildrenTo(root, nodes);

        myTree = new CheckboxTree(new CheckboxTree.CheckboxTreeCellRenderer(true, false) {
            @Override
            public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (!(value instanceof CheckedTreeNode)) {
                    return;
                }
                Object userObject = ((CheckedTreeNode) value).getUserObject();
                if (!(userObject instanceof Pair)) return;
                ColoredTreeCellRenderer renderer = getTextRenderer();
                Pair<?, ?> pair = (Pair<?, ?>) userObject;
                JavaGenModel.JavaGenField field = (JavaGenModel.JavaGenField) pair.second;
                renderer.append(field.getPsiField().getName());
                PsiDocComment docComment = field.getPsiField().getDocComment();
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

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(myTree)
                .setToolbarPosition(ActionToolbarPosition.BOTTOM);

        return decorator;
    }

    public void doGenerate() {
        System.out.println(myTree);
        List<CheckedTreeNode> checkedTreeNodes = nodes.stream().filter(e -> e.isChecked()).collect(Collectors.toList());
        Set<Integer> checkedIndexs = checkedTreeNodes.stream()
                .map(DefaultMutableTreeNode::getUserObject)
                .map(e -> (Pair<Integer, JavaGenModel.JavaGenField>) e)
                .map(e -> e.getFirst())
                .collect(Collectors.toSet());

        List<JavaGenModel.JavaGenField> filtered = new ArrayList<>();
        for (int i = 0; i < javaGenModel.getFields().size(); i++) {
            if (checkedIndexs.contains(i)) {
                filtered.add(javaGenModel.getFields().get(i));
            }
        }

        javaGenModel.setFields(filtered);

        // TODO 使用 freemarker 进行代码生成

        // TODO 使用 SPI 生成文件

        // 对 Psi 执行非读操作(创建、编辑、删除)，需要用到 WriteCommandAction
        WriteCommandAction.runWriteCommandAction(project, "create.java.file", "psi", () -> {
            try {
                // 检查下是否可以在当前目录下创建指定类名的类
                String className = "MyGenClass1";
                // PsiJavaDirectoryFactory.getInstance(project).createDirectory()
                JavaDirectoryService.getInstance().checkCreateClass(psiDirectory, className);
                PsiClass aClass = JavaDirectoryService.getInstance().createClass(psiDirectory, className);
                // TODO 继续添加内容

            } catch (Exception exception) {
                // 错误通知，右下角小弹框 (Balloon) UI :https://jetbrains.design/intellij/controls/balloon/
                // 可以新建个同类名看下效果
                // NotificationUtils.INSTANCE.showWarning(exception.getMessage(), myProject);
                System.out.println("校验失败");
                return;
            }
        });

        // CodeStyleManager.getInstance().reformatText();
        PsiDirectoryFactory instance = PsiDirectoryFactory.getInstance(project);
        System.out.println("javaGenModel.getFields() = " + javaGenModel.getFields());

        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        // JavaDirectoryService.getInstance().createClass()

        System.out.println("SampleDialogWrapper.doGenerate");
    }
}