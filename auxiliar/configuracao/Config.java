//pacote que essa classe pertence
package auxiliar.configuracao;

//bibliotecas
import javax.json.*;
import java.util.List;
import java.util.ArrayList;
 
//classe responsavel por criar a configuracao utilizada
public class Config {
  private String apoCompila;
  private String apoAtual;
  private List <String> listAppServer = new ArrayList<String>();
  
  //altera os atributos
  public Config(){
    this.apoCompila = " ";
    this.apoAtual = " ";
  }
  
  public String getCompila(){
    return this.apoCompila;
  }
  
  public void setCompila(String diretorio){
    //se nao estiver em branco
    if (!(diretorio.isEmpty())){
      //se o ultimo caracter nao for barra
      //if ( (!(diretorio.substring(diretorio.length()-1,diretorio.length()).equals("/"))) && (!(diretorio.equals(" ")))){
	//diretorio += "\\";
      //}
    }
    
    this.apoCompila = diretorio;
  }
  
  public List <String> getList(){
    return this.listAppServer;
  }
  
  public String getAtual(){
    return this.apoAtual;
  }
  
  public void setAtual(String diretorio){
    //se nao estiver em branco
    if (!(diretorio.isEmpty())){
      //se o ultimo caracter nao for barra
      //if ( (!(diretorio.substring(diretorio.length()-1,diretorio.length()).equals("/"))) && (!(diretorio.equals(" ")))){
	//diretorio += "/";
      //}
    }
    
    this.apoAtual = diretorio;
  }
  
  //adiciona um registro na lista encadeada
  public void addReg(String diretorio){
    //se nao estiver em branco
    if (!(diretorio.isEmpty())){
      //se o ultimo caracter nao for barra
      //if ( (!(diretorio.substring(diretorio.length()-1,diretorio.length()).equals("/"))) && (!(diretorio.equals(" ")))){
	//diretorio += "/";
      //}
      
      if (!(diretorio.equals(" ")) )
	this.listAppServer.add(diretorio);
    }
  }
  
  //remove um registro na lista encadeada
  public void remReg(int atual){
    if (atual >= 1) {
      //tenta remover um registro
      try {
	if (listAppServer.size() >= atual)
	  this.listAppServer.remove(atual-1);
      }
      //se acontecer alguma falha, mostra mensagem
      catch (Exception ex){
	System.out.println("Excecao causada na remocao de um AppServer:");
	ex.printStackTrace();
      }
    }
  }
  
  //funcao para gerar arquivo JSON
  public String getJSONString(){
    int atual = 0;
    
    //cria o JSON
    JsonObjectBuilder vlJSon = Json.createObjectBuilder();
    JsonArrayBuilder vlApps = Json.createArrayBuilder();
    
    //adiciona os atributos
    vlJSon.add("apo_compila", this.apoCompila);
    vlJSon.add("apo_atual", this.apoAtual);
    
    //se a lista estiver vazia, adiciona um registro em branco
    if (listAppServer.size() == 0)
      vlApps.add(Json.createObjectBuilder().add("dir", " "));
    else
      //percorrendo a lista e adicionando nos atributos
      for(atual = 0; atual < listAppServer.size(); atual++){
	vlApps.add(Json.createObjectBuilder().add("dir", listAppServer.get(atual)));
      }
    vlJSon.add("app_diretorios", vlApps);
    
    //retorna a string no formato JSON
    JsonObject vlObj = vlJSon.build();
    return vlObj.toString();
  }
}