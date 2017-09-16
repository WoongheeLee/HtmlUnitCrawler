package crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class huCrawler {
	// ACCOUNT INFORMATION
	private static String account_email = "EMAIL";
	private static String account_password = "PASSWORD";
	
	private WebClient webClient = null;
	
	private void init() {
		webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		
		webClient.waitForBackgroundJavaScript(1000);
	}
	
	
	private void loginTwitter() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		final HtmlPage page = webClient.getPage("https://mobile.twitter.com/session/new");
		final HtmlForm form = page.getForms().get(0);
		form.reset();
		
		HtmlTextInput username = (HtmlTextInput) form.getInputByName("session[username_or_email]");
		username.setValueAttribute(account_email);
		
		HtmlPasswordInput password = (HtmlPasswordInput) form.getInputByName("session[password]");
		password.setValueAttribute(account_password);
		
		HtmlInput button = form.getInputByName("commit");
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		HtmlPage p = button.click();
	}
	
	private void getFollowing() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		final HtmlPage page = webClient.getPage("https://mobile.twitter.com/WoongheeLee2/following");
		
		List<?> following = page.getByXPath("//div[@class='user-list']//table[@class='user-item']//td[@class='info']//a");
		for (int i = 0; i < following.size(); i++) {
			String str = following.get(i).toString().substring(21, following.get(i).toString().length()-7);
			System.out.println(str);
		}
	}
	
	private void closePage() {
		webClient.close();
	}
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException  {
		huCrawler crawler = new huCrawler();
		crawler.init();
		crawler.loginTwitter();
		crawler.getFollowing();
		crawler.closePage();
	}
}
