package controller;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import dao.NhanVienDAO;
import dao.ThueDAO;
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

public class ThemNhanVienCtrl {
	@FXML public ComboBox<String> cmbGioiTinh;
	@FXML public ComboBox<String> cmbChucVu;
	@FXML public ComboBox<Thue> cmbThue;
	@FXML public Button btnChonAnh;
	@FXML public Button btnThem;
    @FXML public Button btnLamMoi;
    @FXML public TextField txtTenNV;
    @FXML public TextField txtSdt;
    @FXML public TextField txtLuong;
    @FXML public TextField txtEmail;
    @FXML public DatePicker dpNgaySinh;
    @FXML public DatePicker dpNgayVaoLam;
    @FXML public ImageView imgAnhNV;
    
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
		setItemComboBoxThue();
		btnLamMoi.setOnMouseClicked(e -> khoiPhucDuLieu());
		btnThem.setOnMouseClicked(e -> themNhanVien());
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
    
    public void setItemComboBoxThue() {
            ThueDAO thueDAO = new ThueDAO();
            List<Thue> dsThue = thueDAO.layListThue();

            cmbThue.getItems().clear();
            cmbThue.getItems().addAll(dsThue);

            if (!dsThue.isEmpty()) {
                cmbThue.setValue(dsThue.get(0));
            }
  	}
    
    public boolean ktTenNhanVienHopLe() {
		String ten = txtTenNV.getText().trim();
		String regex = "^[\\p{L}\\s]+$";

		if (ten.isEmpty()) {
			toolCtrl.hienThiThongBao("Tên nhân viên không hợp lệ!", "Tên không được để trống", false);
			txtTenNV.requestFocus();
			return false;
		} else if (!ten.matches(regex)) {
			toolCtrl.hienThiThongBao("Tên khách hàng không hợp lệ!", "Tên không được chứa số hoặc ký tự đặc biệt", false);
			txtTenNV.requestFocus();
			txtTenNV.selectAll();
			return false;
		}
		txtSdt.requestFocus();
		return true;
	}

	public boolean ktSoDienThoaiHopLe() {
		String sdt = txtSdt.getText().trim();
		String regex = "^0\\d{9}$";

		if (sdt.isEmpty()) {
			toolCtrl.hienThiThongBao("Số điện thoại không hợp lệ!", "Không được để trống", false);
			txtSdt.requestFocus();
			return false;
		} else if (!sdt.matches(regex)) {
			toolCtrl.hienThiThongBao("Số điện thoại không hợp lệ!", "Phải gồm 10 chữ số và bắt đầu bằng 0", false);
			txtSdt.requestFocus();
			txtSdt.selectAll();
			return false;
		}
		txtSdt.requestFocus();
		return true;
	}

	public boolean ktTuoiHopLe() {
		LocalDate tuoiStr = dpNgaySinh.getValue();
		LocalDate ngayHienTai = LocalDate.now();
	    int tuoi = ngayHienTai.getYear() - tuoiStr.getYear();
		try {

			if (tuoi < 0) {
				toolCtrl.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi không được là số âm.", false);
				dpNgaySinh.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			toolCtrl.hienThiThongBao("Tuổi không hợp lệ!", "Tuổi phải là số nguyên.", false);
			dpNgaySinh.requestFocus();
			return false;
		}
		return true;
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
 	
 	public void khoiPhucDuLieu() {
	    txtTenNV.setText("");
	    txtSdt.setText("");
	    txtLuong.setText("");
	    txtEmail.setText("");
	    cmbChucVu.setValue("Nhân viên");
	    cmbGioiTinh.setValue("Nam");
	    imgAnhNV.setImage(null);
	}
 	
 	public void themNhanVien() {
	    try {
	        // Lấy thông tin từ giao diện
	    	
	        String maNV = toolCtrl.taoKhoaChinh("NV");
	        String tenNV = txtTenNV.getText();
	        String chucVu = cmbChucVu.getValue();
	        String sdt = toolCtrl.chuyenSoDienThoai(txtSdt.getText());
	        String email = txtEmail.getText();
	        boolean gioiTinh = cmbGioiTinh.getValue().equals("Nam");
	        LocalDate ngaySinh = dpNgaySinh.getValue();
	        LocalDate ngayVaoLam = dpNgayVaoLam.getValue();
	        Thue thue = cmbThue.getValue();
	        double luong = toolCtrl.chuyenTienSangSo(txtLuong.getText());
	        String anh = luuAnh(maNV);
	        
	        
	        boolean trangThai = true;

	        // Tạo đối tượng NhanVien
	        NhanVien nv = new NhanVien(maNV, tenNV, chucVu, ngaySinh, gioiTinh,sdt, ngayVaoLam, luong, thue, trangThai, anh);

	        // Gọi DAO để cập nhật
	        if (toolCtrl.hienThiXacNhan("Thêm nhân viên", "Xác nhận thêm nhân viên?")) {
	        	NhanVienDAO dao = new NhanVienDAO();
	        	boolean kq = dao.themNhanVien(nv);
	        	if (kq) {
		            System.out.println("✅ Đã thêm nhân viên: " + maNV);
		        } else {
		            System.out.println("❌ Thêm nhân viên thất bại.");
		        }
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
}
