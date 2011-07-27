package org.jboss.seam.jcr.ocm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

import javax.inject.Inject;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.jboss.logging.Logger;
import org.jboss.seam.solder.reflection.Reflections;

public class NodeConverter {
	@Inject JcrOCMExtension ocmExtension;
	private Logger logger = Logger.getLogger(NodeConverter.class);
	
	public <T> T nodeToObject(javax.jcr.Node node, Class<T> nodeType) {
		OCMMapping mapping = ocmExtension.getOCMMappingStore().findMapping(nodeType);
		if(mapping == null) {
			throw new RuntimeException("No mapping found for class "+nodeType);
		}
		try{
			T returnValue = nodeType.newInstance();
			Set<String> jcrProperties = mapping.getPropertiesToFields().keySet();
			for(String jcrProperty : jcrProperties) {
				Field field = mapping.getPropertiesToFields().get(jcrProperty);
				Object value = null;
				if(field != null && jcrProperty.equalsIgnoreCase("uuid")) {
					value = node.getIdentifier();
				} else {
					Property property = node.getProperty(jcrProperty);
					if(field != null && property != null) {
						Class<?> fieldType = field.getType();
						if(fieldType.equals(java.util.Calendar.class)) {
							value = property.getDate();
						} else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
							value = property.getBoolean();
						} else if(fieldType.equals(double.class) || fieldType.equals(Double.class)) {
							value = property.getDouble();
						} else if(fieldType.equals(BigDecimal.class)) {
							value = property.getDecimal();
						} else if(fieldType.equals(Long.class) || fieldType.equals(long.class)) {
							value = property.getLong();
						} else if(fieldType.equals(String.class)) {
							value = property.getString();
						} else {
							logger.warnf("invalid field type %s",field);
						}
					}
				}
				if(value != null) {
					String setterMethodName = "set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
					Method method = Reflections.findDeclaredMethod(nodeType, setterMethodName, field.getType());
					Reflections.invokeMethod(method, returnValue, value);
				}
			}
			return returnValue;
		} catch (InstantiationException e) {
			throw new RuntimeException("Unable to instantiate type "+nodeType,e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Unable to instantiate type "+nodeType,e);
		} catch (RepositoryException e) {
			throw new RuntimeException("Unable to read property on "+nodeType,e);
		}
	}
	
	public <T> void objectToNode(T object, javax.jcr.Node node) throws RepositoryException {
		Class<?> nodeType = object.getClass();
		OCMMapping mapping = ocmExtension.getOCMMappingStore().findMapping(nodeType);
		Set<String> jcrProperties = mapping.getPropertiesToFields().keySet();
		for(String jcrProperty : jcrProperties) {
			Field field = mapping.getPropertiesToFields().get(jcrProperty);
			String getterMethodName = "get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
			Method method = Reflections.findDeclaredMethod(nodeType, getterMethodName);
			Object value = Reflections.invokeMethod(method, object);
			if(field != null && jcrProperty.equalsIgnoreCase("uuid")) {
				//don't set UUID
			} else {
				if(field != null) {
					Class<?> fieldType = field.getType();
					if(fieldType.equals(java.util.Calendar.class)) {
						node.setProperty(jcrProperty, (Calendar)value);
					} else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
						node.setProperty(jcrProperty, (Boolean)value);
					} else if(fieldType.equals(double.class) || fieldType.equals(Double.class)) {
						node.setProperty(jcrProperty, (Double)value);
					} else if(fieldType.equals(BigDecimal.class)) {
						node.setProperty(jcrProperty, (BigDecimal)value);
					} else if(fieldType.equals(Long.class) || fieldType.equals(long.class)) {
						node.setProperty(jcrProperty, (Long)value);
					} else if(fieldType.equals(String.class)) {
						node.setProperty(jcrProperty, value.toString());
					} else {
						logger.warnf("invalid field type %s",field);
					}
				}
			}
		}
	}
}
