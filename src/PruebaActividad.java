import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;

class Resultado {
	String v[];
	private int agregarIndice;
	private static Random GENERADOR = new Random();

	public Resultado(int tam) {
		v = new String[tam];

		for (int i = 0; i < v.length; i++) {
			insertarDato();
		}

	}

	public void insertarDato() {
		int posicion = agregarIndice;

		if (posicion < v.length) {

			if (GENERADOR.nextInt(2) == 1)
				v[posicion] = "SI";
			else
				v[posicion] = "NO";
		}

		agregarIndice++;
	}

	@Override
	public String toString() {
		return "Resultado [v=" + Arrays.toString(v) + "]";
	}
}

@SuppressWarnings("serial")
class VentanaInicio extends JFrame implements ActionListener {

	GridBagLayout gbl = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	JTextArea j1, j2;
	JButton iniciar;
	JProgressBar jp;

	Resultado r1 = new Resultado(50000);

	public VentanaInicio() {

		getContentPane().setLayout(gbl);

		gbc.insets = new Insets(5, 10, 5, 10);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Ventana Principal");
		setSize(960, 620);
		setLocationRelativeTo(null);
		setVisible(true);
		gbc.fill = GridBagConstraints.BOTH;

		iniciar = new JButton("Iniciar");

		j1 = new JTextArea(10, 20);
		j2 = new JTextArea(10, 20);

		j1.setLineWrap(true);
		j1.setWrapStyleWord(true);
		j2.setLineWrap(true);
		j2.setWrapStyleWord(true);

		j1.setEditable(false);
		j2.setEditable(false);

		JScrollPane s1 = new JScrollPane(j1);
		JScrollPane s2 = new JScrollPane(j2);

		jp = new JProgressBar();

		alinearComponente(new JLabel("Analisis encuesta"), 0, 0, 1, 1);
		alinearComponente(s1, 0, 1, 1, 1);
		alinearComponente(s2, 1, 1, 1, 1);

		alinearComponente(iniciar, 0, 2, 2, 1);

		alinearComponente(jp, 0, 3, 2, 1);

		iniciar.addActionListener(this);
		pack();

	}

	public void alinearComponente(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;

		gbc.gridwidth = w;
		gbc.gridheight = h;

		gbl.setConstraints(c, gbc);
		add(c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == iniciar) {
			j1.setText("");
			j2.setText("");

			Thread h1 = new Thread(new EscribirVector(r1));

			Thread h2 = new Thread(new Progreso());
			h2.setPriority(1);
			h1.setPriority(5);
			h1.start();
			h2.start();

		}
	}

	class EscribirVector implements Runnable {

		Resultado r1;

		public EscribirVector(Resultado r1) {
			super();
			this.r1 = r1;
		}

		public void escribir() {

			for (int i = 0; i < r1.v.length; i++) {

				if (r1.v[i].equals("SI"))
					j1.append("SI\n");
				else
					j2.append("NO\n");

			}

		}

		@Override
		public void run() {
			iniciar.setEnabled(false);
			escribir();
			iniciar.setEnabled(true);

		}

	}

	class Progreso implements Runnable {

		@Override
		public void run() {
			iniciar.setEnabled(false);
			while ((j1.getLineCount() + j2.getLineCount()) < r1.v.length) {
				jp.setValue(((j1.getLineCount() + j2.getLineCount()) * 100) / r1.v.length);

			}

			jp.setValue(100);

			iniciar.setEnabled(true);

		}

	}

}

public class PruebaActividad {
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new VentanaInicio();

			}
		});
	}

}