package com.crowdplatform.util;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Project;
import com.google.api.client.auth.oauth2.TokenResponse;

public interface DataViewer {

	public String getDataURL(Project project, Batch batch, BatchExecutionCollection collection, TokenResponse response);
	
}
