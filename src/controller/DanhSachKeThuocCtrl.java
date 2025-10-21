package controller;

import java.util.ArrayList;
import java.util.List;

import dao.KeThuocDAO;
import entity.KeThuoc;
import entity.KeThuoc;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DanhSachKeThuocCtrl {

	@FXML
	public ComboBox<String> cmbLoaiKe, cmbSucChua;
	@FXML
	public Button btnXemCT, btnLamMoi, btnXoaTatCa, btnLichSuXoa;
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
	public ImageView imgXoaTatCa;
	@FXML
	public TableView<KeThuoc> tblKeThuoc;
	public boolean tblDangLam = true;
	public TrangChuQLCtrl QL;
	public TrangChuNVCtrl NV;

	public ObservableList<KeThuoc> listKeThuoc = FXCollections.observableArrayList();
	public KeThuocDAO ktDAO = new KeThuocDAO();
	public ToolCtrl tool = new ToolCtrl();

	@FXML
	public void initialize() {
		listKeThuoc = ktDAO.layListKeThuoc();
		setItemComboBox();
		locTatCa();
		setUpTextFieldVaButton();
	}

	public void setUpTextFieldVaButton() {
		btnXoaTatCa.setVisible(false);
		btnXemCT.setOnAction(event -> xemChiTietKeThuoc());
		btnLamMoi.setOnAction(event -> lamMoiBang());
		cmbLoaiKe.setOnAction(event -> locTatCa());
		cmbSucChua.setOnAction(event -> locTatCa());
		btnXoaTatCa.setOnAction(event -> xoaNhieu());
		tblKeThuoc.setOnMouseClicked(event -> hienThiNutXoaNhieu());
		btnLichSuXoa.setOnAction(event -> hienThiLichSuXoa());
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
		tblKeThuoc.setItems(list);
	}

	public void setUpColCheckBox() {
		CheckBox chkChonTatCa = new CheckBox();
		chkChonTatCa.setAlignment(Pos.CENTER);
		colSelect.setGraphic(chkChonTatCa);

		chkChonTatCa.setOnAction(e -> {
			boolean isSelected = chkChonTatCa.isSelected();
			for (KeThuoc kh : tblKeThuoc.getItems()) {
				kh.setSelected(isSelected);
			}
			tblKeThuoc.refresh();

			hienThiNutXoaNhieu();
		});

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

	public List<KeThuoc> hienThiNutXoaNhieu() {
		List<KeThuoc> danhSachChon = tblKeThuoc.getItems().stream().filter(KeThuoc::isSelected).toList();

		if (danhSachChon.size() >= 2) {
			btnXoaTatCa.setVisible(true);
			return danhSachChon;
		}

		else {
			btnXoaTatCa.setVisible(false);
			return null;
		}
	}

	public void xemChiTietKeThuoc() {

		List<KeThuoc> danhSachChon = tblKeThuoc.getItems().stream().filter(KeThuoc::isSelected).toList();

		if (danhSachChon.isEmpty()) {
			tool.hienThiThongBao("Lỗi", "Vui lòng chọn 1 kệ thuốc để xem chi tiết.", false);
			return;
		}

		if (danhSachChon.size() > 1) {
			tool.hienThiThongBao("Lỗi", "Chỉ được chọn 1 kệ thuốc để xem chi tiết.", false);
			return;
		}

		KeThuoc kt = danhSachChon.get(0);

		if (QL != null) {
			ChiTietKeThuocCtrl ctKT = QL.doiCenterPane("/fxml/ChiTietKeThuoc.fxml");
			ctKT.hienThiThongTin(kt);
			ctKT.setTrangChuQLCtrl(QL);

		} else if (NV != null) {
			ChiTietKeThuocCtrl ctKT = NV.doiCenterPane("/fxml/ChiTietKeThuoc.fxml");
			ctKT.hienThiThongTin(kt);
			ctKT.setTrangChuNVCtrl(NV);

		} else {
			tool.hienThiThongBao("Lỗi hệ thống", "Controller cha chưa được set. Vui lòng kiểm tra cách load FXML.",
					false);
		}

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
						if(tool.hienThiXacNhan("Xóa kệ thuốc", "Xác nhận xóa kệ thuốc mã: "+kh.getMaKe()+"?")) {
							xoaKeThuoc(kh);
						}
					} else { // xử lý hoàn tác
						if(tool.hienThiXacNhan("Khôi phục kệ thuốc", "Xác nhận khôi phục kệ thuốc mã: "+kh.getMaKe()+"?")) {
							hoanTacKeThuoc(kh);
						}
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
	}

	public void locTatCa() {
		String loaiKe = cmbLoaiKe.getValue();
		String mucChon = cmbSucChua.getValue();

		ObservableList<KeThuoc> listLoc = listKeThuoc.filtered(kt -> {
			boolean hopLoai = true;
			boolean hopSucChua = true;
			boolean hopTrangThai = true;

			// Lọc theo loại kệ
			if (loaiKe != null && !loaiKe.equals("Tất cả")) {
				hopLoai = kt.getLoaiKe().equalsIgnoreCase(loaiKe);
			}

			// Lọc theo sức chứa
			if (mucChon != null && !mucChon.equals("Tất cả")) {
				try {
					int gioiHan = Integer.parseInt(mucChon.replace("<", "").trim());
					hopSucChua = kt.getSucChua() < gioiHan;
				} catch (NumberFormatException e) {
					hopSucChua = true;
				}
			}

			// Lọc theo trạng thái
			if (btnLichSuXoa.getText().equals("Lịch sử xóa")) {
				hopTrangThai = kt.isTrangThai();
			} else {
				hopTrangThai = !kt.isTrangThai();
			}

			return hopLoai && hopSucChua && hopTrangThai;
		});

		setDataChoTable(listLoc);
	}

	public void xoaNhieu() {
		List<KeThuoc> listkh = hienThiNutXoaNhieu();

		if (tblDangLam) {
			// === TRƯỜNG HỢP ĐANG LÀM → XOÁ ===
			boolean xacNhan = tool.hienThiXacNhan("Xác nhận xoá",
					"Bạn có chắc muốn xoá " + listkh.size() + " kệ thuốc đã chọn?");
			if (!xacNhan)
				return;

			for (KeThuoc kh : listkh) {
				if (kh.isTrangThai()) {
					xoaKeThuoc(kh);
				}
			}
			tool.hienThiThongBao("Thành công", "Đã xoá thành công " + listkh.size() + " kệ thuốc.", true);
		} else {
			// === TRƯỜNG HỢP ĐÃ NGHỈ → KHÔI PHỤC ===
			boolean xacNhan = tool.hienThiXacNhan("Xác nhận khôi phục",
					"Bạn có chắc muốn khôi phục " + listkh.size() + " kệ thuốc đã chọn?");
			if (!xacNhan)
				return;

			for (KeThuoc kh : listkh) {
				if (!kh.isTrangThai()) {
					hoanTacKeThuoc(kh);
				}
			}
			tool.hienThiThongBao("Thành công", "Đã khôi phục thành công " + listkh.size() + " kệ thuốc.", true);
		}

		// Ẩn nút Xóa/KHôi phục và làm mới bảng
		btnXoaTatCa.setVisible(false);
		lamMoiBang();
	}
	
	public void hienThiLichSuXoa() {
		for (KeThuoc kh : tblKeThuoc.getItems()) {
	        kh.setSelected(false);
	    }
		btnXoaTatCa.setVisible(false);
	    tblKeThuoc.refresh();
	    
		if(tblDangLam) {
			btnLichSuXoa.setText("Danh sách hiện tại");
			btnXoaTatCa.setText("Khôi phục tất cả");
			tblDangLam = false;
			Image image = new Image(getClass().getResourceAsStream("/picture/nhanVien/system-restore.png"));
			imgXoaTatCa.setImage(image);
			lamMoiBang();
		} else {
			btnLichSuXoa.setText("Lịch sử xóa");
			btnXoaTatCa.setText("Xóa tất cả");
			tblDangLam = true;
			Image image = new Image(getClass().getResourceAsStream("/picture/KeThuoc/trash.png"));
			imgXoaTatCa.setImage(image);
			lamMoiBang();
		}
	}

	public void xoaKeThuoc(KeThuoc kt) {
		if (ktDAO.xoaKeThuoc(kt.getMaKe())) {
			kt.setTrangThai(false);
			tool.hienThiThongBao("Xóa kệ thuốc", "Đã xóa thành công", true);
		} else {
			tool.hienThiThongBao("Lỗi xóa kệ thuốc", "Không thể xóa kệ thuốc mã: " + kt.getMaKe(), false);
		}
		lamMoiBang();
	}

	public void hoanTacKeThuoc(KeThuoc kt) {
		if (ktDAO.khoiPhucKeThuoc(kt.getMaKe())) {
			kt.setTrangThai(true);
			tool.hienThiThongBao("Khôi phục kệ thuốc", "Đã khôi phục thành công", true);
		} else {
			tool.hienThiThongBao("Lỗi khôi phục kệ thuốc", "Không thể khôi phục kệ thuốc mã: " + kt.getMaKe(), false);
		}
		lamMoiBang();
	}

	public String setTrangThai(KeThuoc kt) {
		Boolean trangThai = true;
		String display = !trangThai.equals(kt.isTrangThai()) ? "Ngừng hoạt động" : "Hoạt động";
		return display;
	}

	public void setItemComboBox() {
		cmbLoaiKe.getItems().add("Tất cả");
		for(String kt : ktDAO.layTatCaKeThuoc()) {
			cmbLoaiKe.getItems().add(kt);
		}
		cmbLoaiKe.setValue("Tất cả");
		khoiTaoComboSucChua();
	}

	public void khoiTaoComboSucChua() {
		int max = listKeThuoc.stream().mapToInt(KeThuoc::getSucChua).max().orElse(0);

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
		this.QL = trangChuQLCtrl;
	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.NV = trangChuNVCtrl;

	}

}
