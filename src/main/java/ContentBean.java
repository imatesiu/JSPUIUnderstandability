


import java.io.Serializable;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import eu.learnpad.verification.plugin.bpmn.guideline.factory.GuidelinesFactory;






@ManagedBean(name="ContentBean")
@SessionScoped
public class ContentBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2574895928017048791L;

	private int id;
	private String Content;
	private String restid;
	private String filecontent;
	private String filename;
	private String status;
	private GuidelinesFactory guidelinesfactory;
	private UploadedFile file;
	 
    
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }








	public ContentBean(){

	}

	public void handleFileUpload(FileUploadEvent event) {

		if(event.getFile() != null) {

			filecontent = 	new String(event.getFile().getContents());

			filename = event.getFile().getFileName();
		}
	}




	public GuidelinesFactory getGuidelinesfactory() {
		return guidelinesfactory;
	}






	public void setGuidelinesfactory(GuidelinesFactory guidelinefactory) {
		this.guidelinesfactory = guidelinefactory;
	}






	public String getFilename() {
		return filename;
	}












	public String getFilecontent() {
		return filecontent;
	}


	public void setFilecontent(String filecontent) {
		this.filecontent = filecontent;
	}


	public String getRestid() {
		return restid;
	}



	public void setRestid(String restid) {
		this.restid = restid;
	}




	public String getContent() {
		return Content;
	}



	public void setContent(String content) {
		Content = content;
	}

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}




	public void submitButton(ActionEvent event) {

		if(filecontent!=null){

			Client client = ClientBuilder.newClient();
			WebTarget target = client.target("http://localhost:8080").path("verification-component-understandability-plugin/validatemodel/put");


			Entity<String> entity = Entity.entity(filecontent,MediaType.TEXT_PLAIN);

			Response response =  target.request(MediaType.TEXT_PLAIN).post(entity);

			String id = response.readEntity(String.class);

			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getRequestMap().put("rest", id);

			//context.getApplication().evaluateExpressionGet(context, "#{ContentAnalysisBean.setId("+id+")}", String.class);

			this.setRestid(id);

			System.out.println("Submit Clicked: " + Content + ", " + id + "; ");


			target = client.target("http://localhost:8080").path("verification-component-understandability-plugin/validatemodel/"+id+"/status");
			String 	status ="";
			while (!status.equals("OK")) {


				status = target.request().get(String.class);

				this.setStatus(status);
			}
			System.out.println("Status: "+status);


			target = client.target("http://localhost:8080").path("verification-component-understandability-plugin/validatemodel/"+id);
			Response response2 =  target.request().get();

			GuidelinesFactory glres = response2.readEntity(GuidelinesFactory.class);
			this.setGuidelinesfactory(glres);
			// context = FacesContext.getCurrentInstance();
			//context.getExternalContext().getRequestMap().put("guidelinefactory", glres);

			//GuidelinesFactory res = context.
			//getApplication().
			//evaluateExpressionGet(context, "#{ContentAnalysis.guidelinefactory}", GuidelinesFactory.class);



			System.out.println("Guideline: "+this.getGuidelinesfactory());
			//this.setCollectionannotatedcontent(
			//annotatecontent.readEntity(
			//new GenericType<Collection<AnnotatedCollaborativeContentAnalysis>>() {}));



		}
	}
}
