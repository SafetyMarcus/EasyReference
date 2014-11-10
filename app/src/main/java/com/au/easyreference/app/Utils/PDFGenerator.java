package com.au.easyreference.app.Utils;

import android.os.Environment;
import com.au.easyreference.app.References.ReferenceItem;
import com.au.easyreference.app.References.ReferenceList;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Marcus Hooper
 */
public class PDFGenerator
{
	public String generate(ReferenceList referenceList) throws FileNotFoundException, DocumentException
	{
		Document referenceDocument = new Document();
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
				+ "/temp/" + referenceList.id + ".pdf";

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

		for(ReferenceItem referenceItem : referenceList.referenceList)
		{
			if(referenceList.referenceType == ReferenceList.APA)
			{
				if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
					referenceTable.addCell(new PdfPCell(new Phrase(HelperFunctions.getAPABookReferenceString(referenceItem))));
				else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
					referenceTable.addCell(new PdfPCell(new Phrase(HelperFunctions.getAPAJournalReferenceString(referenceItem))));
			}
		}

		referenceDocument.add(referenceTable);
		referenceDocument.close();

		return path;
	}
}
