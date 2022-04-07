package com.campus.myapp.vo;

public class DataVO {
	private int no;
	private String userid;
	private String subject;
	private String content;
	private String writedate;
	
	private String filename1;
	private String filename2;
	
	// 삭제할 파일명 담을 변수
	private String[] delFile;
	
	
	public String toString() {
		return userid + ", " + subject + ", " + content + ", " + filename1 + ", " + filename2;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWritedate() {
		return writedate;
	}
	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}
	public String getFilename1() {
		return filename1;
	}
	public void setFilename1(String filename1) {
		this.filename1 = filename1;
	}
	public String getFilename2() {
		return filename2;
	}
	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}


	public String[] getDelFile() {
		return delFile;
	}


	public void setDelFile(String[] delFile) {
		this.delFile = delFile;
	}

	
}
