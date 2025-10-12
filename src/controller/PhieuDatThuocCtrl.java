package controller;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PhieuDatThuocCtrl {
	
	@FXML Button btnLuuTep;
	@FXML TableView<String> tblPhieuDatThuoc; //Sửa lại thành đối tượng
	ToolCtrl thongBao = new ToolCtrl();
	
	@FXML public void luuTep() {
		Stage stage = (Stage) btnLuuTep.getScene().getWindow();
		
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Lưu phiếu đặt hàng");
		 fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
		 
		 File file = fileChooser.showSaveDialog(stage);
		 
		 if(file != null) {
			 try {
				Document document =  new Document();
				PdfWriter.getInstance(document, new FileOutputStream(file));
				BaseFont bf = BaseFont.createFont("/font/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				Font mainFont = new Font(bf, 18, Font.NORMAL);
				Font secondFont = new Font(bf, 15, Font.NORMAL);
				Font thirdFont = new Font(bf, 12, Font.NORMAL);
				document.open();
				
				document.add(new Paragraph("PHIẾU ĐẶT HÀNG",mainFont));
                document.add(new Paragraph("Hiệu thuốc: Tam Thanh", thirdFont));
                document.add(new Paragraph("Ngày đặt: 10/10/2025", thirdFont));
                document.add(new Paragraph("----------------------------------------------"));

                PdfPTable table = new PdfPTable(4);
                table.addCell(new PdfPCell(new Phrase("STT", secondFont)));
                table.addCell(new PdfPCell(new Phrase("Tên thuốc", secondFont)));
                table.addCell(new PdfPCell(new Phrase("Đơn vị tính", secondFont)));
                table.addCell(new PdfPCell(new Phrase("Số lượng", secondFont)));

                int index = 1;
                for (String item : tblPhieuDatThuoc.getItems()) {
                    table.addCell(String.valueOf(index++));
                    //table.addCell(item.getTenThuoc());
                    //table.addCell(item.getDonViTinh());
                    //table.addCell(String.valueOf(item.getSoLuong()));
                }
                
                document.add(table);
                document.add(new Paragraph("Người lập phiếu: ",secondFont));
                
                document.close();
                
                thongBao.hienThiThongBaoThanhCong("Lưu thành công");

			} catch (Exception e) {
				thongBao.hienThiThongBaoThatBai("Lưu thất bại" ,"Ai béc lỗi gì đâu", "101");
			}
		 }
	}
}
