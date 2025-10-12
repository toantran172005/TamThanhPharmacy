package controller;

import java.text.NumberFormat;
import java.util.Locale;

import dao.KhachHangDAO;
import entity.KhachHang;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

public class TimKiemKHCtrl {

	@FXML
	public ComboBox<String> cmbTrangThai;

	@FXML
	public TableView<KhachHang> tblKhachHang;

	@FXML
	public TableColumn<KhachHang, Boolean> colSelect;
	@FXML
	public TableColumn<KhachHang, String> colMaKH;
	@FXML
	public TableColumn<KhachHang, String> colTenKH;
	@FXML
	public TableColumn<KhachHang, String> colSdt;
	@FXML
	public TableColumn<KhachHang, Integer> colTuoi;
	@FXML
	public TableColumn<KhachHang, Integer> colTongDonHang;
	@FXML
	public TableColumn<KhachHang, String> colTongTienHang;
	@FXML
	public TableColumn<KhachHang, String> colTrangThai;
	@FXML
	public TableColumn<KhachHang, Void> colHoatDong;

	@FXML
	public TextField txtTenKH, txtSdt;

	public KhachHangDAO khDAO = new KhachHangDAO();

	public ObservableList<KhachHang> listKH = FXCollections.observableArrayList();

	public ObservableList<KhachHang> listKHThongKe = FXCollections.observableArrayList();

	public void initialize() {
		listKH = khDAO.layListKhachHang();
		listKHThongKe = khDAO.layListKHThongKe();
		setItemCmbTrangThai();
		setDataChoTable(listKH);
		setUpTextFieldVaButton();
	}
	
	public void setUpTextFieldVaButton() {
		txtTenKH.setOnAction(event -> timTheoTen()); // txt tìm kiếm theo tên khách hàng
		txtSdt.setOnAction(event -> timTheoSDT()); // txt tìm kiếm theo số điện thoại
	}

	public void setDataChoTable(ObservableList<KhachHang> list) {

		// Checkbox
		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(index -> {
			KhachHang kh = tblKhachHang.getItems().get(index);
			BooleanProperty selected = new SimpleBooleanProperty(false);

			selected.addListener((obs, oldVal, newVal) -> {
				// xử lý khi tick checkbox

			});

			return selected;
		}));

		// Các cột data
		colMaKH.setCellValueFactory(new PropertyValueFactory<>("maKH"));
		colTenKH.setCellValueFactory(new PropertyValueFactory<>("tenKH"));
		colSdt.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleStringProperty(chuyenSoDienThoai(kh.getSdt()));
		});
		colTuoi.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
		colTrangThai.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleStringProperty(setTrangThai(kh));
		});
		colTongDonHang.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleIntegerProperty(tinhTongDonHang(kh)).asObject();
		});
		colTongTienHang.setCellValueFactory(cellData -> {
			KhachHang kh = cellData.getValue();
			return new SimpleStringProperty(tinhTongTien(kh));
		});
		// Cột hoạt động
		setupColHoatDong();
		// Đưa data lên table
		tblKhachHang.setEditable(true);
		tblKhachHang.setItems(list);
	}

	private void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<KhachHang, Void>() {
			private final Button btn = new Button("Xóa");

			{
				btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 6;");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> { // Xử lý khi nhấn nút xóa
					KhachHang kh = getTableView().getItems().get(getIndex());

				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				setGraphic(empty ? null : btn);
			}
		});
	}
	
	public void timTheoSDT() {
		String sdt = chuyenSoDienThoai(txtSdt.getText().trim().toLowerCase());

		if (sdt.isEmpty()) {
			setDataChoTable(listKH);
			return;
		}

		// Lọc danh sách theo tên (không phân biệt hoa thường)
		ObservableList<KhachHang> listLoc = listKH.filtered(kh -> kh.getSdt().toLowerCase().contains(sdt));

		// Cập nhật bảng
		setDataChoTable(listLoc);
	}

	public void timTheoTen() {
		String tenNhap = txtTenKH.getText().trim().toLowerCase();

		if (tenNhap.isEmpty()) {
			setDataChoTable(listKH);
			return;
		}

		// Lọc danh sách theo tên (không phân biệt hoa thường)
		ObservableList<KhachHang> listLoc = listKH.filtered(kh -> kh.getTenKH().toLowerCase().contains(tenNhap));

		// Cập nhật bảng
		setDataChoTable(listLoc);
	}

	public String tinhTongTien(KhachHang kh) {
		KhachHang tempKH = null;
		for (KhachHang KH : listKHThongKe) {
			if (KH.getMaKH().equalsIgnoreCase(kh.getMaKH())) {
				tempKH = KH;
				break;
			}
		}
		return dinhDangVND(tempKH.getTongTien());
	}

	public static String dinhDangVND(double amount) {
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
		return currencyFormatter.format(amount);
	}

	public String setTrangThai(KhachHang kh) {
		Boolean trangThai = kh.isTrangThai();
		@SuppressWarnings("unlikely-arg-type")
		String display = "0".equals(trangThai) ? "Ngừng hoạt động" : "Hoạt động";
		return display;
	}

	public int tinhTongDonHang(KhachHang kh) {
		KhachHang tempKH = null;
		for (KhachHang KH : listKHThongKe) {
			if (KH.getMaKH().equalsIgnoreCase(kh.getMaKH())) {
				tempKH = KH;
				break;
			}
		}
		return tempKH.getTongDonHang();
	}

	public String chuyenSoDienThoai(String sdt) {
		if (sdt == null || sdt.isEmpty())
			return sdt;

		sdt = sdt.trim();

		if (sdt.startsWith("+84-")) {
			return "0" + sdt.substring(4);
		} else if (sdt.startsWith("0")) {
			return "+84-" + sdt.substring(1);
		}

		return sdt;
	}

	public void setItemCmbTrangThai() {
		cmbTrangThai.getItems().addAll("Hoạt động", "Ngừng hoạt động");
	}

}
