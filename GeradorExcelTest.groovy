@Grapes([
    @Grab('org.apache.poi:poi:3.8')    
])
//compile 'org.apache.poi:poi:3.8'

import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row

class GeradorExcel {
	int rownum = 0;
    HSSFSheet firstSheet;
    Collection<File> files;
    HSSFWorkbook workbook;
    File exactFile;

    OutputStream saida
    Integer qtdColunas

    GeradorExcel(OutputStream outputStream) {
    	saida = outputStream
    	workbook = new HSSFWorkbook()
        firstSheet = workbook.createSheet("FIRST SHEET");
        //Row headerRow = firstSheet.createRow(rownum);
        //headerRow.setHeightInPoints(40);    	
    }

    void adicionarLinha(List colunas) {
    	Row row = firstSheet.createRow(rownum)
        Integer pos = 0
        for(int k=0; k<colunas.size(); k++){
            Cell cell = row.createCell(k)
            cell.setCellValue(colunas[k])
        }    	
        rownum++
        //saida.flush()
        //workbook.write(saida);
    }

    void encerrar(){
        HSSFCellStyle hsfstyle=workbook.createCellStyle();
        hsfstyle.setBorderBottom((short) 1);
        hsfstyle.setFillBackgroundColor((short)245);
        workbook.write(saida);
    }


}


output = new FileOutputStream(new File('/tmp/test.xls'))
gerador = new GeradorExcel(output)

List<String> headerRow = new ArrayList<String>()
headerRow << 'Employee No'
headerRow << 'Employee Name'
headerRow << 'Employee Address'

println headerRow

gerador.adicionarLinha(headerRow)

List<String> firstRow = new ArrayList<String>();
firstRow << '1111'
firstRow << 'Gautam'
firstRow << 'India'

println firstRow

gerador.adicionarLinha(firstRow)

gerador.encerrar()

output.close()

//cls.createExcelFile();
    