package controller;

public class DanhSachHDCtrl {
	
	public TrangChuNVCtrl trangChuCtrl;
	
	public void setTrangChuCtrl(TrangChuNVCtrl ctrl) {
	    this.trangChuCtrl = ctrl;
	}
	
	public void chuyenDenTrangTaoHD() {
		trangChuCtrl.doiCenterPane("/fxml/TaoHoaDon.fxml");
	}
}
