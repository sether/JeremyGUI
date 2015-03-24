package com.jeremy.junit;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Test;

import com.jeremy.Serialized;

/**
 * A JUnit test class for the Serialized class. Contains an Enum for testing.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class SerializedTest {
	private static final String TEST_FILE = "file.ser";
	
	/**
	 * Deletes serialized file after each test
	 */
	@After
	public void cleanUp(){
		File file = new File(TEST_FILE);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * Test that enums can be saved and loaded correctly
	 * @throws Exception 
	 */
	@Test
	public void enumSerialize() throws Exception{
		Serialized<EnumTest> serialized = new Serialized<EnumTest>();
		File file = new File(TEST_FILE);
		EnumTest saved = EnumTest.NORTH;
		EnumTest loaded;
		
		//save and load
		serialized.save(saved, file);
		loaded = serialized.load(file);
		
		//assert that both match
		assertEquals("Loaded object does not match saved", saved, loaded);
	}
	
	/**
	 * Test that strings can be saved and loaded correctly
	 * @throws Exception 
	 */
	@Test
	public void stringSerialize() throws Exception{
		Serialized<String> serialized = new Serialized<String>();
		File file = new File(TEST_FILE);
		String saved = "this is a test string";
		String loaded;
		
		//save and load
		serialized.save(saved, file);
		loaded = serialized.load(file);
		
		//assert that both match
		assertEquals("Loaded object does not match saved", saved, loaded);
	}
	
	/**
	 * Test that integers can be saved and loaded correctly
	 * @throws Exception 
	 */
	@Test
	public void intSerialize() throws Exception{
		Serialized<Integer> serialized = new Serialized<Integer>();
		File file = new File(TEST_FILE);
		Integer saved = 1337;
		Integer loaded;
		
		//save and load
		serialized.save(saved, file);
		loaded = serialized.load(file);
		
		//assert that both match
		assertEquals("Loaded object does not match saved", saved, loaded);
	}
	
	/**
	 * Test that doubles can be saved and loaded correctly
	 * @throws Exception 
	 */
	@Test
	public void doubleSerialize() throws Exception{
		Serialized<Double> serialized = new Serialized<Double>();
		File file = new File(TEST_FILE);
		Double saved = 1337.1337;
		Double loaded;
		
		//save and load
		serialized.save(saved, file);
		loaded = serialized.load(file);
		
		//assert that both match
		assertEquals("Loaded object does not match saved", saved, loaded);
	}
	
	/**
	 * Test that floats can be saved and loaded correctly
	 * @throws Exception 
	 */
	@Test
	public void floatSerialize() throws Exception{
		Serialized<Float> serialized = new Serialized<Float>();
		File file = new File(TEST_FILE);
		Float saved = 1337.1337f;
		Float loaded;
		
		//save and load
		serialized.save(saved, file);
		loaded = serialized.load(file);
		
		//assert that both match
		assertEquals("Loaded object does not match saved", saved, loaded);
	}
	
	/**
	 * Test that longs can be saved and loaded correctly
	 * @throws Exception 
	 */
	@Test
	public void longSerialize() throws Exception{
		Serialized<Long> serialized = new Serialized<Long>();
		File file = new File(TEST_FILE);
		Long saved = 13371337L;
		Long loaded;
		
		//save and load
		serialized.save(saved, file);
		loaded = serialized.load(file);
		
		//assert that both match
		assertEquals("Loaded object does not match saved", saved, loaded);
	}
	
	/**
	 * An enum to test serialization
	 */
	private enum EnumTest{
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
}
