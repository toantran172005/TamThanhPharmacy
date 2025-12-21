	package dao;
	
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.time.LocalDate;
	import java.util.ArrayList;
	
	import javax.swing.JOptionPane;
	
	import controller.ToolCtrl;
	import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
	import entity.PhieuDoiTra;
	import connectDB.KetNoiDatabase;
	
	public class PhieuDoiTraDAO {
		public ToolCtrl toolCtrl = new ToolCtrl();
		NhanVienDAO nvDAO = new NhanVienDAO();
		KhachHangDAO khDAO = new KhachHangDAO();
		ThuocDAO thuocDAO = new ThuocDAO();
		HoaDonDAO hdDAO = new HoaDonDAO();
		Connection con = KetNoiDatabase.getConnection();
		
	
//		public ArrayList<PhieuDoiTra> layListPDT() {
//		    ArrayList<PhieuDoiTra> listPDT = new ArrayList<>();
//		    
//		    String sql = """
//		        SELECT pdt.maPhieuDT, pdt.maHD, pdt.maNV, pdt.ngayDoiTra, pdt.lyDo
//		        FROM PhieuDoiTra pdt
//		        ORDER BY CAST(SUBSTRING(pdt.maPhieuDT, 6, LEN(pdt.maPhieuDT)) AS INT)
//		        """;
//	
//		    try (PreparedStatement pstmt = con.prepareStatement(sql);
//		         ResultSet rs = pstmt.executeQuery()) {
//	
//		        while (rs.next()) {
//		            String maPhieuDT = rs.getString("maPhieuDT");
//		            String maHD = rs.getString("maHD");
//		            String maNV = rs.getString("maNV");
//		            String lyDo = rs.getString("lyDo");
//		            
//		            HoaDon hd = hdDAO.timHoaDonTheoMa(maHD);
//		            NhanVien nv = nvDAO.timNhanVienTheoMa(maNV);
//	
//		            // Xử lý ngày an toàn (có thể null)
//		            java.sql.Date sqlDate = rs.getDate("ngayDoiTra");
//		            LocalDate ngayDoiTra = (sqlDate != null) ? sqlDate.toLocalDate() : null;
//	
//		            // Tạo đối tượng đúng cách
//		            PhieuDoiTra pdt = new PhieuDoiTra(maPhieuDT, hd, nv, ngayDoiTra, lyDo);
//		            listPDT.add(pdt);
//		        }
//	
//		    } catch (SQLException e) {
//		        e.printStackTrace();
//		        JOptionPane.showMessageDialog(null, 
//		            "Lỗi tải danh sách phiếu đổi trả: " + e.getMessage(),
//		            "Lỗi Database", JOptionPane.ERROR_MESSAGE);
//		    }
//	
//		    return listPDT;
//		}
		
		public ArrayList<PhieuDoiTra> layListPDT() {
	        ArrayList<PhieuDoiTra> listPDT = new ArrayList<>();
	        
	        // Join bảng PhieuDoiTra -> NhanVien -> HoaDon -> KhachHang
	        String sql = """
	            SELECT pdt.maPhieuDT, pdt.ngayDoiTra, pdt.lyDo, 
	                   nv.maNV, nv.tenNV, 
	                   hd.maHD, 
	                   kh.maKH, kh.tenKH
	            FROM PhieuDoiTra pdt
	            JOIN NhanVien nv ON pdt.maNV = nv.maNV
	            JOIN HoaDon hd ON pdt.maHD = hd.maHD
	            JOIN KhachHang kh ON hd.maKH = kh.maKH
	            ORDER BY CAST(SUBSTRING(pdt.maPhieuDT, 6, LEN(pdt.maPhieuDT)) AS INT) DESC
	        """;

	        try (PreparedStatement pstmt = con.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {

	            while (rs.next()) {
	                // 1. Tạo đối tượng Nhân viên (Chỉ cần Mã và Tên để hiển thị)
	                NhanVien nv = new NhanVien(rs.getString("maNV"));
	                nv.setTenNV(rs.getString("tenNV"));

	                // 2. Tạo đối tượng Khách hàng
	                KhachHang kh = new KhachHang(rs.getString("maKH"));
	                kh.setTenKH(rs.getString("tenKH"));

	                // 3. Tạo đối tượng Hóa đơn gắn Khách hàng vào
	                HoaDon hd = new HoaDon(rs.getString("maHD"));
	                hd.setKhachHang(kh);

	                // 4. Tạo Phiếu đổi trả
	                String maPhieuDT = rs.getString("maPhieuDT");
	                String lyDo = rs.getString("lyDo");
	                java.sql.Date sqlDate = rs.getDate("ngayDoiTra");
	                LocalDate ngayDoiTra = (sqlDate != null) ? sqlDate.toLocalDate() : null;

	                PhieuDoiTra pdt = new PhieuDoiTra(maPhieuDT, hd, nv, ngayDoiTra, lyDo);
	                listPDT.add(pdt);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return listPDT;
	    }
	
		public boolean themPDT(PhieuDoiTra pdt, String maNV, String maHD) {
			String sql = "INSERT INTO PhieuDoiTra (maPhieuDT, maHD, maNV, ngayDoiTra, lyDo) " + "VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
	
				ps.setString(1, pdt.getMaPhieuDT());
				ps.setString(2, maHD);
				ps.setString(3, maNV);
				ps.setDate(4, java.sql.Date.valueOf(pdt.getNgayDoiTra()));
				ps.setString(5, pdt.getLyDo());
	
				return ps.executeUpdate() > 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	
		public boolean themChiTietPDTBangObject(Object[] ct) {
			String sql = "INSERT INTO CT_PhieuDoiTra (maPhieuDT, maThuoc, soLuong, ghiChu, tienHoan, mucHoan, maDVT) VALUES (?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
	
				ps.setString(1, ct[0].toString());
				ps.setString(2, ct[1].toString());
				ps.setInt(3, Integer.parseInt(ct[2].toString()));
				ps.setString(4, ct[3].toString());
				ps.setDouble(5, Double.parseDouble(ct[4].toString()));
				ps.setDouble(6, Double.parseDouble(ct[5].toString()));
				ps.setString(7, ct[6].toString());
				return ps.executeUpdate() > 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	
		public String layMaPDTMoiNhat() {
			String sql = "SELECT TOP 1 pdt.maPhieuDT\r\n" + "FROM PhieuDoiTra pdt\r\n"
					+ "ORDER BY TRY_CAST(SUBSTRING(pdt.maPhieuDT, 6, LEN(pdt.maPhieuDT)) AS INT) DESC;";
			try (PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
	
				if (rs.next()) {
					return rs.getString("maPhieuDT");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	
		public ArrayList<Object[]> layDanhSachThuocTheoPhieuDT(String maPhieuDT) {
			ArrayList<Object[]> list = new ArrayList<>();
			String sql = """
					     SELECT ct.maPhieuDT, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT,  ct.mucHoan, ct.tienHoan, ct.ghiChu
						FROM CT_PhieuDoiTra ct
						JOIN Thuoc t ON ct.maThuoc = t.maThuoc
						JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
						LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
						WHERE maPhieuDT = ?
					""";
	
			try (PreparedStatement ps = con.prepareStatement(sql)) {
	
				ps.setString(1, maPhieuDT);
				ResultSet rs = ps.executeQuery();
	
				while (rs.next()) {
					Object[] row = { rs.getString("maPhieuDT"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
							rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT"), rs.getDouble("mucHoan"),
							rs.getDouble("tienHoan"), rs.getString("ghiChu") };
					list.add(row);
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
	
		public double tinhTongTienHoanTheoPhieuDT(String maPhieuDT) {
			String sql = """
							SELECT ct.maPhieuDT, SUM(ct.tienHoan) AS tongTienHoan
							FROM CT_PhieuDoiTra ct
							WHERE ct.maPhieuDT = ?
							GROUP BY ct.maPhieuDT;
										""";
	
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setString(1, maPhieuDT);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getDouble("tongTienHoan");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
		
	    // ================= TÌM HOÁ ĐƠN THEO MÃ =================
		public PhieuDoiTra timPhieuDoiTraTheoMa(String maPDT) {
	         String sql = """
	            SELECT pdt.maPhieuDT, pdt.ngayDoiTra, pdt.lyDo, 
	                   nv.maNV, nv.tenNV, 
	                   hd.maHD, 
	                   kh.maKH, kh.tenKH
	            FROM PhieuDoiTra pdt
	            JOIN NhanVien nv ON pdt.maNV = nv.maNV
	            JOIN HoaDon hd ON pdt.maHD = hd.maHD
	            JOIN KhachHang kh ON hd.maKH = kh.maKH
	            WHERE pdt.maPhieuDT = ?
	        """;
	         try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	             pstmt.setString(1, maPDT);
	             ResultSet rs = pstmt.executeQuery();
	             if(rs.next()) {
	                 NhanVien nv = new NhanVien(rs.getString("maNV"));
	                 nv.setTenNV(rs.getString("tenNV"));
	                 KhachHang kh = new KhachHang(rs.getString("maKH"));
	                 kh.setTenKH(rs.getString("tenKH"));
	                 HoaDon hd = new HoaDon(rs.getString("maHD"));
	                 hd.setKhachHang(kh);
	                 
	                 java.sql.Date sqlDate = rs.getDate("ngayDoiTra");
	                 LocalDate ngayDoiTra = (sqlDate != null) ? sqlDate.toLocalDate() : null;
	                 
	                 return new PhieuDoiTra(rs.getString("maPhieuDT"), hd, nv, ngayDoiTra, rs.getString("lyDo"));
	             }
	         } catch (Exception e) {
	            e.printStackTrace();
	        }
	         return null;
	    }
	
	}
