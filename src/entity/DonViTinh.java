package entity;

import java.util.Objects;

public class DonViTinh {
	String maDVT;
	String tenDVT;
	
	public DonViTinh() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DonViTinh(String maDVT) {
		super();
		this.maDVT = maDVT;
	}

	public DonViTinh(String maDVT, String tenDVT) {
		super();
		this.maDVT = maDVT;
		this.tenDVT = tenDVT;
	}

	public String getMaDVT() {
		return maDVT;
	}

	public void setMaDVT(String maDVT) {
		this.maDVT = maDVT;
	}

	public String getTenDVT() {
		return tenDVT;
	}

	public void setTenDVT(String tenDVT) {
		this.tenDVT = tenDVT;
	}

	@Override
	public String toString() {
		return tenDVT;
	}
	
	
	
}
