package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import connectDB.KetNoiDatabase;
import entity.*;

public class HoaDonDAO {
	Connection con = KetNoiDatabase.getConnection();
	public NhanVienDAO nvDAO = new NhanVienDAO();
	public KhachHangDAO khDAO = new KhachHangDAO();

	// ========== LẤY DANH SÁCH HÓA ĐƠN CÒN HIỆU LỰC ==========
	public List<HoaDon> layListHoaDon() {

		List<HoaDon> listHD = new ArrayList<>();
		String sql = """
				SELECT hd.maHD, hd.maKH, kh.tenKH, hd.maNV, nv.tenNV, hd.loaiTT,
				       hd.ngayLap, hd.diaChiHT, hd.tenHT, hd.ghiChu, hd.hotline, hd.tienNhan, hd.trangThai
				FROM HoaDon hd
				JOIN NhanVien nv ON hd.maNV = nv.maNV
				JOIN KhachHang kh ON hd.maKH = kh.maKH
				WHERE hd.trangThai = 1
				ORDER BY TRY_CAST(SUBSTRING(hd.maHD, 5, LEN(hd.maHD)) AS INT)
				""";
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				HoaDon hd = mapResultSetToHoaDon(rs);
				listHD.add(hd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listHD;
	}

	// ========== LẤY DANH SÁCH HÓA ĐƠN ĐÃ XÓA ==========
	public List<HoaDon> layListHDDaXoa() {
		List<HoaDon> listHDDaXoa = new ArrayList<>();
		String sql = """
				SELECT hd.maHD, hd.maKH, kh.tenKH, hd.maNV, nv.tenNV, hd.loaiTT,
				       hd.ngayLap, hd.diaChiHT, hd.tenHT, hd.ghiChu, hd.hotline, hd.tienNhan, hd.trangThai
				FROM HoaDon hd
				JOIN NhanVien nv ON hd.maNV = nv.maNV
				JOIN KhachHang kh ON hd.maKH = kh.maKH
				WHERE hd.trangThai = 0
				ORDER BY TRY_CAST(SUBSTRING(hd.maHD, 5, LEN(hd.maHD)) AS INT)
				""";
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				HoaDon hd = mapResultSetToHoaDon(rs);
				listHDDaXoa.add(hd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listHDDaXoa;
	}
	
	// ========== LẤY DOANH THU HOÁ ĐƠN THEO NGÀY ==========
	public Map<LocalDate, Double> layDoanhThuTheoNgay(LocalDate ngayBD, LocalDate ngayKT) {
	    Map<LocalDate, Double> map = new LinkedHashMap<>();
	    String sql = """
	            SELECT hd.ngayLap, SUM(cthd.soLuong * cthd.donGia) AS tongTien
	            FROM HoaDon hd
	            JOIN CT_HoaDon cthd ON hd.maHD = cthd.maHD
	            WHERE hd.ngayLap BETWEEN ? AND ? AND hd.trangThai = 1
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

	// ========== LẤY LIST KHÁCH HÀNG THỐNG KÊ ==========
	public List<KhachHang> layListKHThongKe(LocalDate ngayBD, LocalDate ngayKT) {
		List<KhachHang> list = new ArrayList<>();

		String query = """
				SELECT maKH, tenKH, tuoi, sdt
				FROM (
				    SELECT DISTINCT KH.maKH, KH.tenKH, KH.tuoi, KH.sdt,
				           TRY_CAST(REPLACE(KH.maKH, 'TTKH', '') AS INT) AS maSo
				    FROM KhachHang KH
				    JOIN HoaDon HD ON KH.maKH = HD.maKH
				    WHERE HD.ngayLap BETWEEN ? AND ? AND hd.trangThai = 1
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


	// ========= CHUYỂN ResultSet → HoaDon ==========
	public HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
		KhachHang kh = khDAO.timKhachHangTheoMa(rs.getString("maKH"));
		NhanVien nv = nvDAO.timNhanVienTheoMa(rs.getString("maNV"));

		return new HoaDon(rs.getString("maHD"), kh, nv, rs.getString("loaiTT"), rs.getDate("ngayLap").toLocalDate(),
				rs.getString("diaChiHT"), rs.getString("tenHT"), rs.getString("ghiChu"), rs.getString("hotline"),
				rs.getDouble("tienNhan"), rs.getBoolean("trangThai"));
	}

	// ========= TÌM HOÁ ĐƠN THEO MÃ =========
	public HoaDon timHoaDonTheoMa(String maHD) {
		for (HoaDon hd : layListHoaDon()) {
			if (hd.getMaHD().equalsIgnoreCase(maHD)) {
				return hd;
			}
		}
		for (HoaDon hd : layListHDDaXoa()) {
			if (hd.getMaHD().equalsIgnoreCase(maHD)) {
				return hd;
			}
		}
		return null;
	}

	// ========== THÊM HÓA ĐƠN MỚI ==========
	public boolean themHoaDon(HoaDon hd) {
		String sql = """
				INSERT INTO HoaDon (maHD, maKH, maNV, loaiTT, ngayLap, diaChiHT, tenHT, ghiChu, hotline, tienNhan, trangThai)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, hd.getMaHD());
			ps.setString(2, hd.getKhachHang().getMaKH());
			ps.setString(3, hd.getNhanVien().getMaNV());
			ps.setString(4, hd.getLoaiTT());
			ps.setDate(5, java.sql.Date.valueOf(hd.getNgayLap()));
			ps.setString(6, hd.getDiaChiHT());
			ps.setString(7, hd.getTenHT());
			ps.setString(8, hd.getGhiChu());
			ps.setString(9, hd.getHotline());
			ps.setDouble(10, hd.getTienNhan());
			ps.setBoolean(11, hd.isTrangThai());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========= THÊM CHI TIẾT HÓA ĐƠN =========
	public boolean themChiTietHoaDon(String maHD, String maThuoc, int soLuong, String maDVT, double donGia) {
		String sql = """
				INSERT INTO CT_HoaDon (maHD, maThuoc, soLuong, maDVT, donGia)
				VALUES (?, ?, ?, ?, ?)
				""";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			ps.setString(2, maThuoc);
			ps.setInt(3, soLuong);
			ps.setString(4, maDVT);
			ps.setDouble(5, donGia);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========= XÓA / KHÔI PHỤC =========
	public boolean xoaHD(String maHD) {
		String sql = "UPDATE HoaDon SET trangThai = 0 WHERE maHD = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========= KHÔI PHỤC HOÁ ĐƠN =========
	public boolean khoiPhucHD(String maHD) {
		String sql = "UPDATE HoaDon SET trangThai = 1 WHERE maHD = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, maHD);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// ========= LẤY TỔNG HOÁ ĐƠN CỦA KHÁCH HÀNG =========
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

	// ========= TÍNH TỔNG TIỀN CỦA CÁC HOÁ ĐƠN CỦA KHÁCH HÀNG =========
	public double layTongTien(String maKH) {
	    String sql = """
	            SELECT SUM(cthd.soLuong * cthd.donGia)
	            FROM HoaDon HD
	            JOIN CT_HoaDon cthd ON HD.maHD = cthd.maHD
	            WHERE HD.maKH = ? AND HD.trangThai = 1
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

	// ========= TÍNH TỔNG TIỀN THEO SẢN PHẨM =========
	public double layTongTienTheoSanPham(String maHD, String maSP) {
		String sql = """
				SELECT SUM(
						CASE
				         WHEN km.loaiKM = N'Giảm giá' THEN cthd.soLuong * cthd.donGia * (1 - km.mucKM / 100.0)
				         WHEN km.loaiKM = N'Mua tặng' THEN
				             (cthd.soLuong - FLOOR(cthd.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * cthd.donGia
				         ELSE
				             cthd.soLuong * cthd.donGia
						END)
				FROM CT_HoaDon cthd
				 JOIN Thuoc t ON cthd.maThuoc = t.maThuoc
				 LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				WHERE cthd.maHD = ? AND cthd.maThuoc = ?
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

	// ========= TÍNH TỔNG TIỀN THEO HÓA ĐƠN =========
	public double tinhTongTienTheoHoaDon(String maHD) {
	    String sql = """
	            SELECT SUM(soLuong * donGia) AS tongTien
	            FROM CT_HoaDon
	            WHERE maHD = ?
	            """;
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maHD);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getDouble("tongTien");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0.0;
	}

	// ========= LẤY CHI TIẾT HÓA ĐƠN (DÙNG CHO BẢNG THUỐC) =========
	public List<Object[]> layChiTietHoaDon(String maHD) {
	    List<Object[]> list = new ArrayList<>();
	    String sql = """
	            SELECT ct.maHD, t.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT, ct.donGia,
	                   (ct.soLuong * ct.donGia) AS thanhTien
	            FROM CT_HoaDon ct
	            JOIN Thuoc t ON ct.maThuoc = t.maThuoc
	            JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
	            WHERE ct.maHD = ?
	            """;
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, maHD);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            list.add(new Object[] { 
	                rs.getString("maHD"), 
	                rs.getString("maThuoc"), 
	                rs.getString("tenThuoc"),
	                rs.getInt("soLuong"), 
	                rs.getString("maDVT"), 
	                rs.getString("tenDVT"), 
	                rs.getDouble("donGia"), 
	                rs.getDouble("thanhTien") 
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	// ========= LẤY MÃ HOÁ ĐƠN MỚI NHẤT =========
	public String layMaHoaDonMoiNhat() {
		String sql = "SELECT TOP 1 maHD FROM HoaDon ORDER BY TRY_CAST(SUBSTRING(maHD, 5, LEN(maHD)) AS INT) DESC";
		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next())
				return rs.getString("maHD");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}