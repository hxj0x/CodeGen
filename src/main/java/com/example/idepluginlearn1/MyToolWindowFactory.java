package com.example.idepluginlearn1;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.*;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MyToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {


        System.out.println("MyToolWindowFactory.createToolWindowContent");

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        BorderLayoutPanel borderLayoutPanel = new BorderLayoutPanel().withMinimumWidth(580);

        EditorTextFieldWithBrowseButton justPackage = justPackage(project);
        borderLayoutPanel.addToTop(justPackage);

        // 树
        ToolbarDecorator decorator = getToolbarDecorator();
        borderLayoutPanel.addToCenter(decorator.createPanel());
        Content content = contentFactory.createContent(borderLayoutPanel, "", false);
        toolWindow.getContentManager().addContent(content);


        // JBUI.Panels.simplePanel(ScrollPaneFactory.createScrollPane(myTree))

        // JButton helpButton = PackageChooserDialog.createHelpButton(new DialogWrapper.HelpAction(() -> {
        //     System.out.println("MyToolWindowFactory.createToolWindowContent");
        // }));


        // FileEditor
        // VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
        // PsiFile psiFile = PsiManager.getInstance(project).findFile(selectedFiles[0]);
        // if (psiFile instanceof PsiJavaFile) {
        //     PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        //     PsiClass aClass = psiJavaFile.getClasses()[0];
        //     PsiDocComment docComment = aClass.getDocComment();
        // }

        // System.out.println("selectedFiles = " + selectedFiles);

        // PsiFile file = PsiManager.getInstance(project).findFile(project.getProjectFile().getCanonicalFile());
        // System.out.println("file = " + file);

        // PsiDocumentManager.getInstance(project).getPsiFile(project.getProjectFile())

        // PsiDocumentManager.getInstance(project)


        // 一个组件

        // PackageSetChooserCombo packageSetChooserCombo = new PackageSetChooserCombo(project, "");

        // EditorTextField myPackageTextField = new EditorTextField();
        //
        // MyBrowseModuleValueActionListener actionListener = new MyBrowseModuleValueActionListener(project);
        //

        // JButton btn = new JButton("btn");
        // btn.addActionListener(new AbstractAction() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         PackageChooserDialog chooserDialog = new PackageChooserDialog("选择包位置", project);
        //         chooserDialog.selectPackage("");
        //         chooserDialog.show();
        //
        //         PsiPackage aPackage = chooserDialog.getSelectedPackage();
        //         if (aPackage != null) {
        //             myPackageTextField.setText(aPackage.getQualifiedName());
        //         }
        //     }
        // });
        // PackageNameReferenceEditorCombo component = new PackageNameReferenceEditorCombo("", project, null, "ss");

        // // 包选择器
        // LabeledComponent<EditorTextFieldWithBrowseButton> myPackage = new LabeledComponent<>();
        // EditorTextFieldWithBrowseButton component = new EditorTextFieldWithBrowseButton(project, false);
        // component.addActionListener(e -> {
        //     PackageChooserDialog chooserDialog = new PackageChooserDialog("选择包位置", project);
        //     chooserDialog.selectPackage("");
        //     chooserDialog.show();
        //     PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
        //     if (selectedPackage != null) {
        //         component.setText(selectedPackage.getQualifiedName());
        //         myPackage.setText(selectedPackage.getQualifiedName());
        //     }
        // });
        // myPackage.setComponent(component);
        //
        //
        // // 树
        // // CheckboxTree myTree = new CheckboxTree();
        //
        // final CheckedTreeNode root = new CheckedTreeNode();
        // List<CheckedTreeNode> nodes = new ArrayList<>();
        // nodes.add(new CheckedTreeNode(Pair.create("first1", "second1")));
        // nodes.add(new CheckedTreeNode(Pair.create("first2", "second2")));
        // nodes.add(new CheckedTreeNode(Pair.create("first3", "second3")));
        // nodes.add(new CheckedTreeNode(Pair.create("first4", "second4")));
        //
        // TreeUtil.addChildrenTo(root, nodes);
        //
        // CheckboxTree myTree = new CheckboxTree(new CheckboxTree.CheckboxTreeCellRenderer(true, false) {
        //     @Override
        //     public void customizeRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        //         if (!(value instanceof CheckedTreeNode)) {
        //             return;
        //         }
        //         Object userObject = ((CheckedTreeNode) value).getUserObject();
        //         if (!(userObject instanceof Pair)) return;
        //         ColoredTreeCellRenderer renderer = getTextRenderer();
        //         renderer.setIcon(AllIcons.General.ExclMark);
        //         Pair<?, ?> pair = (Pair<?, ?>) userObject;
        //         renderer.setToolTipText(pair.getSecond().toString());
        //         setToolTipText((String) pair.second);
        //     }
        // }, root);
        //
        // TreeUtil.expand(myTree, 1);
        //
        // ToolbarDecorator decorator = ToolbarDecorator.createDecorator(myTree)
        //         .setToolbarPosition(ActionToolbarPosition.BOTTOM);
        //
        //
        // // JPanel jPanel = new JPanel();
        // // // jPanel.add(myPackage);
        // // jPanel.add(decorator.createPanel());
        // BorderLayoutPanel layoutPanel = JBUI.Panels.simplePanel();
        // myPackage.setMaximumSize(new Dimension(500, 500));
        // layoutPanel.add(myTree, BorderLayout.WEST);
        // layoutPanel.add(myPackage, BorderLayout.EAST);
        //
        //
        // ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        // JPanel treePanel = decorator.createPanel();
        // treePanel.add(myTree);
        // Content content = contentFactory.createContent(layoutPanel, "", false);
        // toolWindow.getContentManager().addContent(content);
        //
        // System.out.println("MyToolWindowFactory.createToolWindowContent");
    }

    @NotNull
    public static ToolbarDecorator getToolbarDecorator() {
        final CheckedTreeNode root = new CheckedTreeNode();
        List<CheckedTreeNode> nodes = new ArrayList<>();
        nodes.add(new CheckedTreeNode(Pair.create("first1", "second1")));
        nodes.add(new CheckedTreeNode(Pair.create("first2", "second2")));
        nodes.add(new CheckedTreeNode(Pair.create("first3", "second3")));
        nodes.add(new CheckedTreeNode(Pair.create("first4", "second4")));

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
                renderer.append((String) pair.first).append("(").append("字段注释说明").append(")");
                renderer.setIcon(AllIcons.Nodes.Field);
                renderer.setToolTipText(pair.getSecond().toString());
                renderer.setName((String) pair.first);
                setToolTipText((String) pair.second);
            }
        }, root);

        TreeUtil.expand(myTree, 1);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(myTree)
                .setToolbarPosition(ActionToolbarPosition.BOTTOM);


        return decorator;
    }

    public static EditorTextFieldWithBrowseButton justPackage(@NotNull Project project) {
        EditorTextFieldWithBrowseButton component = new EditorTextFieldWithBrowseButton(project, false);
        PackageChooserDialog chooserDialog = new PackageChooserDialog("选择包位置", project);
        component.addActionListener(e -> {
            chooserDialog.show();
            final PsiPackage aPackage = chooserDialog.getSelectedPackage();
            component.setText(aPackage != null ? aPackage.getQualifiedName() : "");
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
    private static LabeledComponent<EditorTextFieldWithBrowseButton> getMyPackage(@NotNull Project project) {
        // 包选择器
        LabeledComponent<EditorTextFieldWithBrowseButton> myPackage = new LabeledComponent<>();
        EditorTextFieldWithBrowseButton component = new EditorTextFieldWithBrowseButton(project, false);
        component.addActionListener(e -> {
            PackageChooserDialog chooserDialog = new PackageChooserDialog("选择包位置", project);
            chooserDialog.selectPackage("");
            chooserDialog.show();
            PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
            if (selectedPackage != null) {
                component.setText(selectedPackage.getQualifiedName());
                myPackage.setText(selectedPackage.getQualifiedName());
            }
        });
        myPackage.setComponent(component);
        return myPackage;
    }


}
