//pacote que essa classe pertence
package config;

//bibliotecas
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableData extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private final int posSequence=0;
	private final int posAppServer=1;

	//setando as columns e linhas
	private String columns[]={"Sequencia", "Caminho AppServer"};
	private List<Data> dataList;
	private Boolean editMode = true;

	//iniciliazando o construtor
	public TableData(List<Data> listData, Boolean lEdit) {
		this.dataList = listData;
		this.editMode = lEdit;
	}

	//metodo para retornar o total de columns
	@Override
	public int getColumnCount() {
		return columns.length;
	}

	//metodo para retornar o total de linhas
	@Override
	public int getRowCount() {
		//retorna o total de linhas na tabela
		return dataList.size();
	}

	//metodo para retornar o nome da coluna
	@Override
	public String getColumnName(int indexColumn) {
		return columns[indexColumn];
	}

	//metodo para pegar o valor da posicao atual
	@Override
	public Object getValueAt(int indexLine, int indexColumn) {

		//Pega os dados da linha atual
		Data currentData = dataList.get(indexLine);

		//retorna o valor da coluna
		switch (indexColumn) {
		case posSequence:
			return currentData.getSequence();
		case posAppServer:
			return currentData.getAppServer();
		default:
			throw new IndexOutOfBoundsException("Coluna Invalida!!!");
		}
	}

	//identificando qual celula pode ser editada
	@Override
	public boolean isCellEditable(int indexLine, int indexColumn) {
		//se for editavel
		if (this.editMode)
			return true;

		//Senao
		else
			return false;
	}

	//metodo para setar o valor
	@Override
	public void setValueAt(Object objValue, int indexLine, int indexColumn) {
		Data currentData=dataList.get(indexLine);

		//se for a coluna do conteudo
		if(indexColumn == posSequence){
			currentData.setSequence((int) objValue);
		}

		//se for a coluna do conteudo
		if(indexColumn == posAppServer){
			currentData.setAppServer((String) objValue);
		}
	}

	//retorna o valor da linha atual
	public Data getValue(int indexLine){
		return dataList.get(indexLine);
	}

	//retorna o indice do objeto
	public int indexOf(Data dataRet) {
		return dataList.indexOf(dataRet);
	}

	//adiciona uma linha
	public void onAdd(Data currentData) {
		dataList.add(currentData);
		fireTableRowsInserted(indexOf(currentData), indexOf(currentData));
	}

	//adiciona uma lista inteira
	public void onAddAll(List<Data> dataListIn) {
		dataList.addAll(dataListIn);
		fireTableDataChanged();
	}

	//remove registro conforme índice
	public void onRemove(int indexLine) {
		dataList.remove(indexLine);
		fireTableRowsDeleted(indexLine, indexLine);
	}

	//remove um registro da lista
	public void onRemove(Data currentData) {
		dataList.remove(currentData);
		fireTableRowsDeleted(indexOf(currentData), indexOf(currentData));
	}

	//remove todos os itens da lista
	public void onRemoveAll() {
		dataList.clear();
		fireTableDataChanged();
	}

	//seta os valores
	public void setData(List<Data> listData) {
		dataList.clear();
		this.dataList=listData;
	}

	//pega os valores
	public List<Data> getData() {
		return this.dataList;
	}

}