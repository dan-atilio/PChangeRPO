//pacote que essa classe pertence
package auxiliar.configuracao;

//bibliotecas
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabelaDad extends AbstractTableModel{
  private final int nPosTab=0;
  private final int nPosCont=1;
  
  //setando as colunas e linhas
  private String colunas[]={"Sequencia","Caminho AppServer"};
  private List<Dados> listDados;
  private Boolean modoEdicao = true;
  
  //iniciliazando o construtor
  public TabelaDad(List<Dados> oDad, Boolean lEdit) {
    this.listDados = oDad;
    this.modoEdicao = lEdit;
  }
  
  //metodo para retornar o total de colunas
  @Override
  public int getColumnCount() {
    return colunas.length;
  }

  //metodo para retornar o total de linhas
  @Override
  public int getRowCount() {
    //retorna o total de linhas na tabela
    return listDados.size();
  }
  
  //metodo para retornar o nome da coluna
  @Override
  public String getColumnName(int nColIndex) {
    return colunas[nColIndex];
  }
  
  //metodo para pegar o valor da posicao atual
  @Override
  public Object getValueAt(int nLinIndex, int nColIndex) {
	  
    //Pega os dados da linha atual
    Dados oDadAtual = listDados.get(nLinIndex);
    
    //retorna o valor da coluna
    switch (nColIndex) {
      case nPosTab:
	      return oDadAtual.getSequencia();
      case nPosCont:
	      return oDadAtual.getAppServer();
      default:
	throw new IndexOutOfBoundsException("Coluna Invalida!!!");
    }
  }
  
  //identificando qual celula pode ser editada
  @Override
  public boolean isCellEditable(int nLinIndex, int nColIndex) {
    //se for editavel
    if (this.modoEdicao)
      return true;
    
    //Senao
    else
      return false;
  }
  
  //metodo para setar o valor
  @Override
  public void setValueAt(Object oValue, int nLinIndex, int nColIndex) {
    Dados oDadoAtu=listDados.get(nLinIndex);
    
    //se for a coluna do conteudo
    if(nColIndex == nPosTab){
      oDadoAtu.setSequencia((int) oValue);
    }
    
    //se for a coluna do conteudo
    if(nColIndex == nPosCont){
      oDadoAtu.setAppServer((String) oValue);
    }
  }
  
  //retorna o valor da linha atual
  public Dados getValue(int nLinIndex){
    return listDados.get(nLinIndex);
  }
  
  //retorna o indice do objeto
  public int indexOf(Dados oDadRet) {
    return listDados.indexOf(oDadRet);
  }
  
  //adiciona uma linha
  public void onAdd(Dados oDadoAtu) {
    listDados.add(oDadoAtu);
    fireTableRowsInserted(indexOf(oDadoAtu), indexOf(oDadoAtu));
  }
  
  //adiciona uma lista inteira
  public void onAddAll(List<Dados> listDadosIn) {
    listDados.addAll(listDadosIn);
    fireTableDataChanged();
  }
  
  //remove registro conforme índice
  public void onRemove(int nLinIndex) {
    listDados.remove(nLinIndex);
    fireTableRowsDeleted(nLinIndex, nLinIndex);
  }
  
  //remove um registro da lista
  public void onRemove(Dados oDadoAtu) {
    listDados.remove(oDadoAtu);
    fireTableRowsDeleted(indexOf(oDadoAtu), indexOf(oDadoAtu));
  }
  
  //remove todos os itens da lista
  public void onRemoveAll() {
    listDados.clear();
    fireTableDataChanged();
  }
  
  //seta os valores
  public void setDados(List<Dados> oDad) {
    listDados.clear();
    this.listDados=oDad;
  }
  
  //pega os valores
  public List<Dados> getDados() {
    return this.listDados;
  }
      
}