package com.littlezheng.ultrasound3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public final class ObjectUtils {

	private ObjectUtils() {

	}

	/**
	 * 保存对象到指定文件，必须确定该对象实现了Serializable接口
	 * 
	 * @param obj
	 *            对象
	 * @param file
	 *            文件
	 * @return
	 */
	public static boolean saveObject(Object obj, File file) {
		if (obj == null)
			return false;
		//该判断方法不够严谨，对象的字段如果没有实现Serializable接口，序列化的过程也会失败
		if (!(obj instanceof Serializable))
			return false;

        boolean rst = true;
		OutputStream out = null;
		ObjectOutputStream objOut = null;
		try {
            File path = file.getParentFile();
            if(!path.exists()) path.mkdirs();

			out = new FileOutputStream(file);
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
			objOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			rst = false;
		} finally {
			if (objOut != null) {
				try {
					objOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return rst;
	}

	/**
	 * 保存对象到指定文件，必须确定该对象实现了Serializable接口
	 * 
	 * @param obj
	 *            对象
	 * @param pathname
	 *            文件路径+名称
	 * @return
	 */
	public static boolean saveObject(Object obj, String pathname) {
		if(pathname == null)
			return false;
		return saveObject(obj, new File(pathname));
	}

	/**
	 * 从指定文件读取一个对象
	 * 
	 * @param file
	 *            对象文件
	 * @param clazz
	 *            对象类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(File file, Class<T> clazz) {
		if (file == null || !file.exists())
			return null;
		InputStream in = null;
		ObjectInputStream objIn = null;
		T t = null;
		try {
			in = new FileInputStream(file);
			objIn = new ObjectInputStream(in);
			t = (T) objIn.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return t;
	}

	/**
	 * 从指定文件读取一个对象
	 * 
	 * @param pathname
	 *            对象文件路径+名称
	 * @param clazz
	 *            对象类型
	 * @return
	 */
	public static <T> T readObject(String pathname, Class<T> clazz) {
		return readObject(new File(pathname), clazz);
	}

	/**
	 * 从指定文件读取一个对象，不做转换
	 * 
	 * @param file
	 * @return
	 */
	public static Object readObject(File file) {
		return readObject(file, Object.class);
	}

	/**
	 * 从指定文件读取一个对象，不做转换
	 * 
	 * @param pathname
	 * @return
	 */
	public static Object readObject(String pathname) {
		return readObject(pathname, Object.class);
	}

}
