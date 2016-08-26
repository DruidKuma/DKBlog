package com.druidkuma.blog.service.excel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SimpleExcelDocument implements Iterable<List<String>> {
    private ExcelDocument document;

    public SimpleExcelDocument(ExcelDocument document) {
        this.document = document;
    }

    public SimpleExcelDocument(String sheetName) {
        document = new ExcelDocument(sheetName);
        document.setDefaultCellStyle("font-family:Arial; font-size:10; border:thin; border-color:#366092; width:auto");
        document.setDefaultHeaderStyle("font-weight:bold; readonly:true; background-color:#b8cce4; width:auto");
    }

    public void addHeaders(String... headers) {
        for (String header : headers) {
            document.addHeaderCell(header);
        }
        document.newLine();
    }

    public void addContentRow(String... row) {
        for (String cell : row) {
            document.addStringCell(cell);
        }
        document.newLine();
    }

    public List<String> getHeaders() {
        String header;
        int column = 1;
        final ArrayList<String> headers = new ArrayList<>();
        while ((header = document.readString(1, column)) != null) {
            headers.add(header);
            column++;
        }
        return headers;
    }

    public ExcelDocument getDocument() {
        return document;
    }

    @Override
    public Iterator<List<String>> iterator() {
        return new ExcelDocumentIterator();
    }

    private class ExcelDocumentIterator implements Iterator<List<String>> {

        private int totalCols;
        private int currentRow = 2;

        public ExcelDocumentIterator() {
            countHeaders();
        }

        private void countHeaders() {
            int column = 1;

            while (document.readString(1, column) != null) {
                column++;
            }

            this.totalCols = column - 1;
        }

        @Override
        public boolean hasNext() {
            boolean emptyRow = true;
            for (int column = 1; column <= totalCols; column++) {
                final String cellValue = document.readString(currentRow, column);
                if (cellValue != null) {
                    emptyRow = false;
                }
            }
            return !emptyRow;
        }

        @Override
        public List<String> next() {
            if (hasNext()) {
                final ArrayList<String> row = new ArrayList<>();
                for (int currentColumn = 1; currentColumn <= totalCols; currentColumn++) {
                    final String cell = document.readString(currentRow, currentColumn);
                    row.add(cell);
                }
                currentRow++;
                return row;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}