package com.catorv.gallop.io.transport.support;

import com.catorv.gallop.io.transport.FileSystem;
import com.catorv.gallop.io.transport.Path;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FtpFileSystem implements FileSystem {

	private String server;

	private int port;

	private String username;

	private String password;

	public FtpFileSystem(String server, int port, String username, String password) {
		this.server = server;
		this.port = port;
		if (this.port < 1 || this.port > 65535) {
			this.port = 21;
		}
		this.username = username;
		this.password = password;
	}

	@Override
	public String getPartition() {
		return null;
	}

	@Override
	public String getScheme() {
		return "ftp";
	}

	@Override
	public String getHost() {
		return server + ":" + port;
	}

	@Override
	public String[] getChild(final String path) throws IOException {
		return (String[]) (new FtpOperationTemplate() {

			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				FTPFile[] listFiles = ftp.listFiles(path);
				if (listFiles.length == 1 && listFiles[0].getType() == FTPFile.FILE_TYPE) {
					return new String[0];
				}

				List<String> names = new ArrayList<>();
				for (FTPFile ff : listFiles) {
					String name = ff.getName();
					if (".".equals(name) || "..".equals(name)) {
						continue;
					}
					names.add(name);
				}
				return names.toArray(new String[names.size()]);
			}

		}).execute();
	}

	@Override
	public String resolve(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return path;
	}

	@Override
	public void write(final String path, final InputStream is) throws IOException {
		(new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				if (!ftp.storeFile(path, is)) {
					throw new IOException("Fail to store remote watch " + path);
				}
				return null;
			}

		}).execute();
	}

	@Override
	public void read(final String path, final OutputStream os)
			throws IOException {
		(new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				if (!ftp.retrieveFile(path, os)) {
					throw new IOException("Fail to receive remote watch " + path);
				}
				return null;
			}

		}).execute();
	}

	@Override
	public boolean exists(final String path) throws IOException {
		return (Boolean) (new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				return ftp.changeWorkingDirectory(path) || ftp.listFiles(path).length > 0;
			}
		}).execute();
	}

	@Override
	public Path mkdir(final String parent, final String name) throws IOException {
		return (Path) (new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				if (!ftp.changeWorkingDirectory(parent)) {
					throw new IOException("The parent directory does not exist " + parent);
				}
				if (!ftp.changeWorkingDirectory(parent + "/" + name)) {
					if (!FTPReply.isPositiveCompletion(ftp.mkd(name))) {
						throw new IOException("Fail to create path " + name + " under " + parent);
					}
				}
				return new FtpPath(FtpFileSystem.this, parent + "/" + name);
			}
		}).execute();
	}

	@Override
	public Path mkdirs(final String parent, final String path) throws IOException {
		return (Path) (new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				String base = parent;
				String[] parts = path.split("\\/");

				if (!ftp.changeWorkingDirectory(parent)) {
					base = "";
					parts = (parent + "/" + path).split("\\/");
					if (!ftp.changeWorkingDirectory("/")) {
						throw new IOException("FTP list root failed");
					}
				}

				for (String part : parts) {
					if (part.length() == 0) {
						continue;
					}
					base = base + "/" + part;
					if (ftp.changeWorkingDirectory(base)) {
						continue;
					}
					if (!FTPReply.isPositiveCompletion(ftp.mkd(part))) {
						throw new IOException("Fail to create path " + part);
					}
					if (!ftp.changeWorkingDirectory(base)) {
						throw new IOException("Fail to change to path " + base);
					}
				}
				return new FtpPath(FtpFileSystem.this, parent + "/" + path);
			}
		}).execute();
	}

	@Override
	public boolean isFile(final String path) throws IOException {

		return (Boolean) (new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				FTPFile[] listFiles = ftp.listFiles(path);
				return listFiles.length == 1 && listFiles[0].getType() == FTPFile.FILE_TYPE;
			}
		}).execute();
	}

	@Override
	public boolean isDirectory(final String path) throws IOException {
		return (Boolean) (new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				return ftp.changeWorkingDirectory(path);
			}
		}).execute();
	}

	@Override
	public void remove(final String path) throws IOException {
		(new FtpOperationTemplate() {
			@Override
			protected Object doOperation(FTPClient ftp) throws IOException {
				removePath(path, ftp);
				return null;
			}

			public void removePath(String path, FTPClient ftp)
					throws IOException {
				if (ftp.changeWorkingDirectory(path)) {
					FTPFile[] listFile = ftp.listFiles();
					for (FTPFile ff : listFile) {
						if (FTPFile.FILE_TYPE == ff.getType()) {
							ftp.deleteFile(ff.getName());
						} else {
							removePath(ff.getName(), ftp);
						}
					}
					ftp.cdup();
					ftp.removeDirectory(path);
				} else {
					FTPFile[] listFiles = ftp.listFiles(path);
					if (listFiles.length == 1 && listFiles[0].getType() == FTPFile.FILE_TYPE) {
						ftp.deleteFile(listFiles[0].getName());
					}
				}
			}
		}).execute();
	}

	private abstract class FtpOperationTemplate {
		public Object execute() throws IOException {
			FTPClient ftp = null;
			try {
				ftp = new FTPClient();
				ftp.setAutodetectUTF8(true);
				ftp.connect(server, port);

				int reply = ftp.getReplyCode();

				if (!FTPReply.isPositiveCompletion(reply)) {
					throw new IOException("FTP server refused connection.");
				}

				if (!ftp.login(username, password)) {
					throw new IOException("FTP server authentication failed.");
				}
				return doOperation(ftp);
			} finally {
				if (ftp != null) {
					ftp.disconnect();
				}
			}
		}

		protected abstract Object doOperation(FTPClient ftp) throws IOException;
	}

}
