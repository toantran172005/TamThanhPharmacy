package controller;

import java.io.File;
import java.time.LocalDate;

import dao.NhanVienDAO;
import entity.NhanVien;
import entity.Thue;
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
	@FXML public Button btnChonAnh;
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
    @FXML public ImageView imgAnhNV;
    
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
	
	private File fileAnhDaChon;
	private String duongDanAnh;
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
    	setAction();
	}
    
    public void setAction() {
    	setItemComboBoxGioiTinh();
		setItemComboBoxChucVu();
		btnLamMoi.setOnMouseClicked(e -> khoiPhucDuLieu());
		btnCapNhat.setOnMouseClicked(e -> capNhatNhanVien());
		btnChonAnh.setOnMouseClicked(e -> chonAnhMoi());
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
//	    image = new Image(getClass().getResourceAsStream(nv.getAnh()));
	    imgAnhNV.setImage(taiAnh(nv.getAnh()));
	    
	    luuDuLieuGoc();
	}
	
	private Image taiAnh(String duongDanTuDB) {
	    File file = new File(System.getProperty("user.dir") + "\\resource" + duongDanTuDB);
	    return file.exists()
	        ? new Image(file.toURI().toString(), false)
	        : new Image(getClass().getResourceAsStream("/picture/default.png"));
	}
	
	// ========== ĐỔI ẢNH NHÂN VIÊN ==========
	public void chonAnhMoi() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Chọn ảnh nhân viên");

	    // chỉ cho chọn file ảnh
	    fileChooser.getExtensionFilters().addAll(
	            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
	    );

	    Stage stage = (Stage) imgAnhNV.getScene().getWindow();

	    File file = fileChooser.showOpenDialog(stage);

	    if (file != null) {
	    	fileAnhDaChon = file; 
	        Image newImage = new Image(file.toURI().toString());
	        imgAnhNV.setImage(newImage);
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
	    anh_goc = imgAnhNV.getImage();
	}

	// ========== Khôi phục dữ liệu ==========
	public void khoiPhucDuLieu() {
	    txtTenNV.setText(tenNV_goc);
	    txtSdt.setText(sdt_goc);
	    txtLuong.setText(luong_goc);
	    txtEmail.setText(email_goc);
	    cmbChucVu.setValue(chucVu_goc);
	    cmbGioiTinh.setValue(gioiTinh_goc);
	    imgAnhNV.setImage(anh_goc);
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
	    	
	    	if (!kiemTraHopLe()) return;
	        // Lấy thông tin từ giao diện
	        String maNV = txtMaNV.getText();
	        String tenNV = txtTenNV.getText();
	        String chucVu = cmbChucVu.getValue();
	        String sdt = toolCtrl.chuyenSoDienThoai(txtSdt.getText());
	        String email = txtEmail.getText();
	        boolean gioiTinh = cmbGioiTinh.getValue().equals("Nam");
	        LocalDate ngaySinh = dtpNgaySinh.getValue();
	        LocalDate ngayVaoLam = dtpNgayVaoLam.getValue();
	        
	        double luong = toolCtrl.chuyenTienSangSo(txtLuong.getText());
	        String anh = luuAnh(maNV);
	        
	        
	        boolean trangThai = lblTrangThai.getText().equals("Còn làm");

	        // Tạo đối tượng NhanVien
	        NhanVien nv = new NhanVien(maNV, tenNV, chucVu, ngaySinh, gioiTinh,sdt, ngayVaoLam, luong, null, trangThai, anh);

	        // Gọi DAO để cập nhật
	        NhanVienDAO dao = new NhanVienDAO();
	        boolean kq = dao.capNhatNhanVien(nv);

	        if (kq) {
	        	 toolCtrl.hienThiXacNhan("Thông báo", "Cập nhật thông tin nhân viên thành công!");
	        } else {
	        	toolCtrl.hienThiXacNhan("Thông báo", "Không thể cập nhật nhân viên. Vui lòng thử lại!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public String luuAnh(String maNV) {
	    try {
	    	//Lấy đường dẫn gốc của dự án
	        String duongDanProject = System.getProperty("user.dir");

	        // Tới thư mục ảnh nhân viên
	        File thuMucDich = new File(duongDanProject, "resource/picture/nhanVien");

	        if (fileAnhDaChon != null) {
	            // Lấy phần đuôi file (jpg, png, ...)
	            String extension = "";
	            int i = fileAnhDaChon.getName().lastIndexOf('.');
	            if (i > 0) {
	                extension = fileAnhDaChon.getName().substring(i);
	            }

	            // Tạo tên file mới theo mã nhân viên
	            String tenFileMoi = maNV + extension;
	            File fileDich = new File(thuMucDich, tenFileMoi);

	            // Ghi đè nếu đã có
	            java.nio.file.Files.copy(
	                fileAnhDaChon.toPath(),
	                fileDich.toPath(),
	                java.nio.file.StandardCopyOption.REPLACE_EXISTING
	            );

	            // Đường dẫn tương đối để lưu vào DB
	            duongDanAnh = "/picture/nhanVien/" + tenFileMoi;
	            System.out.println("✅ Ảnh đã lưu vào: " + fileDich.getAbsolutePath());

	            return duongDanAnh;
	        } else {
	            // Không chọn ảnh mới → giữ ảnh cũ
	            if (imgAnhNV.getImage() != null && imgAnhNV.getImage().getUrl() != null) {
	                String duongDanCu = imgAnhNV.getImage().getUrl();
	                
	                // Trường hợp ảnh cũ đang ở trong resources
	                if (duongDanCu.contains("picture/nhanVien")) {
	                    return duongDanCu.substring(duongDanCu.indexOf("/picture"));
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	
	// ========== KIỂM TRA DỮ LIỆU HỢP LỆ ==========
	private boolean kiemTraHopLe() {
		String ten = txtTenNV.getText().trim();
	    // Kiểm tra họ tên
	    if (ten.isEmpty()) {
	        toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Tên nhân viên không được để trống!");
	        txtTenNV.requestFocus();
	        return false;
	    }
	    if (ten.matches("^[\\p{L}\\s]+$")) {
			toolCtrl.hienThiThongBao("Lỗi nhập liệu", "Tên nhân viên chỉ được chứa chữ cái và khoảng trắng.", false);
			return false;
		}

	    // Kiểm tra số điện thoại
	    String sdt = txtSdt.getText().trim();
	    if (sdt.isEmpty()) {
	        toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Số điện thoại không được để trống!");
	        txtSdt.requestFocus();
	        return false;
	    }
	    if (!sdt.matches("^0\\d{9}$")) { 
	        toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
	        txtSdt.requestFocus();
	        return false;
	    }

	    // Kiểm tra email
	    String email = txtEmail.getText().trim();
	    if (!email.isEmpty() && !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
	        toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Email không đúng định dạng!");
	        txtEmail.requestFocus();
	        return false;
	    }

	    // Kiểm tra lương
	    String luongText = txtLuong.getText().trim().replace(",", "").replace("₫", "");
	    if (luongText.isEmpty()) {
	        toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Lương không được để trống!");
	        txtLuong.requestFocus();
	        return false;
	    }
	    try {
	        double luong = Double.parseDouble(luongText);
	        if (luong <= 0) {
	            toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Lương phải lớn hơn 0!");
	            txtLuong.requestFocus();
	            return false;
	        }
	    } catch (NumberFormatException e) {
	        toolCtrl.hienThiXacNhan("Lỗi nhập liệu", "Lương không hợp lệ!");
	        txtLuong.requestFocus();
	        return false;
	    }

	    return true;
	}
}
