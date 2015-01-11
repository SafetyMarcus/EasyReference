package com.au.easyreference.app.Utils;

import android.app.Application;
import android.os.Environment;
import com.au.easyreference.app.R;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Marcus Hooper
 */
public class PDFGenerator
{
	public String generate(ReferenceList referenceList, Application app) throws FileNotFoundException, DocumentException
	{
		Document referenceDocument = new Document();

		String title = referenceList.title .length() > 0 ? referenceList.title : app.getString(R.string.no_title);

		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		path +=  "/PDF/" + title + ".pdf";

		File pdf = new File(path);
		pdf.getParentFile().delete();
		pdf.getParentFile().mkdirs();

		PdfWriter writer = null;
		FileOutputStream outputStream = new FileOutputStream(path);

		writer = PdfWriter.getInstance(referenceDocument, outputStream);

		writer.setLinearPageMode();
		referenceDocument.setMargins(16, 16, referenceDocument.topMargin(), 16);

		Rectangle rect = new Rectangle(referenceDocument.getPageSize());

		rect.setRight(596);
		rect.setTop(842);
		referenceDocument.setPageSize(rect);

		referenceDocument.open();

		PdfPTable referenceTable = new PdfPTable(1);
		referenceTable.addCell(new PdfPCell(new Paragraph(app.getText(R.string.references) + "")));

		for(ReferenceItem referenceItem : referenceList.referenceList)
		{
			if(referenceList.referenceType == ReferenceList.APA)
			{
				if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
					referenceTable.addCell(new PdfPCell(new Phrase(HelperFunctions.getAPABookReferenceString(referenceItem))));
				else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
					referenceTable.addCell(new PdfPCell(new Phrase(HelperFunctions.getAPAJournalReferenceString(referenceItem))));
				else if(referenceItem.type == ReferenceItem.BOOK_CHAPTER)
					referenceTable.addCell(new PdfPCell(new Phrase(HelperFunctions.getAPABookChapterReferenceString(referenceItem, app))));
				else if(referenceItem.type == ReferenceItem.WEB_PAGE)
					referenceTable.addCell(new PdfPCell(new Phrase(HelperFunctions.getAPAWebPageReferenceString(referenceItem))));
			}
		}

		referenceDocument.add(referenceTable);
		referenceDocument.close();

		return path;
	}
}
