package com.faceid.model;

import java.io.File;

import static com.faceid.config.AppConfig.THRESHOLD;

public class Result {

    private final String folder;
    private final String id;
    private final String selfie;
    private final double similarity;
    private final String decision;

    private final File idFile;
    private final File selfieFile;

    public Result(
            String folder,
            String id,
            String selfie,
            double similarity,
            File idFile,
            File selfieFile
    ) {

        this.folder = folder;
        this.id = id;
        this.selfie = selfie;
        this.similarity = similarity;

        this.decision =
                similarity >= THRESHOLD
                        ? "SAME PERSON"
                        : "DIFFERENT";

        this.idFile = idFile;
        this.selfieFile = selfieFile;
    }

    public String getFolder() {
        return folder;
    }

    public String getId() {
        return id;
    }

    public String getSelfie() {
        return selfie;
    }

    public double getSimilarity() {
        return similarity;
    }

    public String getDecision() {
        return decision;
    }

    public File getIdFile() {
        return idFile;
    }

    public File getSelfieFile() {
        return selfieFile;
    }
}

