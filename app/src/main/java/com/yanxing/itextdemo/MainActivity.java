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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
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
     * 创建PDF文件
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
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            //标题下划线，居中
            String title = "This is the title";
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 30, Font.BOLDITALIC);
            Chunk chunk = new Chunk(title, chapterFont);
            chunk.setUnderline(2, -6);
            Paragraph paragraph = new Paragraph(chunk);
            paragraph.setAlignment(Element.ALIGN_CENTER);


            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
            Chapter chapter = new Chapter(paragraph, 1);
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph("This is the paragraph", paragraphFont));
            document.add(chapter);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean success = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "文件读取权限授权失败，将无法创建PDF文件", Toast.LENGTH_LONG).show();
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
