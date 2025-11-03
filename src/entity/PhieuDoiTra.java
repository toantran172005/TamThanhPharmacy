package entity;

import java.time.LocalDate;
import java.util.Objects;

public class PhieuDoiTra {
	private String maPhieuDT;
	private HoaDon hoaDon;
	private NhanVien nhanVien;
	private LocalDate ngayDoiTra;
	private String lyDo;
	
	public PhieuDoiTra() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhieuDoiTra(String maPhieuDT, HoaDon hoaDon, NhanVien nhanVien, LocalDate ngayDoiTra, String lyDo) {
		super();
		this.maPhieuDT = maPhieuDT;
		this.hoaDon = hoaDon;
		this.nhanVien = nhanVien;
		this.ngayDoiTra = ngayDoiTra;
		this.lyDo = lyDo;
	}

	public String getMaPhieuDT() {
		return maPhieuDT;
	}

	public void setMaPhieuDT(String maPhieuDT) {
		this.maPhieuDT = maPhieuDT;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}

	public LocalDate getNgayDoiTra() {
		return ngayDoiTra;
	}

	public void setNgayDoiTra(LocalDate ngayDoiTra) {
		this.ngayDoiTra = ngayDoiTra;
	}

	public String getLyDo() {
		return lyDo;
	}

	public void setLyDo(String lyDo) {
		this.lyDo = lyDo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maPhieuDT);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhieuDoiTra other = (PhieuDoiTra) obj;
		return Objects.equals(maPhieuDT, other.maPhieuDT);
	}

	@Override
	public String toString() {
		return "PhieuDoiTra [maPhieuDT=" + maPhieuDT + ", hoaDon=" + hoaDon + ", nhanVien=" + nhanVien
				+ ", ngayDoiTra=" + ngayDoiTra + ", lyDo=" + lyDo + "]";
	}
}
