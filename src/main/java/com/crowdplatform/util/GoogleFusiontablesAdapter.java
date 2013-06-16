package com.crowdplatform.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Project;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.model.Column;
import com.google.api.services.fusiontables.model.Table;
import com.google.common.collect.Lists;

@Service
public class GoogleFusiontablesAdapter implements DataViewer {

	private static final String APPLICATION_NAME = "Crowd Platform";

	private static final String FUSIONTABLES_URL = "https://www.google.com/fusiontables/data?docid=";

	@Value("#{systemEnvironment['GOOGLE_API_SECRETS']}")
	private String CLIENT_SECRET;
	//private String CLIENT_SECRET = "{\"web\":{\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"client_secret\":\"K63ErMkAwKTsGfn3c_tHD3OT\",\"token_uri\":\"https://accounts.google.com/o/oauth2/token\",\"client_email\":\"584658910433@developer.gserviceaccount.com\",\"redirect_uris\":[\"http://crowd.clarabayarri.com:8080/\",\"http://crowd.clarabayarri.com:56043/Callback\",\"http://crowd.clarabayarri.com/Callback\",\"http://gentle-gorge-9660.herokuapp.com/oauth2callback\",\"http://gentle-gorge-9660.herokuapp.com:8080/\",\"http://gentle-gorge-9660.herokuapp.com:56043/Callback\",\"http://gentle-gorge-9660.herokuapp.com/Callback\",\"http://localhost:38866/Callback\",\"http://localhost/Callback\"],\"client_x509_cert_url\":\"https://www.googleapis.com/robot/v1/metadata/x509/584658910433@developer.gserviceaccount.com\",\"client_id\":\"584658910433.apps.googleusercontent.com\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"javascript_origins\":[\"http://crowd.clarabayarri.com\",\"http://gentle-gorge-9660.herokuapp.com\"]}}";

	public String getCLIENT_SECRET() {
		return CLIENT_SECRET;
	}

	public void setCLIENT_SECRET(String cLIENT_SECRET) {
		CLIENT_SECRET = cLIENT_SECRET;
	}

	private HttpTransport HTTP_TRANSPORT;

	private final JsonFactory JSON_FACTORY = new JacksonFactory();

	private Fusiontables fusiontables;

	@Autowired
	private FileWriter fileWriter;

	public String getDataURL(Project project, Batch batch, BatchExecutionCollection collection, TokenResponse response) {
		try {
			try {
				HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
				// authorization
				Credential credential = authorize(response);
				// set up global FusionTables instance
				fusiontables = new Fusiontables.Builder(
						HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();

				// List tables to see if it exists
				Table table = retrieveTable(project, batch);

				// Inserts
				insertData(table, project, batch, collection);

				return FUSIONTABLES_URL + table.getTableId();

			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return null;
	}

	public Credential authorize(TokenResponse tokenResponse) throws Exception {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, new ByteArrayInputStream(CLIENT_SECRET.getBytes()));
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println(
					"Enter Client Secret as system variable.");
		}

		return createCredentialWithAccessTokenOnly(HTTP_TRANSPORT, JSON_FACTORY, tokenResponse);
	}

	private static GoogleCredential createCredentialWithAccessTokenOnly(
			HttpTransport transport, JsonFactory jsonFactory, TokenResponse tokenResponse) {
		tokenResponse.setScope("https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/fusiontables");
		tokenResponse.setExpiresInSeconds(new Long(3600));
		tokenResponse.setTokenType("Bearer");
		return new GoogleCredential().setFromTokenResponse(tokenResponse);
	}

	private Table retrieveTable(Project project, Batch batch) throws IOException {
		if (batch.getFusiontableId() != null) {
			Fusiontables.Table.Get query = fusiontables.table().get(batch.getFusiontableId());
			try {
				Table table = query.execute();
				Fusiontables.Query.Sql delete = fusiontables.query().sql("DELETE FROM " + table.getTableId());
				delete.execute();
				return table;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				System.out.println("Table could not be retrieved, creating a new one.");
			}
		}
		return createTable(project, batch);
	}

	private Table createTable(Project project, Batch batch) throws IOException {
		Table table = new Table();
		table.setName(batch.getName());
		table.setIsExportable(false);
		table.setDescription("Data from Crowd Platform batch " + batch.getId());
		List<Column> columns = Lists.newArrayList();

		columns.add(new Column().setName("task_id").setType("NUMBER"));
		for (Field field : project.getInputFields()) {
			if (field.getType().equals(Field.Type.DOUBLE) || field.getType().equals(Field.Type.INTEGER)) {
				columns.add(new Column().setName(field.getName()).setType("NUMBER"));
			} else {
				columns.add(new Column().setName(field.getName()).setType("STRING"));
			}
		}
		columns.add(new Column().setName("execution_id").setType("NUMBER"));
		columns.add(new Column().setName("date").setType("DATETIME"));
		columns.add(new Column().setName("userId").setType("NUMBER"));
		for (Field field : project.getOutputFields()) {
			if (field.getType().equals(Field.Type.DOUBLE) || field.getType().equals(Field.Type.INTEGER)) {
				columns.add(new Column().setName(field.getName()).setType("NUMBER"));
			} else {
				columns.add(new Column().setName(field.getName()).setType("STRING"));
			}
		}

		table.setColumns(columns);

		Fusiontables.Table.Insert insert = fusiontables.table().insert(table);

		Table result = insert.execute();
		batch.setFusiontableId(result.getTableId());

		return result;
	}

	private void insertData(Table table, Project project, Batch batch, BatchExecutionCollection collection) throws IOException {
		String writer = fileWriter.writeTasksExecutions(project, batch, collection, false);
		ByteArrayContent byteArrayContent = ByteArrayContent.fromString("application/octet-stream", writer);
		Fusiontables.Table.ImportRows importRows = fusiontables.table().importRows(table.getTableId(), byteArrayContent);
		importRows.setIsStrict(false);
		importRows.execute();
	}
}
