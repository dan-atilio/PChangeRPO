//pacote que essa classe pertence
package config;

//bibliotecas
import javax.json.*;
import java.util.List;
import java.util.ArrayList;

//classe responsavel por criar a configuracao utilizada
public class Config {
	private String apoCompile;
	private String apoCurrent;
	private List <String> listAppServer = new ArrayList<String>();

	//altera os atributos
	public Config(){
		this.apoCompile = " ";
		this.apoCurrent = " ";
	}

	public String getCompile(){
		return this.apoCompile;
	}

	public void setCompile(String directory){
		//se nao estiver em branco
		if (!(directory.isEmpty())){
			//se o ultimo caracter nao for barra
			//if ( (!(directory.substring(directory.length()-1,directory.length()).equals("/"))) && (!(directory.equals(" ")))){
			//directory += "\\";
			//}
		}

		this.apoCompile = directory;
	}

	public List <String> getList(){
		return this.listAppServer;
	}

	public String getCurrent(){
		return this.apoCurrent;
	}

	public void setCurrent(String directory){
		//se nao estiver em branco
		if (!(directory.isEmpty())){
			//se o ultimo caracter nao for barra
			//if ( (!(directory.substring(directory.length()-1,directory.length()).equals("/"))) && (!(directory.equals(" ")))){
			//directory += "/";
			//}
		}

		this.apoCurrent = directory;
	}

	//adiciona um registro na lista encadeada
	public void addReg(String directory){
		//se nao estiver em branco
		if (!(directory.isEmpty())){
			//se o ultimo caracter nao for barra
			//if ( (!(directory.substring(directory.length()-1,directory.length()).equals("/"))) && (!(directory.equals(" ")))){
			//directory += "/";
			//}

			if (!(directory.equals(" ")) )
				this.listAppServer.add(directory);
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
		vlJSon.add("apo_compile", this.apoCompile);
		vlJSon.add("apo_current", this.apoCurrent);

		//se a lista estiver vazia, adiciona um registro em branco
		if (listAppServer.size() == 0)
			vlApps.add(Json.createObjectBuilder().add("dir", " "));
		else
			//percorrendo a lista e adicionando nos atributos
			for(atual = 0; atual < listAppServer.size(); atual++){
				vlApps.add(Json.createObjectBuilder().add("dir", listAppServer.get(atual)));
			}
		vlJSon.add("app_directory", vlApps);

		//retorna a string no formato JSON
		JsonObject vlObj = vlJSon.build();
		return vlObj.toString();
	}
}