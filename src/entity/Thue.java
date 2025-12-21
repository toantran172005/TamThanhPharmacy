package entity;

public class Thue {
	public String maThue;
	public String loaiThue;
	public Double tiLeThue;
	public String moTa;
	public Boolean trangThai;

	public Thue() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Thue(String maThue) {
		super();
		this.maThue = maThue;
	}

	public Thue(String maThue, Double tyLeThue) {
		super();
		this.maThue = maThue;
		this.tiLeThue = tyLeThue;
	}

	public Thue(String maThue, String loaiThue, Double tiLeThue, String moTa) {
		super();
		this.maThue = maThue;
		this.loaiThue = loaiThue;
		this.tiLeThue = tiLeThue;
		this.moTa = moTa;
	}
	
	

	public Thue(String maThue, String loaiThue, Double tiLeThue, String moTa, Boolean trangThai) {
		super();
		this.maThue = maThue;
		this.loaiThue = loaiThue;
		this.tiLeThue = tiLeThue;
		this.moTa = moTa;
		this.trangThai = trangThai;
	}

	public String getMaThue() {
		return maThue;
	}

	public void setMaThue(String maThue) {
		this.maThue = maThue;
	}

	public String getLoaiThue() {
		return loaiThue;
	}

	public void setLoaiThue(String loaiThue) {
		this.loaiThue = loaiThue;
	}

	public Double getTiLeThue() {
		return tiLeThue;
	}

	public void setTiLeThue(Double tyLeThue) {
		this.tiLeThue = tyLeThue;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}
	
	

	public Boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(Boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public String toString() {
		return loaiThue + " - " + tiLeThue + "%";
	}

}
