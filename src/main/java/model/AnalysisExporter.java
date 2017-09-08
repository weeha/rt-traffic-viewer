package model;

import app.MainController;
import model.location.LocationReferencePointImpl;
import model.traffic.AnalysisElemImpl;
import model.traffic.FlowAnalysis;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Florian Noack on 05.09.2017.
 */
public class AnalysisExporter {

    private Workbook workbook = null;
    private final AnalysisElemImpl analysis;
    private DateFormat df = new SimpleDateFormat("HH:mm:ss");
    private DateFormat export = new SimpleDateFormat("yyy_MM__dd_HH_mm_ss");

    public AnalysisExporter(AnalysisElemImpl analysis){
        this.analysis = analysis;
    }

    public void createExcel(){
        if(analysis != null){
            workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Traffic Flow");
            createRows(sheet, analysis.getSituationSize()+1);
            int rowCounter = 0;
            // row = sheet.createRow((short)rowCounter);
            Row row = sheet.getRow(0);
            for(int i = 0; i < 10; i++){
                switch(i){
                    case 0:
                        row.createCell(i).setCellValue("FirstLRP");
                        sheet.getRow(1).createCell(i).setCellValue(analysis.getFirstLRP().toString());
                        break;
                    case 1:
                        row.createCell(i).setCellValue("IntermediateLRPs");
                        int iter = 1;
                        for(LocationReferencePointImpl l : analysis.getIntermediates()){
                            sheet.getRow(iter).createCell(i).setCellValue(analysis.getIntermediates().get(iter-1).toString());
                            iter++;
                        }
                        break;
                    case 2:
                        row.createCell(i).setCellValue("LastLRP");
                        sheet.getRow(1).createCell(i).setCellValue(analysis.getLastLRP().toString());
                        break;
                    case 3:
                        row.createCell(i).setCellValue("Date");
                        sheet.getRow(1).createCell(i).setCellValue(analysis.getDateString());
                        break;
                    case 4:
                        row.createCell(i).setCellValue("Travel Time");
                        iter = 1;
                        for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                            sheet.getRow(iter).createCell(i).setCellValue(f.getTravelTime());
                            iter++;
                        }
                        break;
                    case 5:
                        row.createCell(i).setCellValue("Average Speed");
                        iter = 1;
                        for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                            sheet.getRow(iter).createCell(i).setCellValue(f.getAverageSpeed());
                            iter++;
                        }
                        break;
                    case 6:
                        row.createCell(i).setCellValue("Relative Speed");
                        iter = 1;
                        for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                            sheet.getRow(iter).createCell(i).setCellValue(f.getRelativeSpeed());
                            iter++;
                        }
                        break;
                    case 7:
                        row.createCell(i).setCellValue("Traffic Condition");
                        iter = 1;
                        for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                            sheet.getRow(iter).createCell(i).setCellValue(f.getTrafficCondition());
                            iter++;
                        }
                        break;
                    case 8:
                        row.createCell(i).setCellValue("Confidence");
                        iter = 1;
                        for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                            sheet.getRow(iter).createCell(i).setCellValue(f.getConfidence());
                            iter++;
                        }
                        break;
                    case 9:
                        row.createCell(i).setCellValue("Time");
                        iter = 1;
                        for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                            sheet.getRow(iter).createCell(i).setCellValue(df.format(f.getDate()));
                            iter++;
                        }
                        break;
                }
            }
        }
    }

    private void createRows(Sheet s, int amount){
        if(s != null){
            for (int i = 0; i < amount; i++){
                s.createRow(i);
            }
        }
    }

    public void export(){
        try {
            if(workbook != null) {
                FileOutputStream fileOut = new FileOutputStream(MainController.EXCEL_DIR + "Flow_Analysis" + export.format(new Date())+ ".xls");
                workbook.write(fileOut);
                fileOut.close();
            }
        }catch (IOException ie){
            System.out.println("Something went wrong while exporting to Excel!");
        }

    }

}
