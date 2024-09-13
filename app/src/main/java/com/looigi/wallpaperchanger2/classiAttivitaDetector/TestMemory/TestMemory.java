package com.looigi.wallpaperchanger2.classiAttivitaDetector.TestMemory;

public class TestMemory {

    public DatiMemoria LeggeValori() {
    	DatiMemoria d = new DatiMemoria();
    	
        int mb = 1024;
    	Runtime runtime = Runtime.getRuntime();
    	d.setMemoriaLibera(RitornaMemoriaLibera(runtime, mb));
    	d.setMemoriaUsata(RitornaMemoriaUsata(runtime, mb));
    	d.setMemoriaTotale(RitornaMemoriaTotale(runtime, mb));
    	d.setMemoriaMassima(RitornaMemoriaMassima(runtime, mb));
    	
		return d;
    }
    
    private float RitornaMemoriaUsata(Runtime runtime, int mb) {
    	float Usata = ((float) runtime.totalMemory() - (float) runtime.freeMemory())/mb;
    	long us=(long) (Usata * 100);
        Usata=(float) us/100;
        
        return Usata;
    }
    
    private float RitornaMemoriaLibera(Runtime runtime, int mb) {
    	float Libera = ((float) runtime.freeMemory()) / mb;
    	long us=(long) (Libera * 100);
    	Libera=(float) us/100;
        
        return Libera;
    }
    
    private float RitornaMemoriaTotale(Runtime runtime, int mb) {
    	float Totale = ((float) runtime.totalMemory()) / mb;
    	long us=(long) (Totale * 100);
    	Totale=(float) us/100;
        
        return Totale;
    }
 
    
    private float RitornaMemoriaMassima(Runtime runtime, int mb) {
        float Massima = ((float) runtime.maxMemory()) / mb;
    	long us=(long) (Massima * 100);
    	Massima=(float) us/100;
        
        return Massima;
    }
}