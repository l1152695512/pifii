package com.yf.util;

import java.io.File;

public interface FileStore {

	public abstract File getRootFile();

	public abstract File getFile(String fileName);



}