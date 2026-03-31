package scanner.filehandling;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import scanner.fault.ActiveFault;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

    public class FaultExcelWriter {

        public void generateExcelFile(List<ActiveFault> faults, Path outputPath) throws IOException {
            Objects.requireNonNull(faults, "faults must not be null");
            Objects.requireNonNull(outputPath, "outputPath must not be null");

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Faults");

                CellStyle headerStyle = createHeaderStyle(workbook);

                int rowIndex = 0;

                // Header
                Row headerRow = sheet.createRow(rowIndex++);
                writeHeader(headerRow, headerStyle);


                // Data
                for (ActiveFault fault : faults) {
                    Row row = sheet.createRow(rowIndex++);
                    writeFaultRow(row, fault);
                }

                // Spaltenbreite automatisch anpassen
                for (int i = 0; i < 8; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
                    workbook.write(fos);
                }
            }
        }

        private void writeHeader(Row row, CellStyle headerStyle) {
           String [] headers = {
                    "Device IP",
                    "Object Type",
                    "Object Name",
                    "Object Description",
                    "Event State",
       //             "Event State",
       //             "Present Value",
       //                "Units"
            };


            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
        }

        private void writeFaultRow(Row row, ActiveFault fault) {
      //      row.createCell(0).setCellValue(safe(fault.getDeviceName()));
            row.createCell(0).setCellValue(safe(fault.getDeviceIp()));
            row.createCell(1).setCellValue(safe(fault.getObjectType()));
            row.createCell(2).setCellValue(safe(fault.getObjectName()));
            row.createCell(3).setCellValue(safe(fault.getObjectDescription()));
            row.createCell(4).setCellValue(safe(fault.getEventState()));
     //       row.createCell(6).setCellValue(safe(fault.getPresentValue()));
     //       row.createCell(7).setCellValue(safe(fault.getUnits()));
        }

        private CellStyle createHeaderStyle(Workbook workbook) {
            Font font = workbook.createFont();
            font.setBold(true);

            CellStyle style = workbook.createCellStyle();
            style.setFont(font);
            return style;
        }

        private String safe(String value) {
            return value == null ? "" : value;
        }
    }

