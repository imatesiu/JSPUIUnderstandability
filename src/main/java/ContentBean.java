

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;





@ManagedBean(name="ContentBean")
@RequestScoped
public class ContentBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2574895928017048791L;
	private String Title;
	private int id;
	private String Content;
	private String restid;
	private String filecontent;
	private String filename;





	public ContentBean(){

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

	public void sampleListener(FileEntryEvent e) {
		FileEntry fe = (FileEntry)e.getComponent();
		FileEntryResults results = fe.getResults();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			System.out.println(fileInfo);


			if (fileInfo.isSaved()) {
				// Process the file. Only save cloned copies of results or fileInfo,StandardCharsets.UTF_8

				Path path = Paths.get(fileInfo.getFile().getAbsolutePath());
				try {
					filecontent = 	new String(Files.readAllBytes(path), "UTF8");
					filename = fileInfo.getFileName();
					System.out.println(filecontent);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

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
		}
	}
}