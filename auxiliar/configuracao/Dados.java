//pacote que essa classe pertence
package auxiliar.configuracao;

public class Dados {
  private int sequencia;
  private String appServer;
	
  //Inicializa as variaveis da classe
  public Dados(int seq, String app) {
    this.sequencia = seq;
    this.appServer = app;
  }

  //classe para retornar o campo
  public int getSequencia() {
    return sequencia;
  }
  
  //classe para retornar o conteudo
  public String getAppServer() {
    return appServer;
  }

  //Setando o campo
  public void setSequencia(int seq) {
    this.sequencia = seq;
  }

  //Setando o conteudo
  public void setAppServer(String app) {
    this.appServer = app;
  }
  
}
