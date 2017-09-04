/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Bio
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.*;		
import java.util.regex.*;
import java.nio.charset.*;
import java.nio.file.*;

class AmbilData {
    private String path;
    static String cabang  		= "";
    static String tanggal 		= "";
    static String nmr_rekening  	= "";
    static String nama_rekening  	= "";
    static String jmlh_ditarik  	= "";
    static String[] ont_cabang		= {"cabang", "cabang branch","kantor cabang"};
    static String[] ont_tanggal 	= {"tanggal","date"};
    static String[] ont_nmr_rekening    = {"nomor rekening","aasa"};
    static String[] ont_nama_rekening 	= {"nama pemilik rekening","asasa"};
    static String[] ont_jmlh_ditarik 	= {"jumlah penarikan","jumlah"};

    static ArrayList<String> readLines(String path) throws IOException{
        Charset charset = Charset.forName("UTF-8");
        Path path2 = FileSystems.getDefault().getPath(path);
        BufferedReader bf = Files.newBufferedReader(path2, charset);/*new BufferedReader(file_to_read);*/

        String aLine;
        ArrayList<String> list = new ArrayList<String>();

        while (( aLine = bf.readLine()) != null){
            byte[] b = aLine.getBytes("UTF-8");
            aLine = new String(b, "UTF-8");

            aLine = aLine.trim();

            if (!aLine.equals("")) {    
                list.add(aLine);
            }
        }
        bf.close();
        return new ArrayList<String>(list);
    }

	public static void getCabang(String word) {
            for (String  prefix: ont_cabang) {
		if (word.startsWith(prefix)) {
                    cabang = word.substring(prefix.length() + 1);
                }
            }
	}

	public static void getTanggal(String word) {
            for (String  prefix: ont_tanggal) {
                if (word.startsWith(prefix)) {
                    tanggal = word.substring(prefix.length() + 1);
                }
            }
	}

	public static void getNmrRekening(String word) {
            for (String  prefix: ont_nmr_rekening) {
		if (word.startsWith(prefix)) {
                    nmr_rekening = word.substring(prefix.length() + 1);
		}
            }
	}

	public static void getNamaRekening(String word) {
            for (String  prefix: ont_nama_rekening) {
                    if (word.startsWith(prefix)) {
                    nama_rekening = word.substring(prefix.length() + 1);
		}
            }
	}

	public static void getJmlhDitarik(String word) {
            for (String  prefix: ont_jmlh_ditarik) {
                if (word.startsWith(prefix)) {
                    jmlh_ditarik = word.substring(prefix.length() + 1);
		}
            }
	}
          public static void getDataNasabah(String file_name) {
//        System.out.println("Hellod!");
//        String file_name = "D:/KULIAH/SKRIPSI/Program/txt/tarik dki.txt";
            try{
                ArrayList<String> aryLines = readLines(file_name);
                ArrayList<String> words = new ArrayList<String>();

                for (int i = 0; i < aryLines.size() ; i++) {
                aryLines.set(i, aryLines.get(i).toLowerCase().replaceAll("\t", " "));

                String[] wordsLine = aryLines.get(i).split("  ");
                    for (int j = 0; j < wordsLine.length; j++) {
                        if (!wordsLine[j].trim().equals("")) {
                            words.add(wordsLine[j].trim());
                        }
                    }
                }
			
                for (String word : words) {
                    getCabang(word);
                    getTanggal(word);
                    getNamaRekening(word);
                    getNmrRekening(word);
                    getJmlhDitarik(word);
                }
                System.out.println(cabang);
                System.out.println(tanggal);
                System.out.println(nama_rekening);
                System.out.println(nmr_rekening);
                System.out.println(jmlh_ditarik);
            }
        catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
