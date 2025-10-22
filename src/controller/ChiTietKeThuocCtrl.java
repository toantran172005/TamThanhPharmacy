package controller;

import dao.KeThuocDAO;
import entity.KeThuoc;
import entity.Thuoc;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ChiTietKeThuocCtrl {

	@FXML
	public Button btnThoat, btnCapNhat;
	@FXML
	public TableView<Thuoc> tblChiTietKT;
	@FXML
	public TableColumn<Thuoc, Integer> colSTT;
	@FXML
	public TableColumn<Thuoc, String> colMaThuoc;
	@FXML
	public TableColumn<Thuoc, String> colTenThuoc;
	@FXML
	public TableColumn<Thuoc, String> colTrangThai;
	@FXML
	public TextField txtMaKe, txtLoaiKe, txtSucChua;
	@FXML
	public TextArea txaMoTa;
	public KeThuocDAO ktDAO = new KeThuocDAO();
	public KeThuoc keThuoc;
	public TrangChuQLCtrl QL;
	public TrangChuNVCtrl NV;

	public void initialize() {
		setHoatDong();
	}
	
	public void setHoatDong() {
		btnThoat.setOnAction(event -> veTrangDanhSach());
		btnCapNhat.setOnAction(event -> capNhapKeThuoc());
	}
	
	public void capNhapKeThuoc() {
		if(btnCapNhat.getText().equals("Cập nhật")) {
			choPhepCapNhat(true);
			btnCapNhat.setText("Hoàn tất");
		} else {
			choPhepCapNhat(false);
			btnCapNhat.setText("Cập nhật");
		}
	}

	public void setTrangChuQLCtrl(TrangChuQLCtrl trangChuQLCtrl) {
		this.QL = trangChuQLCtrl;

	}

	public void setTrangChuNVCtrl(TrangChuNVCtrl trangChuNVCtrl) {
		this.NV = trangChuNVCtrl;
	}

	public void hienThiThongTin(KeThuoc kt) {
		keThuoc = kt;
		setDataChoTXT(kt);
		choPhepCapNhat(false);
		setDataChoTable(kt.getMaKe());
	}
	
	public void choPhepCapNhat(boolean capNhat) {
		if(capNhat) {
			txtLoaiKe.setEditable(true);
			txtSucChua.setEditable(true);
			txaMoTa.setEditable(true);
		} else {
			txtMaKe.setEditable(false);
			txtLoaiKe.setEditable(false);
			txtSucChua.setEditable(false);
			txaMoTa.setEditable(false);
		}
	}

	public void setDataChoTable(String maKe) {
		ObservableList<Thuoc> list = ktDAO.layListThuocTrongKe(maKe);

		colSTT.setCellValueFactory(cellData -> {
			return new SimpleIntegerProperty(tblChiTietKT.getItems().indexOf(cellData.getValue()) + 1).asObject();
		});
		colMaThuoc.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
		colTenThuoc.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
		colTrangThai.setCellValueFactory(cellData -> {
			return new SimpleStringProperty(setTrangThai(cellData.getValue()));
		});

		tblChiTietKT.setItems(list);

	}

	public String setTrangThai(Thuoc thuoc) {
		Boolean trangThai = true;
		return trangThai.equals(thuoc.getTrangThai()) ? "Đang kinh doanh" : "Ngừng kinh doanh";
	}

	public void setDataChoTXT(KeThuoc kt) {
		txtMaKe.setText(kt.getMaKe());
		txtLoaiKe.setText(kt.getLoaiKe());
		txaMoTa.setText(kt.getMoTa());
		txtSucChua.setText(String.valueOf(kt.getSucChua()));
	}

	public void veTrangDanhSach() {
		if (QL != null) {
			QL.moTrangDanhSachKeThuoc();
		} else if (NV != null) {
			NV.moTrangDanhSachKeThuoc();
		}
	}

}
