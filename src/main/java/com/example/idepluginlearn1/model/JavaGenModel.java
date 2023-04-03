package com.example.idepluginlearn1.model;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;

import java.util.List;

public class JavaGenModel {
    private List<String> imports;
    private String fileDocument;
    private List<JavaGenField> fields;
    private String packageName;

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }


    public static class JavaGenField {
        private String fullName;
        private String document;

        private PsiField psiField;


        public String getFullName() {
            return fullName;
        }

        public void setFullName(String name) {
            this.fullName = name;
        }

        public String getDocument() {
            return document;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public PsiField getPsiField() {
            return psiField;
        }

        public void setPsiField(PsiField psiField) {
            this.psiField = psiField;
        }
    }

    public String getFileDocument() {
        return fileDocument;
    }

    public void setFileDocument(String fileDocument) {
        this.fileDocument = fileDocument;
    }

    public List<JavaGenField> getFields() {
        return fields;
    }

    public void setFields(List<JavaGenField> fields) {
        this.fields = fields;
    }
}
