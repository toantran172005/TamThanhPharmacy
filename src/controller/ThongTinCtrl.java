package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ThongTinCtrl {
	@FXML
    private Button btnChinhSuaTT;
	
	
	@FXML
    private ImageView imgNhanVien;
	private Image image;

    private TrangChuNVCtrl trangChuNVCtrl;  // tham chiếu controller cha
    private TrangChuQLCtrl trangChuQLCtrl; 

    public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
        this.trangChuNVCtrl = ctrl;
    }
    
    public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
        this.trangChuQLCtrl = ctrl;
    }

    @FXML
    private void initialize() {
        btnChinhSuaTT.setOnAction(e -> moTrangCapNhat());
        
        Image image = new Image(getClass().getResourceAsStream("/picture/thongTin/anhDaiDien.jpg"));
        imgNhanVien.setImage(image);
    }

    // ========== MỞ TRANG CẬP NHẬT THÔNG TIN NHÂN VIÊN ==========
    private void moTrangCapNhat() {
        if (trangChuNVCtrl != null) {
            trangChuNVCtrl.doiCenterPane("/fxml/CapNhatThongTin.fxml");
        } 
        else if (trangChuQLCtrl != null) {
            trangChuQLCtrl.doiCenterPane("/fxml/CapNhatThongTin.fxml");
        } 
        else {
            System.out.println("⚠ Không có tham chiếu TrangChu");
        }
    }
    
}
