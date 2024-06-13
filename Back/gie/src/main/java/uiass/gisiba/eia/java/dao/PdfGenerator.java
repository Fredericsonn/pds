package uiass.gisiba.eia.java.dao;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfGenerator {

    public static void main(String[] args) {

        // Create a document object
        Document document = new Document();

        try {
            // Initialize PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\Ayoub\\OneDrive\\Bureau\\pdf\\generated.pdf"));

            // Open the document
            document.open();

            // Add a paragraph
            document.add(new Paragraph("Hello, World! This is a PDF document generated using iText."));

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the document
            document.close();
        }

        System.out.println("PDF created successfully!");
    }

}
