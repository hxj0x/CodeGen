package com.example.idepluginlearn1;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.codeStyle.CodeStyleManager;

import javax.swing.border.Border;

public class MainToolBarDemo extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {



        // Border defaultBorder = PackageChooserDialog.createDefaultBorder();
        // System.out.println("defaultBorder = " + defaultBorder);
        //
        // // TODO: insert action logic here
        // System.out.println("e = " + e);
        // 格式化
        // CodeStyleManager.getInstance(e.getProject()).reformat()
    }
}
