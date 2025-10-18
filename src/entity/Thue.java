package entity;

public class Thue {
	private String maThue;
	private String loaiThue;
	private Double tyLeThue;
	private String moTa;

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
		this.tyLeThue = tyLeThue;
	}

	public Thue(String maThue, String loaiThue, Double tiLeThue, String moTa) {
		super();
		this.maThue = maThue;
		this.loaiThue = loaiThue;
		this.tyLeThue = tiLeThue;
		this.moTa = moTa;
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
		return tyLeThue;
	}

	public void setTiLeThue(Double tyLeThue) {
		this.tyLeThue = tyLeThue;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	@Override
	public String toString() {
		return loaiThue + " - " + tyLeThue + "%";
	}

}
