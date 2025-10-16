package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import dao.NhanVienDAO;
import entity.NhanVien;

public class TimKiemNhanVienCtrl {
	@FXML public Button btnXemChiTiet;
	@FXML public Button btnLamMoi;
	@FXML public TextField txtMaNV, txtTenNV;
	@FXML public ComboBox<String> cmbTrangThai;
	@FXML public TableView<NhanVien> tblNhanVien;
	@FXML public TableColumn<NhanVien, Boolean> colSelect;
	@FXML public TableColumn<NhanVien, String> colMaNV;
	@FXML public TableColumn<NhanVien, String> colTenNV;
	@FXML public TableColumn<NhanVien, String> colSdt;
	@FXML public TableColumn<NhanVien, String> colGioiTinh;
	@FXML public TableColumn<NhanVien, String> colChucVu;
	@FXML public TableColumn<NhanVien, String> colTrangThai;
	@FXML public TableColumn<NhanVien, Void> colHoatDong;

	public NhanVienDAO nvDAO = new NhanVienDAO();

	// Lưu trạng thái checkbox của từng nhân viên
	private Map<NhanVien, BooleanProperty> selectedMap = new HashMap<>();

	public ObservableList<NhanVien> listNV = nvDAO.layListNhanVien();
	

	private TrangChuNVCtrl trangChuNVCtrl; // tham chiếu controller cha
	private TrangChuQLCtrl trangChuQLCtrl;
	private ToolCtrl toolCtrl = new ToolCtrl();

	public void initialize() {
		setItemComboBoxTrangThai();
		btnXemChiTiet.setOnMouseClicked(e -> moTrangChiTietNhanVien());

		setDataChoTable(listNV);
		setAction();
	}

	public void setItemComboBoxTrangThai() {
		cmbTrangThai.getItems().addAll("Còn làm", "Đã nghỉ");
		cmbTrangThai.setValue("Còn làm");
	}
	
	public void setAction() {
		txtMaNV.setOnAction(event -> timTheoMaNV()); // txt tìm kiếm theo mã nhân viên
		txtTenNV.setOnAction(event -> timTheoTenNV()); // txt tìm kiếm theo tên nhân viên
		btnLamMoi.setOnAction(event -> lamMoiBang()); // btn làm mới lại table
		cmbTrangThai.setOnAction(event -> timTheoTrangThai()); 
		setCheckBox();
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}
	
	public void lamMoiBang() {
		setDataChoTable(listNV);
		txtMaNV.setText("");
		txtTenNV.setText("");
		cmbTrangThai.setValue("Còn làm");
	}
	
	public void timTheoMaNV() {
		String maNV = txtMaNV.getText().trim().toLowerCase();

		if (maNV.isEmpty()) {
			setDataChoTable(listNV);
			return;
		}

		// Lọc danh sách theo mã
		ObservableList<NhanVien> listLoc  = listNV.filtered(nv -> nv.getMaNV().toLowerCase().contains(maNV));

		// Cập nhật bảng
		setDataChoTable(listLoc);
	}

	public void timTheoTenNV() {
		String timTenNV = txtTenNV.getText().trim().toLowerCase();

		if (timTenNV.isEmpty()) {
			setDataChoTable(listNV);
			return;
		}

		// Lọc danh sách theo tên (không phân biệt hoa thường)
		ObservableList<NhanVien> listLoc = listNV.filtered(nv -> nv.getTenNV().toLowerCase().contains(timTenNV));

		// Cập nhật bảng
		setDataChoTable(listLoc);
	}
	
	public void timTheoTrangThai() {
	    String trangThaiChon = cmbTrangThai.getValue();
	    if (trangThaiChon == null || trangThaiChon.isEmpty()) {
	        setDataChoTable(listNV);
	        return;
	    }

	    boolean isConLam = trangThaiChon.equals("Còn làm");
	    ObservableList<NhanVien> listLoc = listNV.filtered(nv -> nv.isTrangThai() == isConLam);
	    setDataChoTable(listLoc);
	}

	// ========== MỞ TRANG CHI TIẾT NHÂN VIÊN ==========
	private void moTrangChiTietNhanVien() {
		// Lấy danh sách nhân viên được chọn
		var nhanVienChon = listNV.stream()
				.filter(nv -> selectedMap.getOrDefault(nv, new SimpleBooleanProperty(false)).get()).toList();

		if (nhanVienChon.isEmpty()) {
			new Alert(AlertType.WARNING, "Vui lòng chọn một nhân viên để xem chi tiết!").show();
			return;
		}
		if (nhanVienChon.size() > 1) {
			new Alert(AlertType.WARNING, "Chỉ được chọn một nhân viên mỗi lần!").show();
			return;
		}

		// Lấy nhân viên được chọn duy nhất
		NhanVien nv = nhanVienChon.get(0);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChiTietNhanVien.fxml"));
			Parent root = loader.load();

			ChiTietNhanVienCtrl controller = loader.getController();
			controller.hienThiThongTinNhanVien(nv); // Gọi hàm truyền dữ liệu

			// Hiển thị trang
			if (trangChuNVCtrl != null) {
	            controller.setTrangChuNVCtrl(trangChuNVCtrl);
	            trangChuNVCtrl.moTrangDaTai(root);
	        } else if (trangChuQLCtrl != null) {
	            controller.setTrangChuQLCtrl(trangChuQLCtrl);
	            trangChuQLCtrl.moTrangDaTai(root);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ========== LOAD DỮ LIỆU NHÂN VIÊN TỪ DATABASE ==========
	public void setDataChoTable(ObservableList<NhanVien> listNV) {

		// Data
		colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
		colTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
		colSdt.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			return new SimpleStringProperty(toolCtrl.chuyenSoDienThoai(nv.getSdt()));
		});
		colGioiTinh.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			return new ReadOnlyObjectWrapper(setGioiTinh(nv));
		});
		colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

		colTrangThai.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			return new ReadOnlyObjectWrapper(setTrangThai(nv));
		});

		setupColHoatDong();

		tblNhanVien.setItems(listNV);
	}
	
	// ========== SET CHECKBOX TRONG TABLE ==========
	public void setCheckBox() {
		tblNhanVien.setEditable(true);
		colSelect.setEditable(true);
		
		// ========== THÊM CHECKBOX TRÊN HEADER ==========
		CheckBox selectAllCheckBox = new CheckBox();
	    colSelect.setGraphic(selectAllCheckBox);
	    colSelect.setSortable(false); // không cho sắp xếp cột này

	    selectAllCheckBox.setOnAction(e -> {
	        boolean selected = selectAllCheckBox.isSelected();
	        for (NhanVien nv : tblNhanVien.getItems()) {
	            selectedMap.putIfAbsent(nv, new SimpleBooleanProperty(false));
	            selectedMap.get(nv).set(selected);
	        }
	        tblNhanVien.refresh(); //Hiển thị tất cả các check sau khi chọn
	    });

	    // ========== GÁN CHECKBOX TỪNG DÒNG ==========
		colSelect.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			selectedMap.putIfAbsent(nv, new SimpleBooleanProperty(false));
			return selectedMap.get(nv);
		});

		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(index -> {
			NhanVien nv = tblNhanVien.getItems().get(index);
			return selectedMap.get(nv);
		}));
	}
	
	// ========== SET GIỚI TÍNH CHO NHÂN VIÊN ==========
	public String setGioiTinh(NhanVien nv) {
		Boolean gioiTinh = nv.isGioiTinh();
		String display = (gioiTinh != null && gioiTinh) ? "Nam" : "Nữ";
		return new String(display);
	}

	// ========== SET TRẠNG THÁI CHO NHÂN VIÊN ==========
	public String setTrangThai(NhanVien nv) {
		Boolean trangThai = nv.isTrangThai();
		String display = (trangThai != null && trangThai) ? "Còn làm" : "Đã nghỉ";
		return new String(display);
	}

	// ========== SETUP CỘT HOẠT ĐỘNG TRONG TABLE ==========
	private void setupColHoatDong() {
		colHoatDong.setCellFactory(col -> new TableCell<>() {
			Image imgXoa = new Image(getClass().getResourceAsStream("/picture/nhanVien/trash.png"));
			private final ImageView imgDelete = new ImageView(imgXoa);
			private final Button btnXoa = new Button();
			private final HBox box = new HBox(10, btnXoa);

			{
				// Căn giữa các nút trong ô
				box.setAlignment(Pos.CENTER);

				// Kích thước icon
				imgDelete.setFitWidth(16);
				imgDelete.setFitHeight(16);

				// Gán icon cho nút
				btnXoa.setGraphic(imgDelete);

				// Ẩn nền để chỉ thấy icon
				btnXoa.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

				btnXoa.setOnAction(event -> {
					NhanVien nv = getTableView().getItems().get(getIndex());
					Alert confirm = new Alert(AlertType.CONFIRMATION, "Bạn có chắc muốn xóa nhân viên này?");
					Optional<ButtonType> result = confirm.showAndWait();
					if (result.isPresent() && result.get() == ButtonType.OK) {
						xoaNhanVien(nv);
					}
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : box);
			}
		});
	}

	// ========== XOÁ NHÂN VIÊN ==========
	private void xoaNhanVien(NhanVien nv) {
		if (nvDAO.xoaNhanVien(nv.getMaNV())) {
			listNV.remove(nv);
		}
	}

	// ========== MỞ TRANG CHI TIẾT NHÂN VIÊN ==========
	private void moTrangCapNhatNhanVien(NhanVien nv) {
		if (trangChuNVCtrl != null) {
			trangChuNVCtrl.moTrang("/fxml/CapNhatNhanVien.fxml", CapNhatNhanVienCtrl.class);
		} else if (trangChuQLCtrl != null) {
			trangChuQLCtrl.moTrang("/fxml/CapNhatNhanVien.fxml", CapNhatNhanVienCtrl.class);
		} else {
			System.out.println("⚠ Không có tham chiếu TrangChu");
		}
	}
}
