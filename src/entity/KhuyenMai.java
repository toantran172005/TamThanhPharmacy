package entity;

import java.time.LocalDate;
import java.util.Objects;

public class KhuyenMai {
//	BooleanProperty selected = new SimpleBooleanProperty(false);
	String maKM;
	String tenKM;
	String loaiKM;
	int mucKM;
	Integer soLuongMua;
	Integer soLuongTang;
	LocalDate ngayBD;
	LocalDate ngayKT;
	boolean trangThai;
	
	public KhuyenMai() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhuyenMai(String maKM, String tenKM, String loaiKM, int mucKM, int soLuongMua,
			int soLuongTang, LocalDate ngayBD, LocalDate ngayKT, boolean trangThai) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.loaiKM = loaiKM;
		this.mucKM = mucKM;
		this.soLuongMua = soLuongMua;
		this.soLuongTang = soLuongTang;
		this.ngayBD = ngayBD;
		this.ngayKT = ngayKT;
		this.trangThai = trangThai;
	}
	
	public String getMucKhuyenMai() {
		if(loaiKM.equalsIgnoreCase("Giảm giá")) {
			return "Giảm " + mucKM + "%";
		} else if(loaiKM.equalsIgnoreCase("Mua tặng")) {
			if (soLuongMua != null && soLuongTang != null) {
                return "Mua " + soLuongMua + " tặng " + soLuongTang;
            } else {
                return "Mua tặng";
            }
		}
		return null;
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

	public String getMaKM() {
		return maKM;
	}

	public void setMaKM(String maKM) {
		this.maKM = maKM;
	}

	public String getTenKM() {
		return tenKM;
	}

	public void setTenKM(String tenKM) {
		this.tenKM = tenKM;
	}

	public String getLoaiKM() {
		return loaiKM;
	}

	public void setLoaiKM(String loaiKM) {
		this.loaiKM = loaiKM;
	}

	public int getMucKM() {
		return mucKM;
	}

	public void setMucKM(int mucKM) {
		this.mucKM = mucKM;
	}

	public int getSoLuongMua() {
		return soLuongMua;
	}

	public void setSoLuongMua(int soLuongMua) {
		this.soLuongMua = soLuongMua;
	}

	public int getSoLuongTang() {
		return soLuongTang;
	}

	public void setSoLuongTang(int soLuongTang) {
		this.soLuongTang = soLuongTang;
	}

	public LocalDate getNgayBD() {
		return ngayBD;
	}

	public void setNgayBD(LocalDate ngayBD) {
		this.ngayBD = ngayBD;
	}

	public LocalDate getNgayKT() {
		return ngayKT;
	}

	public void setNgayKT(LocalDate ngayKT) {
		this.ngayKT = ngayKT;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	

	@Override
	public int hashCode() {
		return Objects.hash(maKM);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KhuyenMai other = (KhuyenMai) obj;
		return Objects.equals(maKM, other.maKM);
	}

	@Override
	public String toString() {
		return "KhuyenMai ["+ ", maKM=" + maKM + ", tenKM=" + tenKM + ", loaiKM=" + loaiKM
				+ ", mucKM=" + mucKM + ", soLuongMua=" + soLuongMua + ", soLuongTang=" + soLuongTang + ", ngayBD="
				+ ngayBD + ", ngayKT=" + ngayKT + ", trangThai=" + trangThai + "]";
	}

}
