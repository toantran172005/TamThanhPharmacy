package entity;

import java.time.LocalDate;
import java.util.Objects;

public class HoaDon {
	private String maHD;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private String loaiTT;
	private LocalDate ngayLap;
	private String diaChiHT;
	private String tenHT;
	private String ghiChu;
	private String hotline;
	private double tienNhan;
	private boolean trangThai;
	
	public HoaDon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HoaDon(String maHD, KhachHang khachHang, NhanVien nhanVien, String loaiTT, LocalDate ngayLap,
			String diaChiHT, String tenHT, String ghiChu, String hotline, double tienNhan, boolean trangThai) {
		super();
		this.maHD = maHD;
		this.khachHang = khachHang;
		this.nhanVien = nhanVien;
		this.loaiTT = loaiTT;
		this.ngayLap = ngayLap;
		this.diaChiHT = diaChiHT;
		this.tenHT = tenHT;
		this.ghiChu = ghiChu;
		this.hotline = hotline;
		this.tienNhan = tienNhan;
		this.trangThai = trangThai;
	}

	public HoaDon(String maHD) {
	    this.maHD = maHD;
	}
	
	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
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

	public String getLoaiTT() {
		return loaiTT;
	}

	public void setLoaiTT(String loaiTT) {
		this.loaiTT = loaiTT;
	}

	public LocalDate getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
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

	public double getTienNhan() {
		return tienNhan;
	}

	public void setTienNhan(double tienNhan) {
		this.tienNhan = tienNhan;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maHD);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHD, other.maHD);
	}

	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien + ", loaiTT=" + loaiTT
				+ ", ngayLap=" + ngayLap + ", diaChiHT=" + diaChiHT + ", tenHT=" + tenHT + ", ghiChu=" + ghiChu
				+ ", hotline=" + hotline + ", tienNhan=" + tienNhan + ", trangThai=" + trangThai + "]";
	}
}
