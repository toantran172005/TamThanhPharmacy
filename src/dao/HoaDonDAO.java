package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.KhachHang;
import entity.NhanVien;
import entity.Thue;
import entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HoaDonDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();
	NhanVienDAO nvDAO = new NhanVienDAO();
	KhachHangDAO khDAO = new KhachHangDAO();
	ThuocDAO thuocDAO = new ThuocDAO();

	public ObservableList<Object[]> layListHoaDon() {
		ObservableList<Object[]> listHD = FXCollections.observableArrayList();

		String sql = """
				    SELECT hd.maHD, hd.maKH, kh.tenKH, hd.maNV, nv.tenNV, hd.loaiTT,
				           hd.ngayLap, hd.diaChiHT, hd.tenHT, hd.ghiChu, hd.hotline, hd.tienNhan
				    FROM HoaDon hd
				    JOIN NhanVien nv ON hd.maNV = nv.maNV
				    JOIN KhachHang kh ON hd.maKH = kh.maKH
				    ORDER BY TRY_CAST(SUBSTRING(hd.maHD, 5, LEN(hd.maHD)) AS INT)
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Object[] hd = { rs.getString("maHD"), rs.getString("maKH"), rs.getString("tenKH"), rs.getString("maNV"),
						rs.getString("tenNV"), rs.getString("loaiTT"), rs.getDate("ngayLap").toLocalDate(),
						rs.getString("diaChiHT"), rs.getString("tenHT"), rs.getString("ghiChu"),
						rs.getString("hotline"), rs.getDouble("tienNhan") };
				listHD.add(hd);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listHD;
	}

	public boolean themHoaDon(String maHD, String maKH, String maNV, String loaiTT, LocalDate ngayLap, String diaChiHT,
			String tenHT, String ghiChu, String hotline, double tienNhan) {
		String sql = "INSERT INTO HoaDon (maHD, maKH, maNV, loaiTT, ngayLap, diaChiHT, tenHT, ghiChu, hotline, tienNhan) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maHD);
			ps.setString(2, maKH);
			ps.setString(3, maNV);
			ps.setString(4, loaiTT);
			ps.setDate(5, java.sql.Date.valueOf(ngayLap));
			ps.setString(6, diaChiHT);
			ps.setString(7, tenHT);
			ps.setString(8, ghiChu);
			ps.setString(9, hotline);
			ps.setDouble(10, tienNhan);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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

	public double layTongTienTheoSanPham(String maHD, String maSP) {
		String sql = """
				SELECT CT.soLuong * CT.donGia
				FROM CT_HoaDon CT
				WHERE CT.maHD = ? AND CT.maThuoc = ?
							""";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			ps.setString(2, maSP);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double tinhTongTienTheoHoaDon(String maHD) {
		String sql = "SELECT SUM(donGia * soLuong) AS tongTien FROM CT_HoaDon WHERE maHD = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("tongTien");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Map<LocalDate, Double> layDoanhThuTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
		Map<LocalDate, Double> map = new LinkedHashMap<>();
		String sql = """
				SELECT hd.ngayLap, SUM(ct.soLuong * ct.donGia) AS tongTien
				FROM HoaDon hd
				JOIN CT_HoaDon ct ON hd.maHD = ct.maHD
				WHERE hd.ngayLap BETWEEN ? AND ?
				GROUP BY hd.ngayLap
				ORDER BY hd.ngayLap
				    """;
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setDate(1, java.sql.Date.valueOf(ngayBD));
			pstmt.setDate(2, java.sql.Date.valueOf(ngayKT));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getDate("ngayLap").toLocalDate(), rs.getDouble("tongTien"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
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

	public ObservableList<Object[]> layDanhSachThuocTheoHoaDon(String maHD) {
		ObservableList<Object[]> list = FXCollections.observableArrayList();
		String sql = """
				    SELECT ct.maHD, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.donGia,
				           (ct.soLuong * ct.donGia) AS thanhTien
				    FROM CT_HoaDon ct
				    JOIN Thuoc t ON ct.maThuoc = t.maThuoc
				    WHERE ct.maHD = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maHD);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maHD"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getDouble("donGia"), rs.getDouble("thanhTien") };
				list.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// ========== TÌM KHÁCH HÀNG THEO MÃ ==========
	public Object[] timHoaDonTheoMa(String maHD) {
		for (Object[] row : layListHoaDon()) {
			if (row[0].toString().equalsIgnoreCase(maHD))
				return row;
		}
		return null;
	}

}
