package controller;

import java.io.File;
import java.time.LocalDate;

import dao.NhanVienDAO;
import entity.NhanVien;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    @FXML private Label lblTrangThai;
    
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
    
    private TrangChuNVCtrl trangChuNVCtrl;
    private TrangChuQLCtrl trangChuQLCtrl;
    private ToolCtrl toolCtrl = new ToolCtrl();

    public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
        this.trangChuNVCtrl = ctrl;
    }

    public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
        this.trangChuQLCtrl = ctrl;
    }
    
    public void initialize() {
		setItemComboBoxGioiTinh();
		setItemComboBoxChucVu();
		btnLamMoi.setOnMouseClicked(e -> khoiPhucDuLieu());
		btnCapNhat.setOnMouseClicked(e -> capNhatNhanVien());
	}
    
    public void setItemComboBoxGioiTinh() {
		cmbGioiTinh.getItems().addAll("Nam", "Nữ");
		cmbGioiTinh.setValue("Nam");
	}
    
    public void setItemComboBoxChucVu() {
  		cmbChucVu.getItems().addAll("Nhân viên", "Quản lý");
  		cmbChucVu.setValue("Nhân viên");
  	}
	

	public void setNhanVienHienTai(NhanVien nv) {
	    if (nv == null) return;
	    txtMaNV.setText(nv.getMaNV());
	    txtTenNV.setText(nv.getTenNV());
	    cmbChucVu.setValue(nv.getChucVu());
	    txtSdt.setText(toolCtrl.chuyenSoDienThoai(nv.getSdt()));
	    txtLuong.setText(toolCtrl.dinhDangVND(nv.getLuong()));
	    cmbGioiTinh.setValue(nv.isGioiTinh() ? "Nam" : "Nữ");
	    dtpNgaySinh.setValue(nv.getNgaySinh());
	    dtpNgayVaoLam.setValue(nv.getNgayVaoLam());
	    txtThue.setText(nv.getThue() != null ? String.valueOf(nv.getThue().getTiLeThue()) : "");
	    lblTrangThai.setText(nv.isTrangThai() ? "Còn làm" : "Đã nghỉ");
	    System.out.println("Ngày sinh NV: " + nv.getNgaySinh());
	    System.out.println("Ngày vào làm NV: " + nv.getNgayVaoLam());
	    
	    luuDuLieuGoc();
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
	    tenNV_goc = txtTenNV.getText();
	    sdt_goc = txtSdt.getText();
	    luong_goc = txtLuong.getText();
	    email_goc = txtEmail.getText();
	    chucVu_goc = cmbChucVu.getValue();
	    gioiTinh_goc = cmbGioiTinh.getValue();
//	    anh_goc = imgNhanVien.getImage();
	}

	// ========== Khôi phục dữ liệu ==========
	public void khoiPhucDuLieu() {
	    txtTenNV.setText(tenNV_goc);
	    txtSdt.setText(sdt_goc);
	    txtLuong.setText(luong_goc);
	    txtEmail.setText(email_goc);
	    cmbChucVu.setValue(chucVu_goc);
	    cmbGioiTinh.setValue(gioiTinh_goc);
//	    imgNhanVien.setImage(anh_goc);
	}

	
	// ========== QUAY LẠI TRANG TÌM KIẾM NHÂN VIÊN ==========
	private void quayLai() {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/ChiTietNhanVien.fxml", ChiTietNhanVienCtrl.class);
		} else if (trangChuQLCtrl != null) {
			trangChuQLCtrl.moTrang("/fxml/ChiTietNhanVien.fxml", ChiTietNhanVienCtrl.class);
		} else {
			System.out.println("⚠ Không có tham chiếu TrangChu");
		}
	}
	
	public void capNhatNhanVien() {
	    try {
	        // Lấy thông tin từ giao diện
	        String maNV = txtMaNV.getText();
	        String tenNV = txtTenNV.getText();
	        String chucVu = cmbChucVu.getValue();
	        String sdt = txtSdt.getText();
	        String email = txtEmail.getText();
	        boolean gioiTinh = cmbGioiTinh.getValue().equals("Nam");
	        LocalDate ngaySinh = dtpNgaySinh.getValue();
	        LocalDate ngayVaoLam = dtpNgayVaoLam.getValue();

	        double luong = toolCtrl.chuyenTienSangSo(txtLuong.getText());

	        // Tạo đối tượng NhanVien
	        NhanVien nv = new NhanVien(maNV, tenNV, chucVu, ngaySinh, gioiTinh, sdt, ngayVaoLam, luong, null, true, null);

	        // Gọi DAO để cập nhật
	        NhanVienDAO dao = new NhanVienDAO();
	        boolean kq = dao.capNhatNhanVien(nv);

	        if (kq) {
	            System.out.println("✅ Đã cập nhật nhân viên: " + maNV);
	        } else {
	            System.out.println("❌ Cập nhật nhân viên thất bại.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
