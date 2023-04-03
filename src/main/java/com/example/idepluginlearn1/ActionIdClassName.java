package com.example.idepluginlearn1;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.border.Border;

public class ActionIdClassName extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Border defaultBorder = PackageChooserDialog.createDefaultBorder();
        System.out.println("defaultBorder = " + defaultBorder);

        // JBPopup popup = JBPopupFactory.getInstance().createConfirmation("Title", () -> {
        //     System.out.println("yes");
        // }, 0);
        // popup.show(new JButton("btn"));
        // popup.showInFocusCenter();
        // // TODO: insert action logic here
        // PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        // if (psiFile instanceof PsiJavaFile) {
        //     PsiClass[] classes = ((PsiJavaFile) psiFile).getClasses();
        //     PsiDocComment docComment = classes[0].getDocComment();
        //     System.out.println("docComment = " + docComment);
        // }
        System.out.println(e);
    }
}
