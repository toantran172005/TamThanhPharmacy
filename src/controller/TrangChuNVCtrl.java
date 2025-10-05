package controller;

import java.io.IOException;
import java.util.List;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TrangChuNVCtrl {

	@FXML
	public VBox vbTrangChu, vbThuoc, vbKeThuoc, vbKhachHang, vbHoaDon;
	
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

	public void setThuocTinhMainMenu() {
		setMauClickVaMenuCon(vbTrangChu, List.of());
		setMauClickVaMenuCon(vbThuoc,
				List.of("Tìm kiếm thuốc", "Thêm thuốc", "Cập nhật thuốc"));
		setMauClickVaMenuCon(vbKeThuoc, List.of("Danh sách kệ", "Thêm kệ thuốc", "Cập nhật kệ"));
		setMauClickVaMenuCon(vbKhachHang,
				List.of("Tìm kiếm khách hàng", "Thêm khách hàng","Khiếu nại & Hỗ trợ"));
		setMauClickVaMenuCon(vbHoaDon, List.of("Tìm kiếm hóa đơn", "Lập hóa đơn", "Đổi - Trả"));
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

	public void menuConClick(Label lbl) {
	    String text = lbl.getText().trim();

	    switch (text) {
	        // ===== THUỐC =====
	        case "Tìm kiếm thuốc":

	            break;
	        case "Thêm thuốc":

	            break;
	        case "Cập nhật thuốc":

	            break;

	        // ===== KỆ THUỐC =====
	        case "Danh sách kệ":

	            break;
	        case "Thêm kệ thuốc":

	            break;
	        case "Cập nhật kệ":

	            break;

	        // ===== KHÁCH HÀNG =====
	        case "Tìm kiếm khách hàng":
	        	doiCenterPane("/fxml/TimKiemKH.fxml");
	            break;
	        case "Thêm khách hàng":
	        	doiCenterPane("/fxml/ThemKhachHang.fxml");
	            break;
	        case "Khiếu nại & Hỗ trợ":

	            break;

	        // ===== HÓA ĐƠN =====
	        case "Tìm kiếm hóa đơn":

	            break;
	        case "Lập hóa đơn":

	            break;
	        case "Đổi - Trả":

	            break;

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
	
	public void dangXuat() {
		Stage stage = (Stage) imgDangXuat.getScene().getWindow();
		Parent root = null;
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
		        controller.setTrangChuNVCtrl(this);

		        mainPane.setCenter(root);
		    } 
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		public void moTrangTT(ImageView imgTaiKhoan) {
		    imgTaiKhoan.setOnMouseClicked(e -> setTrangTaiKhoan());
		}
}