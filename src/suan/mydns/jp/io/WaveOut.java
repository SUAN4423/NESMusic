package suan.mydns.jp.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import suan.mydns.jp.Thoone;
import suan.mydns.jp.music.MM2;
import suan.mydns.jp.music.WavFileWriter;
import suan.mydns.jp.music.Wave;

public class WaveOut extends Thread
{
	private AudioInputStream audioInputStream;
	private File outputFile;

	private BufferedOutputStream bufOutput = null;
	private BufferedInputStream bufInput = null;

	private int MAX_VALUE;
	private int MIN_VALUE;

	private ByteBuffer bb;

	private int byteOffset;
	private int sampleSizeInByte;

	private static final int INT_BYTE_LENGTH = 4;

	private int n;// write values

	private long time = 0;


	@Override
	public void run()
	{
		if(Wave.th != null)
		{
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					MM2.HzMu,
					Byte.SIZE,
					1,
					1,
					48000.0f,
					false);

			long frameLength = 0;

			ArrayList<Integer> Ch = new ArrayList<>();
			int[] startNotes = {0, 0, 0, 0};
			int[] nowNotes = {0, 0, 0, 0};

			boolean b = false;
			boolean bb = false;

			int kurikaeshi = 1;

			for(;;)
			{
				b = false;
				for(int i = 0; i < 4; i++)
				{
					if(nowNotes[i] < Wave.th.SPT[i].Volume.size())
					{
						b = true;
						if(Wave.th.SPT[i].Time.get(nowNotes[i]) <= time)
						{
							if(Wave.th.ch.canPlay[i]) this.ChStat(Wave.th.SPT[i].Freque.get(nowNotes[i]), Wave.th.SPT[i].Duty.get(nowNotes[i]), Wave.th.SPT[i].Volume.get(nowNotes[i]), Wave.th.SPT[i].Voldow.get(nowNotes[i]), Wave.th.SPT[i].Fredow.get(nowNotes[i]), true, nowNotes[i], i, Wave.th.SPT[i].VolDUM.get(nowNotes[i]));
							else this.ChStat(i == 3 ? 0 : -1, Wave.th.SPT[i].Duty.get(nowNotes[i]), Wave.th.SPT[i].Volume.get(nowNotes[i]), Wave.th.SPT[i].Voldow.get(nowNotes[i]), Wave.th.SPT[i].Fredow.get(nowNotes[i]), true, nowNotes[i], i, Wave.th.SPT[i].VolDUM.get(i));
							if(time >= Wave.th.SPT[i].Time.get(nowNotes[i])+Wave.th.SPT[i].SoundT.get(nowNotes[i]))
							{
								nowNotes[i]++;
							}
						}
						else
						{
							this.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i, 16);
						}
					}
					else
					{
						this.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i, 16);
					}
				}
				if(!b)
				{
					if((int) (Wave.th.state.NortsSize) > kurikaeshi)
					{
						this.time = -MM2.onecool;
						for(int i = 0; i < 4; i++)
						{
							nowNotes[i] = 0;
						}
						kurikaeshi++;
					}
					else
					{
						for(int i = 0; i < 4; i++)
						{
							this.ChStat(i == 3 ? 0 : -1, 0.5f, 16, 0.0f, 1.0f, true, 0, i, 16);
						}
						for(int i = 0; i < 4; i++)
						{
							nowNotes[i] = 0;
						}
						bb = true;
					}
				}
				if(bb) break;

				byte a0[] = Square(Frequencys[0], Dutys[0], Volume[0], VDown[0], Mod[0], Mods[0], MNum[0], (byte)1);
				byte b0[] = Square(Frequencys[1], Dutys[1], Volume[1], VDown[1], Mod[1], Mods[1], MNum[1], (byte)2);
				byte c0[] = Triangle(Frequencys[2], Dutys[2], Volume[2], VDown[2], Mod[2], Mods[2], MNum[2], (byte)3);
				byte d0[] = Noise(Frequencys[3], Dutys[3], Volume[3], VDown[3], Mod[3], Mods[3], MNum[3], (byte)4);

				if(b)
				{
					for(int i = 0; i < MM2.onecool; i++)
					{
						Ch.add((a0[i]+b0[i]+c0[i]+d0[i])/4);
					}
				}

				time += MM2.onecool;
			}

			frameLength = Ch.size();

			File f2 = null;
			try
			{
				f2 = new File("./MUSIC.wav");
			}
			catch (Exception e)
			{
				try
				{
					Files.createFile(Paths.get("./MUSIC.wav"));
					f2 = new File("./MUSIC.wav");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}

			WavFileWriter writer = new WavFileWriter(f2, format, frameLength);

			try
			{
				writer.putFrame(toArr(Ch));
				writer.close();
			}
			catch (IOException e)
			{
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}

			Thoone.FileWriteTime = System.currentTimeMillis();
		}
	}

	public static int[] toArr(List<Integer> list){
		// List<Integer> -> int[]
		int l = list.size();
		int[] arr = new int[l];
		Iterator<Integer> iter = list.iterator();
		for (int i=0;i<l;i++) arr[i] = iter.next();
		return arr;
	}

	double[] MFreq = {0, 0, 0, 0};
	float[] MDuty = {0f, 0f, 0f, 0f};
	byte[] MVolu = {0, 0, 0, 0};
	double[] MVDow = {0, 0, 0, 0};
	double[] MModu = {0, 0, 0, 0};
	boolean[] MMEna = {false, false, false, false};
	byte[] MNumb = {-1, -1, -1, -1};
	int[] MVDUM = {0, 0, 0, 0};

	public void ChStat(double Fr, float Du, int Vo, double VD, double Mo, boolean ME, int Nu, int Ch, int vdum)
	{
		MFreq[Ch] = Fr;
		MDuty[Ch] = Du;
		MVolu[Ch] = (byte) Vo;
		MVDow[Ch] = VD;
		MModu[Ch] = Mo;
		MMEna[Ch] = ME;
		MNumb[Ch] = (byte) Nu;
		MVDUM[Ch] = vdum;
		this.Set(Ch);
	}

	private void Set(int Ch)
	{
		Frequencys[Ch] = MFreq[Ch];
		Dutys[Ch] = MDuty[Ch];
		Volume[Ch] = MVolu[Ch];
		VDown[Ch] = MVDow[Ch];
		Mod[Ch] = MModu[Ch];
		Mods[Ch] = MMEna[Ch];
		MNum[Ch] = MNumb[Ch];
		MVolDUM[Ch] = MVDUM[Ch];
	}

	static byte[] MusicNumbers = {-1, -1, -1, -1};
	static double[] Frequencys = {0, 0, 0, 0};
	static double[] Frequencyss= {0, 0, 0, 0};
	static float[] Dutys       = {0f, 0f, 0f, 0f};
	static byte[] Volume       = {0, 0, 0, 0};
	static double[] VDown      = {0, 0, 0, 0};
	static double[] Volumes    = {0, 0, 0, 0};
	static double[] Mod        = {0, 0, 0, 0};
	static boolean[] Mods      = {false, false, false, false};
	static int[] Numbers       = {0, 0, 0, 0};
	static byte[] MNum         = {0, 0, 0, 0};
	static int[] MVolDUM       = {0, 0, 0, 0};

	static byte[] Square(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		byte[] b = new byte[MM2.onecool];
		if(Frequency == -1)
		{
	        for(int i = 0; i < b.length; i++)
	        {
	        	b[i] = 0;
	        }
			return b;
		}
		if(MusicNumbers[Ch - 1] != MusicNumber)
		{
			Frequencyss[Ch - 1] = Frequency;
			Numbers[Ch - 1] = 0;
			Volumes[Ch - 1] = (VolumeR+0.5) * 1.0;
		}
		for(int i = 0; i < b.length; i++)
        {
            double phase = (i + (MM2.onecool * Numbers[Ch - 1])) / (MM2.HzMu / Frequencyss[Ch - 1]);
            phase -= Math.floor(phase);
            b[i] = (byte)(((phase <= Duty ? 127 : -128) / 127.0) * Math.max(Math.min(((byte)(Volumes[Ch - 1])*8), VolumeDownUp < 0 ? 127 : MVolDUM[Ch - 1] == 16 ? 127 : MVolDUM[Ch - 1] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[Ch - 1] == 16 ? 127 : MVolDUM[Ch - 1] * 8));
			Volumes[Ch - 1] = Math.max(Math.min(Volumes[Ch - 1] + VolumeDownUp, 16), 0);
			Frequencyss[Ch - 1] = Frequencyss[Ch - 1] * Moderation;
        }
		MusicNumbers[Ch - 1] = MusicNumber;
		Numbers[Ch - 1]++;
		return b;
	}

	static byte[] Tri = {0, 18, 36, 54, 72, 90, 108, 126};
	static byte Trinum = 0;
	static byte a = 1;
	static int bbold = -128;

	static byte[] Triangle(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		byte[] b = new byte[MM2.onecool];
		if(Frequency == -1)
		{
	        for(int i = 0; i < b.length; i++)
	        {
	        	b[i] = 0;
	        }
			return b;
		}
		if(MusicNumbers[2] != MusicNumber)
		{
			Frequencyss[2] = Frequency;
			Numbers[2] = 0;
		}
		Volumes[2] = 127.0;
		for(int i = 0; i < b.length; i++)
		{
            double phase = (i + (MM2.onecool * Numbers[2])) / (MM2.HzMu / (Frequencyss[2] / 1));
            phase -= Math.floor(phase);
            int bb = (int)(Math.abs(phase - 0.5) * 510 - 128) + 128;
            if(bbold < bb) a = 1;
            else if(bbold > bb) a = -1;
            b[i] = (byte)(Tri[((bb / 16) <= 7 ? (bb / 16) : ((bb / 16) * -1 + 15))] * a);
            bbold = bb;
    		Frequencyss[2] *= Moderation;
		}
		MusicNumbers[2] = MusicNumber;
		Numbers[2]++;
		return b;
	}

	static int reg = 0x8000;
	static byte TempN = 0;
	static int Nokori = 0;

	static byte[] Noise(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		byte[] b = new byte[MM2.onecool];

		if(Frequency == -1 || Frequency == 0.0)
		{
	        for(int i = 0; i < b.length; i++)
	        {
	        	b[i] = 0;
	        }
			return b;
		}

		for(int i = 0; i < Nokori; i++)
		{
			b[i] = TempN;
		}

		if(MusicNumbers[3] != MusicNumber)
		{
			Nokori = 0;
			Frequencyss[3] = Frequency;
			Numbers[3] = 0;
			Volumes[3] = (VolumeR+0.5) * 1.0;
		}

		for(int i = (int) (Nokori / Frequencyss[3]); i < b.length / Frequencyss[3]; i++)
		{
			reg >>>= 1;
			reg |= ((reg ^ (reg >>> (Duty == 1.0f ? 6 : 1))) & 1) << 15;
			b[(int) (i * Frequencyss[3])] = (byte) (((reg & 1) - 0.5) * 2 * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp < 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8));
        	for(int j = 1; j < Frequencyss[3]; j++)
        	{
    			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
        		if(i * (int)Frequencyss[3] + j < MM2.onecool) b[(int) (i * (int)Frequencyss[3] + j)] = b[(int) (i * (int)Frequencyss[3])];
        		else
        		{
        			TempN = b[b.length-1];
        			Nokori = i * (int)Frequencyss[3] + j - MM2.onecool;
        		}
        	}
			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
		}

		Frequencyss[3] *= Moderation;
		MusicNumbers[3] = MusicNumber;

		return b;
	}
}