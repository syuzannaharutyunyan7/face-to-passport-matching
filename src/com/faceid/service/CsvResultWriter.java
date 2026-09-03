package com.faceid.service;

import com.faceid.model.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvResultWriter {

    private final PrintWriter writer;

    public CsvResultWriter(
            File csv
    ) throws IOException {

        writer =
                new PrintWriter(
                        new FileWriter(csv)
                );

        writer.println(
                "Folder,ID,Selfie,Similarity,Same_Person"
        );
    }

    public void write(
            Result result
    ) {

        writer.println(
                csvLine(result)
        );

        writer.flush();
    }

    private String csvLine(
            Result r
    ) {

        return r.getFolder() +
                "," +
                r.getId() +
                "," +
                r.getSelfie() +
                "," +
                r.getSimilarity() +
                "," +
                r.getDecision();
    }

    public void close() {

        writer.close();
    }
}

