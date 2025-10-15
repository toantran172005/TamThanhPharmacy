package controller;

import dao.KeThuocDAO;
import entity.KeThuoc;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class DanhSachKeThuocCtrl {

	@FXML
	public ComboBox<String> cmbTrangThai;
	@FXML
	public ComboBox<String> cmbLoaiKe;
	@FXML
	public Button btnChiTiet, btnLamMoi;
	@FXML
	private TableColumn<KeThuoc, Boolean> colSelect;
	@FXML
	private TableColumn<KeThuoc, String> colMaKe;
	@FXML
	private TableColumn<KeThuoc, String> colLoaiKe;
	@FXML
	private TableColumn<KeThuoc, Integer> colSucChua;
	@FXML
	private TableColumn<KeThuoc, String> colMoTa;
	@FXML
	private TableColumn<KeThuoc, String> colTrangThai;
	@FXML
	private TableColumn<KeThuoc, Void> colHoatDong;
	@FXML
	private TableView<KeThuoc> tblKeThuoc;

	public TrangChuQLCtrl Ql;
	public TrangChuNVCtrl NV;

	public ObservableList<KeThuoc> listKeThuoc = FXCollections.observableArrayList();
	public KeThuocDAO ktDAO = new KeThuocDAO();

	@FXML
	public void initialize() {
		listKeThuoc = ktDAO.layListKeThuoc();
		setItemComboBox();
		setDataChoTable(listKeThuoc);
	}

	public void setDataChoTable(ObservableList<KeThuoc> list) {
		// Checkbox
		setUpColCheckBox();
		// Các cột data
		colMaKe.setCellValueFactory(new PropertyValueFactory<>("maKe"));
		colLoaiKe.setCellValueFactory(new PropertyValueFactory<>("loaiKe"));
		colSucChua.setCellValueFactory(new PropertyValueFactory<>("sucChua"));
		colMoTa.setCellValueFactory(new PropertyValueFactory<>("moTa"));
		colTrangThai.setCellValueFactory(cellData -> {
			KeThuoc kt = cellData.getValue();
			return new SimpleStringProperty(setTrangThai(kt));
		});
		// Cột hoạt động
		setupColHoatDong();
		// Đưa data lên table
		tblKeThuoc.setEditable(true);
		tblKeThuoc.setItems(list);
	}

	public void setUpColCheckBox() {
		colSelect.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
		colSelect.setCellFactory(tc -> {
			CheckBoxTableCell<KeThuoc, Boolean> cell = new CheckBoxTableCell<>() {
				@Override
				public void updateItem(Boolean item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty) {
						TableRow<KeThuoc> row = getTableRow();
						if (row != null) {
							KeThuoc kt = row.getItem();
							if (kt != null) {
								kt.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
									if (isNowSelected) {
										tblKeThuoc.getSelectionModel().select(kt);
									} else {
										tblKeThuoc.getSelectionModel()
												.clearSelection(tblKeThuoc.getItems().indexOf(kt));
									}
								});
							}
						}
					}
				}
			};
			return cell;
		});

		tblKeThuoc.setRowFactory(tv -> {
			TableRow<KeThuoc> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty() && event.getClickCount() == 1) {
					KeThuoc kh = row.getItem();
					kh.setSelected(!kh.isSelected());
				}
			});
			return row;
		});
	}
	
	public void setupColHoatDong() {
		colHoatDong.setCellFactory(column -> new TableCell<KeThuoc, Void>() {
			private final Button btn = new Button();

			{
				btn.getStyleClass().add("btnXoaVaHoanTac");
				btn.setCursor(javafx.scene.Cursor.HAND);
				btn.setOnAction(event -> {
					KeThuoc kh = getTableView().getItems().get(getIndex());

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
					KeThuoc kh = getTableView().getItems().get(getIndex());
					if (kh != null) {
						btn.setText(kh.isTrangThai() ? "Xóa" : "Hoàn Tác");
					}
					setGraphic(btn);
				}
			}
		});
	}

	public String setTrangThai(KeThuoc kt) {
		Boolean trangThai = true;
		String display = !trangThai.equals(kt.isTrangThai()) ? "Ngừng hoạt động" : "Hoạt động";
		return display;
	}

	public void setItemComboBox() {
		cmbTrangThai.getItems().addAll("Tất cả", "Hoạt động", "Ngừng hoạt động");
		cmbTrangThai.setValue("Tất cả");
		cmbLoaiKe.getItems().addAll("Tất cả", "Thuốc kê đơn", "Thuốc không kê đơn", "Tim mạch", "Thần kinh",
				"Dược mỹ phẩm", "Thực phẩm chức năng", "Tiểu đường", "Kháng sinh");
		cmbLoaiKe.setValue("Tất cả");
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.Ql = trangChuQLCtrl;
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.NV = trangChuNVCtrl;

	}

	@FXML
	public void moTrangChiTiet() {
		if (Ql != null) {
			Ql.setTrangChiTietKeThuoc();
		} else if (NV != null) {
			NV.setTrangChiTietKeThuoc();
		}
	}

}
