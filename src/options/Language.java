package options;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class Language implements Serializable {

	final private Locale ENGLISH = Locale.ENGLISH;
	final private Locale BULGARIAN = new Locale("bg");
	private Locale langLocale;
	
	public Language() {
		langLocale = ENGLISH;
	}
	
	public Locale getLangLocale() {
		return langLocale;
	}
	
	public void setEnglishOutput() {
		langLocale = ENGLISH;
	}
	
	public void setBulgarianOutput() {
		langLocale = BULGARIAN;
	}
	
	public void setEnglishLocale(ActionEvent event) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(ENGLISH);
		setEnglishOutput();
	}
	
	public void setBulgarianLocale(ActionEvent event) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(BULGARIAN);
		setBulgarianOutput();
	}
	
}
