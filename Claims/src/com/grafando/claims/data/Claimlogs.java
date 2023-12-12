package com.grafando.claims.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Claimlogs {

    private File Logfile;
    private File Logfolder;

    public void createDirectory() {
        Logfolder = new File("C:/Users/Pius/Desktop/Plugin/Server/Plugins/ClaimingLogs");
        if (!Logfolder.exists()) {
            Logfolder.mkdir();
        }
    }

    public void writeClaimFile(String content) {
        byte data[] = content.getBytes();
        Path p = Paths.get("C:/Users/Pius/Desktop/Plugin/Server/Plugins/ClaimingLogs/SoldClaimBlocks.txt");
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
