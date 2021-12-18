package com.mpatric.mp3agic;

/*
File editted to remove java.io.nio support
//*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FileWrapper {
	protected long length;
	protected long lastModified;
	//.
	File mFile;
	
	public FileWrapper(String filename) throws IOException {
		mFile = new File(filename);
		init();
	}

	public FileWrapper(File file) throws IOException {
		if (file == null) throw new NullPointerException();
		mFile = file;
		init();
	}

	private void init() throws IOException {
		if (!fileExists(mFile)) throw new FileNotFoundException("File not found " + mFile.getAbsolutePath());
		if (!mFile.canRead()) throw new IOException("File not readable");
		length = mFile.length();
		lastModified = mFile.lastModified();
	}

	public String getFilename() {
		return mFile.getName();
	}

	public long getLength() {
		return length;
	}
	
	public boolean fileExists(File filename)	{
		return filename.exists();
	}

	public long getLastModified() {
		return lastModified;
	}
}
