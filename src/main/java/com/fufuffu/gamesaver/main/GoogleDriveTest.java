package com.fufuffu.gamesaver.main;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/* class to demonstarte use of Drive files list API */
public class GoogleDriveTest {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveTest.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String folderId = createFolderIfNotExists(service, "TestFolder", "root");

        URL fileToUploadURL = GoogleDriveTest.class.getResource("/test.json");
        if (fileToUploadURL != null) {
            java.io.File filePath = new java.io.File(fileToUploadURL.getPath());

            File file = uploadAndReplaceFile(service, folderId, "config.json", filePath);

            ByteArrayOutputStream outputStream = getJsonOutputStream(service, file.getId());

            try (OutputStream outputFile = new FileOutputStream("downloadedLastFile.json")) {
                outputStream.writeTo(outputFile);
            }
        }
    }

    public static void deleteFile(Drive service, String fileId) throws IOException {
        try {
            service.files().delete(fileId).execute();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to delete file: " + e.getDetails());
            throw e;
        }
    }

    public static File uploadAndReplaceFile(Drive service, String folderId, String fileName, java.io.File fileContentPath) throws IOException {
        Drive.Files.List request = service.files().list().setQ(
                "'" + folderId + "' in parents and trashed=false and name='" + fileName + "'");
        FileList files = request.execute();

        if (!files.getFiles().isEmpty()) {
            deleteFile(service, files.getFiles().get(0).getId());
        }

        return uploadJsonFileToFolder(service, folderId, fileName, fileContentPath);
    }

    // https://developers.google.com/drive/api/guides/folder?hl=es-419#create_a_file_in_a_folder
    private static File uploadJsonFileToFolder(Drive service, String driveFolderId, String fileName, java.io.File fileContentPath) throws IOException {
        // File's metadata.
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(driveFolderId));
        FileContent mediaContent = new FileContent("application/json", fileContentPath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    public static ByteArrayOutputStream getJsonOutputStream(Drive service, String realFileId) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            service.files().get(realFileId)
                    .executeMediaAndDownloadTo(outputStream);

            return outputStream;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to download file: " + e.getDetails());
            throw e;
        }
    }

    public static String createFolderIfNotExists(Drive service, String folderName, String parentId) throws IOException {
        Drive.Files.List request = service.files().list().setQ(
                "mimeType='application/vnd.google-apps.folder' and trashed=false and name='" + folderName + "'");
        FileList files = request.execute();

        if (files.getFiles().isEmpty()) {
            return createFolder(service, folderName, parentId);
        }

        return files.getFiles().get(0).getId();
    }

    private static String createFolder(Drive service, String folderName, String parentId) throws IOException {
        // File's metadata.
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setParents(Collections.singletonList(parentId));
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        try {
            File file = service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            System.out.println("Folder ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to create folder: " + e.getDetails());
            throw e;
        }
    }
}