package controller;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CapNhatThongTinCtrl {
	@FXML public ComboBox<String> cmbGioiTinh;
	@FXML public ImageView imgNhanVien;
	@FXML public Button btnChonAnhNV;
	@FXML public Button btnCapNhat;
    @FXML public Button btnKhioiPhuc;
    @FXML public TextField txtTenNhanVien;
    @FXML public TextField txtSdtNV;
    @FXML public TextField txtTenDangNhap;
    @FXML public TextField txtMaNV;
    @FXML public TextField txtEmail;
    @FXML public DatePicker dtpNgaySinh;
    @FXML public DatePicker dtpNgayVaoLam;
	
	public Image image;
	
	// ====== Biến lưu dữ liệu gốc ======
	public String tenNV_goc;
	public String sdt_goc;
	public String gioiTinh_goc;
	public java.time.LocalDate ngaySinh_goc;
	public String email_goc;
	public Image anh_goc;
	
	public void initialize() {
		setItemComboBox();
		
		Image image = new Image(getClass().getResourceAsStream("/picture/thongTin/anhDaiDien.jpg"));
        imgNhanVien.setImage(image);
        
        btnChonAnhNV.setOnAction(e -> chonAnhMoi());
        btnKhioiPhuc.setOnAction(e -> khoiPhucDuLieu());

        luuDuLieuGoc();
	}
	
	public void setItemComboBox() {
		cmbGioiTinh.getItems().addAll(
		        "Nam",
		        "Nữ",
		        "Khác"
		    );
	}
	
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
        tenNV_goc = txtTenNhanVien.getText();
        sdt_goc = txtSdtNV.getText();
        email_goc = txtEmail.getText();
        gioiTinh_goc = cmbGioiTinh.getValue();
        ngaySinh_goc = dtpNgaySinh.getValue();
        anh_goc = imgNhanVien.getImage();
    }

    // ========== Khôi phục dữ liệu ==========
	public void khoiPhucDuLieu() {
        txtTenNhanVien.setText(tenNV_goc);
        txtSdtNV.setText(sdt_goc);
        txtEmail.setText(email_goc);
        cmbGioiTinh.setValue(gioiTinh_goc);
        dtpNgaySinh.setValue(ngaySinh_goc);
        imgNhanVien.setImage(anh_goc);
    }
	
}
