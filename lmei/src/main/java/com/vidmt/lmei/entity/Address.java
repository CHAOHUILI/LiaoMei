package com.vidmt.lmei.entity;




public class Address {
	


    private String returnPath;//正常返回地址
	
	private String thumbnails;//缩略图地址

	/**
	 * 获取正常返回地址
	 * @return
	 */
	public String getReturnPath() {
		return returnPath;
	}

	public void setReturnPath(String returnPath) {
		returnPath = returnPath;
	}

	/**
	 * 获取压缩后的地址
	 * @return
	 */
	public String getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String thumbnails) {
		thumbnails = thumbnails;
	}
}
