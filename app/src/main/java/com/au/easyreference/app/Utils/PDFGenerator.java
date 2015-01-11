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
import java.util.ArrayList;
import java.util.Collections;

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

		PdfWriter writer;
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

		PdfPCell titleCell = new PdfPCell(new Paragraph(app.getText(R.string.references).toString()));
		disableBorders(titleCell);
		titleCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		titleCell.setPadding(80);
		referenceTable.addCell(titleCell);

		ArrayList<String> references = new ArrayList<>(referenceList.referenceList.size());
		for(ReferenceItem referenceItem : referenceList.referenceList)
		{
			String phrase = "";
			if(referenceList.referenceType == ReferenceList.APA)
			{
				if(referenceItem.type == ReferenceItem.BOOK_REFERENCE)
					phrase = HelperFunctions.getAPABookReferenceString(referenceItem);
				else if(referenceItem.type == ReferenceItem.JOURNAL_REFERENCE)
					phrase = HelperFunctions.getAPAJournalReferenceString(referenceItem);
				else if(referenceItem.type == ReferenceItem.BOOK_CHAPTER)
					phrase = HelperFunctions.getAPABookChapterReferenceString(referenceItem, app);
				else if(referenceItem.type == ReferenceItem.WEB_PAGE)
					phrase = HelperFunctions.getAPAWebPageReferenceString(referenceItem);
			}


			references.add(phrase);
		}

		Collections.sort(references);
		int count = 0;
		for(String reference : references)
		{
			PdfPCell cell = new PdfPCell(new Phrase(++count + ". " + reference));
			disableBorders(cell);
			referenceTable.addCell(cell);
		}

		referenceDocument.add(referenceTable);
		referenceDocument.close();

		return path;
	}

	public void disableBorders(PdfPCell cell)
	{
		cell.disableBorderSide(PdfPCell.TOP);
		cell.disableBorderSide(PdfPCell.LEFT);
		cell.disableBorderSide(PdfPCell.RIGHT);
		cell.disableBorderSide(PdfPCell.BOTTOM);
	}
}
