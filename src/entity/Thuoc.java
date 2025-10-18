package entity;

import java.time.LocalDate;
import java.util.Objects;

public class Thuoc {
	String maThuoc;
	Thue thue;
	KeThuoc keThuoc;
	DonViTinh dvt;
	NhaCungCap ncc;
	String tenThuoc;
	String dangThuoc;
	String phanLoai;
	float giaBan;
	LocalDate hanSuDung;
	Boolean trangThai;
	String anh;
	
	public Thuoc() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Thuoc(String maThuoc, Thue thue, KeThuoc keThuoc, DonViTinh dvt, NhaCungCap ncc, String tenThuoc,
			String dangThuoc, String phanLoai, float giaBan, LocalDate hanSuDung, Boolean trangThai, String anh) {
		super();
		this.maThuoc = maThuoc;
		this.thue = thue;
		this.keThuoc = keThuoc;
		this.dvt = dvt;
		this.ncc = ncc;
		this.tenThuoc = tenThuoc;
		this.dangThuoc = dangThuoc;
		this.phanLoai = phanLoai;
		this.giaBan = giaBan;
		this.hanSuDung = hanSuDung;
		this.trangThai = trangThai;
		this.anh = anh;
	}


	public String getMaThuoc() {
		return maThuoc;
	}
	public void setMaThuoc(String maThuoc) {
		this.maThuoc = maThuoc;
	}
	public Thue getThue() {
		return thue;
	}
	public void setThue(Thue thue) {
		this.thue = thue;
	}
	public KeThuoc getKeThuoc() {
		return keThuoc;
	}
	public void setKeThuoc(KeThuoc keThuoc) {
		this.keThuoc = keThuoc;
	}
	public DonViTinh getDvt() {
		return dvt;
	}
	public void setDvt(DonViTinh dvt) {
		this.dvt = dvt;
	}

	public NhaCungCap getNcc() {
		return ncc;
	}
	
	public void setNcc(NhaCungCap ncc) {
		this.ncc = ncc;
	}

	public String getTenThuoc() {
		return tenThuoc;
	}
	public void setTenThuoc(String tenThuoc) {
		this.tenThuoc = tenThuoc;
	}
	public String getDangThuoc() {
		return dangThuoc;
	}
	public void setDangThuoc(String dangThuoc) {
		this.dangThuoc = dangThuoc;
	}
	public String getPhanLoai() {
		return phanLoai;
	}
	public void setPhanLoai(String phanLoai) {
		this.phanLoai = phanLoai;
	}
	public float getGiaBan() {
		return giaBan;
	}
	public void setGiaBan(float giaBan) {
		this.giaBan = giaBan;
	}
	public LocalDate getHanSuDung() {
		return hanSuDung;
	}
	public void setHanSuDung(LocalDate hanSuDung) {
		this.hanSuDung = hanSuDung;
	}
	public Boolean getTrangThai() {
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
	public int hashCode() {
		return Objects.hash(maThuoc);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Thuoc other = (Thuoc) obj;
		return Objects.equals(maThuoc, other.maThuoc);
	}


	@Override
	public String toString() {
		return "Thuoc [maThuoc=" + maThuoc + ", thue=" + thue + ", keThuoc=" + keThuoc + ", dvt=" + dvt + ", ncc=" + ncc
				+ ", tenThuoc=" + tenThuoc + ", dangThuoc=" + dangThuoc + ", phanLoai=" + phanLoai + ", giaBan="
				+ giaBan + ", hanSuDung=" + hanSuDung + ", trangThai=" + trangThai + ", anh=" + anh + "]";
	}
	
	
	
	
}
