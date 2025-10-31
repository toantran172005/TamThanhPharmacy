package entity;

import java.util.Objects;

public class DonViTinh {
	String maDVT;
	String tenDVT;
	Boolean trangThai;
	
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

	public DonViTinh(String maDVT, String tenDVT, Boolean trangThai) {
		super();
		this.maDVT = maDVT;
		this.tenDVT = tenDVT;
		this.trangThai = trangThai;
	}

//	public boolean isSelected() {
//		return selected.get();
//	}
//
//	public void setSelected(boolean selected) {
//		this.selected.set(selected);
//	}
//
//	public BooleanProperty selectedProperty() {
//		return selected;
//	}
	
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
	

	public Boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return tenDVT;
	}
	
}
