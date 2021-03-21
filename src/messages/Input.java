//pacote que essa classe pertence
package messages;

//bibliotecas
import javax.swing.*;

//classe publica de pergunta
public class Input
{	 
	private Object textInput;
	private String retDefault;

	//funcao responsavel por montar a pergunta com o texto, titulo e icone
	public Input(String msgDialog, ImageIcon img, String vlrDefault)	{
		retDefault = vlrDefault;

		//criando a janela
		JDialog dialog = new JDialog();
		dialog.setIconImage(img.getImage());

		//mostrando a janela
		textInput = JOptionPane.showInputDialog(dialog, msgDialog, "");
		dialog.dispose();
	}

	//metodo que retorna a opcao
	public String getInput(){
		String retorno = "";

		if (textInput == null || textInput.equals(""))
			retorno = retDefault;
		else
			retorno = this.textInput.toString();

		return retorno;
	}
} 
