package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KhachHangDAO {

	public ToolCtrl tool = new ToolCtrl();

	public boolean themKhachHang(String maKH, String tenKH, String sdt, String tuoi) {
		String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, tuoi, trangThai) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maKH);
			ps.setString(2, tenKH);
			ps.setString(3, sdt);
			ps.setInt(4, Integer.parseInt(tuoi));
			ps.setInt(5, 1); // trạng thái mặc định: 1 = hoạt động

			int rows = ps.executeUpdate();

			if (rows > 0) {
				tool.hienThiThongBao("Thêm khách hàng thành công",
						"Khách hàng '" + tenKH + "' đã được thêm vào hệ thống.", true);
				return true;
			} else {
				tool.hienThiThongBao("Không thể thêm khách hàng", "Không có bản ghi nào được chèn.", false);
				return false;
			}

		} catch (NumberFormatException e) {
			tool.hienThiThongBao("Tuổi không hợp lệ", "Tuổi phải là số nguyên hợp lệ.", false);
			return false;

		} catch (SQLException e) {
			tool.hienThiThongBao("Lỗi khi thêm khách hàng", e.getMessage(), false);
			return false;
		}
	}

	public ObservableList<KhachHang> layListKhachHang() {

		ObservableList<KhachHang> list = FXCollections.observableArrayList();

		String query = "SELECT \r\n" + "    maKH, \r\n" + "    tenKH, \r\n" + "    sdt, \r\n" + "    tuoi, \r\n"
				+ "    trangThai\r\n" + "FROM KhachHang\r\n" + "ORDER BY \r\n"
				+ "    TRY_CAST(REPLACE(maKH, 'TTKH', '') AS INT);\r\n" + "";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				list.add(new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdt"),
						rs.getInt("tuoi"), rs.getBoolean("trangThai")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ObservableList<KhachHang> layListKHThongKe() {
		ObservableList<KhachHang> list = FXCollections.observableArrayList();

		String query = "SELECT \r\n" + "    KH.maKH,\r\n" + "    KH.tenKH,\r\n"
				+ "    COUNT(DISTINCT HD.maHD) AS tongDonHang,\r\n" + "    SUM(CT.soLuong * CT.donGia) AS tongTien\r\n"
				+ "FROM KhachHang KH\r\n" + "JOIN HoaDon HD ON KH.maKH = HD.maKH\r\n"
				+ "JOIN CT_HoaDon CT ON HD.maHD = CT.maHD\r\n" + "GROUP BY KH.maKH, KH.tenKH, KH.sdt\r\n" + "";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				list.add(new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getInt("tongDonHang"),
						rs.getDouble("tongTien")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
