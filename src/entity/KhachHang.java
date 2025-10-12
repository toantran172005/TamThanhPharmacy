package entity;

public class KhachHang {
	private String maKH;
	private String tenKH;
	private String sdt;
	private int tuoi;
	private boolean trangThai;
	private int tongDonHang;
	private double tongTien;

	public KhachHang() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KhachHang(String maKH, String tenKH, String sdt, int tuoi, boolean trangThai) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.sdt = sdt;
		this.tuoi = tuoi;
		this.trangThai = trangThai;
	}

	public KhachHang(String maKH, String tenKH, int tongDonHang, double tongTien) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
		this.tongDonHang = tongDonHang;
		this.tongTien = tongTien;
	}

	public int getTongDonHang() {
		return tongDonHang;
	}

	public void setTongDonHang(int tongDonHang) {
		this.tongDonHang = tongDonHang;
	}

	public double getTongTien() {
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
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
