package com.fufuffu.gamesaver.repository.resource.drive;

import com.fufuffu.gamesaver.main.GoogleDriveTest;
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
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import java.io.ByteArrayOutputStream;

public class GoogleDriveFileResourceImpl implements GoogleDriveFileResource {
    private final String APPLICATION_NAME = "Game config saver";

    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final String TOKENS_DIRECTORY_PATH = "tokens";

    private final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE_FILE);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private final Drive service;

    public GoogleDriveFileResourceImpl() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String createFile(String fileName, String parentFolder, java.io.File fileContent) throws IOException {
        String foundFileId = null;
        try {
            foundFileId = getFileIdIfExists(parentFolder, fileName);
        } catch (IOException e) {
            // Do nothing, expected if not found
        }

        if (foundFileId != null) {
            deleteFile(foundFileId);
        }

        // Create the dile
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(parentFolder));
        FileContent mediaContent = new FileContent("application/octet-stream", fileContent);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    public boolean deleteFile(String filePath) throws IOException {
        try {
            service.files().delete(filePath).execute();
            return true;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to delete file: " + e.getDetails());
            throw e;
        }
    }

    public String getFileIdIfExists(String parentFolder, String fileName) throws IOException {
        Drive.Files.List request = service.files().list().setQ(
                "'" + parentFolder + "' in parents and trashed=false and name='" + fileName + "'");
        FileList files = request.execute();

        if (!files.getFiles().isEmpty()) {
            return files.getFiles().get(0).getId();
        }

        throw new FileNotFoundException("Could not find " + fileName + " in Drive");
    }

    public ByteArrayOutputStream getFileContent(String filePath) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            service.files().get(filePath)
                    .executeMediaAndDownloadTo(outputStream);

            return outputStream;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to download file: " + e.getDetails());
            throw e;
        }
    }

    public String makeDirectoryIfNotExists(String dirName, String parent) throws IOException {
        Drive.Files.List request = service.files().list().setQ(
                "mimeType='application/vnd.google-apps.folder' and trashed=false and name='" + dirName + "'");
        FileList files = request.execute();

        if (files.getFiles().isEmpty()) {
            // File's metadata.
            File fileMetadata = new File();
            fileMetadata.setName(dirName);
            fileMetadata.setParents(Collections.singletonList(parent));
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

        return files.getFiles().get(0).getId();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
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
}
