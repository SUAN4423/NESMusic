package suan.mydns.jp.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import suan.mydns.jp.Thoone;

public class SaveLoad
{
	private long[] time = {0, 0};
	private boolean[] pressed = {false, false};

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/6*5+1280/12, 0, 1280/12, 720/16))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.pressed[0])
			{
				this.Save(th);
				this.pressed[0] = true;
			}
			else if(!th.kmState.MLeft && this.pressed[0])
			{
				this.pressed[0] = false;
			}
		}
		th.rect(1280/6*5+1280/12, 0, 1280/12, 720/16);
		th.fill(0xFF);
		if(th.kmState.IsMouseIn(1280/6*5+1280/12, 720/16, 1280/12, 720/16))
		{
			th.fill(0xFF, 0x00, 0x00);
			if(th.kmState.MLeft && !this.pressed[1])
			{
				this.Load(th);
				this.pressed[1] = true;
			}
			else if(!th.kmState.MLeft && this.pressed[1])
			{
				this.pressed[1] = false;
			}
		}
		th.rect(1280/6*5+1280/12, 720/16, 1280/12, 720/16);
		th.fill(0);
		th.textSize(20);
		if(System.currentTimeMillis() - this.time[0] < 1000) th.text("SAVE OK", 1280/6*5+1280/12+10, 25);
		else th.text("SAVE", 1280/6*5+1280/12+10, 25);
		if(System.currentTimeMillis() - this.time[1] < 1000) th.text("LOAD OK", 1280/6*5+1280/12+10, 720/16+25);
		else th.text("LOAD", 1280/6*5+1280/12+10, 720/16+25);
	}

	private void Save(Thoone th)
	{
		FileWriter fw2 = null;
		try
		{
			fw2 = new FileWriter("./MUSIC.thh");
		}
		catch (IOException e)
		{
			try
			{
				Files.createFile(Paths.get("./MUSIC.thh"));
				fw2 = new FileWriter("./MUSIC.thh");
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		this.write(fw2, th);
		this.time[0] = System.currentTimeMillis();
	}

	private void write(FileWriter file, Thoone th)
	{
		try
		{
			String str = "";
			for(int i = 0; i < 4; i++)
			{
				str += th.SPT[i].Tempo + "\r\n";
			}
			for(int i = 0; i < 4; i++)
			{
				for(int j = 0; j < th.SPT[i].Volume.size(); j++)
				{
					str += i + "," + th.SPT[i].Volume.get(j) + "," + th.SPT[i].Freque.get(j) + "," + th.SPT[i].FrequI.get(j) + "," + th.SPT[i].Time.get(j) + "," + th.SPT[i].SoundT.get(j) + "," + th.SPT[i].Duty.get(j) + "," + th.SPT[i].Voldow.get(j) + "," + th.SPT[i].Fredow.get(j) + "\r\n";
				}
			}
			file.write(str);
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void Load(Thoone th)
	{
		File file = new File("./MUSIC.thh");
		BufferedReader br = null;
		String str = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
			str = br.readLine();
			for(int i = 0; i < 4; i++)
			{
				th.SPT[i].Tempo = Double.parseDouble(str);
				str = br.readLine();
			}
			th.state.TempoSet(th, th.SPT[0].Tempo);

			while(str != null)
			{
				int a = Integer.parseInt(str.substring(0, str.indexOf(",")));
				int b = str.indexOf(",");
				int c = Integer.parseInt(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double d = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				int e = Integer.parseInt(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				long f = Long.parseLong(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				int g = Integer.parseInt(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double h = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double i = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double j = Double.parseDouble(str.substring(b+1));

				th.SPT[a].Volume.add((byte)c);
				th.SPT[a].Freque.add(d);
				th.SPT[a].FrequI.add(e);
				th.SPT[a].Time.add(f);
				th.SPT[a].SoundT.add(g);
				th.SPT[a].Duty.add((float)h);
				th.SPT[a].Voldow.add(i);
				th.SPT[a].Fredow.add(j);

				str = br.readLine();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.time[1] = System.currentTimeMillis();
	}

	public void Load(Thoone th, String path)
	{
		File file = new File(path);
		BufferedReader br = null;
		String str = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
			str = br.readLine();
			for(int i = 0; i < 4; i++)
			{
				th.SPT[i].Tempo = Double.parseDouble(str);
				str = br.readLine();
			}
			th.state.TempoSet(th, th.SPT[0].Tempo);

			for(int i = 0; i < 4; i++)
			{
				th.SPT[i].Volume.clear();
				th.SPT[i].Freque.clear();
				th.SPT[i].FrequI.clear();
				th.SPT[i].Time.clear();
				th.SPT[i].SoundT.clear();
				th.SPT[i].Duty.clear();
				th.SPT[i].Voldow.clear();
				th.SPT[i].Fredow.clear();
			}

			while(str != null)
			{
				int a = Integer.parseInt(str.substring(0, str.indexOf(",")));
				int b = str.indexOf(",");
				int c = Integer.parseInt(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double d = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				int e = Integer.parseInt(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				long f = Long.parseLong(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				int g = Integer.parseInt(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double h = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double i = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
				b = str.indexOf(",", b+1);
				double j = Double.parseDouble(str.substring(b+1));

				th.SPT[a].Volume.add((byte)c);
				th.SPT[a].Freque.add(d);
				th.SPT[a].FrequI.add(e);
				th.SPT[a].Time.add(f);
				th.SPT[a].SoundT.add(g);
				th.SPT[a].Duty.add((float)h);
				th.SPT[a].Voldow.add(i);
				th.SPT[a].Fredow.add(j);

				str = br.readLine();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.time[1] = System.currentTimeMillis();
	}
}
