package controller;

import java.util.ArrayList;

import com.itextpdf.text.List;

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
	public ComboBox<String> cmbTrangThai, cmbLoaiKe, cmbSucChua;
	@FXML
	public Button btnXemCT, btnLamMoi;
	@FXML
	public TableColumn<KeThuoc, Boolean> colSelect;
	@FXML
	public TableColumn<KeThuoc, String> colMaKe;
	@FXML
	public TableColumn<KeThuoc, String> colLoaiKe;
	@FXML
	public TableColumn<KeThuoc, Integer> colSucChua;
	@FXML
	public TableColumn<KeThuoc, String> colMoTa;
	@FXML
	public TableColumn<KeThuoc, String> colTrangThai;
	@FXML
	public TableColumn<KeThuoc, Void> colHoatDong;
	@FXML
	public TableView<KeThuoc> tblKeThuoc;

	public TrangChuQLCtrl Ql;
	public TrangChuNVCtrl NV;

	public ObservableList<KeThuoc> listKeThuoc = FXCollections.observableArrayList();
	public KeThuocDAO ktDAO = new KeThuocDAO();

	@FXML
	public void initialize() {
		listKeThuoc = ktDAO.layListKeThuoc();
		setItemComboBox();
		setDataChoTable(listKeThuoc);
		setUpTextFieldVaButton();
	}

	public void setUpTextFieldVaButton() {

		btnLamMoi.setOnAction(event -> lamMoiBang());
		cmbTrangThai.setOnAction(event -> locTatCa());
		cmbLoaiKe.setOnAction(event -> locTatCa());
		cmbSucChua.setOnAction(event -> locTatCa());
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
			public final Button btn = new Button();

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

	public void lamMoiBang() {
		listKeThuoc = ktDAO.layListKeThuoc();
		setGiaTriMacDinh();
		locTatCa();
	}
	
	public void setGiaTriMacDinh() {
		cmbLoaiKe.setValue("Tất cả");
		cmbSucChua.setValue("Tất cả");
		cmbTrangThai.setValue("Tất cả");
	}

	public void locTatCa() {
	    String loaiKe = cmbLoaiKe.getValue();
	    String mucChon = cmbSucChua.getValue();
	    String trangThai = cmbTrangThai.getValue();

	    ObservableList<KeThuoc> listLoc = listKeThuoc.filtered(kt -> {
	        boolean hopLoai = true;
	        boolean hopSucChua = true;
	        boolean hopTrangThai = true;

	        //  Lọc theo loại kệ 
	        if (loaiKe != null && !loaiKe.equals("Tất cả")) {
	            hopLoai = kt.getLoaiKe().equalsIgnoreCase(loaiKe);
	        }

	        //  Lọc theo sức chứa 
	        if (mucChon != null && !mucChon.equals("Tất cả")) {
	            try {
	                int gioiHan = Integer.parseInt(mucChon.replace("<", "").trim());
	                hopSucChua = kt.getSucChua() < gioiHan;
	            } catch (NumberFormatException e) {
	                hopSucChua = true;
	            }
	        }

	        //  Lọc theo trạng thái 
	        if (trangThai != null && !trangThai.equals("Tất cả")) {
	            if (trangThai.equals("Hoạt động")) {
	                hopTrangThai = kt.isTrangThai();
	            } else if (trangThai.equals("Ngừng hoạt động")) {
	                hopTrangThai = !kt.isTrangThai();
	            }
	        }

	        return hopLoai && hopSucChua && hopTrangThai;
	    });

	    setDataChoTable(listLoc);
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
		khoiTaoComboSucChua();
	}
	
	public void khoiTaoComboSucChua() {
	    int max = listKeThuoc.stream()
	            .mapToInt(KeThuoc::getSucChua)
	            .max()
	            .orElse(0);

	    // Danh sách mặc định
	    ArrayList<String> mucSucChua = new ArrayList<>();
	    mucSucChua.add("Tất cả");
	    mucSucChua.add("< 100");
	    mucSucChua.add("< 200");
	    mucSucChua.add("< 300");
	    mucSucChua.add("< 400");

	    int muc = 500;
	    while (muc <= (max + 100)) {
	        mucSucChua.add("< " + muc);
	        muc += 100;
	    }

	    cmbSucChua.getItems().setAll(mucSucChua);
	    cmbSucChua.setValue("Tất cả");
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.Ql = trangChuQLCtrl;
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.NV = trangChuNVCtrl;

	}

}
