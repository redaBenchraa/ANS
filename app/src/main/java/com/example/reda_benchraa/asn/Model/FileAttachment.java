package com.example.reda_benchraa.asn.Model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class FileAttachment implements Serializable {
    File file;

    public FileAttachment() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
