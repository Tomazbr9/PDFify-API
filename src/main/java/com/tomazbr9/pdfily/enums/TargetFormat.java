package com.tomazbr9.pdfily.enums;

public enum TargetFormat {
    PDF,
    PNG,
    JPG,
    JPEG,
    TXT,
    HTML,
    DOCX,
    XLSX,
    PPTX;

    public static boolean isSupported(String extension){
        try {
            TargetFormat.valueOf(extension.toUpperCase());
            return true;
        } catch (Exception error) {
            return false;
        }
    }

}
