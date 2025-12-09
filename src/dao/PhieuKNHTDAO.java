package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuKhieuNaiHoTro;

public class PhieuKNHTDAO {

	public ToolCtrl tool = new ToolCtrl();

	public boolean themPhieu(PhieuKhieuNaiHoTro knht) {
		String sql = "INSERT INTO Phieu_KhieuNai_HoTroKH (maPhieu, maNV, maKH, ngayLap, noiDung, loaiDon, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, knht.getMaPhieu());
			ps.setString(2, knht.getNhanVien().getMaNV());
			ps.setString(3, knht.getKhachHang().getMaKH());
			ps.setDate(4, java.sql.Date.valueOf(knht.getNgayLap()));
			ps.setString(5, knht.getNoiDung());
			ps.setString(6, knht.getLoaiDon());
			ps.setString(7, knht.getTrangThai());

			int rows = ps.executeUpdate();
			return rows > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean capNhatPhieu(PhieuKhieuNaiHoTro knht) {
		String sqlKhachHang = """
				UPDATE KhachHang
				SET tenKH = ?, sdt = ?
				WHERE maKH = ?
				""";

		String sqlNhanVien = """
				UPDATE NhanVien
				SET tenNV = ?
				WHERE maNV = ?
				""";

		String sqlPhieu = """
				UPDATE Phieu_KhieuNai_HoTroKH
				SET noiDung = ?, loaiDon = ?, trangThai = ?
				WHERE maPhieu = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection()) {
			con.setAutoCommit(false);

			try (PreparedStatement psKH = con.prepareStatement(sqlKhachHang);
					PreparedStatement psNV = con.prepareStatement(sqlNhanVien);
					PreparedStatement psP = con.prepareStatement(sqlPhieu)) {
				// --- Cập nhật Khách Hàng ---
				psKH.setString(1, knht.getKhachHang().getTenKH());
				psKH.setString(2, knht.getKhachHang().getSdt());
				psKH.setString(3, knht.getKhachHang().getMaKH());
				psKH.executeUpdate();

				// --- Cập nhật Nhân Viên ---
				psNV.setString(1, knht.getNhanVien().getTenNV());
				psNV.setString(2, knht.getNhanVien().getMaNV());
				psNV.executeUpdate();

				// --- Cập nhật Phiếu ---
				psP.setString(1, knht.getNoiDung());
				psP.setString(2, knht.getLoaiDon());
				psP.setString(3, knht.getTrangThai());
				psP.setString(4, knht.getMaPhieu());
				psP.executeUpdate();

				con.commit();
				return true;

			} catch (Exception e) {
				con.rollback();
				e.printStackTrace();
				return false;
			} finally {
				con.setAutoCommit(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean hoanTatPhieu(PhieuKhieuNaiHoTro knht) {
		String query = "UPDATE Phieu_KhieuNai_HoTroKH SET trangThai = N'Hoàn tất' WHERE maPhieu = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setString(1, knht.getMaPhieu());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean hoanTacPhieu(PhieuKhieuNaiHoTro knht) {
		String query = "UPDATE Phieu_KhieuNai_HoTroKH SET trangThai = N'Chờ xử lý' WHERE maPhieu = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setString(1, knht.getMaPhieu());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public ArrayList<PhieuKhieuNaiHoTro> layListPhieuKNHT() {
		ArrayList<PhieuKhieuNaiHoTro> list = new ArrayList<>();

		String query = """
				SELECT
				    p.maPhieu,
				    p.maNV,
				    nv.tenNV,
				    p.maKH,
				    kh.tenKH,
				    kh.sdt,
				    p.ngayLap,
				    p.noiDung,
				    p.loaiDon,
				    p.trangThai
				FROM Phieu_KhieuNai_HoTroKH p
				JOIN NhanVien nv ON p.maNV = nv.maNV
				JOIN KhachHang kh ON p.maKH = kh.maKH
				ORDER BY TRY_CAST(REPLACE(p.maPhieu, 'TTPKN', '') AS INT);
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"));

				KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getString("sdt"));

				String maPhieu = rs.getString("maPhieu");
				LocalDate ngayLap = rs.getDate("ngayLap").toLocalDate();
				String noiDung = rs.getString("noiDung");
				String loaiDon = rs.getString("loaiDon");
				String trangThai = rs.getString("trangThai");

				PhieuKhieuNaiHoTro phieu = new PhieuKhieuNaiHoTro(maPhieu, nv, kh, ngayLap, noiDung, loaiDon,
						trangThai);

				list.add(phieu);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
