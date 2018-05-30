import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Main
{
	private static HashMap<String, String> cookies = new HashMap<String, String>();

	public static void main(String[] args)
	{
		System.setProperty("http.agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36\"");

		setCookies("__cfduid=d1703217e006731a58a49ddf35258d5091521036624;"); // Pas forcèment besoin mais au cas où

		HttpsURLConnection.setFollowRedirects(false);

		// ICI T'AS LE DROIT DE MODIFIER

		String[][] post = new String[][] {{"user", "username"}, {"password", "mypass"}};
		String resp = "";
		try
		{
			resp = HttpHeaderSender.sendPostRequest(new URL("http://etrenak.esy.es/testpost.php"), post, false);
		}catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		System.out.println("---");
		System.out.println(resp);
	
	}

	public static void setCookies(String cookie)
	{
		String key = cookie.split("=")[0];
		String value = cookie.split("=")[1].split(";")[0];
		cookies.put(key, value);
	}

	public static String getCookies()
	{
		String cookie = "";
		for(String key : cookies.keySet())
			cookie += key + "=" + cookies.get(key) + "; ";

		return cookie.substring(0, cookie.length() - 2);
	}
}
