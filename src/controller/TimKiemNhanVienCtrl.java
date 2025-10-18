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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import dao.NhanVienDAO;
import entity.NhanVien;

public class TimKiemNhanVienCtrl {
	@FXML
	public Button btnXemChiTiet;
	@FXML
	public Button btnLamMoi;
	@FXML
	public Button btnLichSuXoa;
	@FXML
	public Button btnXoa;
	@FXML
	public TextField txtMaNV, txtTenNV;
	@FXML
	public TableView<NhanVien> tblNhanVien;
	@FXML
	public TableColumn<NhanVien, Boolean> colSelect;
	@FXML
	public TableColumn<NhanVien, String> colMaNV;
	@FXML
	public TableColumn<NhanVien, String> colTenNV;
	@FXML
	public TableColumn<NhanVien, String> colSdt;
	@FXML
	public TableColumn<NhanVien, String> colGioiTinh;
	@FXML
	public TableColumn<NhanVien, String> colChucVu;
	@FXML
	public TableColumn<NhanVien, Void> colHoatDong;
	@FXML
	public ImageView imgXoaNhieu;

	public NhanVienDAO nvDAO = new NhanVienDAO();
	private boolean tblDangLam = true;

	public ObservableList<NhanVien> listNV = nvDAO.layNhanVienDangLam();
	public ObservableList<NhanVien> listNVDaNghi = nvDAO.layNhanVienNghiLam();

	private TrangChuNVCtrl trangChuNVCtrl; // tham chiếu controller cha
	private TrangChuQLCtrl trangChuQLCtrl;
	private ToolCtrl toolCtrl = new ToolCtrl();

	public void initialize() {
		setDataChoTable(listNV);
		setAction();
		btnXoa.setVisible(false);
	}

	public void setAction() {
		txtMaNV.setOnAction(event -> locNhanVien()); // txt tìm kiếm theo mã nhân viên
		txtTenNV.setOnAction(event -> locNhanVien()); // txt tìm kiếm theo tên nhân viên
		btnLamMoi.setOnAction(event -> lamMoiBang()); // btn làm mới lại table
		btnXemChiTiet.setOnMouseClicked(e -> chuyenDenTrangChiTiet());
		btnLichSuXoa.setOnAction(event -> suKienXemLichSuXoa());
		tblNhanVien.setOnMouseClicked(e -> hienThiNutXoaNhieu());
		btnXoa.setOnAction(event -> {
			xoaNhieu();
		});
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}

	// ========== LỌC NHÂN VIÊN ==========
	public void locNhanVien() {
		String maNV = txtMaNV.getText().trim().toLowerCase();
		String tenNV = txtTenNV.getText().trim().toLowerCase();

		// Lấy danh sách hiện đang hiển thị trên TableView
		ObservableList<NhanVien> danhSachHienTai = tblNhanVien.getItems();

		// Bắt đầu từ toàn bộ danh sách
		ObservableList<NhanVien> listLoc = danhSachHienTai.filtered(nv -> {
			boolean hopLe = true;

			// --- Lọc theo mã NV ---
			if (!maNV.isEmpty()) {
				hopLe = hopLe && nv.getMaNV().toLowerCase().contains(maNV);
			}

			// --- Lọc theo tên NV ---
			if (!tenNV.isEmpty()) {
				hopLe = hopLe && nv.getTenNV().toLowerCase().contains(tenNV);
			}

			return hopLe;
		});

		setDataChoTable(listLoc);
	}

	// ========== LÀM MỚI BẢNG ==========
	public void lamMoiBang() {
		txtMaNV.setText("");
		txtTenNV.setText("");
		if (tblDangLam) {
			setDataChoTable(listNV);
		} else {
			setDataChoTable(listNVDaNghi);
		}
	}

	// ========== SỰ KIỆN CHO NÚT XOÁ ==========
	public void suKienXemLichSuXoa() {
		if (tblDangLam) {
			// Đang xem danh sách đang làm -> chuyển sang đã nghỉ
			setDataChoTable(listNVDaNghi);
			btnLichSuXoa.setText("Danh sách hiện tại");
			tblDangLam = false;
			btnXoa.setText("Khôi phục tất cả");
			Image image = new Image(getClass().getResourceAsStream("/picture/nhanVien/system-restore.png"));
			imgXoaNhieu.setImage(image);
		} else {
			// Đang xem danh sách đã nghỉ -> chuyển về đang làm
			setDataChoTable(listNV);
			btnLichSuXoa.setText("Lịch sử xoá");
			tblDangLam = true;
			btnXoa.setText("Xoá tất cả");
			Image image = new Image(getClass().getResourceAsStream("/picture/nhanVien/trash.png"));
			imgXoaNhieu.setImage(image);
		}
	}

	// ========== THIẾT LẬP CHO NÚT XOÁ NHIỀU NHÂN VIÊN ==========
	public List<NhanVien> hienThiNutXoaNhieu() {
		// Lọc danh sách nhân viên đang hiển thị và được chọn
		List<NhanVien> danhSachChon = tblNhanVien.getItems().stream().filter(NhanVien::isSelected).toList();

		if (danhSachChon.size() >= 2) {
			btnXoa.setVisible(true);
			return danhSachChon;
		}

		else {
			btnXoa.setVisible(false);
			return null;
		}
	}

	// ========== XOÁ NHIỀU NHÂN VIÊN ==========
	public void xoaNhieu() {
		List<NhanVien> listNV = hienThiNutXoaNhieu();

		if (listNV.isEmpty()) {
			toolCtrl.hienThiThongBao("Thông báo", "Vui lòng chọn ít nhất một nhân viên để xoá.", false);
			return;
		}
		if (tblDangLam) {
			// === TRƯỜNG HỢP ĐANG LÀM → XOÁ ===
			boolean xacNhan = toolCtrl.hienThiXacNhan("Xác nhận xoá",
					"Bạn có chắc muốn xoá " + listNV.size() + " nhân viên đã chọn?");
			if (!xacNhan)
				return;

			for (NhanVien nv : listNV) {
				if (nv.isTrangThai()) {
					xoaNhanVien(nv);
				}
			}
			toolCtrl.hienThiThongBao("Thành công", "Đã xoá thành công " + listNV.size() + " nhân viên.", true);
		} else {
			// === TRƯỜNG HỢP ĐÃ NGHỈ → KHÔI PHỤC ===
			boolean xacNhan = toolCtrl.hienThiXacNhan("Xác nhận khôi phục",
					"Bạn có chắc muốn khôi phục " + listNV.size() + " nhân viên đã chọn?");
			if (!xacNhan)
				return;

			for (NhanVien nv : listNV) {
				if (!nv.isTrangThai()) {
					hoanTacNhanVien(nv);
				}
			}
			toolCtrl.hienThiThongBao("Thành công", "Đã khôi phục thành công " + listNV.size() + " nhân viên.", true);
		}

		// Ẩn nút Xóa/KHôi phục và làm mới bảng
		btnXoa.setVisible(false);
		lamMoiBang();
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

		setupColHoatDong();
		tblNhanVien.setItems(listNV);
	}

	// ========== THIẾT LẬP CHO CỘT CHECKBOX ==========
	public void setUpColCheckBox() {
		// Tạo checkbox tiêu đề
		CheckBox chkChonTatCa = new CheckBox();
		chkChonTatCa.setAlignment(Pos.CENTER);
		colSelect.setGraphic(chkChonTatCa);

		// Cài đặt checkbox cho từng hàng
		colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		colSelect.setCellFactory(tc -> {
			CheckBoxTableCell<NhanVien, Boolean> cell = new CheckBoxTableCell<>() {
				@Override
				public void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty) {
						TableRow<NhanVien> row = getTableRow();
						if (row != null) {
							NhanVien nv = row.getItem();
							if (nv != null) {
								// Khi tick hoặc bỏ tick từng nhân viên
								nv.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
									if (isNowSelected) {
										tblNhanVien.getSelectionModel().select(nv);
									} else {
										tblNhanVien.getSelectionModel()
												.clearSelection(tblNhanVien.getItems().indexOf(nv));
									}

									// Nếu bỏ chọn 1 nhân viên → bỏ tick tiêu đề
									if (!isNowSelected) {
										chkChonTatCa.setSelected(false);
									} else {
										// Nếu tất cả đều chọn → tick lại tiêu đề
										boolean allSelected = tblNhanVien.getItems().stream()
												.allMatch(NhanVien::isSelected);
										chkChonTatCa.setSelected(allSelected);
									}
								});
							}
						}
					}
				}
			};
			return cell;
		});

		// Khi click chọn dòng → đảo trạng thái selected (giữ nguyên logic của bạn)
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

		// Khi tick “chọn tất cả” → tất cả nhân viên được chọn hoặc bỏ chọn
		chkChonTatCa.setOnAction(e -> {
			boolean isSelected = chkChonTatCa.isSelected();
			for (NhanVien nv : tblNhanVien.getItems()) {
				nv.setSelected(isSelected);
			}
			tblNhanVien.refresh();

			hienThiNutXoaNhieu();
		});
	}

	// ========== SETUP CỘT HOẠT ĐỘNG TRONG TABLE ==========
	public void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<NhanVien, Void>() {
			private final Button btn = new Button();

			{
				btn.getStyleClass().add("btnXoaVaHoanTac");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> {
					NhanVien nv = getTableView().getItems().get(getIndex());

					if (nv.isTrangThai()) { // xử lý xóa
						xoaNhanVien(nv);
					} else { // xử lý hoàn tác
						hoanTacNhanVien(nv);
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
					NhanVien nv = getTableView().getItems().get(getIndex());
					if (nv != null) {
						btn.setText(nv.isTrangThai() ? "Xóa" : "Hoàn Tác");
					}
					setGraphic(btn);
				}
			}
		});
	}

	// ========== SET GIỚI TÍNH CHO NHÂN VIÊN ==========
	public String setGioiTinh(NhanVien nv) {
		Boolean gioiTinh = nv.isGioiTinh();
		String display = (gioiTinh != null && gioiTinh) ? "Nam" : "Nữ";
		return new String(display);
	}

	// ========== XOÁ NHÂN VIÊN ==========
	private void xoaNhanVien(NhanVien nv) {
		if (nvDAO.xoaNhanVien(nv.getMaNV())) {

			nv.setTrangThai(false);
			listNV.remove(nv);
			listNVDaNghi.add(nv);
			nv.setSelected(false);
			sapXepList(listNVDaNghi);
		}
	}

	// ========== KHÔI PHỤC NHÂN VIÊN ==========
	private void hoanTacNhanVien(NhanVien nv) {
		if (nvDAO.khoiPhucNhanVien(nv.getMaNV())) {
			nv.setTrangThai(true);
			listNVDaNghi.remove(nv);
			listNV.add(nv);
			nv.setSelected(false);
			sapXepList(listNV);
		}
	}

	// ========== SẮP XẾP DANH SÁCH NHÂN VIÊN ==========
	private void sapXepList(ObservableList<NhanVien> listNV) {
		FXCollections.sort(listNV, (nv1, nv2) -> {
			try {
				int so1 = Integer.parseInt(nv1.getMaNV().replaceAll("[^0-9]", ""));
				int so2 = Integer.parseInt(nv2.getMaNV().replaceAll("[^0-9]", ""));
				return Integer.compare(so1, so2); // tăng dần
			} catch (NumberFormatException e) {
				return nv1.getMaNV().compareTo(nv2.getMaNV());
			}
		});
	}

	// ========== MỞ TRANG CHI TIẾT NHÂN VIÊN ==========
	public void chuyenDenTrangChiTiet() {
		List<NhanVien> danhSachChon = tblNhanVien.getItems().stream().filter(NhanVien::isSelected).toList();

		if (danhSachChon.isEmpty()) {
			toolCtrl.hienThiThongBao("Lỗi", "Vui lòng chọn 1 khách hàng để xem chi tiết.", false);
			return;
		}

		if (danhSachChon.size() > 1) {
			toolCtrl.hienThiThongBao("Lỗi", "Chỉ được chọn 1 khách hàng để xem chi tiết.", false);
			return;
		}

		NhanVien nhanVien = danhSachChon.get(0);

		if (trangChuQLCtrl != null) {
			ChiTietNhanVienCtrl ctNVCtrl = trangChuQLCtrl.doiCenterPane("/fxml/ChiTietNhanVien.fxml");
			ctNVCtrl.hienThiThongTinNhanVien(nhanVien);
			ctNVCtrl.setTrangChuQLCtrl(trangChuQLCtrl);

		} else if (trangChuNVCtrl != null) {
			ChiTietNhanVienCtrl ctNVCtrl = trangChuQLCtrl.doiCenterPane("/fxml/ChiTietNhanVien.fxml");
			ctNVCtrl.hienThiThongTinNhanVien(nhanVien);
			ctNVCtrl.setTrangChuNVCtrl(trangChuNVCtrl);

		} else {
			toolCtrl.hienThiThongBao("Lỗi hệ thống", "Controller cha chưa được set. Vui lòng kiểm tra cách load FXML.",
					false);
		}

	}

	// ========== MỞ TRANG CẬP NHẬT NHÂN VIÊN ==========
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
