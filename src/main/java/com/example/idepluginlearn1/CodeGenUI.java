package com.example.idepluginlearn1;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CodeGenUI implements Configurable {

    private CodeGen codeGen = new CodeGen();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "getDisplayName";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return codeGen.getPanel1();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        System.out.println("CodeGenUI.apply");
    }
}
