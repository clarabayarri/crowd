package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Field;

public interface FieldService {

	public void addField(Field field);
	
	public void removeField(Integer id);
	
	public List<Field> listFields();
	
}
