package secureCloud;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemovePermission {

	private static final String APPLICATION_NAME = "Drive API Java FileSharingMain";
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/drive-java-quickstart");
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	public static Credential authorize() throws Exception {
		InputStream in = FileSharingMain.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	public static Drive getDriveService() throws Exception {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) throws Exception {
		Drive service = getDriveService();
		List<User> users = new ArrayList<User>();
		User u1 = new User();
		u1.setEmailAddress("jsahoo.1989@gmail.com");
		users.add(u1);
		FileList result = service.files().list().setPageSize(10)
				.setFields("nextPageToken, files(id,name,webViewLink,owners,permissions)").execute();
		List<File> files = result.getFiles();
try {
			//service.permissions().create("0B0WMaxBFl6s_RjBwQzg0Tnl4M00", newPermission).execute();
		service.permissions().delete("0B0WMaxBFl6s_RjBwQzg0Tnl4M00", "07977998501428979908").execute();
		} catch (Exception e) {
			System.out.println("An error occurred: " + e);
		}

		
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
		} 
		
		else {
			System.out.println("Files:");
			for (File file : files) {
				if (file.getId().equalsIgnoreCase("0B0WMaxBFl6s_RjBwQzg0Tnl4M00")) {
					System.out.printf("File Name : ", file.getName());
					System.out.println("File Id :" + file.getId());
					System.out.println("Link " + file.getWebViewLink());
					System.out.println("Permission :" + file.getPermissions());

				}

			}

		}

	}

}
