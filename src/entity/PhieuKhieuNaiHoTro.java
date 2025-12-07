package entity;

import java.time.LocalDate;


public class PhieuKhieuNaiHoTro {

	private String maPhieu;
	private NhanVien nhanVien;
	private KhachHang khachHang;
	private LocalDate ngayLap;
	private String noiDung;
	private String loaiDon;
	private String trangThai;

	public PhieuKhieuNaiHoTro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuKhieuNaiHoTro(String maPhieu, NhanVien nhanVien, KhachHang khachHang, LocalDate ngayLap, String noiDung,
			String loaiDon, String trangThai) {
		super();
		this.maPhieu = maPhieu;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
		this.ngayLap = ngayLap;
		this.noiDung = noiDung;
		this.loaiDon = loaiDon;
		this.trangThai = trangThai;
	}

	public String getMaPhieu() {
		return maPhieu;
	}

	public void setMaPhieu(String maPhieu) {
		this.maPhieu = maPhieu;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public KhachHang getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}

	public LocalDate getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDate ngayLap) {
		this.ngayLap = ngayLap;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public String getLoaiDon() {
		return loaiDon;
	}

	public void setLoaiDon(String loaiDon) {
		this.loaiDon = loaiDon;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "KhieuNaiHoTro[" + "maPhieu=" + maPhieu + ", nhanVien="
				+ (nhanVien != null ? nhanVien.getTenNV() : "null") + ", khachHang="
				+ (khachHang != null ? khachHang.getTenKH() : "null") + ", ngayLap=" + ngayLap + ", noiDung=" + noiDung
				+ ", loaiDon=" + loaiDon + ", trangThai=" + trangThai + "]";
	}

}
