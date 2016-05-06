package sistemskeoperacije;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import menjacnica.Valuta;

public class SOUcitajIzFajla {

	public static void izvrsi(String absolutePath, LinkedList<Valuta> staraKursnaLista) {
		
		try {
			ObjectInputStream in = new ObjectInputStream(
					new BufferedInputStream(
							new FileInputStream(absolutePath)));
			
			LinkedList<Valuta> novaKursnaLista = (LinkedList<Valuta>)in.readObject();
			staraKursnaLista.clear();
			staraKursnaLista.addAll(novaKursnaLista);
			
			in.close();
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Fajl nije pronadjen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Ne postoji lista kurseva u datom fajlu!");
		}
		
	}

}
