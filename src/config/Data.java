//pacote que essa classe pertence
package config;

public class Data {
	private int sequence;
	private String appServer;

	//Inicializa as variaveis da classe
	public Data(int seq, String app) {
		this.sequence = seq;
		this.appServer = app;
	}

	//classe para retornar o campo
	public int getSequence() {
		return sequence;
	}

	//classe para retornar o conteudo
	public String getAppServer() {
		return appServer;
	}

	//Setando o campo
	public void setSequence(int seq) {
		this.sequence = seq;
	}

	//Setando o conteudo
	public void setAppServer(String app) {
		this.appServer = app;
	}

}
