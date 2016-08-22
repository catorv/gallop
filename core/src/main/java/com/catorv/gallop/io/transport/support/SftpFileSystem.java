package com.catorv.gallop.io.transport.support;


import com.catorv.gallop.io.transport.FileSystem;
import com.catorv.gallop.io.transport.FileSystems;
import com.catorv.gallop.io.transport.Path;
import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class SftpFileSystem implements FileSystem {

	private String server;

	private int port;

	private String username;

	private String privateKey;

	public SftpFileSystem(String server, int port, String username,
	                      String privateKey) {
		this.server = server;
		this.port = port;
		if (this.port < 1 || this.port > 65535) {
			this.port = 22;
		}
		this.username = username;
		this.privateKey = privateKey;
	}

	@Override
	public String getPartition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		return "sftp";
	}

	@Override
	public String getHost() {
		return server + ":" + port;
	}

	@Override
	public String[] getChild(final String path) throws IOException {
		return (String[]) (new SftpOperationTemplate() {

			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {

				SftpATTRS attrs = sftp.lstat(path);
				if (!attrs.isDir()) {
					return null;
				}
				List<String> names = new ArrayList<>();
				for (Object o : sftp.ls(path)) {
					ChannelSftp.LsEntry lsE = (ChannelSftp.LsEntry) o;
					String name = lsE.getFilename();
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
		(new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				sftp.put(is, path);
				return null;
			}
		}).execute();
	}

	@Override
	public void read(final String path, final OutputStream os)
			throws IOException {
		(new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				sftp.get(path, os);
				return null;
			}
		}).execute();
	}

	@Override
	public boolean exists(final String path) throws IOException {
		return (Boolean) (new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				try {
					sftp.lstat(path);
				} catch (SftpException e) {
					return false;
				}
				return true;
			}
		}).execute();
	}

	@Override
	public Path mkdir(final String parent, final String name) throws IOException {
		return (Path) (new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				SftpATTRS attrs;
				attrs = sftp.lstat(parent);

				if (!attrs.isDir()) {
					return null;
				}

				sftp.cd(parent);
				sftp.mkdir(name);

				return new SftpPath(SftpFileSystem.this, parent + "/" + name);
			}
		}).execute();
	}

	@Override
	public Path mkdirs(final String parent, final String path) throws IOException {
		return (Path) (new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException,
					IOException {
				SftpATTRS attrs = null;
				String base = parent;
				String[] parts = path.split("\\/");
				try {
					attrs = sftp.lstat(parent);
					if (!attrs.isDir()) {
						throw new IOException(parent + " is not directory");
					}
					sftp.cd(parent);
				} catch (SftpException e) {
					base = "";
					parts = (parent + "/" + path).split("\\/");
					sftp.cd("/");
				}

				for (String part : parts) {
					if (part.length() == 0) {
						continue;
					}
					base = base + "/" + part;
					try {
						attrs = sftp.lstat(base);
						if (!attrs.isDir()) {
							throw new IOException(base + " is not directory");
						}
					} catch (SftpException e) {
						sftp.mkdir(part);
					}
					sftp.cd(base);
				}
				return new SftpPath(SftpFileSystem.this, parent + "/" + path);
			}
		}).execute();
	}

	@Override
	public boolean isFile(final String path) throws IOException {
		return (Boolean) (new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				SftpATTRS attrs;
				try {
					attrs = sftp.lstat(path);
				} catch (SftpException e) {
					return false;
				}
				return attrs.isReg();
			}
		}).execute();
	}

	@Override
	public boolean isDirectory(final String path) throws IOException {
		return (Boolean) (new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				SftpATTRS attrs;
				try {
					attrs = sftp.lstat(path);
				} catch (SftpException e) {
					return false;
				}
				return attrs.isDir();
			}
		}).execute();
	}

	@Override
	public void remove(final String path) throws IOException {
		(new SftpOperationTemplate() {
			@Override
			protected Object doOperation(ChannelSftp sftp) throws SftpException {
				sftp.cd(path);
				removeDir(path, sftp);
				return null;
			}

			@SuppressWarnings("unchecked")
			public void removeDir(final String path, ChannelSftp sftp)
					throws SftpException {
				SftpATTRS attrs;
				try {
					attrs = sftp.lstat(path);
				} catch (SftpException e) {
					return;
				}

				if (attrs.isReg()) {
					sftp.rm(path);
				} else if (attrs.isDir()) {
					Vector<ChannelSftp.LsEntry> entries = new Vector<>();
					sftp.cd(path);
					entries.addAll(sftp.ls(".*"));
					entries.addAll(sftp.ls("*"));
					for (ChannelSftp.LsEntry lsE : entries) {
						String name = lsE.getFilename();
						if (".".equals(name) || "..".equals(name)) {
							continue;
						}
						removeDir(name, sftp);
					}
					sftp.cd("..");
					sftp.rmdir(path);
				}
			}
		}).execute();
	}

	private static byte[] convertSshKey(String key) {
		int index = 0;
		int length = key.length();

		StringBuilder sb = new StringBuilder(64 + length / 64);
		sb.append("-----BEGIN RSA PRIVATE KEY-----\r\n");

		while (index < length) {
			sb.append(key.substring(index, Math.min(index + 64, length)));
			sb.append("\r\n");
			index += 64;
		}
		sb.append("-----END RSA PRIVATE KEY-----");
		return sb.toString().getBytes();
	}

	private abstract class SftpOperationTemplate {
		public Object execute() throws IOException {
			JSch jsch = new JSch();
			Session session = null;
			Channel channel = null;
			try {
				jsch.addIdentity("mykey", convertSshKey(privateKey), null, null);
				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				session = jsch.getSession(username, server, 22);
				session.setConfig(config);
				session.connect();
				channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;
				return doOperation(sftp);
			} catch (JSchException | SftpException e) {
				throw new IOException(e);
			} finally {
				if (channel != null) {
					channel.disconnect();
				}
				if (session != null) {
					session.disconnect();
				}
			}
		}

		protected abstract Object doOperation(ChannelSftp sftp)
				throws SftpException, IOException;
	}

	public static void main(String args[]) throws Exception {
		String sshkey = "MIICWwIBAAKBgQDI9bJ9LeZP+CPhvAp4XO9VpN5q0cyoJ+onuKglrd5jx1O036h3ca0WK//DoMFb1RrxkNVExAQKELToME0/4KEVjhEYN9ct7kNjcAKmPqjOnRUcwyfgp4cysJYL2uHgmrXQLUFp7fRA4FTGjDmIEZ7LhXQqe6AXXO+wvlb/y+VgkwIBJQKBgHIO6MOWl3l+/5vLlz1lLeSAKzW1WHsdkryZPNcdfjihkF+9NhpHcBOAwZiLrAOwTZAGGC3yti856iLq+2lxp4fEetI6TVQjAg0NMZWjf08BJ2B0CtyBF0Z6GvbDSwai/daPDymXtYj/9VmkkpIGYQE3IDfrlGiya+WeI/LPWW39AkEA7irQ43OWpDovFG1OGNxmcMpAgX6bF3gGcg02XIcK8s3dF1lXYohk+qGUJb2+cWegvYswbQN/qy4k63DEcDl6ewJBANgBsFthcMM2WlCHH7yWxJf5SrIoa7jNyHOWR0rWpWQotgF4Bjb7P9mVMu8wZtHtxx2kBT7H3zpxKAVp8RLpwskCQQC6q+/U3g5JXgk5hhqd3S2yWVUnObfUH82zVna3OWlyMqZeaJ5v0rACtf5+ch+k/jHgq11OhjOh2A8ZZjkZsIKXAkEAzFSf570JzWrEIqlVX14vlq2L2PXNrtB/WJUTAZqcc4B00PT+9b08ZgmsuL8VJ3I45K/pSTmbz3/L6XINjm5sJQJAMn9z8QUOKZaMfucCPGXxrkSiVUhq3fumkN+AiXA2XnG/UQjG+93s+XbcMvNTJ7H0JriiU08yOssh3aUQMHTtcw==";

		Path path = FileSystems.getPath("udoore@sftp://180.153.242.196/data/tmp2", sshkey);
		//path.mkdirs("sample");
		path.remove();
	}
}
