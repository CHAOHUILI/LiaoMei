package com.vidmt.lmei.util.rule;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.ta.util.TALogger;
import com.vidmt.lmei.util.think.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

public class FileUtils {
	public static String TAG = "ImagePaht";
	public static boolean inNativeAllocAccessError = false;
//	public static String SDPATH = Environment.getExternalStorageDirectory()
//			+ "/formats/";
//
//	public  static String saveBitmap(Bitmap bm, String picName) {
//		Log.e("", "保存图片");
//		try {
//			if (!isFileExist("")) {
//				File tempf = createSDDir("");
//			}
//			File f = new File(SDPATH, picName + ".JPEG"); 
//			if (f.exists()) {
//				f.delete();
//			}
//			else
//			{
//				f.createNewFile();
//			}
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			FileOutputStream out = new FileOutputStream(f);			
//			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
//			out.write(baos.toByteArray());
//			out.flush();
//			out.close();
//			Log.e("", "已经保存");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//		String path = SDPATH + picName + ".JPEG";
//		return path;
//	}
//
//	public static File createSDDir(String dirName) throws IOException {
//		File dir = new File(SDPATH + dirName);
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//
//			System.out.println("createSDDir:" + dir.getAbsolutePath());
//			System.out.println("createSDDir:" + dir.mkdir());
//		}
//		return dir;
//	}
//
//	public static boolean isFileExist(String fileName) {
//		File file = new File(SDPATH + fileName);
//		file.isFile();
//		return file.exists();
//	}
//	
//	public static void delFile(String fileName){
//		File file = new File(SDPATH + fileName);
//		if(file.isFile()){
//			file.delete();
//        }
//		file.exists();
//	}
//
//	public static void deleteDir() {
//		File dir = new File(SDPATH);
//		if (dir == null || !dir.exists() || !dir.isDirectory())
//			return;
//		
//		for (File file : dir.listFiles()) {
//			if (file.isFile())
//				file.delete(); // 删除所有文件
//			else if (file.isDirectory())
//				deleteDir(); // 递规的方式删除文件夹
//		}
//		dir.delete();// 删除目录本身
//	}
//
//	public static boolean fileIsExists(String path) {
//		try {
//			File f = new File(path);
//			if (!f.exists()) {
//				return false;
//			}
//		} catch (Exception e) {
//
//			return false;
//		}
//		return true;
//	}
	
	public static File getTackPicFilePath() {
		String ImageName = "car" + System.currentTimeMillis() + ".jpg";
        File localFile = new File(getExternalStorePath()+ "/Frame/.tempchat" , ImageName);
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            TALogger.i("hhe", "SD卡不存在");
            localFile = null;
        }
        return localFile;
    }
	
	 /**
     * 外置存储卡的路径
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }
    
    /**
     * 是否有外存卡
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /**
	 * @param context
	 * @param intent
	 * @param appPath
	 * @return
	 */
	public static String resolvePhotoFromIntent(Context context, Intent intent,
			String appPath) {
		if (context == null || intent == null || appPath == null) {
			Log.e("FileUtils.class", "resolvePhotoFromIntent fail, invalid argument");
			return null;
		}
		Uri uri = Uri.parse(intent.toURI());
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		try {

			String pathFromUri = null;
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int columnIndex = cursor
						.getColumnIndex(MediaStore.MediaColumns.DATA);
				// if it is a picasa image on newer devices with OS 3.0 and up
				if (uri.toString().startsWith(
						"content://com.google.android.gallery3d")) {
					// Do this in a background thread, since we are fetching a
					// large image from the web
					pathFromUri = saveBitmapToLocal(appPath,
							createChattingImageByUri(intent.getData()));
				} else {
					// it is a regular local image file
					pathFromUri = cursor.getString(columnIndex);
				}
				cursor.close();
				Log.e(TAG, "photo from resolver, path: " + pathFromUri);
				return pathFromUri;
			} else {

				if (intent.getData() != null) {
					pathFromUri = intent.getData().getPath();
					if (new File(pathFromUri).exists()) {
						Log.e(TAG, "photo from resolver, path: "
								+ pathFromUri);
						return pathFromUri;
					}
				}

				// some devices (OS versions return an URI of com.android
				// instead of com.google.android
				if ((intent.getAction() != null)
						&& (!(intent.getAction().equals("inline-data")))) {
					// use the com.google provider, not the com.android
					// provider.
					// Uri.parse(intent.getData().toString().replace("com.android.gallery3d","com.google.android.gallery3d"));
					pathFromUri = saveBitmapToLocal(appPath, (Bitmap) intent
							.getExtras().get("data"));
					Log.d(TAG, "photo from resolver, path: " + pathFromUri);
					return pathFromUri;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		Log.e(TAG, "resolve photo from intent failed ");
		return null;
	}
	
	/**
	 * save image from uri
	 * 
	 * @param outPath
	 * @param bitmap
	 * @return
	 */
	public static String saveBitmapToLocal(String outPath, Bitmap bitmap) {
		try {
			String imagePath = outPath
					+ FileUtils.md5(DateFormat.format("yyyy-MM-dd-HH-mm-ss",
							System.currentTimeMillis()).toString()) + ".jpg";
			File file = new File(imagePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					bufferedOutputStream);
			bufferedOutputStream.close();
			TALogger.d(TAG, "photo image from data, path:" + imagePath);
			return imagePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static MessageDigest md = null;
	public static String md5(final String c) {
		if (md == null) {
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if (md != null) {
			md.update(c.getBytes());
			return byte2hex(md.digest());
		}
		return "";
	}
	
	public static String byte2hex(byte b[]) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = (new StringBuffer(String.valueOf(hs))).toString();
		}

		return hs.toUpperCase();
	}
	
	
	/**
	 *
	 * @param uri
	 * @return
	 */
	public static Bitmap createChattingImageByUri(Uri uri) {
		return createChattingImage(0, null, null, uri, 0.0F, 400, 800);
	}
	
	/**
	 *
	 * @param resource
	 * @param path
	 * @param b
	 * @param uri
	 * @param dip
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createChattingImage(int resource, String path,
			byte[] b, Uri uri, float dip, int width, int height) {
		if (width <= 0 || height <= 0) {
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		int outWidth = 0;
		int outHeight = 0;
		int sampleSize = 0;
		try {

			do {
				if (dip != 0.0F) {
					options.inDensity = (int) (160.0F * dip);
				}
				options.inJustDecodeBounds = true;
				decodeMuilt(options, b, path, uri, resource);
				//
				outWidth = options.outWidth;
				outHeight = options.outHeight;

				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				if (outWidth <= width || outHeight <= height) {
					sampleSize = 0;
					setInNativeAlloc(options);
					Bitmap decodeMuiltBitmap = decodeMuilt(options, b, path,
							uri, resource);
					return decodeMuiltBitmap;
				} else {
					options.inSampleSize = (int) Math.max(outWidth / width,
							outHeight / height);
					sampleSize = options.inSampleSize;
				}
			} while (sampleSize != 0);

		} catch (IncompatibleClassChangeError e) {
			e.printStackTrace();
			throw ((IncompatibleClassChangeError) new IncompatibleClassChangeError(
					"May cause dvmFindCatchBlock crash!").initCause(e));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			BitmapFactory.Options catchOptions = new BitmapFactory.Options();
			if (dip != 0.0F) {
				catchOptions.inDensity = (int) (160.0F * dip);
			}
			catchOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			if (sampleSize != 0) {
				catchOptions.inSampleSize = sampleSize;
			}
			setInNativeAlloc(catchOptions);
			try {
				return decodeMuilt(options, b, path, uri, resource);
			} catch (IncompatibleClassChangeError twoE) {
				twoE.printStackTrace();
				throw ((IncompatibleClassChangeError) new IncompatibleClassChangeError(
						"May cause dvmFindCatchBlock crash!").initCause(twoE));
			} catch (Throwable twoThrowable) {
				twoThrowable.printStackTrace();
			}
		}

		return null;
	}
	public static void setInNativeAlloc(BitmapFactory.Options options) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
				&& !inNativeAllocAccessError) {
			try {
				BitmapFactory.Options.class.getField("inNativeAlloc")
						.setBoolean(options, true);
				return;
			} catch (Exception e) {
				inNativeAllocAccessError = true;
			}
		}
	}
	
	/**
	 *
	 * @param options
	 * @param data
	 * @param path
	 * @param uri
	 * @param resource
	 * @return
	 */
	public static Bitmap decodeMuilt(BitmapFactory.Options options,
			byte[] data, String path, Uri uri, int resource) {
		try {

			if (!checkByteArray(data) && TextUtils.isEmpty(path) && uri == null
					&& resource <= 0) {
				return null;
			}

			if (checkByteArray(data)) {
				return BitmapFactory.decodeByteArray(data, 0, data.length,
						options);
			}

//			if (uri != null) {
//				InputStream inputStream = CCPAppManager.getContext()
//						.getContentResolver().openInputStream(uri);
//				Bitmap localBitmap = BitmapFactory.decodeStream(inputStream,
//						null, options);
//				inputStream.close();
//				return localBitmap;
//			}

//			if (resource > 0) {
//				return BitmapFactory.decodeResource(CCPAppManager.getContext()
//						.getResources(), resource, options);
//			}
			return BitmapFactory.decodeFile(path, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean checkByteArray(byte[] b) {
		return b != null && b.length > 0;
	}
}
