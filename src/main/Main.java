/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
//import services.InstanceService;
import utils.Constant;
import xbrlcore.constants.NamespaceConstants;
import xbrlcore.exception.XBRLException;
import xbrlcore.instance.*;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DTSFactory;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;

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


public class Main {

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
        
    public static void main(String[] args) {
        convertToXBRL("C:\\Users\\Bio\\Documents\\tarik_mandiri_syariah.txt");
    }
    
    public static void convertToXBRL(String file_name) {
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
        String CompanyName = "Bank Mandiri Syariah";
        DTSFactory ivFactory = DTSFactory.get();
        try {
            /**
            * membaca taxonomy yang sudah ada
            */
            DiscoverableTaxonomySet dts = ivFactory.createTaxonomy(new File("C:\\Users\\Bio\\Downloads\\taxonomy_icm\\icm-bs-2006-07-31.xsd"));
            Set<DiscoverableTaxonomySet> setDts = new HashSet<>();
            setDts.add(dts);
    
            /**
            * create Instance
            */
            Instance instance = new Instance(setDts);
            instance.addNamespace(NamespaceConstants.XSI_NAMESPACE);
            instance.addNamespace(NamespaceConstants.LINK_NAMESPACE);
            instance.addNamespace(NamespaceConstants.XLINK_NAMESPACE);
            instance.addNamespace(Constant.NameSpace.ICM_BS_NAMESPACE);
            instance.addNamespace(Constant.NameSpace.REF_NAMESPACE);
            instance.addNamespace(Constant.NameSpace.ICM_BS_A_NAMESPACE);
            instance.addNamespace(NamespaceConstants.ISO4217_NAMESPACE);
            instance.addNamespace(NamespaceConstants.XSI_NAMESPACE);
    
            instance.addSchemaLocation("icm-bs-2006-07-31.xsd","xbrl-bs");
            instance.addSchemaLocation("icm-bs-2006-07-31-audited.xsd","xbrl-audited");
            /* create InstanceUnit
            */
            InstanceUnit instanceUnit = new InstanceUnit("U-IDR");
            instanceUnit.setValue("IDR");
            instanceUnit.setNamespaceURI(NamespaceConstants.ISO4217_NAMESPACE.getURI());
            instance.addUnit(instanceUnit);
    
            /**
            * create InstanceContext
            * Prior_AsOf_Audited
            */
            InstanceContext icPrior_AsOf_Audited = new InstanceContext("Prior_AsOf_Audited");
            icPrior_AsOf_Audited.setIdentifier("Alpha");
            icPrior_AsOf_Audited.setIdentifierScheme(Constant.IDENTIFIER_SCHEMA);
            icPrior_AsOf_Audited.setPeriodValue("2004-12-31");
            //create Scenario
            Element element = new Element("Audited", Constant.NameSpace.ICM_BS_A_NAMESPACE);
            icPrior_AsOf_Audited.addScenarioElement(element);
            instance.addContext(icPrior_AsOf_Audited);
    
            /**
            * create InstanceContext
            * Current_Period_Audited
            */
            InstanceContext icCurrent_Period_Audited = new InstanceContext("Current_Period_Audited");
            icCurrent_Period_Audited.setIdentifier("Alpha");
            icCurrent_Period_Audited.setIdentifierScheme(Constant.IDENTIFIER_SCHEMA);
            icCurrent_Period_Audited.setPeriodStartDate("2005-01-01");
            icCurrent_Period_Audited.setPeriodEndDate("2005-12-31");
            //create Scenario
            Element element1 = new Element("Audited",Constant.NameSpace.ICM_BS_A_NAMESPACE);
            icCurrent_Period_Audited.addScenarioElement(element1);
            instance.addContext(icCurrent_Period_Audited);
    
    
            /**
            * create InstanceContext
            * Current_AsOf_Audited
            */
            InstanceContext icCurrent_AsOf_Audited = new InstanceContext("Current_AsOf_Audited");
            icCurrent_AsOf_Audited.setIdentifier("Alpha");
            icCurrent_AsOf_Audited.setIdentifierScheme(Constant.IDENTIFIER_SCHEMA);
            icCurrent_AsOf_Audited.setPeriodValue("2005-12-31");
            //create Scenario
            Element element2 = new Element("Audited",Constant.NameSpace.ICM_BS_A_NAMESPACE);
            icCurrent_AsOf_Audited.addScenarioElement(element2);
            instance.addContext(icCurrent_AsOf_Audited);
    
            /**
            * create Fact
            */
            Main main = new Main();
    
            /**
            * Fact CompanyName
            */
            Fact factCompanyName = new Fact(main.findConceptById("icm-bs_CompanyName", setDts));
            factCompanyName.setValue(CompanyName);
            factCompanyName.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factCompanyName);
    
            /**
            * Fact ListedCompanyCode
            */
            Fact factListedCompanyCode = new Fact(main.findConceptById("icm-bs_ListedCompanyCode", setDts));
            factListedCompanyCode.setValue("ONEC");
            factListedCompanyCode.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factListedCompanyCode);
    
            /**
            * Fact Current_Period_Audited
            */
            Fact factIndustry = new Fact(main.findConceptById("icm-bs_Industry", setDts));
            factIndustry.setValue("Perbankan");
            factIndustry.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factIndustry);
    
            /**
            * Fact MainOperation
            */
            Fact factMainOperation = new Fact(main.findConceptById("icm-bs_MainOperation", setDts));
            factMainOperation.setValue("Heavy Industry");
            factMainOperation.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factMainOperation);
    
            /**
            * Fact AddresLine1
            */
            Fact factAddressLine1 = new Fact(main.findConceptById("icm-bs_AddressLine1", setDts));
            factAddressLine1.setValue(cabang);
            factAddressLine1.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factAddressLine1);
    
            /**
    
            /**
            * Fact AddressLine3
            */
            Fact factAddressLine3 = new Fact(main.findConceptById("icm-bs_AddressLine3", setDts));
            factAddressLine3.setValue("London");
            factAddressLine3.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factAddressLine3);
    
            /**
            * Fact AddressLine4
            */
            Fact factAddressLine4 = new Fact(main.findConceptById("icm-bs_AddressLine4", setDts));
            factAddressLine4.setValue("England");
            factAddressLine4.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factAddressLine4);
    
            /**
            *Fact ReportingType
            */
            Fact factReportingType = new Fact(main.findConceptById("icm-bs_ReportingType", setDts));
            factReportingType.setValue("Semi-annual");
            factReportingType.setInstanceContext(icCurrent_Period_Audited);
            instance.addFact(factReportingType);
    
    
            /**
            * menyimpan instance
            */
            InstanceOutputter instanceOutputter = new InstanceOutputter(instance);
            Document instanceXML = instanceOutputter.getXML();
            /* outputting XML */
            XMLOutputter serializer = new XMLOutputter();
            Format f = Format.getPrettyFormat();
            f.setOmitDeclaration(false);
            serializer.setFormat(f);
            OutputStream os = new FileOutputStream("D:\\test2.xbrl");
            serializer.output(instanceXML, os);

        } catch (IOException e) {
        e.printStackTrace();
        } catch (XBRLException e) {
        e.printStackTrace();
        } catch (JDOMException e) {
        e.printStackTrace();
        }
    }

    public Concept findConceptById(String conceptId, Set<DiscoverableTaxonomySet> dts) {
    Concept concept = null;
    Iterator<DiscoverableTaxonomySet> iterator = dts.iterator();
    while (iterator.hasNext()) {
    DiscoverableTaxonomySet next = iterator.next();
    concept = next.getConceptByID(conceptId);
    if (concept != null)
    break;
    }
    return concept;
    }

}
/**
 *
 * @author Bio
 */

