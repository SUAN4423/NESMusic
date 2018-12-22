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
import suan.mydns.jp.music.MM2;

public class SaveLoad
{
	private long[] time = {0, 0};
	private boolean[] pressed = {false, false};
	private boolean[] saveload = {false, false};

	Thoone th;

	public void Draw(Thoone th)
	{
		th.fill(0xFF);
		if(saveload[0]) th.fill(0x00, 0xFF, 0x00);
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
		if(saveload[1]) th.fill(0x00, 0xFF, 0x00);
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

	FileWriter fw2;

	private void Save(Thoone th)
	{
		saveload[0] = true;
		this.th = th;
		fw2 = null;
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
		Write write = new Write();
		write.start();
		//this.write(fw2, th);
		//this.time[0] = System.currentTimeMillis();
	}

	class Write extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				String str = "";
				for(int i = 0; i < 4; i++)
				{
					if(i == 2) str += th.ch.loopstartnum + "\r\n";
					else str += th.SPT[i].Tempo + "\r\n";
					fw2.write(str);
					str = "";
				}
				for(int i = 0; i < 5; i++)
				{
					for(int j = 0; j < th.SPT[i].Volume.size(); j++)
					{
						str += i + "," + th.SPT[i].Volume.get(j) + "," + th.SPT[i].Freque.get(j) + "," + th.SPT[i].FrequI.get(j) + "," + th.SPT[i].Time.get(j) + "," + th.SPT[i].SoundT.get(j) + "," + th.SPT[i].Duty.get(j) + "," + th.SPT[i].Voldow.get(j) + "," + th.SPT[i].Fredow.get(j) + "," + th.SPT[i].VolDUM.get(j) + "\r\n";
					}
					fw2.write(str);
					str = "";
				}
				for(int i = 0; i < 8; i++)
				{
					if(MM2.DPCMo[i].size() > 0)
					{
						for(int j = 0; j < MM2.DPCMo[i].size(); j++)
						{
							if(j == 0) str += "DPCM" + MM2.DPCMo[i].get(j) + "D" + i + "M";
							else str += MM2.DPCMo[i].get(j);
							fw2.write(str);
							str = "";
						}
						str += "\r\n";
					}
				}
				fw2.write(str);
				fw2.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			time[0] = System.currentTimeMillis();
			saveload[0] = false;
		}
	}
	private void write(FileWriter file, Thoone th)
	{
		try
		{
			String str = "";
			for(int i = 0; i < 4; i++)
			{
				if(i == 2) str += th.ch.loopstartnum + "\r\n";
				else str += th.SPT[i].Tempo + "\r\n";
			}
			for(int i = 0; i < 5; i++)
			{
				for(int j = 0; j < th.SPT[i].Volume.size(); j++)
				{
					str += i + "," + th.SPT[i].Volume.get(j) + "," + th.SPT[i].Freque.get(j) + "," + th.SPT[i].FrequI.get(j) + "," + th.SPT[i].Time.get(j) + "," + th.SPT[i].SoundT.get(j) + "," + th.SPT[i].Duty.get(j) + "," + th.SPT[i].Voldow.get(j) + "," + th.SPT[i].Fredow.get(j) + "," + th.SPT[i].VolDUM.get(j) + "\r\n";
				}
			}
			for(int i = 0; i < 8; i++)
			{
				if(MM2.DPCMo[i].size() > 0)
				{
					for(int j = 0; j < MM2.DPCMo[i].size(); j++)
					{
						if(j == 0) str += "DPCM" + MM2.DPCMo[i].get(j) + "D" + i + "M";
						else str += MM2.DPCMo[i].get(j);
					}
					str += "\r\n";
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
		saveload[1] = true;
		File file = new File("./MUSIC.thh");
		BufferedReader br = null;
		String str = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
			str = br.readLine();
			int loop = 0;
			for(int i = 0; i < 4; i++)
			{
				th.SPT[i].Tempo = Double.parseDouble(str);
				th.SPT[4].Tempo = Double.parseDouble(str);
				if(i == 2 && str.indexOf(".") == -1) loop = Integer.parseInt(str);
				str = br.readLine();
			}
			th.state.TempoSet(th, th.SPT[0].Tempo);

			while(str != null)
			{
				if(str.contains(","))
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
					double j;
					int k;
					try
					{
						j = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
						b = str.indexOf(",", b+1);
						k = Integer.parseInt(str.substring(b+1));
					}
					catch(Exception es)
					{
						j = Double.parseDouble(str.substring(b+1));
						if(i < 0) k = 0;
						else k = 16;
					}

					th.SPT[a].Volume.add((byte)c);
					th.SPT[a].Freque.add(d);
					th.SPT[a].FrequI.add(e);
					th.SPT[a].Time.add(f);
					th.SPT[a].SoundT.add(g);
					th.SPT[a].Duty.add((float)h);
					th.SPT[a].Voldow.add(i);
					th.SPT[a].Fredow.add(j);
					th.SPT[a].VolDUM.add(k);
				}
				else
				{
					int a = str.indexOf("DPCM") + 4;
					int b = str.indexOf("D", a);
					int c = Integer.parseInt(str.substring(a, b));
					int d = Integer.parseInt(str.substring(b + 1, b + 2));
					//System.out.println(a + " " + b + " " + c + " " + d);
					MM2.DPCMo[d].clear();
					MM2.DPCMo[d].add((byte) c);
					for(int i = b + 3; i < str.length(); i++)
					{
						MM2.DPCMo[d].add((byte) Integer.parseInt(str.substring(i, i + 1)));
					}
				}

				str = br.readLine();
			}
			loopsetting(loop, th);
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
		saveload[1] = false;
	}

	public void Load(Thoone th, String path)
	{
		saveload[1] = true;
		File file = new File(path);
		BufferedReader br = null;
		String str = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
			str = br.readLine();
			int loop = 0;
			for(int i = 0; i < 4; i++)
			{
				th.SPT[i].Tempo = Double.parseDouble(str);
				th.SPT[4].Tempo = Double.parseDouble(str);
				if(i == 2 && str.indexOf(".") == -1) loop = Integer.parseInt(str);
				str = br.readLine();
			}
			th.state.TempoSet(th, th.SPT[0].Tempo);

			for(int i = 0; i < 5; i++)
			{
				th.SPT[i].Volume.clear();
				th.SPT[i].Freque.clear();
				th.SPT[i].FrequI.clear();
				th.SPT[i].Time.clear();
				th.SPT[i].SoundT.clear();
				th.SPT[i].Duty.clear();
				th.SPT[i].Voldow.clear();
				th.SPT[i].Fredow.clear();
				th.SPT[i].VolDUM.clear();
			}

			while(str != null)
			{
				if(str.contains(","))
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
					double j;
					int k;
					try
					{
						j = Double.parseDouble(str.substring(b+1, str.indexOf(",", b+1)));
						b = str.indexOf(",", b+1);
						k = Integer.parseInt(str.substring(b+1));
					}
					catch(Exception es)
					{
						j = Double.parseDouble(str.substring(b+1));
						if(i < 0) k = 0;
						else k = 16;
					}

					if(a < 3)
					{
						int ChangeRate = (int)(MM2.FamicomHz / d + 0.5);
						d = MM2.FamicomHz / ChangeRate;
					}
					else if(a == 3)
					{
						double sa = Double.MAX_VALUE;
						int No = 0;
						for(int Z = 0; Z < MM2.SnN.length; Z++)
						{
							if(sa > Math.abs(MM2.SnN[Z] - d))
							{
								No = Z;
								sa = Math.abs(MM2.SnN[Z] - d);
							}
						}
						d = MM2.SnN[No];
						e = No + 1;
					}

					th.SPT[a].Volume.add((byte)c);
					th.SPT[a].Freque.add(d);
					th.SPT[a].FrequI.add(e);
					th.SPT[a].Time.add(f);
					th.SPT[a].SoundT.add(g);
					th.SPT[a].Duty.add((float)h);
					th.SPT[a].Voldow.add(i);
					th.SPT[a].Fredow.add(j);
					th.SPT[a].VolDUM.add(k);
				}
				else
				{
					int a = str.indexOf("DPCM") + 4;
					int b = str.indexOf("D", a);
					int c = Integer.parseInt(str.substring(a, b));
					int d = Integer.parseInt(str.substring(b + 1, b + 2));
					//System.out.println(a + " " + b + " " + c + " " + d);
					MM2.DPCMo[d].clear();
					MM2.DPCMo[d].add((byte) c);
					for(int i = b + 3; i < str.length(); i++)
					{
						MM2.DPCMo[d].add((byte) Integer.parseInt(str.substring(i, i + 1)));
					}
				}

				str = br.readLine();
			}
			loopsetting(loop, th);
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
		saveload[1] = false;
	}

	void loopsetting(int loopstart, Thoone th)
	{
		th.musics.loopBarTime = (long)(loopstart*(60.0 / th.SPT[th.ch.GetChannel()].Tempo * 4) * 1000);
		th.ch.loopstring = loopstart + "";

		for(int i = 0; i < 5; i++)
		{
			th.musics.loopNotes[i] = 0;
			while(true)
			{
				if(th.musics.loopNotes[i] < th.SPT[i].Volume.size())
				{
					if(th.SPT[i].Time.get(th.musics.loopNotes[i]) <= th.musics.loopBarTime/1000.0*th.mm2.HzMu)
					{
						if(th.musics.loopBarTime/1000.0*th.mm2.HzMu >= th.SPT[i].Time.get(th.musics.loopNotes[i])+th.SPT[i].SoundT.get(th.musics.loopNotes[i]))
						{
							th.musics.loopNotes[i]++;
						}
						else
						{
							break;
						}
					}
					else
					{
						break;
					}
				}
				else
				{
					break;
				}
			}
		}
	}
	//*/
}
