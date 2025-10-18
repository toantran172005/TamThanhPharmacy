package entity;

import java.time.LocalDate;
import java.util.Objects;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class KhuyenMai {
	BooleanProperty selected = new SimpleBooleanProperty(false);
	String maKM;
	String tenKM;
	String phuongThucKM;
	int mucKM;
	LocalDate ngayBD;
	LocalDate ngayKT;
	boolean trangThai;
	
	public KhuyenMai() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhuyenMai(String maKM, String tenKM, String phuongThucKM, int mucKM, LocalDate ngayBD, LocalDate ngayKT,
			boolean trangThai) {
		super();
		this.maKM = maKM;
		this.tenKM = tenKM;
		this.phuongThucKM = phuongThucKM;
		this.mucKM = mucKM;
		this.ngayBD = ngayBD;
		this.ngayKT = ngayKT;
		this.trangThai = trangThai;
	}

	public boolean isSelected() {
		return selected.get();
	}

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}

	public BooleanProperty selectedProperty() {
		return selected;
	}

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

	public String getPhuongThucKM() {
		return phuongThucKM;
	}

	public void setPhuongThucKM(String phuongThucKM) {
		this.phuongThucKM = phuongThucKM;
	}

	public int getMucKM() {
		return mucKM;
	}

	public void setMucKM(int mucKM) {
		this.mucKM = mucKM;
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
		return "KhuyenMai [maKM=" + maKM + ", tenKM=" + tenKM + ", phuongThucKM=" + phuongThucKM + ", mucKM=" + mucKM
				+ ", ngayBD=" + ngayBD + ", ngayKT=" + ngayKT + ", trangThai=" + trangThai + "]";
	}	
	
}
