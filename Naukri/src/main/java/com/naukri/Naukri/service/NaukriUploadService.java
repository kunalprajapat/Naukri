package com.naukri.Naukri.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class NaukriUploadService {

    @Value("${naukri.upload.url}")
    private String naukriUrl;

    @Value("${naukri.formKey}")
    private String formKey;

    @Value("${naukri.fileKey}")
    private String fileKey;

    @Value("${naukri.resume.fileName}")
    private String resumeFileName;

    @Value("${resume.temp.path}")
    private String tempPath;

    private final GoogleDriveService driveService;

    public NaukriUploadService(GoogleDriveService driveService) {
        this.driveService = driveService;
    }

    // New method: downloads from Drive and uploads
    public void uploadFromDrive(String driveFileUrl) {
        try {
            java.io.File file = driveService.downloadFile(driveFileUrl, tempPath); // <- use the parameter
            uploadResume(file);
            file.delete(); // clean up
        } catch (Exception e) {
            System.out.println("Error downloading/uploading resume: " + e.getMessage());
        }
    }


    private void uploadResume(java.io.File file) {
        try {
            String boundary = "----WebKitFormBoundaryVJkNNMl7OAn521Zj";

            URL url = new URL(naukriUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            // headers
            connection.setRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
            connection.setRequestProperty("appid", "105");
            connection.setRequestProperty("origin", "https://www.naukri.com");
            connection.setRequestProperty("referer", "https://www.naukri.com/");
            connection.setRequestProperty("systemid", "fileupload");
            connection.setRequestProperty("user-agent", "Mozilla/5.0");
            connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

            // formKey
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"formKey\"").append("\r\n\r\n")
                    .append(formKey).append("\r\n");

            // file
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + resumeFileName + "\"")
                    .append("\r\n");
            writer.append("Content-Type: application/pdf\r\n\r\n");
            writer.flush();

            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytes;
            while ((bytes = in.read(buffer)) != -1) {
                output.write(buffer, 0, bytes);
            }
            output.flush();
            in.close();
            writer.append("\r\n");

            // fileName
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"fileName\"").append("\r\n\r\n")
                    .append(resumeFileName).append("\r\n");

            // uploadCallback
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"uploadCallback\"").append("\r\n\r\ntrue\r\n");

            // fileKey
            writer.append("--" + boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"fileKey\"").append("\r\n\r\n")
                    .append(fileKey).append("\r\n");

            // end boundary
            writer.append("--" + boundary + "--").append("\r\n");
            writer.close();

            int responseCode = connection.getResponseCode();
            InputStream responseStream = (responseCode == 200) ? connection.getInputStream() : connection.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            br.close();

            System.out.println("Naukri Response: " + response);

        } catch (Exception e) {
            System.out.println("Upload Error: " + e.getMessage());
        }
    }
}
