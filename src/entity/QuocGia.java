package entity;

public class QuocGia {
	String maQG;
	String tenQG;
	
	public QuocGia() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QuocGia(String maQG, String tenQG) {
		super();
		this.maQG = maQG;
		this.tenQG = tenQG;
	}
	public QuocGia(String quocGia) {
		// TODO Auto-generated constructor stub
	}
	public String getMaQG() {
		return maQG;
	}
	public void setMaQG(String maQG) {
		this.maQG = maQG;
	}
	public String getTenQG() {
		return tenQG;
	}
	public void setTenQG(String tenQG) {
		this.tenQG = tenQG;
	}
	
	@Override
	public String toString() {
		return "QuocGia [maQG=" + maQG + ", tenQG=" + tenQG + "]";
	}
	
	
}
