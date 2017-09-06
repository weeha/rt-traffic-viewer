package model;

import app.MainController;
import model.location.LocationReferencePointImpl;
import model.traffic.AnalysisElemImpl;
import model.traffic.FlowAnalysis;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
            // Traffic Information  0
            Row fLRP = sheet.createRow((short)0);
            Cell fLRPLabel = fLRP.createCell(0);
            fLRPLabel.setCellValue("FirstLRP");
            Cell fLRPValue = fLRP.createCell(1);
            fLRPValue.setCellValue(analysis.getFirstLRP().toString());

            Row iLRP = sheet.createRow((short)1);
            Cell iLRPLabel = iLRP.createCell(0);
            iLRPLabel.setCellValue("IntermediateLRPs");
            int iter = 1;
            for(LocationReferencePointImpl i : analysis.getIntermediates()){
                Cell iLRPValue = iLRP.createCell(iter);
                iLRPValue.setCellValue(analysis.getLastLRP().toString());
                iter++;
            }

            Row lLRP = sheet.createRow((short)2);
            Cell lLRPLabel = lLRP.createCell(0);
            lLRPLabel.setCellValue("LastLRP");
            Cell lLRPValue = lLRP.createCell(1);
            lLRPValue.setCellValue(analysis.getLastLRP().toString());

            Row date = sheet.createRow((short)3);
            Cell dateLabel = date.createCell(0);
            dateLabel.setCellValue("LastLRP");
            Cell dateValue = date.createCell(1);
            dateValue.setCellValue(analysis.getDateString());

            Row tTime = sheet.createRow((short)4);
            Cell tTimeLabel = tTime.createCell(0);
            tTimeLabel.setCellValue("Travel Time");
            iter = 1;
            for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                Cell tTimeValue = tTime.createCell(iter);
                tTimeValue.setCellValue(f.getTravelTime());
                iter++;
            }

            Row aSpeed = sheet.createRow((short)5);
            Cell aSpeedLabel = aSpeed.createCell(0);
            aSpeedLabel.setCellValue("Average Speed");
            iter = 1;
            for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                Cell aSpeedeValue = aSpeed.createCell(iter);
                aSpeedeValue.setCellValue(f.getAverageSpeed());
                iter++;
            }

            Row rSpeed = sheet.createRow((short)6);
            Cell rSpeedLabel = rSpeed.createCell(0);
            rSpeedLabel.setCellValue("Relative Speed");
            iter = 1;
            for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                Cell rSpeedValue = rSpeed.createCell(iter);
                rSpeedValue.setCellValue(f.getRelativeSpeed());
                iter++;
            }

            Row cond = sheet.createRow((short)7);
            Cell condLabel = cond.createCell(0);
            condLabel.setCellValue("Traffic Condition");
            iter = 1;
            for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                Cell condValue = cond.createCell(iter);
                condValue.setCellValue(f.getTrafficCondition());
                iter++;
            }

            Row time = sheet.createRow((short)8);
            Cell timeLabel = time.createCell(0);
            timeLabel.setCellValue("Time");
            iter = 1;
            for(FlowAnalysis f : (List<FlowAnalysis>)(Object)analysis.getTraffic()){
                Cell timeValue = time.createCell(iter);
                timeValue.setCellValue(df.format(f.getDate()));
                iter++;
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
