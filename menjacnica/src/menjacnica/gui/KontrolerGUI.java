package menjacnica.gui;

import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
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
	
	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(null,
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak",
				JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	public static void sacuvajUFajl() {
		try {
			if(menjacnica.vratiKursnuListu().isEmpty())
				throw new RuntimeException("Kursna lista je prazna!");
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
				KontrolerGUI.prikazValuta();
			}	
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor.getContentPane(), e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static LinkedList<Valuta> vratiKursnuListu() {
		return menjacnica.vratiKursnuListu();
	}
	
	public static void obrisiValutuKontroler(Valuta valuta){
		menjacnica.obrisiValutu(valuta);
	}

	public static void dodajValutuKontroler(String naziv,String skraceniNaziv,
			int sifra,String prodajni, String kupovni,String srednji) {
		try {
			Valuta novaValuta = new Valuta();
			novaValuta.setNaziv(naziv);
			novaValuta.setSkraceniNaziv(skraceniNaziv);
			novaValuta.setSifra(sifra);
			novaValuta.setSrednji(Double.parseDouble(srednji));
			novaValuta.setKupovni(Double.parseDouble(kupovni));
			novaValuta.setProdajni(Double.parseDouble(prodajni));
			
			menjacnica.dodajValutu(novaValuta);
			prikazValuta();
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(dodajKursProzor, "Pogresno uneti podaci o valuti!",
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziDodajKursGUIKontroler() {
			if(dodajKursProzor == null){
				dodajKursProzor = new DodajKursGUI();
				dodajKursProzor.setLocationRelativeTo(null);
				dodajKursProzor.setVisible(true);
			}else {
				JOptionPane.showMessageDialog(glavniProzor, "Dodavanje drugog kursa u toku!",
						"Upozorenje",JOptionPane.WARNING_MESSAGE);
			}
	}

	public static void prikaziIzvrsiZamenuGUIKontroler() {
		if (glavniProzor.getTable().getSelectedRow() != -1) {
			if(izvrsiZamenuProzor == null){
				izvrsiZamenuProzor = new IzvrsiZamenuGUI();
				ispisiPodatkeOValutiUIzvrsiZamenuGUI();
				izvrsiZamenuProzor.setLocationRelativeTo(null);
				izvrsiZamenuProzor.setVisible(true);
			}else {
				JOptionPane.showMessageDialog(glavniProzor, "Druga transakcija u toku!",
						"Upozorenje",JOptionPane.WARNING_MESSAGE);
			}
			
		}
		else {
			JOptionPane.showMessageDialog(glavniProzor, "Odaberite valutu za transakciju.",
					"Upozorenje", JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	public static void pokreniObrisiKursProzor(){
		if (glavniProzor.getTable().getSelectedRow() != -1) {
			if(obrisiKursProzor == null){
				obrisiKursProzor = new ObrisiKursGUI();
				ispisiPodatkeOValutuUObrisiKursGUI();
				obrisiKursProzor.setVisible(true);
				obrisiKursProzor.setLocationRelativeTo(glavniProzor);
				obrisiKursProzor.toFront();
			}else {
				JOptionPane.showMessageDialog(glavniProzor, "Brisanje druge valute u toku."
						,"Upozorenje",JOptionPane.WARNING_MESSAGE);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Nije izabran red za brisaje!",
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private static void ispisiPodatkeOValutiUIzvrsiZamenuGUI() {
		Valuta valuta = vratiSelektovanuValutu();
		izvrsiZamenuProzor.getTextFieldProdajniKurs().setText(""+valuta.getProdajni());
		izvrsiZamenuProzor.getTextFieldKupovniKurs().setText(""+valuta.getKupovni());
		izvrsiZamenuProzor.getTextFieldValuta().setText(""+valuta.getSkraceniNaziv());
		
	}

	public static Valuta vratiSelektovanuValutu() {
		MenjacnicaTableModel model = (MenjacnicaTableModel)(glavniProzor.getTable().getModel());
		return model.vratiValutu(glavniProzor.getTable().getSelectedRow());
	}

	public static String izvrsiZamenu(Valuta valuta, boolean prodaja, String iznos) {
		double iznosZaTransakciju = Double.parseDouble(iznos);
		DecimalFormat df = new DecimalFormat("#.##");
		double iznosPosleTransakcije = menjacnica.izvrsiTransakciju(valuta, prodaja, iznosZaTransakciju);
		return df.format(iznosPosleTransakcije);
	}

	public static void prikazValuta() {
		MenjacnicaTableModel model = (MenjacnicaTableModel)(glavniProzor.getTable().getModel());
		model.osveziTabelu(vratiKursnuListu());
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(glavniProzor,
				"Autor: Aleksandar Colic 224/14, Verzija 1.1", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void ispisiPodatkeOValutuUObrisiKursGUI(){
		Valuta v = vratiSelektovanuValutu();
		obrisiKursProzor.getTextFieldNaziv().setText(v.getNaziv());
		obrisiKursProzor.getTextFieldSkraceniNaziv().setText(v.getSkraceniNaziv());
		obrisiKursProzor.getTextFieldSifra().setText(Integer.toString(v.getSifra()));
		obrisiKursProzor.getTextFieldProdajniKurs().setText(Double.toString(v.getProdajni()));
		obrisiKursProzor.getTextFieldSrednjiKurs().setText(Double.toString(v.getSrednji()));
		obrisiKursProzor.getTextFieldKupovniKurs().setText(Double.toString(v.getKupovni()));
	}

	public static void ugasiObrisiKursGUI() {
		obrisiKursProzor.dispose();
		obrisiKursProzor = null;
	}

	public static void obrisi() {
		try{
			obrisiValutuKontroler(vratiSelektovanuValutu());
			prikazValuta();
			ugasiObrisiKursGUI();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(glavniProzor, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void izvrsiZamenuGUIKontroler() {
		
			try{
				String iznos = izvrsiZamenuProzor.getTextFieldIznos().getText();
				boolean prodaja = izvrsiZamenuProzor.getRdbtnProdaja().isSelected();
				String konacanIznos = izvrsiZamenu(vratiSelektovanuValutu(),
						prodaja, iznos);
				
				izvrsiZamenuProzor.getTextFieldKonacniIznos().setText(konacanIznos);
				
			}catch(Exception e){
				JOptionPane.showMessageDialog(izvrsiZamenuProzor, "Pogresno unet iznos!",
						"Greska", JOptionPane.ERROR_MESSAGE);
			}		
	}

	public static void ugasiIzvrsiZamenu() {
		izvrsiZamenuProzor.dispose();
		izvrsiZamenuProzor = null;
	}

	public static void ugasiDodajKursGUI() {
		dodajKursProzor.dispose();
		dodajKursProzor = null;
	}
	
}
