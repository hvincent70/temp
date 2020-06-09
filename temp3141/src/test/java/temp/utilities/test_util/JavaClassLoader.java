package com.sprint.iice_tests.utilities.test_util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JavaClassLoader extends ClassLoader {

	public JavaClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	private Class getClass(String name) throws ClassNotFoundException {
		String file = name.replace('.', File.separatorChar) + ".class";
		byte[] b = null;
		try {
			b = loadClassFileData(file);
			Class c = defineClass(name, b, 0, b.length);
			resolveClass(c);
			return c;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Class loadClass(String name) throws ClassNotFoundException {
		if(name.startsWith("com.sprint.iice_tests")) {
			return getClass(name);
		}
		return super.loadClass(name);
	}
	
	private byte[] loadClassFileData(String name) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(name);
		int size = is.available();
		byte buff[] = new byte[size];
		DataInputStream dis = new DataInputStream(is);
		dis.readFully(buff);
		dis.close();
		return buff;
	}
}
