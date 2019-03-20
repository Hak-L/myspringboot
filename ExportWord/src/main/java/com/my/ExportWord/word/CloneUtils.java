package com.my.ExportWord.word;

import net.sf.cglib.beans.BeanCopier;

import java.io.*;

public class CloneUtils {

	public static <T> T clone(T t) {
		if(t == null){
			return null;
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		try {
			ObjectInputStream ois = new ObjectInputStream(bis);
			return (T) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static <T> void beanCopier(T dest, T target) {
		if(dest == null || target == null){
			return;
		}
		
		BeanCopier copier = BeanCopier.create(dest.getClass(), target.getClass(), false);
		copier.copy(dest, target, null);
	}

}
