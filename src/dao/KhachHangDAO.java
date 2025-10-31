package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.KhachHang;

public class KhachHangDAO {

	public ToolCtrl tool = new ToolCtrl();

	// ========== TÌM KHÁCH HÀNG THEO MÃ ==========
	public KhachHang timMaKhachHangTheoSDT(String sdt) {
		ArrayList<KhachHang> listKH = layListKhachHang();
		for (KhachHang kh : listKH) {
			if (kh.getSdt().equalsIgnoreCase(sdt))
				return kh;
		}
		return null;
	}

	public boolean themKhachHangMoi(String maKH, String tenKH, String sdt, int tuoi) {
		String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, tuoi, trangThai) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maKH);
			ps.setString(2, tenKH);
			ps.setString(3, sdt);
			ps.setInt(4, tuoi);
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

	public boolean capNhapKhachHang(KhachHang kh) {
		String sql = """
				    UPDATE KhachHang
				    SET tenKH = ?, sdt = ?, tuoi = ?, trangThai = ?
				    WHERE maKH = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, kh.getTenKH());
			ps.setString(2, kh.getSdt());
			ps.setInt(3, kh.getTuoi());
			ps.setBoolean(4, kh.isTrangThai());
			ps.setString(5, kh.getMaKH());

			int rows = ps.executeUpdate();
			return rows > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

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

	public ArrayList<KhachHang> layListKhachHang() {

		ArrayList<KhachHang> list = new ArrayList<>();

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

	public Map<String, Integer> layTatCaTongDonHang() {
		Map<String, Integer> map = new HashMap<>();
		String sql = "SELECT maKH, COUNT(DISTINCT maHD) AS tongDonHang FROM HoaDon GROUP BY maKH";
		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				map.put(rs.getString("maKH"), rs.getInt("tongDonHang"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public Map<String, Double> layTatCaTongTien() {
		Map<String, Double> map = new HashMap<>();
		String sql = """
				    SELECT HD.maKH, SUM(
					CASE
						WHEN km.loaiKM = N'Giảm giá' THEN CT.soLuong * CT.donGia * (1 - km.mucKM / 100.0)
						WHEN km.loaiKM = N'Mua tặng' THEN
				            		(CT.soLuong - FLOOR(CT.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * CT.donGia
					ELSE
				            		CT.soLuong * CT.donGia
					END
					) AS tongTien
				    FROM HoaDon HD
				    JOIN CT_HoaDon CT ON HD.maHD = CT.maHD
					JOIN Thuoc t ON CT.maThuoc = t.maThuoc
					LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				    GROUP BY HD.maKH
				""";
		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				map.put(rs.getString("maKH"), rs.getDouble("tongTien"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public ArrayList<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		ArrayList<KhachHang> list = new ArrayList<>();

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
