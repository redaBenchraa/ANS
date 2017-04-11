package com.example.reda_benchraa.asn.Model;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class FileRequest {
    LinkedList<SubmitedFile> submittedFiles = new LinkedList<>();

    class SubmitedFile {
        Account account;
        File file;
    }

    public FileRequest() {
    }

    public LinkedList<SubmitedFile> getSubmittedFiles() {
        return submittedFiles;
    }

    public void setSubmittedFiles(LinkedList<SubmitedFile> submittedFiles) {
        this.submittedFiles = submittedFiles;
    }
}
