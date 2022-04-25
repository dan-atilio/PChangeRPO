//bibliotecas
import javax.json.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import javax.swing.UIManager.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import config.*;
import messages.*;

//Classe PChangeRPO
class PChangeRPO extends JFrame implements ActionListener, PropertyChangeListener
{
	private static final long serialVersionUID = 1L;
	private JButton btnQuit, btnAbout, btnConfirm, btnExport, btnImport;
	private JButton btnNew, btnRemove;
	private JButton btnCompile, btnCurrent, btnNewApo;
	private JButton btnViewLog;
	private JLabel lbCompile, lbCurrent, lbNew, lbProces;
	private JTextField jtCompile, jtCurrent, jtNew;
	private static ImageIcon appIcon = new ImageIcon("images/pchangerpo.png");
	private Config objConfig = new Config();
	private String strCompile = "";
	private String strCurrent = "";
	private String strNew = "";
	private String fileName = "log.csv";
	private String messageLog = "";
	private List <String> strList = new ArrayList<String>();
	private int verScroll = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS; 
	private int horScroll = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
	//private JTable tbApps;
	private TableData confApps;
	//private JScrollPane spApps;
	//private List <Data> listApps;
	private JProgressBar progressBar;
    private Task task;

	//construtor dos componentes
	public PChangeRPO() 
	{
		setLayout(null);

		openJson("config.json", false);

		//label de apo de compilacao
		lbCompile = new JLabel("APO Compilacao:");
		lbCompile.setBounds(05, 10, 120, 20);
		add(lbCompile);

		//textfield para armazenar o diretorio de compilacao
		jtCompile = new JTextField(strCompile);
		jtCompile.setBounds(130, 05, 300, 30);
		add(jtCompile);
		
		//botao de selecionar compilar
		btnCompile = new JButton("...");
		btnCompile.setBounds(435, 05, 30, 30);
		btnCompile.setToolTipText("Seleciona a pasta de compilação");
		btnCompile.addActionListener(this);
		add(btnCompile);

		//label do apo atual
		lbCurrent = new JLabel("APO Atual:");
		lbCurrent.setBounds(05, 40, 120, 20);
		add(lbCurrent);

		//textfield para atual
		jtCurrent = new JTextField(strCurrent);
		jtCurrent.setBounds(130, 35, 300, 30);
		add(jtCurrent);
		
		//botao de selecionar atual
		btnCurrent = new JButton("...");
		btnCurrent.setBounds(435, 35, 30, 30);
		btnCurrent.setToolTipText("Seleciona a pasta atual");
		btnCurrent.addActionListener(this);
		add(btnCurrent);

		//label do apo novo
		lbNew = new JLabel("APO Novo:");
		lbNew.setBounds(05, 70, 120, 20);
		add(lbNew);

		//textfield para atual
		jtNew = new JTextField("");
		jtNew.setBounds(130, 65, 300, 30);
		add(jtNew);
		
		//botao de selecionar atual
		btnNewApo = new JButton("...");
		btnNewApo.setBounds(435, 65, 30, 30);
		btnNewApo.setToolTipText("Seleciona a pasta nova");
		btnNewApo.addActionListener(this);
		add(btnNewApo);

		//Criando os models das tabelas
		confApps		= new TableData(getApps(), false);

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
		btnNew = new JButton("+");
		btnNew.setBounds(530, 95, 60, 30);
		btnNew.setToolTipText("Inclui um novo AppServer");
		btnNew.addActionListener(this);
		add(btnNew);

		//botao de remover
		btnRemove = new JButton("-");
		btnRemove.setBounds(530, 125, 60, 30);
		btnRemove.setToolTipText("Remove um AppServer");
		btnRemove.addActionListener(this);
		add(btnRemove);

		//barra de progresso
		progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBounds(05,340,585,30);
        progressBar.setString("...");
        add(progressBar);
		
		//label de processamento
		lbProces = new JLabel("");
		lbProces.setBounds(05, 365, 250, 20);
		add(lbProces);

		//botao de processar
		btnConfirm = new JButton("Processar");
		btnConfirm.setBounds(05, 380, 120, 50);
		btnConfirm.setForeground(Color.RED);
		btnConfirm.setToolTipText("Processa a atualizacao");
		btnConfirm.setMnemonic('P');
		btnConfirm.addActionListener(this);
		add(btnConfirm);
		
		//botao de exportar
		btnViewLog = new JButton("Visualizar Logs");
		btnViewLog.setBounds(135, 380, 120, 50);
		btnViewLog.setToolTipText("Visualizar arquivos de Log");
		btnViewLog.setMnemonic('L');
		btnViewLog.addActionListener(this);
		add(btnViewLog);
		
		//botao de exportar
		btnExport = new JButton("Salvar config...");
		btnExport.setBounds(350, 380, 120, 23);
		btnExport.setToolTipText("Salvar/Exportar configurações");
		btnExport.setMnemonic('S');
		btnExport.addActionListener(this);
		add(btnExport);
		
		//botao de importar
		btnImport = new JButton("Abrir config...");
		btnImport.setBounds(350, 407, 120, 23);
		btnImport.setToolTipText("Abrir/Importar configurações");
		btnImport.setMnemonic('A');
		btnImport.addActionListener(this);
		add(btnImport);
		
		//botao de sobre
		btnAbout = new JButton("Sobre");
		btnAbout.setBounds(480, 380, 120, 23);
		btnAbout.setToolTipText("Sobre a ferramenta");
		btnAbout.setMnemonic('o');
		btnAbout.addActionListener(this);
		add(btnAbout);

		//botao de sair
		btnQuit = new JButton("Sair");
		btnQuit.setBounds(480, 407, 120, 23);
		btnQuit.setToolTipText("Encerra o programa!");
		btnQuit.setMnemonic('S');
		btnQuit.addActionListener(this);
		add(btnQuit);

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
	
	public void openJson(String fileName, Boolean update) {
		//pegando o arquivo JSON de configuracao
		File configFile = new File(fileName);
		if (configFile.exists()){
			//tenta abrir o arquivo e pegar os atributos do json
			try {
				//carrega o objeto
				FileReader frFile = new FileReader(configFile);
				JsonReader jsReader = Json.createReader(frFile);
				JsonObject jsObject = jsReader.readObject();

				//setando os apos
				objConfig.setCompile(jsObject.getString("apo_compile"));
				objConfig.setCurrent(jsObject.getString("apo_current"));

				//percorrendo os appservers
				JsonArray jsArray = jsObject.getJsonArray("app_directory");
				for (JsonValue currValue : jsArray){
					objConfig.addReg(((JsonObject) currValue).getString("dir"));
				}
				
			}
			//se acontecer alguma falha, mostra ela
			catch(Exception ex) {
				System.out.println("Excecao causada na leitura do JSON:");
				ex.printStackTrace();
			}
		}
		else
			createJsonFile();
		
		//atualiza as variaveis
		strCompile = objConfig.getCompile();
		strCurrent = objConfig.getCurrent();
		strNew = "";
		strList = objConfig.getList();
		
		if (update) {
			jtCompile.setText(strCompile);
			jtCurrent.setText(strCurrent);
			jtNew.setText(strNew);
			updApps();
		}
	}

	//clique nos objetos
	public void actionPerformed(ActionEvent event){
		int answer;
		String strAux = "";
		
		//se vier de qualquer botão para selecionar pastas
		if ((event.getSource() == btnCompile) || (event.getSource() == btnCurrent) || (event.getSource() == btnNewApo)) {
			//monta a tela para seleção da pasta
			JFileChooser chooser = new JFileChooser(); 
		    chooser.setDialogTitle("Selecione a pasta do APO");
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
		    chooser.setCurrentDirectory(new File (System.getProperty("user.home")) );
		    
		    //Se for confirmada a tela
		    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
		    	if (event.getSource() == btnCompile)
		    		jtCompile.setText(chooser.getSelectedFile().toString());
		    	
		    	else if (event.getSource() == btnCurrent)
		    		jtCurrent.setText(chooser.getSelectedFile().toString());
		    	
		    	else if (event.getSource() == btnNewApo)
		    		jtNew.setText(chooser.getSelectedFile().toString());
		    }
		}

		//se vier do botao incluir
		if (event.getSource() == btnNew){
			strAux = new Input("Insira um novo caminho de AppServer:", appIcon, "").getInput();
			objConfig.addReg(strAux);
			strList = objConfig.getList();
			updApps();
		}

		//se vier do botao incluir
		if (event.getSource() == btnRemove){
			strAux = new Input("Insira a sequencia a ser removida:", appIcon, "-1").getInput();
			objConfig.remReg(Integer.parseInt(strAux));
			strList = objConfig.getList();
			updApps();
		}

		//se vier do sair ou do processar, atualiza o JSON
		if ((event.getSource() == btnQuit) || (event.getSource() ==	btnConfirm))
			updateJsonFile();

		//se vier do botao processar
		if (event.getSource() ==	btnConfirm){
			//desabilita os botoes
			btnQuit.setEnabled(false);
			btnConfirm.setEnabled(false);
			btnNew.setEnabled(false);
			btnRemove.setEnabled(false);
			lbProces.setText("Iniciando processamento...");
			lbProces.setForeground(Color.BLACK);
			
			/* Pega uma descrição para colocar no log */
			messageLog = new Input("Insira um texto de log da operacao:", appIcon, "[atualizacao]").getInput();
			
			//chama a tarefa
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            task = new Task();
            task.addPropertyChangeListener((PropertyChangeListener) this);
            task.execute();
            
            //habilita os botoes
			btnQuit.setEnabled(true);
			btnConfirm.setEnabled(true);
			btnNew.setEnabled(true);
			btnRemove.setEnabled(true);
		}
		else {
			lbProces.setText("");
			lbProces.setForeground(Color.BLACK);
		}
		
		//botão de salvar
		if (event.getSource() == btnExport) {
			//monta a tela para seleção da pasta
			JFileChooser chooser = new JFileChooser(); 
		    chooser.setDialogTitle("Selecione a pasta para salvar");
		    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos JSON", "json");
		    chooser.setFileFilter(filter);
		    chooser.setCurrentDirectory(new File (System.getProperty("user.home")) );
		    
		    //Se for confirmada a tela
		    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { 
		    	File file = chooser.getSelectedFile();
		    	
		    	FileWriter writer;
				try {
					writer = new FileWriter(file);
					writer.write(objConfig.getJSONString());
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		
		//botão de abrir
		if (event.getSource() == btnImport) {
			//monta a tela para seleção da pasta
			JFileChooser chooser = new JFileChooser(); 
		    chooser.setDialogTitle("Selecione o arquivo JSON para abrir");
		    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos JSON", "json");
		    chooser.setFileFilter(filter);
		    chooser.setCurrentDirectory(new File (System.getProperty("user.home")) );
		    
		  //Se for confirmada a tela
		    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
		    	File file = chooser.getSelectedFile();
		    	openJson(file.toString(), true);
		    }
		}
		
		//Se vier do botão de sobre
		if (event.getSource() == btnAbout) {
			String aboutMessage = "";
			aboutMessage += "PChangeRPO versão 1.2b\n";
			aboutMessage += "Revisão em Abril de 2022\n";
			aboutMessage += "Projeto open source, desenvolvido utilizando Java\n\n";
			aboutMessage += "Deseja abrir o nosso site com mais informações?";
			
			answer = new Question(aboutMessage, "Sobre", appIcon).getResposta();
			if (answer == 0) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				    try {
						Desktop.getDesktop().browse(new URI("https://atiliosistemas.com/portfolio/pchangerpo/"));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		/* se vier do botão para abrir o arquivo de log */
		if (event.getSource() == btnViewLog) {
			try {
				Desktop.getDesktop().open(new File(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//se vier do botao sair, mostra pergunta se deseja sair
		if (event.getSource() == btnQuit) {
			answer = new Question("Deseja fechar a janela?", "Atencao", appIcon).getResposta(); //JOptionPane.showConfirmDialog(null, "Quer mesmo sair?", "Continuar", 0);
			if (answer == 0)
				super.dispose();
		}
	}

	//funcao que atualiza o arquivo JSON
	private void updateJsonFile(){
		String strNewAux = jtNew.getText().trim();

		//Atualiza o objeto
		objConfig.setCompile(jtCompile.getText().trim());
		if (!(strNewAux.equals("")))
			objConfig.setCurrent(strNewAux);
		else
			objConfig.setCurrent(jtCurrent.getText().trim());

		//gera o arquivo JSON
		createJsonFile();
	}

	//funcao que gera o arquivo JSON
	private void createJsonFile(){
		String strJSON = objConfig.getJSONString();

		//tenta formatar uma saída
		try {
			Formatter output = new Formatter("config.json");
			output.format(strJSON);
			output.close();
		}
		catch (Exception ex) {
			System.out.println("Excecao causada na criacao do arquivo do JSON:");
			ex.printStackTrace();
		}
	}

	//funcao para criar lista encadeada da tabela
	private List<Data> getApps(){
		//declarando a lista encadeada
		List<Data> dataAux = new ArrayList<Data>();
		int currentApp = 0;

		//adicionando item a lista
		for (currentApp = 0; currentApp < strList.size(); currentApp++){
			dataAux.add(new Data(currentApp+1, strList.get(currentApp)));
		}

		return dataAux;
	}

	//funcao que atualiza a tabela
	private void updApps(){
		//declarando a lista encadeada
		List<Data> dataAux = new ArrayList<Data>();
		int currentApp = 0;

		//remove todas as linhas
		confApps.onRemoveAll();

		//adicionando item a lista
		for (currentApp = 0; currentApp < strList.size(); currentApp++){
			dataAux.add(new Data(currentApp+1, strList.get(currentApp)));
		}

		//adiciona todos os itens
		confApps.onAddAll(dataAux);
	}

	//funcao para mudanca do repositorio
	private void changeReposit(){
		Boolean boolContinue = true;
		int currentApp = 0;
		int currentFile = 0;
		strCompile = jtCompile.getText().trim();
		strCurrent = jtCurrent.getText().trim();
		strNew = jtNew.getText().trim();
		strList = objConfig.getList();

		//testando os campos
		if (strCompile.isEmpty()) {
			lbProces.setText("APO Compilacao em branco...");
			lbProces.setForeground(Color.RED);
			boolContinue = false;
		}

		if (strCurrent.isEmpty()) {
			lbProces.setText("APO Atual em branco...");
			lbProces.setForeground(Color.RED);
			boolContinue = false;
		}

		if (strNew.isEmpty()) {
			lbProces.setText("APO Novo em branco...");
			lbProces.setForeground(Color.RED);
			boolContinue = false;
		}

		if (strList.isEmpty()) {
			lbProces.setText("Lista de AppServers em branco...");
			lbProces.setForeground(Color.RED);
			boolContinue = false;
		}

		//se tudo estiver ok
		if (boolContinue){
			//copia tudo do compilacao criando esse diretorio novo
			File fileOrig = new File(strCompile);
			File fileDest = new File(strNew);

			try {
				lbProces.setText("Copiando APO Compilacao para Novo...");
				lbProces.setForeground(Color.BLACK);
				copyDirectory(fileOrig, fileDest);
			}
			catch (IOException ex) {
				System.out.println("Excecao ocorrida ao copiar RPO: ");
				ex.printStackTrace();
			}

			//percorre a lista encadeada
			for (currentApp = 0; currentApp < strList.size(); currentApp++){
				lbProces.setText("Copiando AppServer "+Integer.toString(currentApp+1)+"...");
				lbProces.setForeground(Color.BLACK);

				//pega do diretorio do AppServer
				File appDir = new File(strList.get(currentApp));

				//Se for diretorio
				if (appDir.isDirectory()) {
					String[] files = appDir.list();

					//percorre os arquivos
					for (currentFile = 0; currentFile < files.length; currentFile++){
						//Se tiver o nome do arquivo tiver appserver e no final tiver .ini
						if (files[currentFile].contains("appserver") && files[currentFile].endsWith(".ini")){
							try {
								File fileEdit = new File(appDir, files[currentFile]);
								BufferedReader reader = new BufferedReader(new FileReader(fileEdit));
								String currentLine = "", oldText = "", newText = "";

								//pega o texto antigo
								while((currentLine = reader.readLine()) != null)
								{
									oldText += currentLine.toLowerCase() + "\r\n";
								}
								reader.close();

								//substitui o conteudo
								//if (novo.contains("\\"))
								//	textoNovo = textoAntigo.replaceAll("\\", "\\\\");
								newText = oldText.replace(strCurrent.toLowerCase(), strNew.toLowerCase());

								//cria o texto novo
								FileWriter writer = new FileWriter(fileEdit);
								writer.write(newText);
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
	
	public void propertyChange(PropertyChangeEvent evt) {
        /* Definindo o valor da barra de progresso na mudanca de propriedade */
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        } 
    }
 
    class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() throws InterruptedException {
        	
            /* Define a barra como 0, e as variaveis de controle */
            setProgress(0);
            int barCurrPercent = 0;
            int barTotalValue = 100;
             
            /* Define a barra como o valor total */
            setProgress(barTotalValue);
            
            /* Define como 30% */
            barCurrPercent = 30;
            setProgress(barCurrPercent);
            progressBar.setString(String.valueOf(barCurrPercent)+" %");
            
            /* Chama a troca de repositório */
            changeReposit();
            
            /* Atualiza o arquivo de logs */
            updateLogFile();
            
            /* Agora define o fim da régua*/
            barCurrPercent = 100;
            setProgress(barCurrPercent);
            progressBar.setString(String.valueOf(barCurrPercent)+" %");
            
            return null;
        }
     
        @Override
        public void done() {
            /* Ao finalizar volta o cursor ao normal */
            Toolkit.getDefaultToolkit().beep();
            setCursor(null);
        }
    }
    
    private void updateLogFile() {
		File log = new File(fileName);
		String headerLine = "";
		String line = "";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String date = sdf.format(c.getTime()).substring(0, 10); 
		String time = sdf.format(c.getTime()).substring(11);
		
		try {
			/* Se o arquivo não existir, cria ele vazio */
			if (! log.exists()) {
				log.createNewFile();
				headerLine = "Usuario;Data;Hora;APO;Descricao;";
			}
			
			/* Agora faz um append com informações do usuário */
			line  = System.getProperty("user.name") + ";";
			line += date + ";";
			line += time + ";";
			line += jtNew.getText().trim() + ";";
			line += messageLog + ";";
			
			/* Grava no log as linhas */
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			if (! headerLine.isEmpty())
				out.println(headerLine);
		    out.println(line);
		    out.close();
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Não foi possível criar/atualizar o arquivo [" + fileName + "]!\nVerifique se o arquivo não esta aberto em outro software.", "Atenção", 0);
		}
	}

	//programa principal
	public static void main(String arg[]){
		int width = 625;
		int height = 475;
		PChangeRPO frame = new PChangeRPO();
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setTitle("PChangeRPO v1.2b - Troca de RPO a quente do Protheus");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.setIconImage(appIcon.getImage());
		frame.setVisible(true);
	}
}