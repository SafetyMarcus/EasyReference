package com.au.easyreference.app.utils;

import android.os.Environment;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import harmony.java.awt.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;

/**
 * @author Marcus Hooper
 */
public class PDFGenerator
{
	private FontSelector titleFont;
	private FontSelector mainFont;
	private FontSelector mainFontItalics;

	public String generate(ReferenceList referenceList) throws FileNotFoundException, DocumentException
	{
		Document referenceDocument = new Document();
		setUpFonts();

		String title = referenceList.title .length() > 0 ? referenceList.title : ERApplication.getInstance().getString(R.string.no_title);

		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		path +=  "/PDF/" + title + ".pdf";

		File pdf = new File(path);
		pdf.getParentFile().delete();
		pdf.getParentFile().mkdirs();

		setUpDocument(referenceDocument, path);
		referenceDocument.add(getReferenceTable(referenceList));
		referenceDocument.close();

		return path;
	}

	private PdfPTable getReferenceTable(ReferenceList referenceList) throws DocumentException
	{
		PdfPTable referenceTable = new PdfPTable(2);
		referenceTable.setWidths(new float[]{10f, 90f});
		referenceTable.addCell(getTitleCell());

		int count = 0;
		Collections.sort(referenceList.referenceList);
		for(ReferenceItem referenceItem : referenceList.referenceList)
		{
			count++;
			String phrase = "";
			if(referenceList.referenceType == ReferenceList.APA)
				phrase = HelperFunctions.getReferenceString(referenceItem);

			referenceTable.addCell(getNumberCell(count));
			referenceTable.addCell(getReferenceCell(referenceItem, phrase));
		}
		return referenceTable;
	}

	private void setUpDocument(Document referenceDocument, String path) throws FileNotFoundException, DocumentException
	{
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
	}

	private PdfPCell getTitleCell()
	{
		PdfPCell titleCell = new PdfPCell(new Paragraph(titleFont.process(ERApplication.getInstance().getText(R.string.references).toString())));
		titleCell.setColspan(2);
		disableBorders(titleCell);
		titleCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		titleCell.setPadding(64);
		return titleCell;
	}

	private PdfPCell getNumberCell(int count)
	{
		PdfPCell numberCell = new PdfPCell(new Phrase(mainFont.process(count + ".")));
		numberCell.setColspan(1);
		numberCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		disableBorders(numberCell);
		return numberCell;
	}

	private PdfPCell getReferenceCell(ReferenceItem referenceItem, String phrase)
	{
		Phrase start = new Phrase(mainFont.process(phrase.substring(0, referenceItem.italicsStart)));
		Phrase italics = new Phrase(mainFontItalics.process(phrase.substring(referenceItem.italicsStart, referenceItem.italicsEnd)));
		Phrase end = new Phrase(mainFont.process(phrase.substring(referenceItem.italicsEnd)));

		Phrase fullReference = new Phrase();
		fullReference.add(start);
		fullReference.add(italics);
		fullReference.add(end);

		PdfPCell textCell = new PdfPCell(fullReference);
		textCell.setColspan(1);
		textCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		disableBorders(textCell);
		return textCell;
	}

	private void setUpFonts()
	{
		titleFont = new FontSelector();
		titleFont.addFont(new Font(Font.TIMES_ROMAN, 16, Font.BOLD, Color.BLACK));

		mainFont = new FontSelector();
		mainFont.addFont(new Font(Font.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK));

		mainFontItalics = new FontSelector();
		mainFontItalics.addFont(new Font(Font.TIMES_ROMAN, 12, Font.ITALIC, Color.BLACK));
	}

	private void disableBorders(PdfPCell cell)
	{
		cell.disableBorderSide(PdfPCell.TOP);
		cell.disableBorderSide(PdfPCell.LEFT);
		cell.disableBorderSide(PdfPCell.RIGHT);
		cell.disableBorderSide(PdfPCell.BOTTOM);
	}
}
