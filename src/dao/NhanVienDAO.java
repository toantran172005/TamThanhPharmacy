package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.Thue;

public class NhanVienDAO {
	public ToolCtrl toolCtrl = new ToolCtrl();
	public ArrayList<NhanVien> listNV = new ArrayList<>();

	// ========== TÌM NHÂN VIÊN THEO MÃ ==========
	public NhanVien timNhanVienTheoMa(String maNV) {
		if (listNV.isEmpty()) {
			layListNhanVien();
		}
		for (NhanVien nv : listNV) {
			if (nv.getMaNV().equalsIgnoreCase(maNV))
				return nv;
		}
		return null;
	}

	// ========== LẤY LIST NHÂN VIÊN ==========
	public ArrayList<NhanVien> layListNhanVien() {
		listNV.clear();

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT nv.*, t.tyLeThue FROM NhanVien nv " + "JOIN Thue t ON nv.maThue = t.maThue "
								+ "ORDER BY TRY_CAST(SUBSTRING(nv.maNV, 5, LEN(nv.maNV)) AS INT)")) {

			while (rs.next()) {
				Thue thue = new Thue(rs.getString("maThue"), rs.getDouble("tyLeThue"));

				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"), rs.getString("chucVu"),

						toolCtrl.sqlDateSangLocalDate(rs.getDate("ngaySinh")), rs.getBoolean("gioiTinh"),
						rs.getString("sdt"), toolCtrl.sqlDateSangLocalDate(rs.getDate("ngayVaoLam")),

						rs.getDouble("luong"), thue, rs.getBoolean("trangThai"), rs.getString("anh"));

				listNV.add(nv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listNV;
	}

	// ========== LIST NHÂN VIÊN CÒN LÀM ==========
	public ArrayList<NhanVien> layNhanVienDangLam() {
		layListNhanVien();
		if (listNV.isEmpty()) {
			layListNhanVien();
		}
		ArrayList<NhanVien> listDangLam = new ArrayList<>();
		for (NhanVien nv : listNV) {
			if (nv.isTrangThai()) {
				listDangLam.add(nv);
			}
		}

		return listDangLam;
	}

	// ========== LIST NHÂN VIÊN ĐÃ NGHỈ ==========
	public ArrayList<NhanVien> layNhanVienNghiLam() {
		layListNhanVien();
		if (listNV.isEmpty()) {
			layListNhanVien();
		}
		ArrayList<NhanVien> listDaNghi = new ArrayList<>();
		for (NhanVien nv : listNV) {
			if (!nv.isTrangThai()) {
				listDaNghi.add(nv);
			}
		}

		return listDaNghi;
	}

	// ========== XOÁ NHÂN VIÊN THEO MÃ ==========
	public boolean xoaNhanVien(String maNV) {
		String sql = "UPDATE NhanVien SET trangThai = 0 WHERE maNV = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNV);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========== KHÔI PHỤC NHÂN VIÊN THEO MÃ ==========
	public boolean khoiPhucNhanVien(String maNV) {
		String sql = "UPDATE NhanVien SET trangThai = 1 WHERE maNV = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNV);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========== CẬP NHẬT THÔNG TIN NHÂN VIÊN ==========
	public boolean capNhatNhanVien(NhanVien nv) {
		String sql = "UPDATE NhanVien SET tenNV=?, chucVu=?, gioiTinh=?, luong=?, sdt=?, anh=? WHERE maNV=?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nv.getTenNV());
			ps.setString(2, nv.getChucVu());
			ps.setBoolean(3, nv.isGioiTinh());
			ps.setDouble(4, nv.getLuong());
			ps.setString(5, nv.getSdt());
			ps.setString(6, nv.getAnh());
			ps.setString(7, nv.getMaNV());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========== THÊM NHÂN VIÊN ==========
	public boolean themNhanVien(NhanVien nv) {
		String sql = "INSERT INTO NhanVien (maNV, tenNV, chucVu, ngaySinh, gioiTinh, sdt, ngayVaoLam, luong, maThue, trangThai, anh) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nv.getMaNV());
			ps.setString(2, nv.getTenNV());
			ps.setString(3, nv.getChucVu());
			ps.setDate(4, java.sql.Date.valueOf(nv.getNgaySinh()));
			ps.setBoolean(5, nv.isGioiTinh());
			ps.setString(6, nv.getSdt());
			ps.setDate(7, java.sql.Date.valueOf(nv.getNgayVaoLam()));
			ps.setDouble(8, nv.getLuong());
			ps.setString(9, nv.getThue() != null ? nv.getThue().getMaThue() : null);
			ps.setBoolean(10, nv.isTrangThai());
			ps.setString(11, nv.getAnh());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// ========== THÊM TÀI KHOẢN NHÂN VIÊN ==========
	public boolean themTaiKhoan(TaiKhoan tk) {
		String sql = "INSERT INTO TaiKhoan (maTK, maNV, tenDangNhap, matKhau, trangThai, loaiTK, email) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, tk.getMaTK());
			ps.setString(2, tk.getNhanVien().getMaNV());
			ps.setString(3, tk.getTenDangNhap());
			ps.setString(4, tk.getMatKhau());
			ps.setBoolean(5, tk.getTrangThai());
			ps.setString(6, tk.getLoaiTK());
			ps.setString(7, tk.getEmail());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========== LẤY EMAIL CỦA NHÂN VIÊN ==========
	public String layEmailNV(String maNV) {
		String sql = """
				SELECT tk.email
				FROM NhanVien nv JOIN TaiKhoan tk ON nv.maNV = tk.maNV
				WHERE nv.maNV = ?
				""";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maNV);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	// ========== LẤY ẢNH CỦA NHÂN VIÊN ==========
	public String layAnhNV(String maNV) {
	    String sql = "SELECT anh FROM NhanVien WHERE maNV = ?";
	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, maNV);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getString("anh"); 
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; 
	}

}
