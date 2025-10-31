package entity;

import java.time.LocalDate;
import java.util.Date;

//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;

public class NhanVien {
//	private BooleanProperty selected = new SimpleBooleanProperty(false);
	private String maNV;
	private String tenNV;
	private String chucVu;
	private LocalDate ngaySinh;
	private Boolean gioiTinh;
	private String sdt;
	private LocalDate ngayVaoLam;
	private Double luong;
	private Thue thue;
	private Boolean trangThai;
	private String anh;

	public NhanVien() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NhanVien(String maNV, String tenNV) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
	}

	public NhanVien(String maNV, String tenNV, String chucVu, LocalDate ngaySinh, Boolean gioiTinh, String sdt,
			LocalDate ngayVaoLam, Double luong, Thue thue, Boolean trangThai, String anh) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.chucVu = chucVu;
		this.ngaySinh = ngaySinh;
		this.gioiTinh = gioiTinh;
		this.sdt = sdt;
		this.ngayVaoLam = ngayVaoLam;
		this.luong = luong;
		this.thue = thue;
		this.trangThai = trangThai;
		this.anh = anh;
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

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}

	public String getTenNV() {
		return tenNV;
	}

	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public Boolean isGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(Boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public LocalDate getNgayVaoLam() {
		return ngayVaoLam;
	}

	public void setNgayVaoLam(LocalDate ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}

	public Double getLuong() {
		return luong;
	}

	public void setLuong(Double luong) {
		this.luong = luong;
	}

	public Thue getThue() {
		return thue;
	}

	public void setThue(Thue thue) {
		this.thue = thue;
	}

	public Boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

	@Override
	public String toString() {
		return "NhanVien [" + "maNV=" + maNV + ", tenNV=" + tenNV + ", chucVu=" + chucVu + ", ngaySinh=" + ngaySinh
				+ ", gioiTinh=" + gioiTinh + ", sdt=" + sdt + ", ngayVaoLam=" + ngayVaoLam + ", luong=" + luong
				+ ", thue=" + thue + ", trangThai=" + trangThai + ", anh=" + anh + "]";
	}

}
