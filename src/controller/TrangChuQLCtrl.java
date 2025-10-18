package controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrangChuQLCtrl {

	@FXML
	public VBox vbTrangChu, vbThuoc, vbKeThuoc, vbKhachHang, vbHoaDon, vbNhanVien, vbKhuyenMai;

	@FXML
	public ImageView imgDangXuat;

	@FXML
	public ImageView imgTaiKhoan;

	public VBox vbHienTai;
	public Label MenuConHienTai;
	public static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");
	public boolean isSelected = false;

	@FXML
	public BorderPane mainPane;
	public Node mainPaneCenter;

	public void initialize() {
		mainPaneCenter = mainPane.getCenter();
		setThuocTinhMainMenu();
		moTrangTT(imgTaiKhoan);
	}

	public void moTrangDaTai(Parent root) {
		mainPane.setCenter(root);
	}

	public void setThuocTinhMainMenu() {
		setMauClickVaMenuCon(vbTrangChu, List.of());
		setMauClickVaMenuCon(vbThuoc,
				List.of("Tìm kiếm thuốc", "Thêm thuốc", "Nhập thuốc", "Đặt thuốc bán", "Thống kê thuốc", "Thuế"));
		setMauClickVaMenuCon(vbKeThuoc, List.of("Danh sách kệ", "Thêm kệ thuốc"));
		setMauClickVaMenuCon(vbKhachHang,
				List.of("Tìm kiếm khách hàng", "Thêm khách hàng", "Khiếu nại & Hỗ trợ", "Thống kê khách hàng"));
		setMauClickVaMenuCon(vbHoaDon, List.of("Tìm kiếm hóa đơn", "Danh sách phiếu đặt thuốc", "Lập hóa đơn",
				"Đặt thuốc", "Đổi - Trả", "Thống kê hóa đơn"));
		setMauClickVaMenuCon(vbNhanVien, List.of("Tìm kiếm nhân viên", "Thêm nhân viên"));
		setMauClickVaMenuCon(vbKhuyenMai, List.of("Danh sách khuyến mãi", "Thêm Khuyến Mãi"));
	}

	@SuppressWarnings("unused")
	public void setMauClickVaMenuCon(VBox box, List<String> menuCon) {
		box.getStyleClass().add("hbox_menu");
		box.setOnMouseClicked(e -> menuChaClicked(box, menuCon));
	}

	public void menuChaClicked(VBox box, List<String> menuCon) {
		if (vbHienTai != null && vbHienTai != box) {
			vbHienTai.pseudoClassStateChanged(SELECTED, false);

			if (!vbHienTai.getChildren().isEmpty()
					&& vbHienTai.getChildren().get(vbHienTai.getChildren().size() - 1) instanceof VBox) {
				vbHienTai.getChildren().remove(vbHienTai.getChildren().size() - 1);
			}
		}

		if (vbHienTai == box && isSelected) {
			if (!box.getChildren().isEmpty() && box.getChildren().get(box.getChildren().size() - 1) instanceof VBox) {
				box.getChildren().remove(box.getChildren().size() - 1);
			}
			box.pseudoClassStateChanged(SELECTED, false);
			vbHienTai = null;
			isSelected = false;
			return;
		}

		box.pseudoClassStateChanged(SELECTED, true);

		VBox subMenu = new VBox();
		subMenu.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 5; -fx-spacing: 5;");
		subMenu.setTranslateY(5);

		for (String name : menuCon) {
			subMenu.getChildren().add(taoMenuCon(name));
		}

		box.getChildren().add(subMenu);

		vbHienTai = box;
		isSelected = true;
	}

	public Label taoMenuCon(String name) {
		Label lbl = new Label(name);
		lbl.setId(name);
		lbl.getStyleClass().add("submenu_label");
		lbl.setStyle("-fx-padding: 8 5 8 10; -fx-cursor: hand; -fx-font-size: 15px; -fx-font-weight: bold;");
		lbl.setMaxWidth(Double.MAX_VALUE);

		lbl.setOnMouseClicked(ev -> {
			menuConClick(lbl);
			ev.consume();
		});

		return lbl;
	}

	public void dangXuat() {
		Stage stage = (Stage) imgDangXuat.getScene().getWindow();
		Parent root = null;

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Xác nhận đăng xuất");
		alert.setHeaderText("Bạn có chắc chắn muốn đăng xuất?");
		alert.setContentText("Nhấn OK để xác nhận hoặc Cancel để ở lại.");

		Stage stageAlert = (Stage) alert.getDialogPane().getScene().getWindow();
		stageAlert.getIcons().add(new Image("/picture/trangChu/logo.jpg"));

		Optional<ButtonType> ketQua = alert.showAndWait();

		if (ketQua.isPresent() && ketQua.get() == ButtonType.OK) {
			try {
				root = FXMLLoader.load(getClass().getResource("/fxml/DangNhap.fxml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StackPane container = new StackPane(root);
			Scene scene = new Scene(container);
			stage.setScene(scene);
			stage.setMaximized(false);
			stage.centerOnScreen();
			stage.setResizable(true);
		}
	}

	public void menuConClick(Label lbl) {
		String text = lbl.getText().trim();

		switch (text) {
		// ===== THUỐC =====
		case "Tìm kiếm thuốc":
			moTrang("/fxml/TimKiemThuoc.fxml", TimKiemThuocCtrl.class);
			break;
		case "Thêm thuốc":
			doiCenterPane("/fxml/ThemThuoc.fxml");
			break;
		case "Nhập thuốc":
			doiCenterPane("/fxml/NhapThuoc.fxml");
			break;
		case "Đặt thuốc bán":
			doiCenterPane("/fxml/DatThuoc.fxml");
			break;
		case "Thống kê thuốc":
			doiCenterPane("/fxml/ThongKeThuoc.fxml");
			break;
		case "Thuế":
			doiCenterPane("/fxml/Thue.fxml");
			break;

		// ===== KỆ THUỐC =====
		case "Danh sách kệ":
			moTrangDanhSachKeThuoc();
			break;
		case "Thêm kệ thuốc":
			doiCenterPane("/fxml/ThemKeThuoc.fxml");
			break;

		// ===== KHÁCH HÀNG =====
		case "Tìm kiếm khách hàng":
			TimKiemKHCtrl tkKHCtrl = doiCenterPane("/fxml/TimKiemKH.fxml");
			tkKHCtrl.setTrangChuQLCtrl(this);
			break;
		case "Thêm khách hàng":
			doiCenterPane("/fxml/ThemKhachHang.fxml");
			break;
		case "Khiếu nại & Hỗ trợ":
			moTrang("/fxml/DanhSachKhieuNaiVaHoTroKH.fxml", DanhSachKhieuNaiVaHoTroHKCtrl.class);
			break;
		case "Thống kê khách hàng":
			moTrang("/fxml/ThongKeKhachHang.fxml", ThongKeKhachHangCtrl.class);
			break;

		// ===== HÓA ĐƠN =====
		case "Tìm kiếm hóa đơn":
			moTrang("/fxml/TimKiemHD.fxml", TimKiemHDCtrl.class);
			break;
		case "Lập hóa đơn":
			doiCenterPane("/fxml/LapHoaDon.fxml");
			break;
		case "Danh sách phiếu đặt thuốc":

			break;
		case "Đặt thuốc":

			break;
		case "Đổi - Trả":
			doiCenterPane("/fxml/PhieuDoiTra.fxml");
			break;
		case "Thống kê hóa đơn":
			doiCenterPane("/fxml/ThongKeHoaDon.fxml");
			break;

		// ===== NHÂN VIÊN =====
		case "Tìm kiếm nhân viên":
			moTrang("/fxml/TimKiemNhanVien.fxml", TimKiemNhanVienCtrl.class);
			break;
		case "Thêm nhân viên":
			doiCenterPane("/fxml/ThemNhanVien.fxml");
			break;

		// ===== Khuyến mãi=====
		case "Danh sách khuyến mãi":
			DanhSachKhuyenMaiCtrl dsKMCtrl =  doiCenterPane("/fxml/DanhSachKhuyenMai.fxml");
			dsKMCtrl.setTrangChuQLCtrl(this);
			break;
		
		case "Thêm Khuyến Mãi":
			doiCenterPane("/fxml/ThemKhuyenMai.fxml");
		default:
			System.out.println("⚠ Menu chưa xử lý: " + text);
			break;
		}

		if (MenuConHienTai != null) {
			MenuConHienTai.pseudoClassStateChanged(SELECTED, false);
		}
		lbl.pseudoClassStateChanged(SELECTED, true);
		MenuConHienTai = lbl;
	}

	public void quayLaiTrangChu() {
		mainPane.setCenter(mainPaneCenter);
	}

	public <T> T doiCenterPane(String fxmlPath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();

			mainPane.setCenter(root);

			return loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ========== MỞ TRANG THÔNG TIN ==========
	public void setTrangTaiKhoan() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThongTin.fxml"));
			Parent root = loader.load();

			// lấy controller của ThongTin.fxml
			ThongTinCtrl controller = loader.getController();
			controller.setTrangChuQLCtrl(this);

			mainPane.setCenter(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void moTrangTT(ImageView imgTaiKhoan) {
		imgTaiKhoan.setOnMouseClicked(e -> setTrangTaiKhoan());
	}

	// ========== MỞ TRANG DANH SÁCH KỆ THUỐC ==========
	public void moTrangDanhSachKeThuoc() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DanhSachKeThuoc.fxml"));
			Parent root = loader.load();

			// lấy controller của ThongTin.fxml
			DanhSachKeThuocCtrl controller = loader.getController();
			controller.setTrangChuQLCtrl(this);

			mainPane.setCenter(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ========== MỞ TRANG ChI TIẾT KỆ THUỐC ==========
	public void setTrangChiTietKeThuoc() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChiTietKeThuoc.fxml"));
			Parent root = loader.load();

			// lấy controller của ChiTietKeThuoc.fxml
			ChiTietKeThuocCtrl controller = loader.getController();
			controller.setTrangChuQLCtrl(this);
			mainPane.setCenter(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> T moTrang(String fxmlPath, Class<T> controllerClass) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();

			// Lấy controller của FXML
			T controller = loader.getController();

			// Nếu controller có setTrangChuNVCtrl thì tự động gán
			try {
				controllerClass.getMethod("setTrangChuQLCtrl", TrangChuQLCtrl.class).invoke(controller, this);
			} catch (NoSuchMethodException ignore) {

			}

			mainPane.setCenter(root);
			return controller;
		} catch (IOException | ReflectiveOperationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
