package menjacnica.gui;

import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;
import sistemskeoperacije.SODodajValutu;
import sistemskeoperacije.SOObrisiValutu;
import sistemskeoperacije.SOSacuvajUFajl;
import sistemskeoperacije.SOUcitajIzFajla;

public class KontrolerGUI {

	private static MenjacnicaGUI glavniProzor;
	private static ObrisiKursGUI obrisiKursProzor;
	private static DodajKursGUI dodajKursProzor;
	private static IzvrsiZamenuGUI izvrsiZamenuProzor;
	private static MenjacnicaInterface menjacnica;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menjacnica = new Menjacnica();
					glavniProzor = new MenjacnicaGUI();
					glavniProzor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void pokreniObrisiKursProzor(){
		
		obrisiKursProzor = new ObrisiKursGUI();
		obrisiKursProzor.setVisible(true);
		obrisiKursProzor.setLocationRelativeTo(null);
	}
	
	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(null,
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(glavniProzor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnica.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(glavniProzor.getContentPane());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnica.ucitajIzFajla(file.getAbsolutePath());
				KontrolerGUI.prikaziSveValuteKontroler();
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static LinkedList<Valuta> vratiKursnuListu() {
		return menjacnica.vratiKursnuListu();
	}
	
	public static void prikaziValutuKontroler() {
		obrisiKursProzor.prikaziValutu();
	}
	
	public static void obrisiValutuKontroler(Valuta valuta){
		menjacnica.obrisiValutu(valuta);
	}

	public static void dodajValutuKontroler(Valuta valuta) {
		menjacnica.dodajValutu(valuta);
	}
	
	public static void prikaziSveValuteKontroler(){
		glavniProzor.prikaziSveValute();
	}

	public static void prikaziDodajKursGUIKontroler() {
		dodajKursProzor = new DodajKursGUI();
		dodajKursProzor.setLocationRelativeTo(null);
		dodajKursProzor.setVisible(true);
	}

	public static void prikaziIzvrsiZamenuGUIKontroler(Valuta valuta) {
		izvrsiZamenuProzor = new IzvrsiZamenuGUI(valuta);
		izvrsiZamenuProzor.setLocationRelativeTo(null);
		izvrsiZamenuProzor.setVisible(true);
		
	}

	public static Valuta vratiSelektovanuValutu() {
		return glavniProzor.vratiSelektovanuValutu();
	}
	
}
