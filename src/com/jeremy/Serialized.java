package com.jeremy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class for saving and loading Serializable objects to and from the file system.
 * @author Scott Micklethwaite
 * @version 1.0
 * @param <T> An object to be serialized. Must implement the Serializable interface.
 */
public class Serialized<T extends Serializable>{
	
	/**
	 * A method for loading an object from a serialized file.
	 * @param file the file to load into an object
	 * @return an instance of T
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T load(File file) throws Exception{
		T o;
		try {
			FileInputStream fs = new FileInputStream(file);
			ObjectInput oi = new ObjectInputStream(fs);
			
			Object ob = oi.readObject();
			o = (T) ob;
			
			oi.close();
		} catch (IOException | ClassNotFoundException e) {
			throw e;
		}
		
		return o;
	}
	
	/**
	 * A method for serializing an object and saving it to a file.
	 * @param o the object to be serialized
	 * @param file the file to write the serialized object to
	 * @throws IOException
	 */
	public void save(T o, File file) throws IOException{
		try{
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(o);
			oo.close();

			FileOutputStream fs = new FileOutputStream(file);
			fs.write(bStream.toByteArray());
			fs.close();
		} catch (IOException e){
			throw e;
		}
	}
}
