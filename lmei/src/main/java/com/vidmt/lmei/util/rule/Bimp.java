package com.vidmt.lmei.util.rule;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();
	private static final int SAMPLE_SIZE_WIDTH = 480;
	private static final int SAMPLE_SIZE_HEIGHT = 800;
	private static final int COMPRESS_FIRST_SIZE = 16*64 * 1024;
	private static final int COMPRESS_SECOND_SIZE = 64*64 * 1024;
	private static final int COMPRESS_THIRD_SIZE = 64*128 * 1024;
	private static final int COMPRESS_FOURTH_SIZE = 64 * 256 * 1024;

	private static final int FACTOR_BEYOND_FIRST_SIZE = 80;
	private static final int FACTOR_BEYOND_SECOND_SIZE = 60;
	private static final int FACTOR_BEYOND_THIRD_SIZE = 50;
	private static final int FACTOR_BEYOND_FOURTH_SIZE = 40;
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static List<String> drr = new ArrayList<String>();
	private static Bitmap bm;


	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmp_old = BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	/**
	 * 图片的质量压缩方法     第二步
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		Bitmap btm=null;
		int isnum = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		//Log.e("长度", baos.toByteArray().length+"");
		while (baos.toByteArray().length / 1024 > 400)
		{ // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if(options!=0)
			{
				options -= 10;// 每次都减少10
			}
			isnum=1;
		}
		if(isnum!=0)
		{
			if(baos!=null)
			{
				btm = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
			}			
			if (baos != null)
			{
				try
				{
					baos.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (image != null && !image.isRecycled())
			{
				image.recycle();
				image = null;
				System.gc(); //提醒系统及时回收
			}
		}
		else
		{
			btm = image;
		}
		
		return btm;
	}

	/**
	 * 图片压缩    2015-4-27    第一步
	* @Title comp
	* @Description TODO(这里用一句话描述这个方法的作用)
	* @param @param bit
	* @param @return    参数
	* @return Bitmap    返回类型
	 */
	 
	public static Bitmap comp(Bitmap bit) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();	
		Log.e("maxMemory", Runtime.getRuntime().maxMemory()+"");
		Log.e("totalMemory", Runtime.getRuntime().totalMemory()+"");
			bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		    if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
		        baos.reset();//重置baos即清空baos
		        bit.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中  
		    }  
		    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
		    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
		    //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
		    newOpts.inJustDecodeBounds = true;  
		    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		    newOpts.inJustDecodeBounds = false;  
		    int w = newOpts.outWidth;  
		    int h = newOpts.outHeight;  
		    //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
		    float hh = 800f;//这里设置高度为800f  
		    float ww = 480f;//这里设置宽度为480f  
		    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
		    int be = 1;//be=1表示不缩放  
		    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
		        be = (int) (newOpts.outWidth / ww);  
		    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
		        be = (int) (newOpts.outHeight / hh);  
		    }  
		    if (be <= 0)  
		        be = 1;  
		    newOpts.inSampleSize = be;//设置缩放比例  
		    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
		    isBm = new ByteArrayInputStream(baos.toByteArray());  
		    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
		if(baos!=null)
		{
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return compressImage(bitmap);
	}
	/**
	 * 根据路径获得图片，压缩返回bitmap用于显示
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(filePath, options);// a must
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, SAMPLE_SIZE_WIDTH, SAMPLE_SIZE_HEIGHT);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap smallBm = BitmapFactory.decodeFile(filePath, options);
		return smallBm;
	}
	/**
	 * 计算图片的缩放值
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}


	/*
	 * 获取压缩后的图片文件
	 */
	public static String getCompressedImgFile(Bitmap bitmap) throws IOException {

		int compressFactor = 100;
//		long picSize = srcFile.length();
		double picSize = bitmap.getRowBytes() * bitmap.getHeight();
		Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		String photobit64 = "";

		if (picSize > COMPRESS_FIRST_SIZE) {// 压缩
			if (picSize < COMPRESS_SECOND_SIZE) {
				compressFactor = FACTOR_BEYOND_FIRST_SIZE;
			} else if (picSize >= COMPRESS_SECOND_SIZE && picSize < COMPRESS_THIRD_SIZE) {
				compressFactor = FACTOR_BEYOND_SECOND_SIZE;
			} else if (picSize >= COMPRESS_THIRD_SIZE && picSize < COMPRESS_FOURTH_SIZE) {
				compressFactor = FACTOR_BEYOND_THIRD_SIZE;
			} else {
				compressFactor = FACTOR_BEYOND_FOURTH_SIZE;
			}

//			albumPic = getBitmapFromFile(srcFile.getAbsolutePath());
			if(compressFactor==FACTOR_BEYOND_FOURTH_SIZE){
				Matrix matrix = new Matrix();
				matrix.setScale(0.5f, 0.5f);
				bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);
				bm.compress(format, compressFactor, stream);
				byte[] b = stream.toByteArray();
				photobit64 = new String(Base64Coder.encodeLines(b));
				bitmap.recycle();
				bm.recycle();
			}else {
				bitmap.compress(format, compressFactor, stream);
				byte[] b = stream.toByteArray();
				photobit64 = new String(Base64Coder.encodeLines(b));
				bitmap.recycle();
			}


		} else {
//			albumPic = getBitmapFromFile(srcFile.getAbsolutePath());
			bitmap.compress(format, 100, stream);
			byte[] b = stream.toByteArray();
			photobit64 = new String(Base64Coder.encodeLines(b));
			bitmap.recycle();

		}
		stream.close();

		return photobit64;
	}
	public static Bitmap getBitmapFromFile(String filePath) {
		Bitmap bm = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			//bm = BitmapFactory.decodeStream(fis);
			bm = BitmapFactory.decodeStream(fis, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bm;
	}


}
