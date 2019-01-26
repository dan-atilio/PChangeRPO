//javac -cp lib/javax.json-api-1.0.jar:javax.json-1.0.4.jar: PChangeRPO.java 
//java -cp lib/javax.json-api-1.0.jar:lib/javax.json-1.0.4.jar: PChangeRPO
//jar cvfm /home/daniel/apps/PChangeRPO/PChangeRPO.jar META-INF/MANIFEST.MF *.class auxiliar/mensagens/*.class auxiliar/configuracao/*.class
//java -jar /home/daniel/apps/PChangeRPO/PChangeRPO.jar

//bibliotecas
import javax.json.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;
import auxiliar.mensagens.*;
import auxiliar.configuracao.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Formatter;
import javax.swing.UIManager.*;

//Classe PChangeRPO
class PChangeRPO extends JFrame implements ActionListener
{
  private JButton btnSair, btnProc, btnInc, btnRem;
  private JLabel lbCompila, lbAtual, lbNovo, lbProces;
  private JTextField jtCompila, jtAtual, jtNovo;
  private static ImageIcon icone = new ImageIcon("icone.png");
  private Config objConfig = new Config();
  private String compila = "";
  private String atual = "";
  private String novo = "";
  private List <String> lista = new ArrayList<String>();
  private int verScroll = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS; 
  private int horScroll = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
  private JTable tbApps;
  private TabelaDad confApps;
  private JScrollPane spApps;
  private List <Dados> listApps;
  
  //construtor dos componentes
  public PChangeRPO() 
  {
    setLayout(null);
    
    //pegando o arquivo JSON de configuracao
    File arqConfiguracao = new File("config.json");
    if (arqConfiguracao.exists()){
      //tenta abrir o arquivo e pegar os atributos do json
      try {
	//carrega o objeto
	FileReader lerArquivo = new FileReader("config.json");
	JsonReader jsReader = Json.createReader(lerArquivo);
	JsonObject jsObject = jsReader.readObject();
	
	//setando os apos
	objConfig.setCompila(jsObject.getString("apo_compila"));
	objConfig.setAtual(jsObject.getString("apo_atual"));
	
	//percorrendo os appservers
	JsonArray jsArray = jsObject.getJsonArray("app_diretorios");
	for (JsonValue vlrAtual : jsArray){
	  objConfig.addReg(((JsonObject) vlrAtual).getString("dir"));
	}
      }
      //se acontecer alguma falha, mostra ela
      catch(Exception ex) {
	System.out.println("Excecao causada na leitura do JSON:");
	ex.printStackTrace();
      }
    }
    else
      geraArqJSON();
    
    //atualiza as variaveis
    compila = objConfig.getCompila();
    atual = objConfig.getAtual();
    novo = "";
    lista = objConfig.getList();
    
    //label de apo de compilacao
    lbCompila = new JLabel("APO Compilacao:");
    lbCompila.setBounds(05, 10, 120, 20);
    add(lbCompila);
    
    //textfield para armazenar o diretorio de compilacao
    jtCompila = new JTextField(compila);
    jtCompila.setBounds(130, 05, 300, 30);
    add(jtCompila);
    
    //label do apo atual
    lbAtual = new JLabel("APO Atual:");
    lbAtual.setBounds(05, 40, 120, 20);
    add(lbAtual);
    
    //textfield para atual
    jtAtual = new JTextField(atual);
    jtAtual.setBounds(130, 35, 300, 30);
    add(jtAtual);
    
    //label do apo novo
    lbNovo = new JLabel("APO Novo:");
    lbNovo.setBounds(05, 70, 120, 20);
    add(lbNovo);
    
    //textfield para atual
    jtNovo = new JTextField("");
    jtNovo.setBounds(130, 65, 300, 30);
    add(jtNovo);
    
    //Criando os models das tabelas
    confApps    = new TabelaDad(getApps(), false);
    
    //criando a tabela
    JTable tbApps = new JTable();
    tbApps.setModel(confApps);
    tbApps.setAutoCreateRowSorter(true); 
    tbApps.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tbApps.getColumnModel().getColumn(0).setPreferredWidth(100);
    tbApps.getColumnModel().getColumn(1).setPreferredWidth(450);
    tbApps.setShowHorizontalLines(true);
	
    //Criando a barra de rolagem e mostrando a tabela
    JScrollPane spApps=new JScrollPane(tbApps, verScroll, horScroll);
    spApps.setViewportView(tbApps);
    spApps.setBounds(05,95,510,240);
    add(spApps);
    
    //botao de incluir
    btnInc = new JButton("+");
    btnInc.setBounds(530, 95, 60, 30);
    btnInc.setToolTipText("Inclui um novo AppServer");
    btnInc.addActionListener(this);
    add(btnInc);
    
    //botao de remover
    btnRem = new JButton("-");
    btnRem.setBounds(530, 125, 60, 30);
    btnRem.setToolTipText("Remove um AppServer");
    btnRem.addActionListener(this);
    add(btnRem);
    
    //label de processamento
    lbProces = new JLabel("");
    lbProces.setBounds(05, 345, 250, 20);
    add(lbProces);
    
    //botao de processar
    btnProc = new JButton("Processar");
    btnProc.setBounds(340, 340, 120, 30);
    btnProc.setForeground(Color.BLUE);
    btnProc.setToolTipText("Processa a atualizacao");
    btnProc.setMnemonic('P');
    btnProc.addActionListener(this);
    add(btnProc);
    
    //botao de sair
    btnSair = new JButton("Sair");
    btnSair.setBounds(470, 340, 120, 30);
    btnSair.setForeground(Color.RED);
    btnSair.setToolTipText("Encerra o programa!");
    btnSair.setMnemonic('S');
    btnSair.addActionListener(this);
    add(btnSair);
    
    //Definindo o tema da janela
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	if ("Nimbus".equals(info.getName())) {
	  UIManager.setLookAndFeel(info.getClassName());
	  SwingUtilities.updateComponentTreeUI(this);    
	  this.pack();
	  break;
	}
      }
    } catch (Exception e) {
    }
  }
 
  //clique nos objetos
  public void actionPerformed(ActionEvent evento){
    int resposta;
    String auxiliar = "";
    
    //se vier do botao incluir
    if (evento.getSource() == btnInc){
      auxiliar = new Input("Insira um novo caminho de AppServer:", icone, "").getInput();
      objConfig.addReg(auxiliar);
      lista = objConfig.getList();
      atuApps();
    }
    
    //se vier do botao incluir
    if (evento.getSource() == btnRem){
      auxiliar = new Input("Insira a sequencia a ser removida:", icone, "-1").getInput();
      objConfig.remReg(Integer.parseInt(auxiliar));
      lista = objConfig.getList();
      atuApps();
    }
    
    //se vier do sair ou do processar, atualiza o JSON
    if ((evento.getSource() == btnSair) || (evento.getSource() ==  btnProc))
      atualizaJSON();
    
    //se vier do botao processar
    if (evento.getSource() ==  btnProc){
      //desabilita os botoes
      btnSair.setEnabled(false);
      btnProc.setEnabled(false);
      btnInc.setEnabled(false);
      btnRem.setEnabled(false);
      lbProces.setText("Iniciando processamento...");
      lbProces.setForeground(Color.BLACK);
      
      //chama a atualizacao
      changeRepositorio();
      
      //habilita os botoes
      btnSair.setEnabled(true);
      btnProc.setEnabled(true);
      btnInc.setEnabled(true);
      btnRem.setEnabled(true);
    }
    else {
      lbProces.setText("");
      lbProces.setForeground(Color.BLACK);
    }
    
    //se vier do botao sair, mostra pergunta se deseja sair
    if (evento.getSource() == btnSair) {
      resposta = new Pergunta("Deseja fechar a janela?", "Atencao", icone).getResposta(); //JOptionPane.showConfirmDialog(null, "Quer mesmo sair?", "Continuar", 0);
      if (resposta == 0)
	super.dispose();
    }
  }
  
  //funcao que atualiza o arquivo JSON
  private void atualizaJSON(){
    String novoAux = jtNovo.getText().trim();
    
    //Atualiza o objeto
    objConfig.setCompila(jtCompila.getText().trim());
    if (!(novoAux.equals("")))
      objConfig.setAtual(novoAux);
    else
      objConfig.setAtual(jtAtual.getText().trim());
    
    //gera o arquivo JSON
    geraArqJSON();
  }
  
  //funcao que gera o arquivo JSON
  private void geraArqJSON(){
    String strJSON = objConfig.getJSONString();
    
    //tenta formatar uma saída
    try {
      Formatter saida = new Formatter("config.json");
      saida.format(strJSON);
      saida.close();
    }
    catch (Exception ex) {
      System.out.println("Excecao causada na criacao do arquivo do JSON:");
      ex.printStackTrace();
    }
  }

  //funcao para criar lista encadeada da tabela
  private List<Dados> getApps(){
    //declarando a lista encadeada
    List<Dados> dadosAux = new ArrayList<Dados>();
    int atual = 0;
    
    //adicionando item a lista
    for (atual = 0; atual < lista.size(); atual++){
      dadosAux.add(new Dados(atual+1, lista.get(atual)));
    }
    
    return dadosAux;
  }
  
  //funcao que atualiza a tabela
  private void atuApps(){
    //declarando a lista encadeada
    List<Dados> dadosAux = new ArrayList<Dados>();
    int atual = 0;
    
    //remove todas as linhas
    confApps.onRemoveAll();
    
    //adicionando item a lista
    for (atual = 0; atual < lista.size(); atual++){
      dadosAux.add(new Dados(atual+1, lista.get(atual)));
    }
    
    //adiciona todos os itens
    confApps.onAddAll(dadosAux);
  }
  
  //funcao para mudanca do repositorio
  private void changeRepositorio(){
    Boolean continua = true;
    int appAtu = 0;
    int arqAtu = 0;
    compila = jtCompila.getText().trim();
    atual = jtAtual.getText().trim();
    novo = jtNovo.getText().trim();
    lista = objConfig.getList();
    
    //testando os campos
    if (compila.isEmpty()) {
      lbProces.setText("APO Compilacao em branco...");
      lbProces.setForeground(Color.RED);
      continua = false;
    }
    
    if (atual.isEmpty()) {
      lbProces.setText("APO Atual em branco...");
      lbProces.setForeground(Color.RED);
      continua = false;
    }
    
    if (novo.isEmpty()) {
      lbProces.setText("APO Novo em branco...");
      lbProces.setForeground(Color.RED);
      continua = false;
    }
    
    if (lista.isEmpty()) {
      lbProces.setText("Lista de AppServers em branco...");
      lbProces.setForeground(Color.RED);
      continua = false;
    }
    
    //se tudo estiver ok
    if (continua){
      //copia tudo do compilacao criando esse diretorio novo
      File origem = new File(compila);
      File destino = new File(novo);
      
      try {
	lbProces.setText("Copiando APO Compilacao para Novo...");
	lbProces.setForeground(Color.BLACK);
	copyDirectory(origem, destino);
      }
      catch (IOException ex) {
	System.out.println("Excecao ocorrida ao copiar RPO: ");
	ex.printStackTrace();
      }
	
      //percorre a lista encadeada
      for (appAtu = 0; appAtu < lista.size(); appAtu++){
	lbProces.setText("Copiando AppServer "+Integer.toString(appAtu+1)+"...");
	lbProces.setForeground(Color.BLACK);
	
	//pega do diretorio do AppServer
	File appDir = new File(lista.get(appAtu));
	
	//Se for diretorio
	if (appDir.isDirectory()) {
	  String[] arquivos = appDir.list();
	  
	  //percorre os arquivos
	  for (arqAtu = 0; arqAtu < arquivos.length; arqAtu++){
	    //Se tiver o texto appserver.ini
	    if (arquivos[arqAtu].contains("appserver.ini")){
	      try {
		File fileEdit = new File(appDir, arquivos[arqAtu]);
		BufferedReader reader = new BufferedReader(new FileReader(fileEdit));
		String linAtu = "", textoAntigo = "", textoNovo = "";
		
		//pega o texto antigo
		while((linAtu = reader.readLine()) != null)
		{
		  textoAntigo += linAtu + "\r\n";
		}
		reader.close();
		
		//substitui o conteudo
		//if (novo.contains("\\"))
		//  textoNovo = textoAntigo.replaceAll("\\", "\\\\");
		textoNovo = textoAntigo.replace(atual, novo);

		//cria o texto novo
		FileWriter writer = new FileWriter(fileEdit);
		writer.write(textoNovo);
		writer.close();
	      }
	      catch (IOException ioe)
	      {
		System.out.println("Excecao ocorrida ao sobrepor appserver.ini: ");
		ioe.printStackTrace();
	      }
	    }
	  }
	}
      }
      
      lbProces.setText("Processamento finalizado...");
      lbProces.setForeground(Color.BLUE);
    }
  }
  
  //funcao para copiar diretorio
  public void copyDirectory(File sourceLocation , File targetLocation)
  throws IOException {
    //se for diretorio
    if (sourceLocation.isDirectory()) {
      //se o diretorio destino nao existir, cria a pasta
      if (!targetLocation.exists()) {
	targetLocation.mkdir();
      }

      //pega os arquivos dentro da origem
      String[] children = sourceLocation.list();
      
      //faz um for nos arquivos e inclui
      for (int i=0; i < children.length; i++) {
	copyDirectory(new File(sourceLocation, children[i]),
	  new File(targetLocation, children[i]));
      }
    }
    else {
      //pega a origem e a saida
      InputStream in = new FileInputStream(sourceLocation);
      OutputStream out = new FileOutputStream(targetLocation);

      //definindo os dados para copia
      byte[] buf = new byte[1024];
      int len;
      
      //Enquanto houver buffer
      while ((len = in.read(buf)) > 0) {
	out.write(buf, 0, len);
      }
      
      //Fecha o arquivo de origem / destino
      in.close();
      out.close();
    }
  }
  
  //programa principal
  public static void main(String arg[]){
    PChangeRPO tela = new PChangeRPO();
    tela.setSize(600,400);
    tela.setLocationRelativeTo(null);
    tela.setTitle("PChangeRPO - Troca de RPO a quente do Protheus");
    tela.getContentPane().setBackground(Color.WHITE);
    tela.setResizable(false);
    tela.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
    tela.setIconImage(icone.getImage());
    tela.setVisible(true);
  }
}