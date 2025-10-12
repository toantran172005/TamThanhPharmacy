package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CapNhatNhanVienCtrl {
	@FXML public ComboBox<String> cmbGioiTinh;
	@FXML public ComboBox<String> cmbChucVu;
	@FXML public ImageView imgNhanVien;
	@FXML public Button btnChonAnhNV;
	@FXML public Button btnCapNhat;
    @FXML public Button btnLamMoi;
    @FXML public TextField txtMaNV;
    @FXML public TextField txtTenNV;
    @FXML public TextField txtSdt;
    @FXML public TextField txtLuong;
    @FXML public TextField txtEmail;
    @FXML public DatePicker dtpNgaySinh;
    @FXML public DatePicker dtpNgayVaoLam;
    @FXML public TextField txtThue;
    
    public Image image;
	
	// ====== Biến lưu dữ liệu gốc ======
	public String tenNV_goc;
	public String luong_goc;
	public String thue_goc;
	public String sdt_goc;
	public String chucVu_goc;
	public String gioiTinh_goc;
	public java.time.LocalDate ngaySinh_goc;
	public String email_goc;
	public Image anh_goc;
	
	// ========== ĐỔI ẢNH NHÂN VIÊN ==========
	public void chonAnhMoi() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Chọn ảnh nhân viên");

	    // chỉ cho chọn file ảnh
	    fileChooser.getExtensionFilters().addAll(
	            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
	    );

	    Stage stage = (Stage) imgNhanVien.getScene().getWindow();

	    File file = fileChooser.showOpenDialog(stage);

	    if (file != null) {
	        Image newImage = new Image(file.toURI().toString());
	        imgNhanVien.setImage(newImage);
	        System.out.println("Ảnh mới: " + file.getAbsolutePath());
	    }
	}
	
	// ========== Lưu dữ liệu gốc ==========
	public void luuDuLieuGoc() {
	    tenNV_goc = txtTenNV.getText();
	    sdt_goc = txtSdt.getText();
	    luong_goc = txtLuong.getText();
	    email_goc = txtEmail.getText();
	    chucVu_goc = cmbChucVu.getValue();
	    gioiTinh_goc = cmbGioiTinh.getValue();
	    anh_goc = imgNhanVien.getImage();
	}

	// ========== Khôi phục dữ liệu ==========
	public void khoiPhucDuLieu() {
	    txtTenNV.setText(tenNV_goc);
	    txtSdt.setText(sdt_goc);
	    txtLuong.setText(luong_goc);
	    txtEmail.setText(email_goc);
	    cmbChucVu.setValue(chucVu_goc);
	    cmbGioiTinh.setValue(gioiTinh_goc);
	    imgNhanVien.setImage(anh_goc);
	}

}
