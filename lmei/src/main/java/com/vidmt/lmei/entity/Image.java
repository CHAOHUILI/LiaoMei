package com.vidmt.lmei.entity;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.net.Uri;

public class Image implements Serializable {

	int status;//状态，1 本地新照片，2网络图片（不再转换bit64）
	String path;
	public Bitmap bitmap;
	String bit64;
	public Uri uri;

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public String getBit64() {
		return bit64;
	}
	public void setBit64(String bit64) {
		this.bit64 = bit64;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public Image() {
		super();
	}
	public Image(int status, String path) {
		super();
		this.status = status;
		this.path = path;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
