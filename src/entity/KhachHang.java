package entity;

import java.util.Map;

import dao.KhachHangDAO;

public class KhachHang {
	// Thuộc tính chính
	private String maKH;
	private String tenKH;
	private String sdt;
	private int tuoi;
	private boolean trangThai;

	public KhachHang() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhachHang(String maKH, String tenKH, String sdt) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sdt = sdt;
	}

	public KhachHang(String maKH, String tenKH, String sdt, int tuoi, boolean trangThai) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sdt = sdt;
		this.tuoi = tuoi;
		this.trangThai = trangThai;
	}

	public KhachHang(String maKH, String tenKH, int tuoi, String sdt) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sdt = sdt;
		this.tuoi = tuoi;
	}

	public KhachHang(String string) {
		// TODO Auto-generated constructor stub
	}

	public String getMaKH() {
		return maKH;
	}

	public void setMaKH(String maKH) {
		this.maKH = maKH;
	}

	public String getTenKH() {
		return tenKH;
	}

	public void setTenKH(String tenKH) {
		this.tenKH = tenKH;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public int getTuoi() {
		return tuoi;
	}

	public void setTuoi(int tuoi) {
		this.tuoi = tuoi;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return "KhachHang [" + "maKH=" + maKH + ", tenKH=" + tenKH + ", sdt=" + sdt + ", tuoi=" + tuoi + ", trangThai="
				+ trangThai + "]";
	}

}
