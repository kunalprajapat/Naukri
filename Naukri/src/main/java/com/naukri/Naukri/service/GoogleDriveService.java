package com.naukri.Naukri.service;


import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class GoogleDriveService {

    public java.io.File downloadFile(String fileUrl, String destinationPath) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream in = conn.getInputStream();
        FileOutputStream out = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();

        return new java.io.File(destinationPath);
    }
}
