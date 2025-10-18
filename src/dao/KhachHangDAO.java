package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.KhachHang;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KhachHangDAO {

	public ToolCtrl tool = new ToolCtrl();

	public boolean xoaKhachHang(String maKH) {
		String sql = "UPDATE KhachHang SET trangThai = 0 WHERE maKH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKH);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean khoiPhucKhachHang(String maKH) {
		String sql = "UPDATE KhachHang SET trangThai = 1 WHERE maKH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKH);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

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

		String query = """
				SELECT
				    maKH,
				    tenKH,
				    sdt,
				    tuoi,
				    trangThai
				FROM KhachHang
				ORDER BY
				    TRY_CAST(REPLACE(maKH, 'TTKH', '') AS INT);
				""";

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

	public int layTongDonHang(String maKH) {
		String sql = "SELECT COUNT(DISTINCT maHD) FROM HoaDon WHERE maKH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKH);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double layTongTien(String maKH) {
		String sql = """
				SELECT SUM(CT.soLuong * CT.donGia)
				FROM HoaDon HD
				JOIN CT_HoaDon CT ON HD.maHD = CT.maHD
				WHERE HD.maKH = ?
				""";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKH);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public ObservableList<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		ObservableList<KhachHang> list = FXCollections.observableArrayList();

		String query = """
			    SELECT maKH, tenKH, tuoi, sdt
			    FROM (
			        SELECT DISTINCT KH.maKH, KH.tenKH, KH.tuoi, KH.sdt,
			               TRY_CAST(REPLACE(KH.maKH, 'TTKH', '') AS INT) AS maSo
			        FROM KhachHang KH
			        JOIN HoaDon HD ON KH.maKH = HD.maKH
			        WHERE HD.ngayLap BETWEEN ? AND ?
			    ) AS t
			    ORDER BY t.maSo;
			    """;

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setDate(1, java.sql.Date.valueOf(ngayBD));
			pstmt.setDate(2, java.sql.Date.valueOf(ngayKT));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				list.add(new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getInt("tuoi"),
						rs.getString("sdt")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
