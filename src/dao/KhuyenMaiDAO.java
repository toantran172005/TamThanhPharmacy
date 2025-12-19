package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.DonViTinh;
import entity.KhuyenMai;
import entity.Thuoc;

public class KhuyenMaiDAO {

	ToolCtrl tool = new ToolCtrl();
	ThuocDAO thuocDAO = new ThuocDAO();

	public KhuyenMai layKhuyenMaiTheoMa(String maKM) {
		String sql = "SELECT * FROM KhuyenMai WHERE maKM = ? AND trangThai = 1";
		try (Connection conn = KetNoiDatabase.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maKM);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				KhuyenMai km = new KhuyenMai();
				km.setMaKM(rs.getString("maKM"));
				km.setTenKM(rs.getString("tenKM"));
				km.setLoaiKM(rs.getString("loaiKM"));
				km.setMucKM(rs.getInt("mucKM"));
				km.setTrangThai(rs.getBoolean("trangThai"));
				km.setNgayBD(tool.sqlDateSangLocalDate(rs.getDate("ngayBD")));
				km.setNgayKT(tool.sqlDateSangLocalDate(rs.getDate("ngayKT")));
				km.setSoLuongMua(rs.getInt("soLuongMua"));
				km.setSoLuongTang(rs.getInt("soLuongTang"));
				return km;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public boolean capNhatKhuyenMai(KhuyenMai km, List<String> danhSachMaThuoc) {
//	    Connection con = null;
//	    PreparedStatement psKM = null;
//	    PreparedStatement psThuoc = null;
//	    
//	    String sqlKM = """
//	            UPDATE KhuyenMai
//	            SET tenKM = ?,
//	                mucKM = ?,
//	                ngayBD = ?,
//	                ngayKT = ?,
//	                trangThai = ?,
//	                loaiKM = ?,
//	                soLuongMua = ?,
//	                soLuongTang = ?
//	            WHERE maKM = ?
//	            """;
//	            
//	    String sqlThuoc = "UPDATE Thuoc SET maKM = ? WHERE maThuoc = ?";
//
//	    try {
//	        con = KetNoiDatabase.getConnection();
//	        con.setAutoCommit(false); 
//
//	        psKM = con.prepareStatement(sqlKM);
//	        psKM.setString(1, km.getTenKM());
//	        psKM.setInt(2, km.getMucKM());
//	        psKM.setDate(3, tool.localDateSangSqlDate(km.getNgayBD()));
//	        psKM.setDate(4, tool.localDateSangSqlDate(km.getNgayKT()));
//	        psKM.setBoolean(5, km.isTrangThai());
//	        psKM.setString(6, km.getLoaiKM());
//	        psKM.setInt(7, km.getSoLuongMua());
//	        psKM.setInt(8, km.getSoLuongTang());
//	        psKM.setString(9, km.getMaKM());
//	        
//	        int rows = psKM.executeUpdate();
//
//	        if (rows > 0 && danhSachMaThuoc != null && !danhSachMaThuoc.isEmpty()) {
//	            psThuoc = con.prepareStatement(sqlThuoc);
//	            
//	            for (String maThuoc : danhSachMaThuoc) {
//	                psThuoc.setString(1, km.getMaKM()); 
//	                psThuoc.setString(2, maThuoc);      
//	                psThuoc.addBatch();
//	            }
//	            
//	            psThuoc.executeBatch(); 
//	        }
//
//	        con.commit();
//	        return true;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        try {
//	            if (con != null) con.rollback(); 
//	        } catch (SQLException ex) {
//	            ex.printStackTrace();
//	        }
//	        return false;
//	    } finally {
//	        try {
//	            if (psKM != null) psKM.close();
//	            if (psThuoc != null) psThuoc.close();
//	            if (con != null) {
//	                con.setAutoCommit(true);
//	                con.close();
//	            }
//	        } catch (SQLException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	}
	
	public boolean capNhatKhuyenMai(KhuyenMai km, List<String> danhSachMaThuoc) {
	    Connection con = null;
	    PreparedStatement psKM = null;
	    PreparedStatement psResetThuoc = null; // Statement để gỡ bỏ thuốc cũ
	    PreparedStatement psThuoc = null;
	    
	    // Câu lệnh Update thông tin khuyến mãi
	    String sqlKM = """
	            UPDATE KhuyenMai
	            SET tenKM = ?, mucKM = ?, ngayBD = ?, ngayKT = ?, 
	                trangThai = ?, loaiKM = ?, soLuongMua = ?, soLuongTang = ?
	            WHERE maKM = ?
	            """;
	            
	    // BƯỚC QUAN TRỌNG: Gỡ bỏ tất cả thuốc đang thuộc KM này trước
	    // Set maKM về NULL cho tất cả thuốc đang có maKM này
	    String sqlResetThuoc = "UPDATE Thuoc SET maKM = NULL WHERE maKM = ?";

	    // Câu lệnh gán lại thuốc mới vào KM
	    String sqlThuoc = "UPDATE Thuoc SET maKM = ? WHERE maThuoc = ?";

	    try {
	        con = KetNoiDatabase.getConnection();
	        con.setAutoCommit(false); // Bắt đầu Transaction

	        // 1. Cập nhật thông tin chung Khuyến Mãi
	        psKM = con.prepareStatement(sqlKM);
	        psKM.setString(1, km.getTenKM());
	        psKM.setInt(2, km.getMucKM());
	        psKM.setDate(3, tool.localDateSangSqlDate(km.getNgayBD()));
	        psKM.setDate(4, tool.localDateSangSqlDate(km.getNgayKT()));
	        psKM.setBoolean(5, km.isTrangThai());
	        psKM.setString(6, km.getLoaiKM());
	        psKM.setInt(7, km.getSoLuongMua());
	        psKM.setInt(8, km.getSoLuongTang());
	        psKM.setString(9, km.getMaKM());
	        
	        psKM.executeUpdate();

	        // 2. [FIX LỖI] Reset toàn bộ thuốc cũ của KM này về trạng thái chưa có KM
	        psResetThuoc = con.prepareStatement(sqlResetThuoc);
	        psResetThuoc.setString(1, km.getMaKM());
	        psResetThuoc.executeUpdate();

	        // 3. Cập nhật danh sách thuốc mới từ GUI vào KM
	        if (danhSachMaThuoc != null && !danhSachMaThuoc.isEmpty()) {
	            psThuoc = con.prepareStatement(sqlThuoc);
	            
	            for (String maThuoc : danhSachMaThuoc) {
	                psThuoc.setString(1, km.getMaKM()); // Gán maKM
	                psThuoc.setString(2, maThuoc);      // Cho thuốc này
	                psThuoc.addBatch();
	            }
	            
	            psThuoc.executeBatch(); 
	        }

	        con.commit(); // Xác nhận thành công
	        return true;

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (con != null) con.rollback(); // Hoàn tác nếu lỗi
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return false;
	    } finally {
	        try {
	            if (psKM != null) psKM.close();
	            if (psResetThuoc != null) psResetThuoc.close(); // Đóng psReset
	            if (psThuoc != null) psThuoc.close();
	            if (con != null) {
	                con.setAutoCommit(true);
	                con.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public ArrayList<KhuyenMai> layDanhSachKM() {
		ArrayList<KhuyenMai> list = new ArrayList<>();

		String query = "select *\r\n" + "from [dbo].[KhuyenMai]\r\n" + "where [trangThai] = 1\r\n"
				+ "order by TRY_CAST(REPLACE([maKM], 'TTKM', '') as int)";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet res = stmt.executeQuery(query);) {
			while (res.next()) {
				list.add(new KhuyenMai(res.getString("maKM"), res.getString("tenKM"), res.getString("loaiKM"),
						res.getInt("mucKM"), res.getInt("soLuongMua"), res.getInt("soLuongTang"),
						tool.sqlDateSangLocalDate(res.getDate("ngayBD")),
						tool.sqlDateSangLocalDate(res.getDate("ngayKT")), res.getBoolean("trangThai")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// lấy danh sách đã xoá
	public ArrayList<KhuyenMai> layDanhSachDaXoa() {
		ArrayList<KhuyenMai> listDaXoa = new ArrayList<>();
		String sql = "SELECT *\r\n" + "FROM KhuyenMai \r\n" + "WHERE [trangThai] = 0\r\n"
				+ "order by TRY_CAST(REPLACE([maKM], 'TTKM', '') as int)";
		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				KhuyenMai km = new KhuyenMai(rs.getString("maKM"), rs.getString("tenKM"), rs.getString("loaiKM"),
						rs.getInt("mucKM"), rs.getInt("soLuongMua"), rs.getInt("soLuongTang"),
						tool.sqlDateSangLocalDate(rs.getDate("ngayBD")),
						tool.sqlDateSangLocalDate(rs.getDate("ngayKT")), rs.getBoolean("trangThai"));
				listDaXoa.add(km);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listDaXoa;
	}

	// xoá khuyến mãi
	public boolean xoaKM(String maKM) {

		String sql = "update [dbo].[KhuyenMai]\r\n" + "set [trangThai] = 0\r\n" + "where [maKM] = ? ";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKM);
			int res = ps.executeUpdate();
			return res > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// Khôi phục khuyến mãi
	public boolean khoiPhucKM(String maKM) {
		String sql = "update [dbo].[KhuyenMai]\r\n" + "set [trangThai] = 1\r\n" + "where [maKM] = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKM);
			int res = ps.executeUpdate();
			return res > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean themThuocVaoChiTietKM(String maThuoc, String maKM) {
		String sql = "UPDATE Thuoc SET maKM = ? WHERE maThuoc = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKM);
			ps.setString(2, maThuoc);
			int check = ps.executeUpdate();
			return check > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// Lấy danh sách chi tiết
	public ArrayList<Object[]> layDanhSachChiTiet(String maKM) {
		ArrayList<Object[]> list = new ArrayList<>();
		String sql = "SELECT km.tenKM, km.loaiKM, km.ngayBD, km.ngayKT, t.maThuoc, t.tenThuoc\r\n"
				+ "FROM Thuoc t JOIN KhuyenMai km ON t.maKM = km.maKM\r\n" + "WHERE km.maKM like ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maKM);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				Object[] row = new Object[6];

				row[0] = res.getString("tenKM");
				row[1] = res.getString("loaiKM");
				row[2] = tool.sqlDateSangLocalDate(res.getDate("ngayBD"));
				row[3] = tool.sqlDateSangLocalDate(res.getDate("ngayKT"));
				row[4] = res.getString("maThuoc");
				row[5] = res.getString("tenThuoc");

				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	// Thêm khuyến mãi
	public boolean themKM(KhuyenMai km, ArrayList<Object[]> listChon) {
		String sql = "INSERT INTO KhuyenMai (maKM, tenKM, loaiKM, mucKM, soLuongMua, soLuongTang, ngayBD, ngayKT, trangThai)\r\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

		String sqlApDung = "UPDATE Thuoc SET maKM = ? WHERE maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, km.getMaKM());
			ps.setString(2, km.getTenKM());
			ps.setString(3, km.getLoaiKM());
			ps.setFloat(4, km.getMucKM());
			ps.setObject(5, km.getSoLuongMua(), java.sql.Types.INTEGER);
			ps.setObject(6, km.getSoLuongTang(), java.sql.Types.INTEGER);
			ps.setDate(7, tool.localDateSangSqlDate(km.getNgayBD()));
			ps.setDate(8, tool.localDateSangSqlDate(km.getNgayKT()));
			ps.setBoolean(9, km.isTrangThai());
			ps.executeUpdate();

			PreparedStatement psApDung = con.prepareStatement(sqlApDung);
			for (Object[] row : listChon) {
				String maThuoc = (String) row[0];
				psApDung.setString(1, km.getMaKM());
				psApDung.setString(2, maThuoc);
				psApDung.addBatch();
			}

			psApDung.executeBatch();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
