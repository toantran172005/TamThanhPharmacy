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
import javafx.scene.control.TableRow;
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
import entity.NhanVien;
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
		setDataChoTable(listNV);
		setAction();
	}

	public void setItemComboBoxTrangThai() {
		cmbTrangThai.getItems().addAll("Còn làm", "Đã nghỉ");
		cmbTrangThai.setValue("Còn làm");
	}
	
	public void setAction() {
		txtMaNV.setOnAction(event -> locNhanVien()); // txt tìm kiếm theo mã nhân viên
		txtTenNV.setOnAction(event -> locNhanVien()); // txt tìm kiếm theo tên nhân viên
		btnLamMoi.setOnAction(event -> lamMoiBang()); // btn làm mới lại table
		cmbTrangThai.setOnAction(event -> locNhanVien()); 
		btnXemChiTiet.setOnMouseClicked(e -> moTrangChiTietNhanVien());
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}
	
	public void lamMoiBang() {
		txtMaNV.setText("");
		txtTenNV.setText("");
		cmbTrangThai.setValue("Còn làm");
		locNhanVien();
	}
	
	public void locNhanVien() {
	    String maNV = txtMaNV.getText().trim().toLowerCase();
	    String tenNV = txtTenNV.getText().trim().toLowerCase();
	    String trangThaiChon = cmbTrangThai.getValue();

	    // Bắt đầu từ toàn bộ danh sách
	    ObservableList<NhanVien> listLoc = listNV.filtered(nv -> {
	        boolean hopLe = true;

	        // --- Lọc theo mã NV ---
	        if (!maNV.isEmpty()) {
	            hopLe = hopLe && nv.getMaNV().toLowerCase().contains(maNV);
	        }

	        // --- Lọc theo tên NV ---
	        if (!tenNV.isEmpty()) {
	            hopLe = hopLe && nv.getTenNV().toLowerCase().contains(tenNV);
	        }

	        // --- Lọc theo trạng thái ---
	        if (trangThaiChon != null && !trangThaiChon.equals("Tất cả")) {
	            boolean isConLam = trangThaiChon.equals("Còn làm");
	            hopLe = hopLe && (nv.isTrangThai() == isConLam);
	        }

	        return hopLe;
	    });

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
		// Checkbox
		setUpColCheckBox();

		// Data
		colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
		colTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
		colSdt.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			return new SimpleStringProperty(toolCtrl.chuyenSoDienThoai(nv.getSdt()));
		});
		colGioiTinh.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			return new SimpleStringProperty(setGioiTinh(nv));
		});
		colChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

		colTrangThai.setCellValueFactory(cellData -> {
			NhanVien nv = cellData.getValue();
			return new SimpleStringProperty(setTrangThai(nv));
		});

		setupColHoatDong();
		tblNhanVien.setItems(listNV);
	}
	
	public void setUpColCheckBox() {
		colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		colSelect.setCellFactory(tc -> {
			CheckBoxTableCell<NhanVien, Boolean> cell = new CheckBoxTableCell<>() {
				@Override
				public void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty) {
						TableRow<NhanVien> row = getTableRow();
						if (row != null) {
							NhanVien kh = row.getItem();
							if (kh != null) {
								kh.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
									if (isNowSelected) {
										tblNhanVien.getSelectionModel().select(kh);
									} else {
										tblNhanVien.getSelectionModel()
												.clearSelection(tblNhanVien.getItems().indexOf(kh));
									}
								});
							}
						}
					}
				}
			};
			return cell;
		});

		tblNhanVien.setRowFactory(tv -> {
			TableRow<NhanVien> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					NhanVien kh = row.getItem();
					kh.setSelected(!kh.isSelected());
				}
			});
			return row;
		});
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
	public void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<NhanVien, Void>() {
			private final Button btn = new Button();

			{
				btn.getStyleClass().add("btnXoaVaHoanTac");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> {
					NhanVien kh = getTableView().getItems().get(getIndex());

					if (kh.isTrangThai()) { // xử lý xóa 

					} else { // xử lý hoàn tác 

					}

					getTableView().refresh();
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					NhanVien kh = getTableView().getItems().get(getIndex());
					if (kh != null) {
						btn.setText(kh.isTrangThai() ? "Xóa" : "Hoàn Tác");
					}
					setGraphic(btn);
				}
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
