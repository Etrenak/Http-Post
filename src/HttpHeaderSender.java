
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHeaderSender
{

	public static HttpURLConnection prepareRequest(URL url)
	{
		try
		{
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
			con.setRequestProperty("cookie", Main.getCookies());

			return con;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static String sendRequest(URL url, String[][] datas, boolean setCookies, File responseFile, boolean post)
	{
		String response = "";
		try
		{
			HttpURLConnection con = prepareRequest(url);

			if(post && datas != null)
			{
				String encodedDatas = "";
				for(int i = 0; i < datas.length; i++)
					encodedDatas += (i != 0 ? "&" : "") + URLEncoder.encode(datas[i][0], "UTF-8") + "=" + URLEncoder.encode(datas[i][1], "UTF-8");

				con.setRequestMethod("POST");
				con.getOutputStream().write(encodedDatas.getBytes());
				System.out.println(encodedDatas);
			}

			if(setCookies)
			{
				con.getInputStream();
				setCookies(con);
			}

			{
				BufferedInputStream reader = new BufferedInputStream(con.getInputStream());
				BufferedOutputStream writer = null;
				if(responseFile != null)
					writer = new BufferedOutputStream(new FileOutputStream(responseFile));
				ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int read;

				while((read = reader.read(buffer)) >= 0)
				{
					if(responseFile != null)
						writer.write(buffer, 0, read);

					stringWriter.write(buffer, 0, read);
				}
				response = stringWriter.toString();
				if(responseFile != null)
				{
					writer.flush();
					writer.close();
				}
				stringWriter.close();
				reader.close();
			}

		}catch(Exception e)
		{
			e.printStackTrace();
		}

		return response;
	}

	public static String sendPostRequest(URL url, String[][] datas, boolean setCookies, File response)
	{
		return sendRequest(url, datas, setCookies, response, true);
	}

	public static String sendPostRequest(URL url, String[][] datas, boolean setCookies)
	{
		return sendRequest(url, datas, setCookies, null, true);
	}

	public static String sendRequest(URL url, File response)
	{
		return sendRequest(url, null, false, response, false);
	}

	public static String sendRequest(URL url)
	{
		return sendRequest(url, null, false, null, false);
	}

	private static void setCookies(HttpURLConnection con)
	{
		if(con.getHeaderFields().containsKey("Set-Cookie"))
			for(String s : con.getHeaderFields().get("Set-Cookie"))
				Main.setCookies(s);
	}
}
