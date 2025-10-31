package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.KetNoiDatabase;
import entity.KeThuoc;
import entity.Thuoc;

public class KeThuocDAO {

	public boolean capNhatKeThuoc(KeThuoc kt) {
		String sql = """
				UPDATE KeThuoc
				SET loaiKe = ?, sucChua = ?, moTa = ?, trangThai = ?
				WHERE maKe = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, kt.getLoaiKe());
			ps.setInt(2, kt.getSucChua());
			ps.setString(3, kt.getMoTa());
			ps.setBoolean(4, kt.isTrangThai());
			ps.setString(5, kt.getMaKe());

			int rows = ps.executeUpdate();
			return rows > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean themKeThuoc(String maKe, String loaiKe, String sucChua, String moTa) {
		String query = "INSERT INTO dbo.KeThuoc (maKe, loaiKe, sucChua, moTa, trangThai) "
				+ "VALUES (?, ?, ?, ?, DEFAULT)";
		try (Connection conn = KetNoiDatabase.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, maKe);
			ps.setString(2, loaiKe);
			ps.setInt(3, Integer.parseInt(sucChua));
			ps.setString(4, moTa);

			int rows = ps.executeUpdate();
			return rows > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<String> layTatCaKeThuoc() {
		ArrayList<String> list = new ArrayList<>();
		String query = "SELECT loaiKe FROM KeThuoc";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stsm = con.createStatement();
				ResultSet rs = stsm.executeQuery(query);) {

			while (rs.next()) {
				list.add(rs.getString("loaiKe"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public boolean xoaKeThuoc(String maKe) {
		String sql = "UPDATE KeThuoc SET trangThai = 0 WHERE maKe = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKe);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean khoiPhucKeThuoc(String maKe) {
		String sql = "UPDATE KeThuoc SET trangThai = 1 WHERE maKe = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKe);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<KeThuoc> layListKeThuoc() {
		ArrayList<KeThuoc> list = new ArrayList<>();

		String query = "SELECT maKe, loaiKe, sucChua, moTa, trangThai FROM KeThuoc";
		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				list.add(new KeThuoc(rs.getString("maKe"), rs.getString("loaiKe"), rs.getInt("sucChua"),
						rs.getString("moTa"), rs.getBoolean("trangThai")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Thuoc> layListThuocTrongKe(String maKe) {
		ArrayList<Thuoc> dsThuoc = new ArrayList<>();
		String sql = "SELECT maThuoc, tenThuoc, trangThai FROM Thuoc WHERE maKe = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setString(1, maKe);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String maThuoc = rs.getString("maThuoc");
				String tenThuoc = rs.getString("tenThuoc");
				Boolean trangThai = rs.getBoolean("trangThai");

				Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, trangThai);
				dsThuoc.add(thuoc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dsThuoc;
	}

	
	public KeThuoc timTheoTen(String tenKe) {
	    String sql = "SELECT * FROM KeThuoc WHERE loaiKe = ?";
	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, tenKe);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new KeThuoc(
	                rs.getString("maKe"),
	                rs.getString("loaiKe"),
	                rs.getInt("sucChua"),
	                rs.getString("moTa"),
	                rs.getBoolean("trangThai")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	
}
