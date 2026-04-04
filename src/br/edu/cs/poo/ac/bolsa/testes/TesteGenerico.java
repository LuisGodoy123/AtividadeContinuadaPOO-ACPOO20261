package br.edu.cs.poo.ac.bolsa.testes;

import java.io.File;

class TesteGenerico {
	void limparDiretorio(String nomeDiretorio) {
        String fileSep = System.getProperty("file.separator");
        File dir = new File("." + fileSep + nomeDiretorio);
        File[] arqs = dir.listFiles();
        if (arqs != null) {
	        for (File file : arqs) {
				file.delete();
			} 
        }
	}
}