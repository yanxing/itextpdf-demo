package com.yanxing.itextdemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * itextpdf demo 实际创建pdf放在子线程中
 * Paragraph（段落）> Phrase(短句，这里相当于一个句话)可以包括多个> Chunk（短句，最小的单位，相当于一个词语）
 * @author lishuangxiang
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private static final int REQUEST_CODE = 1;
    private static final String PATH = Environment.getExternalStorageDirectory() + "/itexdemo.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 点击按钮创建PDF
     */
    public void generatePDF(View view) {
        if (PermissionUtil.findNeedRequestPermissions(this, new String[]{PermissionUtil.STORAGE, PermissionUtil.STORAGE1}).length > 0) {
            PermissionUtil.requestPermission(this, new String[]{PermissionUtil.STORAGE, PermissionUtil.STORAGE1}
                    , REQUEST_CODE);
        } else {
            createPdf(PATH);
        }
    }


    /**
     * 创建PDF文件，实际创建需要放在子线程中
     *
     * @param path
     */
    public void createPdf(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            boolean success = file.createNewFile();
            if (!success) {
                Log.d(TAG, "创建文件" + path + "失败");
                return;
            }
            Document document = new Document(PageSize.A4);
            document.addLanguage("zh");
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            //标题下划线，居中
            String title = "itextpdf-demo文档";
            BaseFont zh = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font chapterFont = new Font(zh, 28, Font.BOLD);
            Chunk chunk = new Chunk(title, chapterFont);
            chunk.setUnderline(1, -6);
            Paragraph paragraph = new Paragraph(chunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            Chapter chapter = new Chapter(paragraph, 1);
            chapter.setNumberDepth(0);
            document.add(chapter);
            document.add(Chunk.NEWLINE);

            Paragraph p = new Paragraph();
            p.add(getChunk("名称:"));
            p.add(getUnderlineChunk("李双祥"));
            p.add(getChunk("                                                                   名称:"));
            p.add(getUnderlineChunk("yanxing"));
            p.setSpacingAfter(10);
            document.add(p);

            Paragraph p1 = new Paragraph();
            p1.add(getChunk("名称:"));
            p1.add(getUnderlineChunk("李双祥"));
            p1.add(getChunk("                                                                   名称:"));
            p1.add(getUnderlineChunk("yanxing"));
            p1.setSpacingAfter(20);
            document.add(p1);


            Paragraph p2 = new Paragraph();
            p2.add(getChunk("1.项目"));
            p2.setSpacingAfter(10);
            document.add(p2);

            //表格
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            Font font = new Font(zh, 16, Font.BOLD);
            //第一行
            PdfPCell cell = new PdfPCell(new Phrase(new Chunk("内容", font)));
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new Chunk("要求", font)));
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new Chunk("结果/值", font)));
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            //第二行
            Font fontNormal = new Font(zh, 14, Font.NORMAL);
            cell = new PdfPCell(new Phrase(new Chunk("内容1",fontNormal)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new Chunk("内容1的要求1占位换行占位换行占位换行占位换行占位换行占位换行占位换行占位换行占位换行占位换行",fontNormal)));
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new Chunk("内容1的结论1",fontNormal)));
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new Chunk("内容1的要求2",fontNormal)));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(new Chunk("内容1的结论2",fontNormal)));
            cell.setPaddingTop(6);
            cell.setPaddingBottom(6);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            document.add(table);

            document.close();
            Toast.makeText(getApplicationContext(),path+getString(R.string.file_create_success),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 带下划线的Chunk
     * @param content
     * @return
     */
    public Chunk getUnderlineChunk(String content){
        Chunk chunk= new Chunk("\u00a0" + content.replace(' ', '\u00a0') + " ", getChineseFont());
        chunk.setUnderline(1, -3);
        return chunk;
    }

    /**
     * 不带下划线的Chunk
     * @param content
     * @return
     */
    public Chunk getChunk(String content){
        return new Chunk("\u00a0" + content.replace(' ', '\u00a0') + " ", getChineseFont());
    }

    /**
     * 中文字体、正常、大小16
     * @return
     */
    public Font getChineseFont(){
        BaseFont zh;
        try {
            //中文字体，itextpdf.jar指定检测中文的文件路径要和itext-asian中存放的文件路径一致
            zh = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return new Font(zh,16f,Font.NORMAL);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean success = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.permission_fail, Toast.LENGTH_LONG).show();
                    success = false;
                    break;
                }
            }
            if (success) {
                createPdf(PATH);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
