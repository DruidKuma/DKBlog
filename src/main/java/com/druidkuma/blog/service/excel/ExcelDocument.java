package com.druidkuma.blog.service.excel;

import com.druidkuma.blog.util.Beans;
import com.druidkuma.blog.util.Strings;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.Map.Entry;

/**
 * Utility class for reading and writing excel files.
 * <p>
 * The style of the cells can be set using CSS notation. So far the following
 * attributes are supported: <p/>
 * <p>
 * <pre>
 * text-align      : left|right|center|fill|justify;
 * vertical-align  : top|center|bottom|justify;
 * font-family     : Arial|Courier|...
 * font-size       : n;   (in pt)
 * font-weight     : bold;
 * font-style      : italic;
 * text-decoration : underline;
 * text-indent     : n;   (in number of spaces)
 * text-rotation   : n;   (in degrees)
 * color           : #rrggbb
 *
 * data-format     : dd.mm.yyyy | #,##0.00 | ...
 * width           : n;   (in pixels, will affect the whole column for autosize use width:auto)
 * height          : n;   (in pixels, will affect the whole row)
 * colspan         : n;
 * rowspan         : n;
 * readonly        : true|false;
 * display         : normal|hidden;
 * word-wrap       : wrap|nowrap;
 * border          : none|thin|medium|thick|dashed|dotted|...
 * border-top      : (same as border attribute)
 * border-left     : (same as border attribute)
 * border-right    : (same as border attribute)
 * border-bottom   : (same as border attribute)
 *
 * background-color    : #rrggbb
 * border-color        : #rrggbb
 * border-top-color    : #rrggbb
 * border-left-color   : #rrggbb
 * border-right-color  : #rrggbb
 * border-bottom-color : #rrggbb
 * pattern             : none|solid|squares|...
 * </pre>
 *
 * @author Andreas Kühnel
 */
public class ExcelDocument {

    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final Map<String, Short> TEXT_ALIGN_CONSTANTS
            = Beans.toMap("default", CellStyle.ALIGN_GENERAL,
            "left", CellStyle.ALIGN_LEFT,
            "center", CellStyle.ALIGN_CENTER,
            "right", CellStyle.ALIGN_RIGHT,
            "fill", CellStyle.ALIGN_FILL,
            "justify", CellStyle.ALIGN_JUSTIFY);
    private static final Map<String, Short> VERTICAL_ALIGN_CONSTANTS
            = Beans.toMap("top", CellStyle.VERTICAL_TOP,
            "center", CellStyle.VERTICAL_CENTER,
            "bottom", CellStyle.VERTICAL_BOTTOM,
            "justify", CellStyle.VERTICAL_JUSTIFY);
    private static final Map<String, Short> BORDER_CONSTANTS
            = Beans.toMap("none", CellStyle.BORDER_NONE,
            "thin", CellStyle.BORDER_THIN,
            "medium", CellStyle.BORDER_MEDIUM,
            "dashed", CellStyle.BORDER_DASHED,
            "hair", CellStyle.BORDER_HAIR,
            "thick", CellStyle.BORDER_THICK,
            "double", CellStyle.BORDER_DOUBLE,
            "dotted", CellStyle.BORDER_DOTTED,
            "medium dashed", CellStyle.BORDER_MEDIUM_DASHED,
            "medium dash dot", CellStyle.BORDER_MEDIUM_DASH_DOT,
            "dash dot dot", CellStyle.BORDER_DASH_DOT_DOT,
            "medium dash dot dot", CellStyle.BORDER_MEDIUM_DASH_DOT_DOT,
            "slanted dash dot", CellStyle.BORDER_SLANTED_DASH_DOT);
    private static final Map<String, Short> PATTERN_CONSTANTS
            = Beans.toMap("none", CellStyle.NO_FILL,
            "solid", CellStyle.SOLID_FOREGROUND,
            "fine dots", CellStyle.FINE_DOTS,
            "alt bars", CellStyle.ALT_BARS,
            "sparse dots", CellStyle.SPARSE_DOTS,
            "thick horiz bands", CellStyle.THICK_HORZ_BANDS,
            "thick vert band", CellStyle.THICK_VERT_BANDS,
            "thick backward diag", CellStyle.THICK_BACKWARD_DIAG,
            "thick forward diag", CellStyle.THICK_FORWARD_DIAG,
            "big spots", CellStyle.BIG_SPOTS,
            "bicks", CellStyle.BRICKS,
            "thin horiz bands", CellStyle.THIN_HORZ_BANDS,
            "thin vert bands", CellStyle.THIN_VERT_BANDS,
            "thin backwad diag", CellStyle.THIN_BACKWARD_DIAG,
            "thin forward diag", CellStyle.THIN_FORWARD_DIAG,
            "squares", CellStyle.SQUARES,
            "diamonds", CellStyle.DIAMONDS);
    private static final int PIXEL_HEIGHT_FACTOR = 15;
    private static final int PIXEL_WIDTH_FACTOR = 37;
    private static final Logger LOG = Logger.getLogger(ExcelDocument.class);
    private Workbook workbook;
    private Sheet sheet;
    private int currentRow = 1;
    private int currentColumn = 1;
    private String defaultCellStyle = "font-family:Arial; font-size:10; border:thin; border-color:#366092";
    private String defaultHeaderStyle = "font-weight:bold; readonly:true; background-color:#b8cce4";
    private Map<String, CellStyle> styles = new HashMap<>();
    private Map<String, Map<String, String>> styleAttributes = new HashMap<>();
    private Map<Sheet, Set<Integer>> autosizeColumns = new HashMap<>();


    /**
     * Creates an empty excel document
     */
    public ExcelDocument() {
        workbook = new XSSFWorkbook();
    }


    /**
     * Creates a new excel document with the given sheet name
     */
    public ExcelDocument(String sheetName) {
        this();
        addSheet(sheetName);
    }


    private ExcelDocument(Workbook workbook) {
        this.workbook = workbook;
    }


    // ------------------------------------------------------------------------
    // Excel reading methods
    // ------------------------------------------------------------------------

    /**
     * Loads the given excel file
     */
    public static ExcelDocument load(File file) {

        try (InputStream in = new FileInputStream(file)) {
            ExcelDocument doc = new ExcelDocument((Workbook) null);
            boolean isXlsx = file.getName().toLowerCase().endsWith(".xlsx");
            doc.workbook = isXlsx ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
            doc.sheet = doc.workbook.getSheetAt(0);
            return doc;
        } catch (Throwable e) {
            throw new RuntimeException("loading " + file, e);
        }
    }

    @SneakyThrows
    public static ExcelDocument load(InputStream in, String fileName) {
        ExcelDocument doc = new ExcelDocument((Workbook) null);
        boolean isXlsx = fileName.toLowerCase().endsWith(".xlsx");
        doc.workbook = isXlsx ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
        doc.sheet = doc.workbook.getSheetAt(0);
        return doc;
    }

    /**
     * Retrieves the excel name of a cell (1,1) is A1
     */
    public static String getCellName(int row, int column) {
        String firstChar = column <= 26 ? "" : "" + (char) ('@' + ((column - 1) / 26));
        String secondChar = "" + (char) ('A' + ((column - 1) % 26));
        return firstChar + secondChar + row;
    }

    /**
     * Opens the sheet at the given index (starting with 1)
     */
    public void selectSheet(int index) {
        sheet = workbook.getSheetAt(index - 1);
    }

    /**
     * Retrieves the number of sheets in the workbook
     */
    public int getSheetCount() {
        return workbook.getNumberOfSheets();
    }

    /**
     * Retrieves the name of the current sheet
     */
    public String getSheetName() {
        return sheet.getSheetName();
    }

    /**
     * Reads a string value at the given row and column (indexes starting at 1)
     */
    public String readString(int row, int column) {

        Cell cell = getCell(row, column);
        if (cell == null) {
            return null;
        }

        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC:
                // make an exact string representation
                DecimalFormat format = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.ENGLISH));
                format.setMaximumFractionDigits(30);
                return format.format(cell.getNumericCellValue());

            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_ERROR:
                return null;
            default:
                return cell.getRichStringCellValue().getString();
        }
    }

    /**
     * Reads an integer value at the given row and column (indexes starting at 1)
     */
    public Integer readInteger(int row, int column) {

        Cell cell = getCell(row, column);
        if (cell == null) {
            return null;
        }

        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:
                return Strings.parseInteger(cell.getRichStringCellValue().getString());
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? 1 : 0;
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_ERROR:
                return null;
            default:
                return (int) cell.getNumericCellValue();
        }
    }

    /**
     * Reads a double value at the given row and column (indexes starting at 1)
     */
    public Double readDouble(int row, int column) {

        Cell cell = getCell(row, column);
        if (cell == null)
            return null;

        int cellType = cell.getCellType();
        if (cellType == Cell.CELL_TYPE_FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:
                return Strings.parseDouble(cell.getRichStringCellValue().getString());
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? -1.0 : 0.0;
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_ERROR:
                return null;
            default:
                return cell.getNumericCellValue();
        }
    }

    /**
     * Retrieves a cell at the given row and column
     */
    private Cell getCell(int row, int column) {
        Row rowObj = sheet.getRow(row - 1);
        return rowObj != null ? rowObj.getCell(column - 1) : null;
    }

    /**
     * Retrieves the number of rows in the current sheet
     */
    public int getRowCount() {
        return sheet.getLastRowNum() + 1;
    }


    // ------------------------------------------------------------------------
    // Style handling methods
    // ------------------------------------------------------------------------

    /**
     * Retrieves the number of columns in the given row
     */
    public int getColumnCount(int row) {
        Row rowObj = sheet.getRow(row - 1);
        return rowObj != null ? rowObj.getLastCellNum() : 0;
    }

    /**
     * Retrieves a style with the given style code
     */
    public CellStyle getStyle(String styleCode) {

        // always consider the global default style
        styleCode = Strings.combine(defaultCellStyle, ";", styleCode);

        // check if we have created this style already
        XSSFCellStyle style = (XSSFCellStyle) styles.get(styleCode);
        if (style != null) {
            return style;
        }

        Map<String, String> attr = getStyleAttributes(styleCode);
        style = (XSSFCellStyle) workbook.createCellStyle();

        // set the border
        style.setBorderBottom(getBorder(attr, "border-bottom"));
        style.setBorderTop(getBorder(attr, "border-top"));
        style.setBorderLeft(getBorder(attr, "border-left"));
        style.setBorderRight(getBorder(attr, "border-right"));

        // set the border colors
        XSSFColor bColor = getColor(attr, "border-color");
        XSSFColor bbColor = Beans.either(getColor(attr, "border-bottom-color"), bColor);
        XSSFColor btColor = Beans.either(getColor(attr, "border-top-color"), bColor);
        XSSFColor blColor = Beans.either(getColor(attr, "border-left-color"), bColor);
        XSSFColor brColor = Beans.either(getColor(attr, "border-right-color"), bColor);

        if (bbColor != null) {
            style.setBottomBorderColor(bbColor);
        }
        if (btColor != null) {
            style.setTopBorderColor(btColor);
        }
        if (blColor != null) {
            style.setLeftBorderColor(blColor);
        }
        if (brColor != null) {
            style.setRightBorderColor(brColor);
        }

        // set the alignment
        String align = attr.get("text-align");
        if (align != null && TEXT_ALIGN_CONSTANTS.containsKey(align)) {
            style.setAlignment(TEXT_ALIGN_CONSTANTS.get(align));
        }
        String valign = attr.get("vertical-align");
        if (valign != null && VERTICAL_ALIGN_CONSTANTS.containsKey(valign)) {
            style.setVerticalAlignment(VERTICAL_ALIGN_CONSTANTS.get(valign));
        } else {
            style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        }

        // set the font properties
        boolean bold = Strings.equals(attr.get("font-weight"), "bold");
        boolean italic = Strings.equals(attr.get("font-style"), "italic");
        boolean underline = Strings.equals(attr.get("text-decoration"), "underline");
        String fontName = attr.get("font-family");
        Integer fontSize = Strings.parseInteger(attr.get("font-size"));
        XSSFColor color = getColor(attr, "color");

        if (bold || italic || underline || fontName != null || fontSize != null || color != null) {
            XSSFFont font = (XSSFFont) workbook.createFont();
            if (bold) font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            if (italic) font.setItalic(true);
            if (underline) font.setUnderline(Font.U_SINGLE);
            if (fontSize != null) font.setFontHeightInPoints(fontSize.shortValue());
            if (fontName != null) font.setFontName(fontName);
            if (color != null) font.setColor(color);
            style.setFont(font);
        }

        // set the color and pattern properties
        XSSFColor bgColor = getColor(attr, "background-color");
        XSSFColor ptColor = getColor(attr, "pattern-color");
        if (bgColor != null && ptColor != null) {
            style.setFillBackgroundColor(bgColor);
            style.setFillForegroundColor(ptColor);
        } else if (bgColor != null) {
            style.setFillForegroundColor(bgColor);
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }

        String pattern = attr.get("pattern");
        if (pattern != null && PATTERN_CONSTANTS.containsKey(pattern)) {
            style.setFillPattern(PATTERN_CONSTANTS.get(pattern));
        }

        // set the text-indent and text-rotation properties
        Integer indent = Strings.parseInteger(attr.get("text-indent"));
        Integer rotation = Strings.parseInteger(attr.get("text-rotation"));
        if (indent != null) style.setIndention(indent.shortValue());
        if (rotation != null) style.setRotation(rotation.shortValue());


        // set the data format
        String dataFormat = attr.get("data-format");
        if (dataFormat != null) {
            DataFormat format = workbook.createDataFormat();
            style.setDataFormat(format.getFormat(dataFormat));
        }

        // set the readonly, hidden and wrapText properties
        style.setLocked(Strings.equals(attr.get("readonly"), "true"));
        style.setHidden(Strings.equals(attr.get("display"), "hidden"));
        style.setWrapText(Strings.equals(attr.get("word-wrap"), "wrap"));
        styles.put(styleCode, style);
        return style;
    }

    /**
     * Splits the given style code into name-value pairs
     */
    private Map<String, String> getStyleAttributes(String styleCode) {

        // check if we have parsed this style code already
        Map<String, String> attributes = styleAttributes.get(styleCode);
        if (attributes != null) {
            return attributes;
        }

        // parse the style code and put it in a map
        attributes = new HashMap<String, String>();

        for (String part : Strings.split(Strings.nz(styleCode), ";")) {
            String[] split = part.split(":");
            if (split.length < 2) {
                continue;
            }
            String property = split[0].toLowerCase().trim();
            String value = Strings.trimToNull(split[1].toLowerCase());
            attributes.put(property, value);
        }

        styleAttributes.put(styleCode, attributes);
        return attributes;
    }

    /**
     * Retrieves the border attribute from the attributes
     */
    private short getBorder(Map<String, String> attr, String borderName) {

        // first try the specific attribute
        String border = attr.get(borderName);

        if (border != null && BORDER_CONSTANTS.containsKey(border)) {
            return BORDER_CONSTANTS.get(border);
        }

        // then try the general border attribute
        border = attr.get("border");
        if (border != null && BORDER_CONSTANTS.containsKey(border)) {
            return BORDER_CONSTANTS.get(border);
        }

        // no attribute, use no border
        return CellStyle.BORDER_NONE;
    }

    /**
     * Retrieves the border attribute from the attributes
     */
    private XSSFColor getColor(Map<String, String> attr, String colorName) {

        String colorStr = attr.get(colorName);
        if (Strings.isEmpty(colorStr) || !colorStr.startsWith("#")
                || colorStr.length() != 7) return null;

        try {
            int r = Integer.valueOf(colorStr.substring(1, 3), 16);
            int g = Integer.valueOf(colorStr.substring(3, 5), 16);
            int b = Integer.valueOf(colorStr.substring(5, 7), 16);
            return new XSSFColor(new byte[]{(byte) r, (byte) g, (byte) b});
        } catch (NumberFormatException e) {
            LOG.error("Invalid color format: " + colorStr, e);
            return null;
        }
    }

    /**
     * Adds an autosize column
     */
    private void addAutosizeColumn(Integer column) {

        Set<Integer> columns = autosizeColumns.get(sheet);
        if (columns == null) {
            columns = new HashSet<>();
            autosizeColumns.put(sheet, columns);
        }
        columns.add(column);
    }


    // ------------------------------------------------------------------------
    // Excel writing methods
    // ------------------------------------------------------------------------

    /**
     * Execute the autosize
     */
    private void doAutosize() {

        for (Entry<Sheet, Set<Integer>> entry : autosizeColumns.entrySet()) {
            Sheet sheet = entry.getKey();
            for (Integer column : entry.getValue()) {
                sheet.autoSizeColumn(column.shortValue(), true);
            }
        }
    }

    /**
     * Adds a cell at the current position with the given style
     */
    public Cell addCell(String styleCode, String defaultStyle) {

        String styles = Strings.combine(Strings.combine(defaultCellStyle, ";", defaultStyle), ";", styleCode);
        CellStyle style = getStyle(styles);

        Map<String, String> styleAttibutes = getStyleAttributes(styles);
        int colSpan = Strings.parseInteger(styleAttibutes.get("colspan"), 1);
        int rowSpan = Strings.parseInteger(styleAttibutes.get("rowspan"), 1);

        if (colSpan > 1 || rowSpan > 1) {
            // if we have colspan or rowspan, we need to add a region

            CellRangeAddress region = new CellRangeAddress(
                    currentRow - 1,
                    currentRow + rowSpan - 2,
                    currentColumn - 1,
                    currentColumn + colSpan - 2);
            sheet.addMergedRegion(region);

            // then fill all the region with cells of the same style
            for (int i = 0; i < Math.max(rowSpan, 1); i++) {
                Row hssfRow = sheet.getRow(currentRow - 1 + i);
                if (hssfRow == null) {
                    hssfRow = sheet.createRow(currentRow - 1 + i);
                }
                for (int k = 0; k < Math.max(colSpan, 1); k++) {
                    Cell cell = hssfRow.getCell(currentColumn - 1 + k);
                    if (cell == null) {
                        cell = hssfRow.createCell(currentColumn - 1 + k);
                    }
                    cell.setCellStyle(style);
                }
            }
        }

        Row hssfRow = sheet.getRow(currentRow - 1);
        if (hssfRow == null) {
            hssfRow = sheet.createRow(currentRow - 1);
        }
        Cell cell = hssfRow.getCell(currentColumn - 1);
        if (cell == null) {
            cell = hssfRow.createCell(currentColumn - 1);
        }
        cell.setCellStyle(style);

        // set column width
        if (Strings.equalsIgnoreCase(styleAttibutes.get("width"), "auto")) {
            addAutosizeColumn(cell.getColumnIndex());
        } else {
            int width = Strings.parseInteger(styleAttibutes.get("width"), 0);
            if (width > 0) {
                sheet.setColumnWidth(currentColumn - 1, (width * PIXEL_WIDTH_FACTOR));
            }
        }

        // set row height
        int height = Strings.parseInteger(styleAttibutes.get("height"), 0);
        if (height > 0) {
            hssfRow.setHeight((short) (height * PIXEL_HEIGHT_FACTOR));
        }

        currentColumn += Math.max(colSpan, 1);
        return cell;
    }

    /**
     * Adds an empty cell to the document
     */
    public void addEmptyCell() {
        addStringCell("", null);
    }

    /**
     * Adds empty cells to the document
     */
    public void addEmptyCells(int count) {
        for (int i = 0; i < count; i++) {
            addStringCell("", null);
        }
    }

    /**
     * Continues writing at the next line
     */
    public void newLine() {
        currentRow++;
        currentColumn = 1;
    }

    /**
     * Continues writing several lines further down
     */
    public void newLine(int count) {
        currentRow += count;
        currentColumn = 1;
    }

    /**
     * Adds a normal string cell at the current position
     */
    public void addStringCell(String content, String styleCode) {
        Cell cell = addCell(styleCode, null);
        cell.setCellValue(new XSSFRichTextString(content));
    }

    /**
     * Adds a normal string cell at the current position
     */
    public void addStringCell(String content) {
        addStringCell(content, null);
    }

    /**
     * Adds a header cell at the current position
     */
    public void addHeaderCell(String content, String styleCode) {
        addStringCell(content, Strings.combine(defaultHeaderStyle, ";", styleCode));
    }

    /**
     * Adds a header cell at the current position
     */
    public void addHeaderCell(String content) {
        addHeaderCell(content, null);
    }

    /**
     * Adds a date cell at the current position
     */
    public void addDateCell(Date date, String styleCode) {

        Cell cell = addCell(styleCode, "text-align:center; data-format: yyyy-mm-dd");
        if (date != null) {
            cell.setCellValue(date);
        }
    }

    /**
     * Adds a date cell at the current position
     */
    public void addDateCell(Date date) {
        addDateCell(date, null);
    }

    /**
     * Adds an number cell with integer format at the current position
     */
    public void addIntegerCell(Integer value, String styleCode) {
        Cell cell = addCell(styleCode, "text-align:right; data-format: #,##0");
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * Adds an number cell with integer format at the current position
     */
    public void addIntegerCell(Integer value) {
        addIntegerCell(value, null);
    }

    /**
     * Adds an number cell with long format at the current position
     */
    public void addLongCell(Long value, String styleCode) {
        Cell cell = addCell(styleCode, "text-align:right; data-format: #,##0");
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        }
    }

    /**
     * Adds an number cell with long format at the current position
     */
    public void addLongCell(Long value) {
        addLongCell(value, null);
    }

    /**
     * Adds an number cell with double format at the current position
     */
    public void addDoubleCell(Double value, String styleCode) {
        Cell cell = addCell(styleCode, "text-align:right; data-format: #0");
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * Adds an number cell with double format at the current position
     */
    public void addDoubleCell(Double value) {
        addDoubleCell(value, null);
    }

    /**
     * Adds an number cell with money format at the current position
     */
    public void addMoneyCell(Double value, String styleCode) {
        Cell cell = addCell(styleCode, "text-align:right; data-format: #,##0.00");
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * Adds an number cell with money format at the current position
     */
    public void addMoneyCell(Double value) {
        addMoneyCell(value, null);
    }

    /**
     * Adds a boolean cell with at the current position
     */
    public void addBooleanCell(Boolean value, String styleCode) {
        Cell cell = addCell(styleCode, "text-align:center;");
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * Adds a boolean cell with at the current position
     */
    public void addBooleanCell(Boolean value) {
        addBooleanCell(value, null);
    }

    /**
     * Adds a formula cell at the current position
     */
    public void addFormulaCell(String formula, String styleCode) {
        Cell cell = addCell(styleCode, "readonly:true;");
        if (cell != null) {
            cell.setCellFormula(formula);
        }
    }

    /**
     * Adds a formula cell with money format at the current position
     */
    public void addFormulaMoneyCell(String formula, String styleCode) {
        Cell cell = addCell(styleCode, "readonly: true; text-align: right; data-format: #,##0.00;");
        if (cell != null) {
            cell.setCellFormula(formula);
        }
    }

    /**
     * Adds a formula cell at the current position
     */
    public void addFormulaIntegerCell(String formula, String styleCode) {
        Cell cell = addCell(styleCode, "readonly: true; text-align: right; data-format: #,##0;");
        if (cell != null) {
            cell.setCellFormula(formula);
        }
    }

    /**
     * Adds a formula cell at the current position
     */
    public void addFormulaCell(String formula) {
        addFormulaCell(formula, null);
    }

    /**
     * Adds a hyperlink cell at the current position
     */
    public void addLinkCell(String label, String sheetName, String location, String styleCode) {
        addHyperlink(label, "#'" + sheetName + "'!" + Strings.either(location, "A1"), styleCode);
    }

    /**
     * Adds a hyperlink cell at the current position
     */
    public void addLinkCell(String label, String sheetName, String location) {
        addLinkCell(label, sheetName, location, null);
    }

    /**
     * Adds a hyperlink cell at the current position
     */
    public void addLinkCell(String label, String sheetName) {
        addLinkCell(label, sheetName, null, null);
    }

    /**
     * Adds a hyperlink cell at the current position
     */
    private void addHyperlink(String label, String url, String styleCode) {
        Cell cell = addCell(styleCode, "readonly:true; text-decoration: underline;");
        if (cell != null) {
            label = Strings.nz(label).replaceAll("\"", "'");
            cell.setCellFormula("HYPERLINK(\"" + url + "\", " + "\"" + label + "\")");
            cell.setCellValue(label);
        }
    }


    // ------------------------------------------------------------------------
    // I/O methods
    // ------------------------------------------------------------------------

    /**
     * Adds a cell comment
     */
    public void addCellComment(String text, int rows, int columns) {

        Cell cell = getCell(currentRow, Math.max(currentColumn - 1, 1));
        if (cell != null) {
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                    currentColumn - 1, currentRow - 1, currentColumn + columns - 1, currentRow + rows - 1);

            Comment comment = sheet.createDrawingPatriarch().createCellComment(anchor);
            comment.setString(new XSSFRichTextString(text));
            cell.setCellComment(comment);
        }
    }

    /**
     * Writes the excel file to disk
     */
    public void write(String filename) {
        write(new File(filename));
    }

    /**
     * Writes the excel file to disk
     */
    public void write(File file) {

        doAutosize();
        try {
            try (FileOutputStream fo = new FileOutputStream(file)) {
                workbook.write(fo);
            }
        } catch (Throwable e) {
            throw new RuntimeException("saving file " + file.getAbsolutePath());
        }
    }


    // ------------------------------------------------------------------------
    // Simple methods
    // ------------------------------------------------------------------------

    /**
     * Retrieves the excel file as a byte array
     */
    public byte[] getBytes() {

        doAutosize();
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                bos.flush();
                return bos.toByteArray();
            }
        } catch (Throwable e) {
            // writing can cause a RuntimeException
            throw new RuntimeException("building excel file");
        }
    }

    /**
     * Moves the pointer to the given position
     */
    public void setPosition(int row, int column) {
        currentRow = row;
        currentColumn = column;
    }

    /**
     * Adds a new sheet to the document
     */
    public void addSheet(String sheetName) {

        // we need to set the default font properties before creating the sheet
        Map<String, String> attr = getStyleAttributes(defaultCellStyle);
        String fontName = attr.get("font-family");
        Integer fontSize = Strings.parseInteger(attr.get("font-size"));

        Font defaultFont = workbook.getFontAt((short) 0);
        if (fontSize != null) {
            defaultFont.setFontHeightInPoints(fontSize.shortValue());
        }
        if (fontName != null) {
            defaultFont.setFontName(fontName);
        }

        // create the sheet
        sheet = workbook.createSheet(fixSheetName(sheetName));

        currentRow = 1;
        currentColumn = 1;
    }

    /**
     * Returns true if the document has a sheet with the given name
     */
    public boolean hasSheetWithName(String sheetName) {
        return workbook.getSheet(fixSheetName(sheetName)) != null;
    }

    /**
     * Sets the current sheet to the sheet with the given name
     */
    public void setSheet(String sheetName) {
        sheet = workbook.getSheet(fixSheetName(sheetName));
        currentRow = 1;
        currentColumn = 1;
    }

    /**
     * Sets the current sheet to the sheet at the given index
     */
    public void setSheet(int index) {
        sheet = workbook.getSheetAt(index);
        currentRow = 1;
        currentColumn = 1;
    }

    /**
     * Gets the name of the sheet at the given position.
     */
    public String getSheetName(int index) {
        return workbook.getSheetName(index);
    }

    /**
     * Fixes a sheet name so that it works in Excel
     */
    private String fixSheetName(String name) {

        name = Strings.isBlank(name) ? "Sheet0" : name.trim();
        name = name.replaceAll("[':\\\\/\\?\\*\\[\\]]", "");
        name = name.replace('\n', ' ').replace('\r', ' ');
        return Strings.left(name, 31);
    }

    /**
     * Creates a split (freezepane). Any existing freezepane or split pane is overwritten.
     *
     * @param colSplit Horizonatal position of split.
     * @param rowSplit Vertical position of split.
     */
    public void createFreezePane(int colSplit, int rowSplit) {
        sheet.createFreezePane(colSplit, rowSplit);
    }

    // ------------------------------------------------------------------------
    // Getter/Setter methods
    // ------------------------------------------------------------------------

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet currentSheet) {
        sheet = currentSheet;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }

    public void setCurrentColumn(int currentColumn) {
        this.currentColumn = currentColumn;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public String getDefaultCellStyle() {
        return defaultCellStyle;
    }

    public void setDefaultCellStyle(String defaultCellStyle) {
        this.defaultCellStyle = defaultCellStyle;
    }

    public String getDefaultHeaderStyle() {
        return defaultHeaderStyle;
    }

    public void setDefaultHeaderStyle(String defaultHeaderStyle) {
        this.defaultHeaderStyle = defaultHeaderStyle;
    }

}