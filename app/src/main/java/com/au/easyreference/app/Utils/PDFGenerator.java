package com.au.easyreference.app.utils;

import android.os.Environment;
import com.au.easyreference.app.R;
import com.au.easyreference.app.references.ReferenceItem;
import com.au.easyreference.app.references.ReferenceList;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.FontSelector;
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

	private Document referenceDocument;

	public String generate(ReferenceList referenceList) throws FileNotFoundException, DocumentException
	{
		referenceDocument = new Document();
		setUpFonts();

		String title = referenceList.title .length() > 0 ? referenceList.title : ERApplication.getInstance().getString(R.string.no_title);

		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		path +=  "/PDF/" + title + ".pdf";

		File pdf = new File(path);
		pdf.getParentFile().delete();
		pdf.getParentFile().mkdirs();

		setUpDocument(referenceDocument, path);
		referenceDocument.add(getTitle());
		referenceDocument.add(Chunk.NEWLINE);
		addReferences(referenceList);
		referenceDocument.close();

		return path;
	}

	private void addReferences(ReferenceList referenceList) throws DocumentException
	{
		Collections.sort(referenceList.referenceList);
		for(ReferenceItem referenceItem : referenceList.referenceList)
		{
			String phrase = "";
			if(referenceList.referenceType == ReferenceList.APA)
				phrase = HelperFunctions.getReferenceString(referenceItem);

			referenceDocument.add(getReferenceCell(referenceItem, phrase));
		}
	}

	private void setUpDocument(Document referenceDocument, String path) throws FileNotFoundException, DocumentException
	{
		PdfWriter writer;
		FileOutputStream outputStream = new FileOutputStream(path);

		writer = PdfWriter.getInstance(referenceDocument, outputStream);
		writer.setLinearPageMode();
		referenceDocument.setMargins(32, 32, referenceDocument.topMargin(), 32);

		Rectangle rect = new Rectangle(referenceDocument.getPageSize());

		rect.setRight(596);
		rect.setTop(842);
		referenceDocument.setPageSize(rect);
		referenceDocument.open();
	}

	private Paragraph getTitle()
	{
		Paragraph title = new Paragraph(titleFont.process(ERApplication.getInstance().getString(R.string.references)));
		title.setAlignment(Element.ALIGN_CENTER);
		return title;
	}

	private Paragraph getReferenceCell(ReferenceItem referenceItem, String phrase)
	{
		Paragraph paragraph = new Paragraph();
		
		if(referenceItem.hasItalics() && (referenceItem.type == ReferenceItem.WEB_PAGE && !isWebPage(phrase)))
		{
			paragraph.add(paragraph.size(), mainFont.process(phrase.substring(0, referenceItem.italicsStart)));
			paragraph.add(paragraph.size(), mainFontItalics.process(phrase.substring(referenceItem.italicsStart, referenceItem.italicsEnd)));
			paragraph.add(paragraph.size(), mainFont.process(phrase.substring(referenceItem.italicsEnd)));
		}
		else
			paragraph.add(mainFont.process(phrase));

		paragraph.setFirstLineIndent(-30);
		paragraph.setIndentationLeft(30);
		return paragraph;
	}

	private boolean isWebPage(String phrase)
	{
		String[] results = phrase.split(".");

		return results.length == 0 || results.length == 1 || !results[results.length - 1].equalsIgnoreCase("pdf");
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
}
