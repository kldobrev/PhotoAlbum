package options;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * 
 * @author kldobrev
 *
 * This class deals with the user's language choice and
 * sets the locale
 */
@ManagedBean
@SessionScoped
public class Language implements Serializable {

	private Locale langLocale;
	
	/**
	 * Sets the default language to english
	 */
	public Language() {
		langLocale = Locale.ENGLISH;
	}
	
	public Locale getLangLocale() {
		return langLocale;
	}
	
	/**
	 * Sets the language by a template parameter named 'language'
	 * 
	 * @param event
	 */
	public void setLangLocale(ActionEvent event) {
		String lang = (String) event.getComponent().getAttributes().get("language");
		langLocale = new Locale(lang);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(langLocale);
	}
	
}
