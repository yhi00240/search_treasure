package algorithm;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.*;
 

///////

/*
 * ������ ��Ÿ�� �ܾ�  ���� 10�� ���� + ������ ���� 0�� �͵� original �������� �����ϱ�
 * ������ ��Ÿ�� �ܾ�  ���� 10�� ���� + ������ ������ �Ѱ��� ��Ÿ�� ���� ���� �ؽ�Ʈ ������ ����� original ���������� �����ϱ�
 * (�ܾ� ���� ������ ���� �پ�����. ���� ��� "���R���R "�̷� �ܾ�� ������ �������� 1���ۿ� �ȳ���. -> ����ڰ� �̻��ϰ� ģ �ܾ��)
 * �� 10�� ������ �͵��� �����ߴ���?
 * "���R���R " + "�ȳ�"
 * "�ȳ�"�̶�� �ܾ�� ���� �������� 2043517��.
 * ���߿� ������ �ܾ� ���� ������ ������ ���, "���R���R"�� �ʿ����.
 * �ֳ�? �˻� �������� �ִ�� �����ִ� ���������� 10����. 10����, �ٸ� �������� ���Ե��� �ʾҴٴ� �Ҹ���. �� �����ص� ������
 * ����, ������ ��Ÿ�� �ܾ� ������ 10�� ������ ��� + ������ ������ 1���� ��Ÿ����
 * �װ͵鸸 ��Ƽ� ���� text������ �����. �̰͵� ����������, �˻� �������� �ִ�� �����ִ� ���������� 10����.
 * "�ȳ�"�� �����Ұ��, original ���������� �����ϰ� �ٸ� �ܾ�� �����ϴ� ���� �ξ� �� ȿ������.
 * ���� �ؽ�Ʈ ������ ���� ���� form data�� ������, �ܾ� ���� ������ ���� �پ���, ���� �������� �þ��.
 * ( �ܾ� ������ �ʿ����(������ ��Ÿ�� �ܾ� ���� 10�� ����+������ ���� 0���ΰ͵�) �ܾ ������ )
 *  */

public class xml_step1 {
 
    public static void main(String[] args) {
 
        try{
 
            new xml_step1().start();
 
        }catch (Exception e){
            e.printStackTrace();
        }
 
    }
 
    private void start() throws Exception{
    	
    	String subURL = null;
    	
    	String inputName="C:/Users/jihyun/Desktop/input/termList50.txt";
    	File inputFile = new File(inputName);
    	
    	FileReader fileReader = new FileReader(inputFile);
    	BufferedReader reader = new BufferedReader(fileReader);
    	
    	String outputName="C:/Users/jihyun/Desktop/input/output1.txt";
    	File outputFile=new File(outputName);
    	
    	FileWriter fileWriter = new FileWriter(outputFile);
    	BufferedWriter writer = new BufferedWriter(fileWriter);
      	
    	while( (subURL = reader.readLine()) != null )
    	{
    		String tmpSubURL="";
    		tmpSubURL+=subURL;
    		XPath xpath = XPathFactory.newInstance().newXPath();
    		
    		String curUrl = "http://treasure.navercorp.com:8080/nx.search?query=";
    		subURL = URLEncoder.encode(subURL,"UTF-8");
        	curUrl = curUrl + subURL;
        	
            URL url = new URL("http://treasure.navercorp.com:8080/nx.search?query="+subURL);
            URLConnection connection = url.openConnection();
     
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(curUrl);
            NodeList descNodes = doc.getElementsByTagName("section");
     
            int total=0;
            int treasure_total=0;
            System.out.print(tmpSubURL+" ");
            
            NodeList totalNode = (NodeList)xpath.evaluate("//section/total", doc, XPathConstants.NODESET);
            for( int idx=0; idx<totalNode.getLength(); idx++ ){
                 total = Integer.parseInt(totalNode.item(idx).getTextContent());
            }
            System.out.print(total+" ");
            
            // ������ NodeList �������� : item �Ʒ��� �ִ� ��� treasure�� ����
            NodeList treasures = (NodeList)xpath.evaluate("//item/treasure", doc, XPathConstants.NODESET);
            for( int idx=0; idx<treasures.getLength(); idx++ ){
                treasure_total = treasure_total+ Integer.parseInt(treasures.item(idx).getTextContent());
            }
     
            System.out.println(treasure_total);
           
            if(total<=10)
            {
            	if(treasure_total==0) // ���� ������ 10�����ϰ�, ������ �ϳ��� ������! �ܾ��忡�� ���ֱ�
            	{	
            		//�ܾ��忡�� ���شٴ� ���� �ƹ��͵� ���� ����.
            	}
            	else // bruteForce�� ���� �ʴ� �ٰ� ����������(������ ��ġ�� ������), ������ �Ѱ��̻� ������ ���������� ���� �� �������, �ܾ��忡�� ���ֱ�(���� �ܰ���ʹ� �ʿ����)
            	{
            		//�ܾ��忡�� ���شٴ� ���� �ƹ��͵� ���� ����.
            		//���������� ���� ���� �ױ�
            	}
            }
            else if(total<=1000) // ��Ÿ���� ���������� 11~1000��, ������������ ��ʸ�, ��鸸�� �ܾ ���ؼ� ��������� �� ��������. 
            					 // ������ �ǽð����� �ܾ������ ���ջ����� ���� ����ڿ��� ����������ϱ����� �ܾ������ �����ϴ� ���� ȿ�������� ����.
            					 // if(total<=10)�� ���� �����ؼ� �ִ´�. 
            {
            }
            else // ���������� �ܾ� ������ �ִ´�.
            {
            	writer.write(tmpSubURL);
            	writer.append("\n");
            }
            
            //////// ���� ������ 10�����ϰ�, ������ �ϳ��� ������! ���� ���񽺷� ������ 
    	}
    	writer.close();
    }
 
    private Document parseXML(InputStream stream) throws Exception{
 
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
 
        try{
 
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
 
            doc = objDocumentBuilder.parse(stream);
 
        }catch(Exception ex){
            throw ex;
        }       
 
        return doc;
    }
 
}
