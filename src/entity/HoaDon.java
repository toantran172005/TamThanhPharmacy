package entity;

import java.time.LocalDateTime;

public class HoaDon {
	private String maHD;
	private KhachHang khachHang;
	private NhanVien nhanVien;
	private String loaiTT;
	private LocalDateTime ngayLap;
	private String diaChiHT;
	private String tenHT;
	private String ghiChu;
	private String hotline;

	public HoaDon() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HoaDon(String maHD, KhachHang khachHang, NhanVien nhanVien, String loaiTT, LocalDateTime ngayLap,
			String diaChiHT, String tenHT, String ghiChu, String hotline) {
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

	public LocalDateTime getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDateTime ngayLap) {
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

	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", khachHang=" + khachHang + ", nhanVien=" + nhanVien + ", loaiTT=" + loaiTT
				+ ", ngayLap=" + ngayLap + ", diaChiHT=" + diaChiHT + ", tenHT=" + tenHT + ", ghiChu=" + ghiChu
				+ ", hotline=" + hotline + "]";
	}

}
