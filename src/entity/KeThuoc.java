package entity;

public class KeThuoc {
//	private BooleanProperty selected = new SimpleBooleanProperty(false);
	private String maKe;
	private String loaiKe;
	private int sucChua;
	private String moTa;
	private boolean trangThai;

	public KeThuoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KeThuoc(String maKe) {
		super();
		this.maKe = maKe;
	}

	public KeThuoc(String maKe, String loaiKe, int sucChua, String moTa, boolean trangThai) {
		super();
		this.maKe = maKe;
		this.loaiKe = loaiKe;
		this.sucChua = sucChua;
		this.moTa = moTa;
		this.trangThai = trangThai;
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

	public String getMaKe() {
		return maKe;
	}

	public void setMaKe(String maKe) {
		this.maKe = maKe;
	}

	public String getLoaiKe() {
		return loaiKe;
	}

	public void setLoaiKe(String loaiKe) {
		this.loaiKe = loaiKe;
	}

	public int getSucChua() {
		return sucChua;
	}

	public void setSucChua(int sucChua) {
		this.sucChua = sucChua;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	
	@Override
	public String toString() {
	    return "KeThuoc [" +
	            "maKe=" + maKe +
	            ", loaiKe=" + loaiKe +
	            ", sucChua=" + sucChua +
	            ", moTa=" + moTa +
	            ", trangThai=" + trangThai +
	            "]";
	}


}
