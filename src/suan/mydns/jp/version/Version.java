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
			URL url = new URL(adress);//URL��ݒ�
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();//�T�C�g�ɐڑ�
			connect.setRequestMethod("GET");//�v���g�R���̐ݒ�
			connect.connect();
			// EUC-JP �Ń��[�_�[���쐬
			InputStreamReader isr = new InputStreamReader(connect.getInputStream(), "UTF-8");
			// �s�P�ʂœǂݍ��ވׂ̏���
			BufferedReader br = new BufferedReader(isr);
			String line_buffer;
			// BufferedReader �́AreadLine �� null ��Ԃ��Ɠǂݍ��ݏI��
			while ( null != (line_buffer = br.readLine() ) )
			{
				// �R�}���h�v�����v�g�ɕ\��
				//System.out.println(line_buffer);
				//if(line_buffer.indexOf("<article class=\"markdown-body entry-content\" itemprop=\"text\"><p>") > 0) str = line_buffer.substring(line_buffer.indexOf("<article class=\"markdown-body entry-content\" itemprop=\"text\"><p>") + 64, line_buffer.indexOf("</p>"));
				str = line_buffer;
			}

			// �e�X�󂯎����N���X�����
			br.close();
			isr.close();
			connect.disconnect();
		}
		catch (Exception e)
		{
			// TODO �����������ꂽ catch �u���b�N
			System.err.println("Version Check Failed. " + e.getMessage());
		}

		return str;
	}
}
