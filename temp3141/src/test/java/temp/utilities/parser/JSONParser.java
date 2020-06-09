package com.sprint.iice_tests.utilities.parser;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {
	
	public static Object parseJson(String path, Class clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Object obj = objectMapper.readValue(new File(path), clazz);
		return obj;
	}
	
}
