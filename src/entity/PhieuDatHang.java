package entity;

import java.time.LocalDate;
import java.util.Objects;

public class PhieuDatHang {
	private String maPDH;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private LocalDate ngayDat;
	private LocalDate ngayHen;
	private String diaChiHT;
	private String tenHT;
	private String ghiChu;
	private String hotline;
	private boolean trangThai;
	
	public PhieuDatHang() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuDatHang(String maPDH, KhachHang khachHang, NhanVien nhanVien, LocalDate ngayDat, LocalDate ngayHen,
			String diaChiHT, String tenHT, String ghiChu, String hotline, boolean trangThai) {
		super();
		this.maPDH = maPDH;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.ngayDat = ngayDat;
		this.ngayHen = ngayHen;
		this.diaChiHT = diaChiHT;
		this.tenHT = tenHT;
		this.ghiChu = ghiChu;
		this.hotline = hotline;
		this.trangThai = trangThai;
	}

	public String getMaPDH() {
		return maPDH;
	}

	public void setMaPDH(String maPDH) {
		this.maPDH = maPDH;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public LocalDate getNgayDat() {
		return ngayDat;
	}

	public void setNgayDat(LocalDate ngayDat) {
		this.ngayDat = ngayDat;
	}

	public LocalDate getNgayHen() {
		return ngayHen;
	}

	public void setNgayHen(LocalDate ngayHen) {
		this.ngayHen = ngayHen;
	}

	public String getDiaChiHT() {
		return diaChiHT;
	}

	public void setDiaChiHT(String diaChiHT) {
		this.diaChiHT = diaChiHT;
	}

	public String getTenHT() {
		return tenHT;
	}

	public void setTenHT(String tenHT) {
		this.tenHT = tenHT;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPDH);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhieuDatHang other = (PhieuDatHang) obj;
		return Objects.equals(maPDH, other.maPDH);
	}

	@Override
	public String toString() {
		return "PhieuDatHang [maPDH=" + maPDH + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien + ", ngayDat="
				+ ngayDat + ", ngayHen=" + ngayHen + ", diaChiHT=" + diaChiHT + ", tenHT=" + tenHT + ", ghiChu="
				+ ghiChu + ", hotline=" + hotline + ", trangThai=" + trangThai + "]";
	}
}
