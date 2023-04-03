package com.github.hxj0x;

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
    }
}
