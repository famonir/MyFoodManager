package model.Exporter;

import java.io.*;
import java.util.List;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.*;
import controller.ExceptionControllers.ExceptionAlertController;
import model.FundamentalClasses.Product;
import model.FundamentalClasses.ShoppingList;

/**
 * create pdf class
 */
public class PDFCreate {
    private static final int NUMCOLUMNS = 3;
    private static final float FONTSIZE = 20f;
    private static final float LINESPACING = 0f;
    private final ShoppingList shoppingList;
    private final File file;

    /** Constructor of the pdf creator
     * @param sList list to be included in the pdf
     * @param f target file
     */
    public PDFCreate(ShoppingList sList, File f){shoppingList=sList; file = f;}

    /**
     * method used to fill the PDF
     * @throws IOException thrown whenever the file cannot be opened
     * @throws DocumentException thrown whenever the document cannot be used
     */
    public void setUpPdf() throws IOException, DocumentException {
        try (OutputStream outputStream = new FileOutputStream((file))) {

            List<Product> products = shoppingList.getProducts();
            String date = shoppingList.getDate();
            String name = shoppingList.getName();
            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(NUMCOLUMNS);
            /* Adding text to the document */
            String text1 = name + "  " + date;

            /* Setting color to the text */
            float fntSize, lineSpacing;
            fntSize = FONTSIZE;
            lineSpacing = LINESPACING;
            Paragraph p = new Paragraph(new Phrase(lineSpacing, text1,
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, fntSize)));
            p.setAlignment(Element.ALIGN_CENTER);

            /*Setting font to the text
             Creating a paragraph 1 */

            document.add(p);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            products.sort((o1, o2) -> o1.getCategory().compareToIgnoreCase(o2.getCategory()));

            for (model.FundamentalClasses.Product product : products) {
                PdfPCell pdfPCell1 = new PdfPCell(new Paragraph(product.getName()));
                table.addCell(pdfPCell1);
                PdfPCell pdfPCell2 = new PdfPCell(new Paragraph(product.getQuantity() + " " + product.getUnits()));
                table.addCell(pdfPCell2);
                PdfPCell pdfPCell3 = new PdfPCell(new Paragraph(product.getCategory()));
                table.addCell(pdfPCell3);
            }

            document.add(table);
            document.close();
        } catch (IOException | DocumentException throwables) {
            new ExceptionAlertController().showExceptionWindow(throwables);
        }
    }

    /**
     * Method to get file
     * @return File
     */
    public File getFile() {
        return file;
    }
}
