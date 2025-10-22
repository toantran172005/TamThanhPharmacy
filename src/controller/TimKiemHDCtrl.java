package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.HoaDonDAO;
import dao.NhanVienDAO;
import entity.NhanVien;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class TimKiemHDCtrl {
	@FXML
	public Button btnChiTiet;
	@FXML
	public TextField txtKhachHang;
	@FXML
	private TableView<Object[]> tblHoaDon;
	@FXML
	public TableColumn<Object[], Boolean> colSelect;
	@FXML
	private TableColumn<Object[], String> colMaHD;
	@FXML
	private TableColumn<Object[], String> colTenNV;
	@FXML
	private TableColumn<Object[], String> colTenKH;
	@FXML
	private TableColumn<Object[], String> colThoiGian;
	@FXML
	private TableColumn<Object[], String> colTongTienHang;
	@FXML
	private TableColumn<Object[], String> colTongTienGiam;

	public HoaDonDAO hdDAO = new HoaDonDAO();
	private ToolCtrl toolCtrl = new ToolCtrl();

	private TrangChuNVCtrl trangChuNVCtrl;
	private TrangChuQLCtrl trangChuQLCtrl;

	public void initialize() {
		btnChiTiet.setOnMouseClicked(e -> chuyenDenTrangChiTiet());
		setDataChoTable(hdDAO.layListHoaDon());
		setUpColCheckBox();
		Platform.runLater(() -> txtKhachHang.getParent().requestFocus()); // Không focus ô text khi load lại trang
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl ctrl) {
		this.trangChuNVCtrl = ctrl;
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl ctrl) {
		this.trangChuQLCtrl = ctrl;
	}

	// ========== LOAD DỮ LIỆU NHÂN VIÊN TỪ DATABASE ==========
	public void setDataChoTable(ObservableList<Object[]> listHD) {
		colMaHD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0].toString()));

		colTenNV.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4].toString()));

		colTenKH.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2].toString()));

		colThoiGian.setCellValueFactory(cellData -> {
		    LocalDate ngayLap = (LocalDate) cellData.getValue()[6];
		    return new SimpleStringProperty(toolCtrl.dinhDangLocalDate(ngayLap));
		});


		colTongTienHang.setCellValueFactory(cellData -> {
			double tien = (double) cellData.getValue()[11];
			return new SimpleStringProperty(toolCtrl.dinhDangVND(tien));
		});

		colTongTienGiam.setCellValueFactory(cellData -> new SimpleStringProperty(""));

		tblHoaDon.setItems(listHD);
	}

	// ========== THIẾT LẬP CHO CỘT CHECKBOX ==========
	public void setUpColCheckBox() {
		// ===== 1. Tạo checkbox tiêu đề =====
		CheckBox chkChonTatCa = new CheckBox();
		chkChonTatCa.setAlignment(Pos.CENTER);
		colSelect.setGraphic(chkChonTatCa);

		// ===== 2. Map để lưu trạng thái chọn từng hàng =====
		Map<Object[], Boolean> trangThaiChon = new HashMap<>();

		// ===== 3. Cài đặt giá trị checkbox cho từng dòng =====
		colSelect.setCellValueFactory(cellData -> {
			Object[] row = cellData.getValue();
			return new javafx.beans.property.SimpleBooleanProperty(trangThaiChon.getOrDefault(row, false));
		});

		// ===== 4. Tạo CellFactory để hiển thị checkbox từng hàng =====
		colSelect.setCellFactory(tc -> new CheckBoxTableCell<>(index -> {
			Object[] row = tblHoaDon.getItems().get(index);
			javafx.beans.property.BooleanProperty prop = new javafx.beans.property.SimpleBooleanProperty(
					trangThaiChon.getOrDefault(row, false));

			prop.addListener((obs, oldVal, newVal) -> {
				trangThaiChon.put(row, newVal);

				// Nếu bỏ tick 1 dòng → bỏ tick “chọn tất cả”
				if (!newVal) {
					chkChonTatCa.setSelected(false);
				} else {
					// Nếu tất cả đều tick → tick “chọn tất cả”
					boolean allSelected = tblHoaDon.getItems().stream()
							.allMatch(r -> trangThaiChon.getOrDefault(r, false));
					chkChonTatCa.setSelected(allSelected);
				}
			});
			return prop;
		}));

		// ===== 5. Khi tick “chọn tất cả” =====
		chkChonTatCa.setOnAction(e -> {
			boolean isSelected = chkChonTatCa.isSelected();
			for (Object[] row : tblHoaDon.getItems()) {
				trangThaiChon.put(row, isSelected);
			}
			tblHoaDon.refresh();
		});

		// ===== 6. Cho phép click chọn dòng để đảo trạng thái checkbox =====
		tblHoaDon.setRowFactory(tv -> {
			TableRow<Object[]> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					Object[] data = row.getItem();
					boolean curr = trangThaiChon.getOrDefault(data, false);
					trangThaiChon.put(data, !curr);
					tblHoaDon.refresh();
				}
			});
			return row;
		});
	}

	// ========== MỞ TRANG CHI TIẾT NHÂN VIÊN ==========
	public void chuyenDenTrangChiTiet() {
		Object[] selected = tblHoaDon.getSelectionModel().getSelectedItem();
		if (selected == null) {
			toolCtrl.hienThiThongBao("Thông báo", "Vui lòng chọn một hóa đơn để xem chi tiết!", false);
			return;
		}

		String maHD = selected[0].toString(); // lấy mã hóa đơn

		if (trangChuQLCtrl != null) {
			ChiTietHoaDonCtrl ctHDCtrl = trangChuQLCtrl.doiCenterPane("/fxml/ChiTietHoaDon.fxml");
			ctHDCtrl.hienThiThongTinHoaDon(selected); // truyền Object[]
			ctHDCtrl.setTrangChuQLCtrl(trangChuQLCtrl);
		} else if (trangChuNVCtrl != null) {
			ChiTietHoaDonCtrl ctHDCtrl = trangChuNVCtrl.doiCenterPane("/fxml/ChiTietHoaDon.fxml");
			ctHDCtrl.hienThiThongTinHoaDon(selected); // truyền Object[]
			ctHDCtrl.setTrangChuNVCtrl(trangChuNVCtrl);
		} else {
			toolCtrl.hienThiThongBao("Lỗi hệ thống", "Controller cha chưa được set!", false);
		}
	}

}
