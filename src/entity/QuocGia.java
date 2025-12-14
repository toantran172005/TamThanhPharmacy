package entity;

import java.util.Objects;

public class QuocGia {
	public String maQG;
	public String tenQG;
	
	public QuocGia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuocGia(String maQG, String tenQG) {
		super();
		this.maQG = maQG;
		this.tenQG = tenQG;
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
	public int hashCode() {
		return Objects.hash(maQG);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuocGia other = (QuocGia) obj;
		return Objects.equals(maQG, other.maQG);
	}

	@Override
	public String toString() {
		return "QuocGia [maQG=" + maQG + ", tenQG=" + tenQG + "]";
	}
	
}
