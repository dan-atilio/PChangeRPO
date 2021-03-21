//pacote que essa classe pertence
package messages;

//bibliotecas
import javax.swing.*;

//classe publica de pergunta
public class Question
{	 
	private int opcao = 1;

	//criando um vetor de string contendo as opcoes
	public static String[] sOpcoes = new String[] { "Sim", "Nao" };

	//funcao responsavel por montar a pergunta com o texto, titulo e icone
	public Question(String textoDialog, String tituloDialog, ImageIcon img)	{	
		//criando o JOptionPane
		JOptionPane jop = new JOptionPane(
				textoDialog,
				JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION,
				null,
				sOpcoes,
				sOpcoes[0]
				);

		//criando a janela
		JDialog dialog = jop.createDialog(tituloDialog);

		//Mostrando a janela
		dialog.setIconImage(img.getImage());
		dialog.setVisible(true);
		dialog.dispose();

		//Se a opcao desejada for Sim, a opcao sera 0
		if (jop.getValue() == "Sim")
			opcao = 0;
	}

	//metodo que retorna a opcao
	public int getResposta(){
		return this.opcao;
	}
} 
