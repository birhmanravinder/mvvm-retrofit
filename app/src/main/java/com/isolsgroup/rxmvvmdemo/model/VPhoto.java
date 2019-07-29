package com.isolsgroup.rxmvvmdemo.model;

import java.io.Serializable;

public class VPhoto implements Serializable{
	
	public String name;
	public int width=1000;
	public int height=1000;


	public VPhoto() {}

	public VPhoto(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
}
