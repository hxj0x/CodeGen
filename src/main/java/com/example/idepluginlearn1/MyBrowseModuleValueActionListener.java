package com.example.idepluginlearn1;

import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.configuration.BrowseModuleValueActionListener;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.Nullable;

public class MyBrowseModuleValueActionListener extends BrowseModuleValueActionListener {
    protected MyBrowseModuleValueActionListener(Project project) {
        super(project);
    }

    @Override
    protected @Nullable String showDialog() {
        final PackageChooserDialog dialog = new PackageChooserDialog(ExecutionBundle.message("choose.package.dialog.title"), getProject());
        dialog.show();
        final PsiPackage aPackage = dialog.getSelectedPackage();
        return aPackage != null ? aPackage.getQualifiedName() : null;
    }
}
