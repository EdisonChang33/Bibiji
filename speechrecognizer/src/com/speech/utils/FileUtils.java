package com.speech.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 拷贝assets文件夹下的文件到apk对应的包下面
 * 
 * @author caiyingyuan
 * 
 */
public class FileUtils {
	private static String RECOGNITION_MODEL_DIRNAME = "recognitionModel";

	public static String getHmmPath(Context context) {
		copyNativeLib(context, "hmm/tdt_sc_8k", "feat.params");
		copyNativeLib(context, "hmm/tdt_sc_8k", "mdef");
		copyNativeLib(context, "hmm/tdt_sc_8k", "means");
		copyNativeLib(context, "hmm/tdt_sc_8k", "noisedict");
		copyNativeLib(context, "hmm/tdt_sc_8k", "sendump");
		copyNativeLib(context, "hmm/tdt_sc_8k", "transition_matrices");
		String path = copyNativeLib(context, "hmm/tdt_sc_8k", "variances");
		int size = path.split("/").length;
		path = path.replace("/" + path.split("/")[size - 1], "");
		return path;
	}

	/** 获取XXX.dic文件的路径 */
	public static String getDicFilePath(Context context) {

		return copyNativeLib(context, "lm", "1663.dic");
	}

	/** 获取XXX.lm文件的路径 */
	public static String getLmFilePath(Context context) {
		return copyNativeLib(context, "lm", "1663.lm");
	}

	public static String copyNativeLib(Context context, String DirName, String name) {
		File rootDir = context.getDir(RECOGNITION_MODEL_DIRNAME, Context.MODE_PRIVATE);
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}

		File dir = new File(rootDir, DirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String assetpath = String.format(RECOGNITION_MODEL_DIRNAME + "/%s/%s", DirName, name);

		File dstFile = new File(dir, name);
		AssetManager assets = context.getAssets();
		if (dstFile.exists()) {
			String assetMd5 = md5AssetFile(assets, assetpath);
			String fileMd5 = Md5Util.md5(dstFile);
			if (assetMd5 != null && assetMd5.equalsIgnoreCase(fileMd5)) {
				try {
					Process process = Runtime.getRuntime().exec("chmod 755 " + dstFile.getPath());
					process.waitFor();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				// md5 一致，说明不需要再拷贝了。直接返回true；
				return dstFile.getPath();
			} else {
				dstFile.delete();
			}
		}
		if (copyAssetFile(assets, assetpath, dstFile.getPath())) {
			String assetMd5 = md5AssetFile(assets, assetpath);
			String fileMd5 = Md5Util.md5(dstFile);
			if (assetMd5 != null && assetMd5.equalsIgnoreCase(fileMd5)) {
				try {
					Process process = Runtime.getRuntime().exec("chmod 755 " + dstFile.getPath());
					process.waitFor();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return dstFile.getPath();
			} else {
				dstFile.delete();
				return null;
			}
		} else {
			return null;
		}
	}

	private static String md5AssetFile(AssetManager am, String assetpath) {
		InputStream in = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			in = am.open(assetpath);
			byte[] buffer = new byte[8192];
			int readed = 0;
			while ((readed = in.read(buffer)) != -1) {
				out.write(buffer, 0, readed);
			}
			return Md5Util.md5(out.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private static boolean copyAssetFile(AssetManager am, String name, String dstPath) {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = am.open(name);
			out = new FileOutputStream(dstPath);
			byte[] buffer = new byte[8192];
			int readed = 0;
			while ((readed = in.read(buffer)) != -1) {
				out.write(buffer, 0, readed);
			}
			return new File(dstPath).exists();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return false;
	}
}
