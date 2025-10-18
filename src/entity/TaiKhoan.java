package entity;

public class TaiKhoan {
	private String maTK;
	private NhanVien nhanVien;
	private String tenDangNhap;
	private String matKhau;
	private Boolean trangThai;
	private String loaiTK;
	private String email;
	
	public TaiKhoan() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaiKhoan(String maTK, NhanVien nhanVien, String tenDangNhap, String matKhau, Boolean trangThai,
			String loaiTK, String email) {
		super();
		this.maTK = maTK;
		this.nhanVien = nhanVien;
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
		this.trangThai = trangThai;
		this.loaiTK = loaiTK;
		this.email = email;
	}

	public String getMaTK() {
		return maTK;
	}

	public void setMaTK(String maTK) {
		this.maTK = maTK;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public Boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}

	public String getLoaiTK() {
		return loaiTK;
	}

	public void setLoaiTK(String loaiTK) {
		this.loaiTK = loaiTK;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "TaiKhoan [maTK=" + maTK + ", nhanVien=" + nhanVien + ", tenDangNhap=" + tenDangNhap + ", matKhau="
				+ matKhau + ", trangThai=" + trangThai + ", loaiTK=" + loaiTK + ", email=" + email + "]";
	}
	
}
