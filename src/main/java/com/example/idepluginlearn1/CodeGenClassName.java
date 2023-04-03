package com.example.idepluginlearn1;

import com.example.idepluginlearn1._0331.GenDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;

import java.util.logging.Logger;

public class CodeGenClassName extends AnAction {

    private static final Logger log = Logger.getLogger(CodeGenClassName.class.getName());


    @Override
    public void actionPerformed(AnActionEvent e) {

        PsiFile psiFile = e.getDataContext().getData(CommonDataKeys.PSI_FILE);

        if (e.getProject() == null) {
            log.warning("没有找到项目 e.getProject() == null");
            return;
        }

        if (!(psiFile instanceof PsiJavaFile)) {
            log.warning("没有找到要生成源java文件");
            return;
        }

        GenDialog genDialog = new GenDialog(e.getProject(), ((PsiJavaFile) psiFile));

        if (genDialog.showAndGet()) {
            genDialog.doGenerate();
        }

        // // 从 AnActionEvent 上下文获取 PSI 元素
        // PsiFile psiFile = e.getDataContext().getData(CommonDataKeys.PSI_FILE);
        // if (psiFile == null) return;
        //
        // Project project = e.getProject();
        // if (project == null) return;
        //
        // Module[] modules = ModuleManager.getInstance(project).getModules();
        // if (modules.length == 0) return;
        //
        // // TODO 多模块项目需要修改吗
        // PsiDirectory psiDirectory = PackageUtil.findOrCreateDirectoryForPackage(modules[0], "org.test.a.b.c", null, true);
        // if (psiDirectory == null) return;
        // JavaDirectoryService.getInstance().createClass(psiDirectory, "MyGenClass2");

        // if (psiFile instanceof PsiJavaFile) {
        //     PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        //     PsiDirectory psiDirectory = psiFile.getContainingDirectory();
        //     new SampleDialogWrapper(project, psiDirectory, null);
        // }

        // VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
        // if (selectedFiles.length == 0) return;
        // PsiManager psiManager = PsiManager.getInstance(project);
        // JavaGenModel javaGenModel = null;
        // for (VirtualFile selectedFile : selectedFiles) {
        //     PsiFile i_psiFile = psiManager.findFile(selectedFile);
        //     if (i_psiFile instanceof PsiJavaFile) {
        //         PsiJavaFile psiJavaFile = (PsiJavaFile) i_psiFile;
        //         javaGenModel = new JavaGenModel();
        //         PsiClass aClass = psiJavaFile.getClasses()[0];
        //         PsiDocComment docComment = aClass.getDocComment();
        //         if (docComment != null) {
        //             javaGenModel.setFileDocument(docComment.getText());
        //         }
        //         javaGenModel.setPackageName(psiJavaFile.getPackageName());
        //         ArrayList<JavaGenModel.JavaGenField> fields = new ArrayList<>();
        //         for (PsiField field : aClass.getAllFields()) {
        //             JavaGenModel.JavaGenField javaGenField = new JavaGenModel.JavaGenField();
        //             javaGenField.setPsiField(field);
        //             javaGenField.setFullName(field.getText());
        //             // doc
        //             if (javaGenField.getDocument() == null) {
        //                 PsiDocComment comment = field.getDocComment();
        //                 if (comment != null) {
        //                     javaGenField.setDocument(comment.getText());
        //                 }
        //             }
        //
        //             // swagger2
        //             if (field.getDocComment() == null) {
        //                 PsiAnnotation annotation = field.getAnnotation("io.swagger.annotations.ApiModelProperty");
        //                 if (annotation != null) {
        //                     PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        //                     if (value != null) {
        //                         javaGenField.setDocument(value.getText());
        //                     }
        //                 }
        //             }
        //
        //             fields.add(javaGenField);
        //         }
        //         javaGenModel.setFields(fields);
        //     }
        // }
        //
        // SampleDialogWrapper sampleDialogWrapper = new SampleDialogWrapper(project, psiFile.getContainingDirectory(), javaGenModel);
        // if (sampleDialogWrapper.showAndGet()) {
        //     // 获取选择的字段
        //
        //     sampleDialogWrapper.doGenerate();
        //     System.out.println("CodeGenClassName.actionPerformed");
        // }


        // Border defaultBorder = PackageChooserDialog.createDefaultBorder();
        // System.out.println("defaultBorder = " + defaultBorder);
        //
        // Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        // ShowSettingsUtil.getInstance().editConfigurable(project, new CodeGenUI());
        // // Disposer.register();
        // // EditorFactory.getInstance().createDocument()
        // // PsiDirectoryFactory.getInstance().createDirectory()
        // // TODO: insert action logic here
        // PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        // if (psiFile instanceof PsiJavaFile) {
        //     PsiClass[] classes = ((PsiJavaFile) psiFile).getClasses();
        //     PsiDocComment docComment = classes[0].getDocComment();
        //     System.out.println("docComment = " + docComment);
        // }
        // System.out.println(e);
    }
}
