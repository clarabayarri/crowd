package com.crowdplatform.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface FileReader {

	public List<Map<String, String>> readCSVFile(MultipartFile file) throws IOException;
	
}
