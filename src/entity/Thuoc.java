package entity;

import java.time.LocalDate;
import java.util.Objects;

//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.SimpleBooleanProperty;

public class Thuoc {
	// Thuộc tính chính
//	private BooleanProperty selected = new SimpleBooleanProperty(false);
	String maThuoc;
	Thue thue;
	KeThuoc keThuoc;
	DonViTinh dvt;
	NhaCungCap ncc;
	String tenThuoc;
	String dangThuoc;
	double giaBan;
	LocalDate hanSuDung;
	Boolean trangThai;
	String anh;
	String noiSanXuat;
	QuocGia quocGia;
	// Thuộc tính tạm
	int stt;
	int soLo;
	int soLuong;
	String loaiThue;
	int soLuongBan;
	int doanhThu;
	int soLuongDat;

	public Thuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Thuoc(String maThuoc, String tenThuoc) {
		super();
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
	}

	public Thuoc(String maThuoc, Thue thue, KeThuoc keThuoc, DonViTinh dvt, NhaCungCap ncc, String tenThuoc,
			String dangThuoc, float giaBan, LocalDate hanSuDung, Boolean trangThai, String anh, int soLuong) {
		super();
		this.maThuoc = maThuoc;
		this.thue = thue;
		this.keThuoc = keThuoc;
		this.dvt = dvt;
		this.ncc = ncc;
		this.tenThuoc = tenThuoc;
		this.dangThuoc = dangThuoc;
		this.giaBan = giaBan;
		this.hanSuDung = hanSuDung;
		this.trangThai = trangThai;
		this.anh = anh;
		this.soLuong = soLuong;
	}

	public Thuoc(String maThuoc, String tenThuoc, Boolean trangThai) {
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.trangThai = trangThai;
	}

	public Thuoc(String maThuoc, String tenThuoc, int soLo, String dangThuoc, DonViTinh dvt,
			LocalDate hanSuDung, int soLuong, double giaBan, Thue thue, String loaiThue) {
		super();
		this.maThuoc = maThuoc;
		this.thue = thue;
		this.dvt = dvt;
		this.tenThuoc = tenThuoc;
		this.dangThuoc = dangThuoc;
		this.giaBan = giaBan;
		this.hanSuDung = hanSuDung;
		this.soLo = soLo;
		this.soLuong = soLuong;
		this.loaiThue = loaiThue;
	}

	public Thuoc(String maThuoc, String tenThuoc, int soLuongBan, int doanhThu) {
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.soLuongBan = soLuongBan;
		this.doanhThu = doanhThu;
	}

	public Thuoc(String maThuoc, String tenThuoc, DonViTinh dvt, int soLuongDat) {
		this.maThuoc = maThuoc;
		this.tenThuoc = tenThuoc;
		this.dvt = dvt;	
		this.soLuongDat = soLuongDat;
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

	public Thuoc(String maThuoc, Thue thue, KeThuoc keThuoc, DonViTinh dvt, NhaCungCap ncc, String tenThuoc,
			String dangThuoc, float giaBan, LocalDate hanSuDung, boolean trangThai, String anh, int soLuong,String noiSanXuat) {
		super();
		this.maThuoc = maThuoc;
		this.thue = thue;
		this.keThuoc = keThuoc;
		this.dvt = dvt;
		this.ncc = ncc;
		this.tenThuoc = tenThuoc;
		this.dangThuoc = dangThuoc;
		this.giaBan = giaBan;
		this.hanSuDung = hanSuDung;
		this.trangThai = trangThai;
		this.anh = anh;
		this.soLuong = soLuong;
		this.noiSanXuat = noiSanXuat;
	}
	
	

	public Thuoc(String maThuoc, Thue thue, KeThuoc keThuoc, DonViTinh dvt, NhaCungCap ncc, String tenThuoc,
			String dangThuoc, double giaBan, LocalDate hanSuDung, Boolean trangThai, String anh, String noiSanXuat,
			QuocGia quocGia, int stt, int soLo, int soLuong, String loaiThue, int soLuongBan, int doanhThu,
			int soLuongDat) {
		super();
		this.maThuoc = maThuoc;
		this.thue = thue;
		this.keThuoc = keThuoc;
		this.dvt = dvt;
		this.ncc = ncc;
		this.tenThuoc = tenThuoc;
		this.dangThuoc = dangThuoc;
		this.giaBan = giaBan;
		this.hanSuDung = hanSuDung;
		this.trangThai = trangThai;
		this.anh = anh;
		this.noiSanXuat = noiSanXuat;
		this.quocGia = quocGia;
		this.stt = stt;
		this.soLo = soLo;
		this.soLuong = soLuong;
		this.loaiThue = loaiThue;
		this.soLuongBan = soLuongBan;
		this.doanhThu = doanhThu;
		this.soLuongDat = soLuongDat;
	}

	public QuocGia getQuocGia() {
		return quocGia;
	}

	public void setQuocGia(QuocGia quocGia) {
		this.quocGia = quocGia;
	}

	public String getMaThuoc() {
		return maThuoc;
	}

	public void setMaThuoc(String maThuoc) {
		this.maThuoc = maThuoc;
	}

	public Thue getThue() {
		return thue;
	}

	public void setThue(Thue thue) {
		this.thue = thue;
	}

	public KeThuoc getKeThuoc() {
		return keThuoc;
	}

	public void setKeThuoc(KeThuoc keThuoc) {
		this.keThuoc = keThuoc;
	}

	public DonViTinh getDvt() {
		return dvt;
	}

	public void setDvt(DonViTinh dvt) {
		this.dvt = dvt;
	}

	public NhaCungCap getNcc() {
		return ncc;
	}

	public void setNcc(NhaCungCap ncc) {
		this.ncc = ncc;
	}

	public String getTenThuoc() {
		return tenThuoc;
	}

	public void setTenThuoc(String tenThuoc) {
		this.tenThuoc = tenThuoc;
	}

	public String getDangThuoc() {
		return dangThuoc;
	}

	public void setDangThuoc(String dangThuoc) {
		this.dangThuoc = dangThuoc;
	}
	public double getGiaBan() {
		return giaBan;
	}

	public void setGiaBan(double giaBan) {
		this.giaBan = giaBan;
	}

	public LocalDate getHanSuDung() {
		return hanSuDung;
	}

	public void setHanSuDung(LocalDate hanSuDung) {
		this.hanSuDung = hanSuDung;
	}

	public Boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	public int getSoLo() {
		return soLo;
	}

	public void setSoLo(int soLo) {
		this.soLo = soLo;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public String getLoaiThue() {
		return loaiThue;
	}

	public void setLoaiThue(String loaiThue) {
		this.loaiThue = loaiThue;
	}

	public int getSoLuongBan() {
		return soLuongBan;
	}

	public void setSoLuongBan(int soLuongBan) {
		this.soLuongBan = soLuongBan;
	}

	public int getDoanhThu() {
		return doanhThu;
	}

	public void setDoanhThu(int doanhThu) {
		this.doanhThu = doanhThu;
	}

	public int getSoLuongDat() {
		return soLuongDat;
	}

	public void setSoLuongDat(int soLuongDat) {
		this.soLuongDat = soLuongDat;
	}
	

	public String getNoiSanXuat() {
		return noiSanXuat;
	}

	public void setNoiSanXuat(String noiSanXuat) {
		this.noiSanXuat = noiSanXuat;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maThuoc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Thuoc other = (Thuoc) obj;
		return Objects.equals(maThuoc, other.maThuoc);
	}

	@Override
	public String toString() {
		return tenThuoc;
	}
	
	
}
