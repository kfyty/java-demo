package com.kfyty.demo.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.export.template.ExcelExportOfTemplateUtil;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.entity.MyXWPFDocument;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class OfficeUtil {
    public static void exportWord(String template, Map<String, Object> params, String savePath) throws Exception {
        InputStream templateInputStream = OfficeUtil.class.getResourceAsStream(template);
        XWPFDocument document = new MyXWPFDocument(templateInputStream);
        WordExportUtil.exportWord07(document, params);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(savePath));
        document.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        document.close();
    }

    public static void exportExcel(String template, Map<String, Object> params, String savePath) throws Exception {
        TemplateExportParams templateExportParams = new TemplateExportParams(template);
        templateExportParams.setScanAllsheet(true);
        templateExportParams.setColForEach(true);
        Workbook workbook = ExcelExportUtil.exportExcel(templateExportParams, params);
        FileOutputStream fileOutputStream = new FileOutputStream(new File(savePath));
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();
    }

    public static void exportCloneExcel(String template, Map<Integer, List<Map<String, Object>>> params, String savePath) throws Exception {
        TemplateExportParams templateExportParams = new TemplateExportParams(template);
        templateExportParams.setScanAllsheet(true);
        templateExportParams.setColForEach(true);
        Workbook workbook = new ExcelExportOfTemplateUtil().createExcelCloneByTemplate(templateExportParams, params);
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();
    }

    public Map<String,Object> createExcelParam(String name, Class<?> clazz, List<Object> data){
        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName(name);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("title", exportParams);
        modelMap.put("entity", clazz);
        modelMap.put("data", data);
        return modelMap;
    }

    public static <T> List<T> importExcel(File excel, Class<T> clazz, Integer headRow) {
        ImportParams params = new ImportParams();
        params.setHeadRows(headRow);
        return ExcelImportUtil.importExcel(excel, clazz, params);
    }

    public static void downloadZip(String name, List<Object> items, HttpServletResponse response) throws IOException, ArchiveException {
        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(name + ".zip", "UTF-8") + "\"");
        ArchiveOutputStream zipOut = new ArchiveStreamFactory().createArchiveOutputStream("zip", response.getOutputStream());
        for (Object item : items) {
            zipOut.putArchiveEntry(new ZipArchiveEntry(item.toString() + ".xls"));
            Workbook workbook = new HSSFWorkbook();
            workbook.write(zipOut);         // 写入到 zip
            zipOut.closeArchiveEntry();
        }
    }
}
