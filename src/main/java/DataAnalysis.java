
import java.io.Serializable;
import java.io.StringWriter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import eu.learnpad.verification.plugin.bpmn.guideline.factory.GuidelinesFactory;

@ManagedBean(name="DataAnalysis")
@SessionScoped
public class DataAnalysis implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4802290213871735048L;

	private GuidelinesFactory guidelinesfactory;

	public DataAnalysis(){
		
		System.out.println("DataAnalysisbean "+guidelinesfactory);
	//	listdata = new ArrayList<DataContent>();
	}



	/*public DataContentAnalysis(AnnotatedCollaborativeContentAnalysis acca) {
		this.acca = acca;
		listdata = new ArrayList<DataContent>();
		createData();
	}*/



	



	public void setGuidelinesfactory(GuidelinesFactory guidelinesfactory) {
		this.guidelinesfactory = guidelinesfactory;
	}



	public String getXml() {

		JAXBContext jaxbCtx;
		StringWriter sw = new StringWriter();
		if(guidelinesfactory!=null){
			try {
				jaxbCtx = javax.xml.bind.JAXBContext.newInstance(GuidelinesFactory.class);

				Marshaller marshaller = jaxbCtx.createMarshaller();
				marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
				marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(guidelinesfactory, sw);


			} catch (JAXBException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return sw.toString();
	}



	/*public void setXml(String xml) {
		this.xml = xml;
	}



	public String getElement() {
		return element;
	}

	
	
	

	public String executeListener(){

		return "";
	}

	public void listener(ActionEvent event){
		//this.setAcca((AnnotatedCollaborativeContentAnalysis) event.getComponent().getAttributes().get("valdata"));
		System.out.println(event);
	}*/

}
