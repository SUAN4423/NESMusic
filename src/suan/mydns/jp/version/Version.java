package suan.mydns.jp.version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version
{
	public String VirsionCheck()
	{
		String str = null;
		try
		{
			String adress="https://raw.githubusercontent.com/SUAN4423/NESMusic/master/version2.md";
			URL url = new URL(adress);//URLを設定
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
			connect.setRequestMethod("GET");//プロトコルの設定
			connect.connect();
			// EUC-JP でリーダーを作成
			InputStreamReader isr = new InputStreamReader(connect.getInputStream(), "UTF-8");
			// 行単位で読み込む為の準備
			BufferedReader br = new BufferedReader(isr);
			String line_buffer;
			// BufferedReader は、readLine が null を返すと読み込み終了
			while ( null != (line_buffer = br.readLine() ) )
			{
				// コマンドプロンプトに表示
				//System.out.println(line_buffer);
				//if(line_buffer.indexOf("<article class=\"markdown-body entry-content\" itemprop=\"text\"><p>") > 0) str = line_buffer.substring(line_buffer.indexOf("<article class=\"markdown-body entry-content\" itemprop=\"text\"><p>") + 64, line_buffer.indexOf("</p>"));
				str = line_buffer;
			}

			// 各々受け持ちクラスを閉じる
			br.close();
			isr.close();
			connect.disconnect();
		}
		catch (Exception e)
		{
			// TODO 自動生成された catch ブロック
			System.err.println("Version Check Failed. " + e.getMessage());
		}

		return str;
	}
}
