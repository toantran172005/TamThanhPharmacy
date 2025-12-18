package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;

public class PhieuDatThuocDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();
	NhanVienDAO nvDAO = new NhanVienDAO();
	KhachHangDAO khDAO = new KhachHangDAO();
	ThuocDAO thuocDAO = new ThuocDAO();

	public ArrayList<Object[]> layListPhieuDatThuoc() {
		ArrayList<Object[]> listPDT = new ArrayList<>();

		String sql = """
				    SELECT pdh.maPDH, pdh.maKH, kh.tenKH, pdh.maNV, nv.tenNV,
				           pdh.ngayDat,pdh.ngayHen, pdh.ghiChu, pdh.trangThai, 
				           pdh.diaChiHT, pdh.hotline
				    FROM PhieuDatHang pdh
				    JOIN NhanVien nv ON pdh.maNV = nv.maNV
				    JOIN KhachHang kh ON pdh.maKH = kh.maKH
				    ORDER BY TRY_CAST(SUBSTRING(pdh.maPDH, 6, LEN(pdh.maPDH)) AS INT)
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Object[] pdt = { rs.getString("maPDH"), rs.getString("maKH"), rs.getString("tenKH"), rs.getString("maNV"),
						rs.getString("tenNV"), rs.getDate("ngayDat").toLocalDate(),
						rs.getDate("ngayHen").toLocalDate(), rs.getString("ghiChu"),
						rs.getString("trangThai"), rs.getString("diaChiHT"), rs.getString("hotline") };
				listPDT.add(pdt);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listPDT;
	}

	public boolean themPhieuDatThuoc(String maPDH, String maKH, String maNV, LocalDate ngayDat, LocalDate ngayHen, String ghiChu) {
		String sql = "INSERT INTO PhieuDatHang (maPDH, maKH, maNV, ngayDat, ngayHen, ghiChu, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, 'Chờ hàng')";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ps.setString(2, maKH);
			ps.setString(3, maNV);
			ps.setDate(4, java.sql.Date.valueOf(ngayDat));
			ps.setDate(5, java.sql.Date.valueOf(ngayHen));
			ps.setString(6, ghiChu);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Object[]> layDanhSachThuocTheoPDT(String maPDH) {
		ArrayList<Object[]> list = new ArrayList<>();
		String sql = """
				    SELECT ct.maPDH, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT
				    FROM CT_PhieuDatHang ct
				    JOIN Thuoc t ON ct.maThuoc = t.maThuoc
				    JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
				    WHERE ct.maPDH = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maPDH"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT") };
				list.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean themPhieuDatHangBangObject(Object[] pdh, String maNV) {
        String sql = "INSERT INTO PhieuDatHang (maPDH, maKH, maNV, ngayDat, ngayHen, diaChiHT, tenHT, ghiChu, hotline, trangThai) "
        			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = KetNoiDatabase.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

        	ps.setString(1, pdh[0].toString());
        	ps.setString(2, pdh[1].toString());
        	ps.setString(3, pdh[2].toString());
        	ps.setDate(4, java.sql.Date.valueOf(pdh[3].toString()));
        	ps.setDate(5, java.sql.Date.valueOf(pdh[4].toString()));
        	ps.setString(6, pdh[5].toString());
        	ps.setString(7, pdh[6].toString());
        	ps.setString(8, pdh[7] != null ? pdh[7].toString() : "");
        	ps.setString(9, pdh[8].toString());
        	ps.setString(10, pdh[9].toString());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	
	 public boolean themChiTietPhieuDatHangBangObject(Object[] ct) {
	        String sql = "INSERT INTO CT_PhieuDatHang (maPDH, maThuoc, soLuong, donGia, maDVT) VALUES (?, ?, ?, ?, ?)";
	        try (Connection con = KetNoiDatabase.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {

	            ps.setString(1, ct[0].toString());
	            ps.setString(2, ct[1].toString());
	            ps.setInt(3, Integer.parseInt(ct[2].toString()));
	            ps.setDouble(4, Double.parseDouble(ct[3].toString()));
	            ps.setString(5, ct[4].toString());
	            return ps.executeUpdate() > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	 
	 public String layMaPDHMoiNhat() {
	        String sql = "SELECT TOP 1 pdh.maPDH\r\n"
	        		+ "FROM PhieuDatHang pdh\r\n"
	        		+ "ORDER BY TRY_CAST(SUBSTRING(pdh.maPDH, 6, LEN(pdh.maPDH)) AS INT) DESC;";
	        try (Connection con = KetNoiDatabase.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                return rs.getString("maPDH");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
	    String sql = "UPDATE PhieuDatHang SET trangThai = ? WHERE maPDH = ?";
	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, trangThaiMoi);
	        ps.setString(2, maPhieu);
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
