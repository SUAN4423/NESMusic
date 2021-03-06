package suan.mydns.jp.music;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import suan.mydns.jp.io.DPCMWaveInport;

public class MM2
{
	public static double[][] Sn = new double[10][12];
	public static double[] SnN = new double[16];
	public static double[] SnD = new double[16];
	public static String[] Onkai = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public static final int C = 0, Cs = 1, D = 2, Ds = 3, E = 4, F = 5, Fs = 6, G = 7, Gs = 8, A = 9, As = 10, B = 11;

	public static SourceDataLine On = null;
	public static SourceDataLine Tw = null;
	public static SourceDataLine Th = null;
	public static SourceDataLine Fo = null;
	public static SourceDataLine Fi = null;

	public boolean resk = false;
	public long resktime = 0;

	public static final int HzMu = 48000;

	public static final double FamicomHz = 1789772.5;
	public static final int NoiseClock[] = {0x002, 0x004, 0x008, 0x010, 0x020, 0x030, 0x040, 0x050, 0x065, 0x07F, 0x0BE, 0x0FE, 0x17D, 0x1FC, 0x3F9, 0x7F2};
	public static final int DPCMClock[] = {428, 380, 340, 320, 286, 254, 226, 214, 190, 160, 142, 128, 106, 85, 72, 54};
	public static final int NoiseHz[] = {894886, 447443, 223722, 111861, 55930, 37287, 27965, 22372, 17721, 14093, 9420, 7046, 4698, 3523, 1760, 880};

	public static ArrayList<Byte> DPCMo[] = new ArrayList[8];

	public static float percent = 1.0f;

	public static int onecool = 480;

	public boolean[] SEMUSIC = {false, false, false, false, false, false, false, false, false};

	public MM2()
	{
		for(int i = 0; i < 10; i++)
		{
			Sn[i][0]  = 261.6255653005986 * Math.pow(2, i - 4); //ド
			Sn[i][1]  = 277.1826309768721 * Math.pow(2, i - 4);
			Sn[i][2]  = 293.6647679174076 * Math.pow(2, i - 4); //レ
			Sn[i][3]  = 311.1269837220809 * Math.pow(2, i - 4);
			Sn[i][4]  = 329.6275569128699 * Math.pow(2, i - 4); //ミ
			Sn[i][5]  = 349.2282314330039 * Math.pow(2, i - 4); //ファ
			Sn[i][6]  = 369.9944227116344 * Math.pow(2, i - 4);
			Sn[i][7]  = 391.9954359817493 * Math.pow(2, i - 4); //ソ
			Sn[i][8]  = 415.3046975799451 * Math.pow(2, i - 4);
			Sn[i][9]  = 440.0000000000000 * Math.pow(2, i - 4); //ラ
			Sn[i][10] = 466.1637615180899 * Math.pow(2, i - 4);
			Sn[i][11] = 493.8833012561241 * Math.pow(2, i - 4); //シ
		}
		for(int i = 0; i < 16; i++)
		{
			SnN[i] = HzMu / (FamicomHz / NoiseClock[i]);
			SnD[i] = HzMu / (FamicomHz / DPCMClock[i]);
			//System.out.println(SnN[i] * HzMu + " " + SnD[i] * HzMu + " " + SnN[i] + " " + SnD[i]);
			//System.out.println(HzMu / SnN[i] + " " + HzMu / SnD[i]);
		}
		/*SnN[0] = HzMu / 901120.0;					//15A
		SnN[1] = HzMu / 450560.0;					//14A
		SnN[2] = HzMu / 225280.0;					//13A
		SnN[3] = HzMu / 112640.0;					//12A
		SnN[4] = HzMu / 56320.0;					//11A
		SnN[5] = HzMu / 37589.0902934281728;		//11D
		SnN[6] = HzMu / 28160.0;					//10A
		SnN[7] = HzMu / 22350.6068117122496;		//10F
		SnN[8] = HzMu / 16744.0361792383104;		//10C
		SnN[9] = HzMu / 14080.0;					//9A
		SnN[10] = HzMu / 9397.2725733570432;		//9D
		SnN[11] = HzMu / 7040.0;					//8A
		SnN[12] = HzMu / 4698.6362866785216;		//8D
		SnN[13] = HzMu / 3520.0;					//7A
		SnN[14] = HzMu / 1760.0;					//6A
		SnN[15] = HzMu / 880.0;						//5A*/
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				HzMu,
				Byte.SIZE,
				1,
				1,
				48000.0f,
				false);
        try
        {
        	On = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			On.open(format);
        	Tw = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			Tw.open(format);
        	Th = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			Th.open(format);
        	Fo = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			Fo.open(format);
        	Fi = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			Fi.open(format);
		}
        catch (LineUnavailableException e)
        {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

        On.start();
        Tw.start();
        Th.start();
        Fo.start();
        Fi.start();

		Timer T2 = new Timer();
		T2.scheduleAtFixedRate(new Audio1Task(), 0, 1000 / (HzMu / onecool));
		Timer T3 = new Timer();
		T3.scheduleAtFixedRate(new Audio2Task(), 0, 1000 / (HzMu / onecool));
		Timer T4 = new Timer();
		T4.scheduleAtFixedRate(new Audio3Task(), 0, 1000 / (HzMu / onecool));
		Timer T5 = new Timer();
		T5.scheduleAtFixedRate(new Audio4Task(), 0, 1000 / (HzMu / onecool));
		Timer T6 = new Timer();
		T6.scheduleAtFixedRate(new Audio5Task(), 0, 1000 / (HzMu / onecool));
		//Timer T6 = new Timer();
		//T6.scheduleAtFixedRate(new TriChange(), 0, 1);
	}

	double[] MFreq = {0, 0, 0, 0, 0};
	float[] MDuty = {0f, 0f, 0f, 0f, 0f};
	byte[] MVolu = {0, 0, 0, 0, 0};
	double[] MVDow = {0, 0, 0, 0, 0};
	double[] MModu = {0, 0, 0, 0, 0};
	boolean[] MMEna = {false, false, false, false, false};
	byte[] MNumb = {-1, -1, -1, -1, -1};
	int[] MVDUM = {0, 0, 0, 0, 0};
	private long Tempsample = 0;

	public void ChStat(double Fr, float Du, int Vo, double VD, double Mo, boolean ME, int Nu, int Ch, int vdum)
	{
		//System.out.println(Ch + " " + Fr);
		MFreq[Ch] = Fr;
		MDuty[Ch] = Du;
		MVolu[Ch] = (byte) Vo;
		MVDow[Ch] = VD;
		MModu[Ch] = Mo;
		MMEna[Ch] = ME;
		MNumb[Ch] = (byte) Nu;
		MVDUM[Ch] = vdum;
		/*if(Ch == 3)
		{
			System.out.println(vdum);
		}//*/
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

	class AudioTask extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			byte[] A =   Square(Frequencys[0], Dutys[0], Volume[0], VDown[0], Mod[0], Mods[0], MNum[0], (byte)1);
			byte[] B =   Square(Frequencys[1], Dutys[1], Volume[1], VDown[1], Mod[1], Mods[1], MNum[1], (byte)2);
			byte[] C = Triangle(Frequencys[2], Dutys[2], Volume[2], VDown[2], Mod[2], Mods[2], MNum[2], (byte)3);
			byte[] D =    Noise(Frequencys[3], Dutys[3], Volume[3], VDown[3], Mod[3], Mods[3], MNum[3], (byte)4);
			for(int i = 0; i < A.length; i++)
			{
				A[i] = (byte) ((A[i] + B[i] + C[i] + D[i]) / 4);
			}
			//On.flush();
			On.write(A, 0, A.length);
		}
	}

	public static byte waves[][] = new byte[5][onecool];

	class Audio1Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			if(!resk) On.write(Square(Frequencys[0], Dutys[0], Volume[0], VDown[0], Mod[0], Mods[0], MNum[0], (byte)1), 0, onecool);
		}
	}

	class Audio2Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			if(!resk) Tw.write(Square(Frequencys[1], Dutys[1], Volume[1], VDown[1], Mod[1], Mods[1], MNum[1], (byte)2), 0, onecool);
		}
	}

	class Audio3Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			if(!resk) Th.write(Triangle(Frequencys[2], Dutys[2], Volume[2], VDown[2], Mod[2], Mods[2], MNum[2], (byte)3), 0, onecool);
		}
	}

	class Audio4Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			//System.out.println(Frequencys[3] + " " + Dutys[3] + " " + Volume[3] + " " + VDown[3] + " " + Mod[3] + " " + Mods[3] + " " + MNum[3] + " " + (byte)4);
			if(!resk) Fo.write(Noise(Frequencys[3], Dutys[3], Volume[3], VDown[3], Mod[3], Mods[3], MNum[3], (byte)4), 0, onecool);
		}
	}

	class Audio5Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			//System.out.println(Frequencys[3] + " " + Dutys[3] + " " + Volume[3] + " " + VDown[3] + " " + Mod[3] + " " + Mods[3] + " " + MNum[3] + " " + (byte)4);
			if(!resk) Fi.write(DPCM(Frequencys[4], Dutys[4], Volume[4], VDown[4], Mod[4], Mods[4], MNum[4], (byte)5), 0, onecool);
		}
	}

	static byte[] MusicNumbers = {-1, -1, -1, -1, -1};
	static double[] Frequencys = {-1, -1, -1, 0, -1};
	public static double[] Frequencyss= {-1, -1, -1, 0, -1};
	public static float[] Dutys       = {0f, 0f, 0f, 0f, 0f};
	static byte[] Volume       = {0, 0, 0, 0, 0};
	public static double[] VDown      = {0, 0, 0, 0, 0};
	public static double[] Volumes    = {0, 0, 0, 0, 0};
	static double[] Mod        = {0, 0, 0, 0, 0};
	static boolean[] Mods      = {false, false, false, false, false};
	static int[] Numbers       = {0, 0, 0, 0, 0};
	static byte[] MNum         = {0, 0, 0, 0, 0};
	public static int[] MVolDUM       = {0, 0, 0, 0, 0};

	/*static int ringing[] = {0, 0};
	static boolean ringed[] = {false, false};//*/
	static double changes[] = {0.0, 0.0};
	static byte hakei[] = {127, 127};
	static byte kais[] = {0, 0};

	static byte[] Square(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		//byte[] b = new byte[onecool];
		byte[] b = waves[Ch - 1];
		if(Frequency == -1)
		{
			for(int i = 0; i < b.length; i++)
			{
				b[i] = 0;
			}
			/*if(ringing[Ch - 1] != 0 && Ch == 1) System.out.println(ringing[Ch - 1]);
			ringing[Ch - 1] = 0;
			ringed[Ch - 1] = false;//*/
			changes[Ch - 1] = 0;
			kais[Ch - 1] = 0;
			return b;
		}
		if(MusicNumbers[Ch - 1] != MusicNumber)
		{
			/*if(ringed[Ch - 1]) ringing[Ch - 1] += Numbers[Ch - 1];
			if(Moderation != 0.0) ringing[Ch - 1] = 0;
			ringed[Ch - 1] = true;//*/
			//System.out.println(Frequency + " " + Moderation);
			Frequencyss[Ch - 1] = Frequency;
			//System.out.println(Frequencyss[Ch - 1]);
			Numbers[Ch - 1] = 0;
			Volumes[Ch - 1] = (VolumeR+0.5) * 1.0;
		}
		for(int i = 0; i < b.length; i++)
		{
			//System.out.println(Frequencyss[Ch - 1]);
			double TempHZ = Frequencyss[Ch - 1];
			int ChangeRate = (int)(MM2.FamicomHz / TempHZ + 0.5);
			ChangeRate = Math.max(Math.min(ChangeRate, 0xFFFF), 0x0001);
			TempHZ = MM2.FamicomHz / ChangeRate;
			//double phase = (i + (onecool * (Numbers[Ch - 1] + ringing[Ch - 1]))) / (HzMu / TempHZ/*Frequencyss[Ch - 1]*/);
			//phase -= Math.floor(phase);
			//b[i] = (byte)(((phase <= Duty ? 127 : -128) / 127.0) * Math.max(Math.min(((byte)(Volumes[Ch - 1])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[Ch - 1] == 16 ? 127 : MVolDUM[Ch - 1] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[Ch - 1] == 16 ? 127 : MVolDUM[Ch - 1] * 8) * percent);
			changes[Ch - 1] += 8;
			while(changes[Ch - 1] >= HzMu / TempHZ/*Frequencyss[2]*/)
			{
				//System.out.println(change);
				changes[Ch - 1] -= HzMu / TempHZ/*Frequencyss[2]*/;
				kais[Ch - 1]++;
				if(hakei[Ch - 1] == 127 && kais[Ch - 1] >= (int)(8*Duty))
				{
					hakei[Ch - 1] = -128;
					kais[Ch - 1] = 0;
				}
				else if(hakei[Ch - 1] == -128 && kais[Ch - 1] >= 8-(int)(8*Duty))
				{
					hakei[Ch - 1] = 127;
					kais[Ch - 1] = 0;
				}
			}

			b[i] = (byte)((hakei[Ch - 1] / 127.0) * Math.max(Math.min(((byte)(Volumes[Ch - 1])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[Ch - 1] == 16 ? 127 : MVolDUM[Ch - 1] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[Ch - 1] == 16 ? 127 : MVolDUM[Ch - 1] * 8) * percent);
			Volumes[Ch - 1] = Math.max(Math.min(Volumes[Ch - 1] + VolumeDownUp, 16), 0);
			if(!ModerationEnable || (Numbers[Ch - 1] + 5) % 20 < 10) Frequencyss[Ch - 1] = Frequencyss[Ch - 1] * Moderation;
			else Frequencyss[Ch - 1] = Frequencyss[Ch - 1] / Moderation;
			//System.out.println(Frequencyss[Ch - 1]);
		}
		MusicNumbers[Ch - 1] = MusicNumber;
		Numbers[Ch - 1]++;
		//System.out.println(Volumes[Ch-1]);
		return b;
	}

	static byte[] Tri = {0, 18, 36, 54, 72, 90, 108, 126};
	static byte Trinum = 0;
	static byte a = 1;
	static int bbold = -128;

	private static double change = 0;
	private static int neiro = 0;
	private static int susumi = 1;
	static boolean resks = false;

	class TriChange extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			if(!resks)
			{
				change += Math.pow(2, 5);
			}
		}
	}

	static byte[] Triangle(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		//byte[] b = new byte[onecool];
		byte[] b = waves[Ch - 1];
		if(Frequency == -1)
		{
	        for(int i = 0; i < b.length; i++)
	        {
	        	//b[i] = 0;
	        	resks = true;
	        	b[i] = (byte)(Tri[neiro] * a * percent);
	        }
	        change = 0;
			return b;
		}
    	resks = false;
		if(MusicNumbers[2] != MusicNumber)
		{
			Frequencyss[2] = Frequency;
			Numbers[2] = 0;
		}
		Volumes[2] = 16.0;

		/*for(int i = 0; i < b.length; i++)
		{
            double phase = (i + (onecool * Numbers[2])) / (HzMu / (Frequencyss[2] / 1));
            phase -= Math.floor(phase);
            int bb = (int)(Math.abs(phase - 0.5) * 510 - 128) + 128;
            if(bbold < bb) a = 1;
            else if(bbold > bb) a = -1;
            b[i] = (byte)(Tri[((bb / 16) <= 7 ? (bb / 16) : ((bb / 16) * -1 + 15))] * a);
            bbold = bb;
    		Frequencyss[2] *= Moderation;
		}//*/
		for(int i = 0; i < b.length; i++)
		{
			double TempHZ = Frequencyss[Ch - 1];
			int ChangeRate = (int)(MM2.FamicomHz / TempHZ + 0.5)/2;
			ChangeRate = Math.max(Math.min(ChangeRate, 0xFFFF), 0x0001);
			TempHZ = MM2.FamicomHz / (ChangeRate*2);
			change += 32;
			while(change >= HzMu / TempHZ/*Frequencyss[2]*/)
			{
				//System.out.println(change + " " + (HzMu / TempHZ) + " " + TempHZ + " " + Frequencyss[Ch - 1]);
				//System.out.println(i + " " + neiro + " " + susumi);
				change -= HzMu / TempHZ/*Frequencyss[2]*/;
				neiro += susumi;
				if(neiro >= 8)
				{
					neiro = 7;
					susumi *= -1;
				}
				else if(neiro < 0)
				{
					neiro = 0;
					susumi *= -1;
					a *= -1;
				}
			}
            b[i] = (byte)(Tri[neiro] * a * percent);
			if(!ModerationEnable || (Numbers[Ch - 1] + 5) % 20 < 10) Frequencyss[Ch - 1] = Frequencyss[Ch - 1] * Moderation;
			else Frequencyss[Ch - 1] = Frequencyss[Ch - 1] / Moderation;
		}//*/
		MusicNumbers[2] = MusicNumber;
		Numbers[2]++;
		return b;
	}

	static int reg = 0x8000;
	static byte TempN = 0;
	static int Nokori = 0;
	public static int old = 2;
	static double count = 0;
	static int noisetype = 0;

	static double noisetemp[] = {0, 0, 0};
	static double noiseMax[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	static float coef[] = {0.005f, 0.05f, 0.25f, 0.95f};

	static double in1, in2, out1, out2;
	static float q = 0.3f;//(float) (1 / Math.sqrt(2));
	static float omega = 2.0f * 3.14159265f *  3500 / HzMu;
	static float alpha = (float) (Math.sin(omega) / (2.0f * q));

	static float a_0 =   1.0f + alpha;
	static float a_1 =  (float) (-2.0f * Math.cos(omega));
	static float a_2 =   1.0f - alpha;
	static float b_0 =  (float) ((1.0f + Math.cos(omega)) / 2.0f);
	static float b_1 = (float) -(1.0f + Math.cos(omega));
	static float b_2 =  (float) ((1.0f + Math.cos(omega)) / 2.0f);

	static byte[] b3 = new byte[onecool];

	static byte[] Noise(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		//byte[] b = new byte[onecool];
		byte[] b = waves[Ch - 1];
		byte[] b2 = waves[Ch - 1];

		if(Frequency == -1 || Frequency == 0.0)
		{
	        for(int i = 0; i < b.length; i++)
	        {
	        	b[i] = 0;
	        }
			return b;
		}

		/*for(int i = 0; i < Nokori && i < onecool; i++)
		{
			b[i] = TempN;
		}//*/

		if(MusicNumbers[3] != MusicNumber)
		{
			Nokori = 0;
			Frequencyss[3] = Frequency;
			Numbers[3] = 0;
			Volumes[3] = (VolumeR+0.5) * 1.0;

			for(int i = 0; i < SnN.length; i++)
			{
				if(Frequencyss[3] == SnN[i])
				{
					noisetype = i;
					break;
				}
			}

			noiseMax[noisetype] = 1;
			in1 = in2 = out1 = out2 = 0;

			omega = (float) (2.0 * Math.PI * (HzMu/4.0) / (FamicomHz/NoiseClock[noisetype]));
			alpha = (float) (Math.sin(omega) / (2.0f * q));
			a_0 =   1.0f + alpha;
			a_1 =  (float) (-2.0f * Math.cos(omega));
			a_2 =   1.0f - alpha;
			b_0 =  (float) ((1.0f - Math.cos(omega)) / 2.0f);
			b_1 = (float) (1.0f + Math.cos(omega));
			b_2 =  (float) ((1.0f - Math.cos(omega)) / 2.0f);

			//System.out.println(omega + " " + alpha + " " + a_0 + " " + a_1 + " " + a_2 + " " + b_0 + " " + b_1 + " " + b_2);

			/*
			for(int i = 0; i < SnN.length; i++)
			{
				if(Frequencyss[3] == SnN[i])
				{
					noisetype = i;
					break;
				}
			}
			switch(noisetype)
			{
				case 0:
				{
					omega = (float) (2.0f * Math.PI *  21000 / HzMu);
					alpha = (float) (Math.sin(omega) / (2.0f * q));
					a_0 =   1.0f + alpha;
					a_1 =  (float) (-2.0f * Math.cos(omega));
					a_2 =   1.0f - alpha;
					b_0 =  (float) ((1.0f + Math.cos(omega)) / 2.0f);
					b_1 = (float) (1.0f + Math.cos(omega));
					b_2 =  (float) ((1.0f + Math.cos(omega)) / 2.0f);
					break;
				}
				case 1:
				{
					omega = (float) (2.0f * Math.PI *  18100 / HzMu);
					alpha = (float) (Math.sin(omega) / (2.0f * q));
					a_0 =   1.0f + alpha;
					a_1 =  (float) (-2.0f * Math.cos(omega));
					a_2 =   1.0f - alpha;
					b_0 =  (float) ((1.0f + Math.cos(omega)) / 2.0f);
					b_1 = (float) (1.0f + Math.cos(omega));
					b_2 =  (float) ((1.0f + Math.cos(omega)) / 2.0f);
					break;
				}
			}//*/
			//System.out.println(noisetype);
		}

		for(int i = 0; i < b.length; i++)
		{
			count++;

			while(count >= ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]))
			{
				count -= ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]);
				reg >>>= 1;
				reg |= ((reg ^ (reg >>> ((Duty + 2 - old) == 1.0f ? 6 : 1))) & 1) << 15;

				noisetemp[0] = ((reg & 0x01) - 0.5) * 2;
				noisetemp[1] = (b_0/a_0 * noisetemp[0] + b_1/a_0 * in1  + b_2/a_0 * in2 - a_1/a_0 * out1 - a_2/a_0 * out2);

				in2  = in1;       // 2つ前の入力信号を更新
				in1  = noisetemp[0];  // 1つ前の入力信号を更新

				out2 = out1;      // 2つ前の出力信号を更新
				out1 = noisetemp[1]; // 1つ前の出力信号を更新

				if(noisetype <= 3)
					noiseMax[noisetype] = Math.max(noiseMax[noisetype], Math.abs(noisetemp[1]));
				//noisetemp[2] = /*noisetemp[0] -*/ //noisetemp[1];
				//System.out.println(reg + " " + count + " " + Frequencyss[3]);
//				System.out.println(noisetemp[0] + " " + noisetemp[1] + " " + noisetemp[2]);
			}
			//System.out.println(Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8) + " " + VolumeDownUp + " " + MVolDUM[3] + " " + Volumes[3]);
			if(noisetype <= -1) b[i] = (byte) (((/*reg & 1*/noisetemp[1]) - 0.5) * 2 * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8) * percent);
			else b[i] = (byte) (((reg & 1) - 0.5) * 2 * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8) * percent);

			//*
			if((Duty + (2 - old)) == 1.0f && noisetype >= 0 && noisetype <= 3)
			{
//				System.out.println((Duty + (2 - old)) + " " + Duty);
				double temp = noisetemp[1] / noiseMax[noisetype] * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8) * percent;
				b[i] = (byte)(temp);//noisetemp[1] * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8) * percent);
//				if(Math.abs(temp) >= 128.0f) System.out.println(temp);
//				if(i != 0 || MusicNumbers[3] == MusicNumber)
//				{
//					b3[i] = b[i];
//					b[i] = (byte) (coef[noisetype] * b3[i] + (1.0f - coef[noisetype]) * b3[i-1 == -1 ? b3.length - 1 : i - 1]);
//					b[i] = (byte) ((b3[i] - b[i]) * 0.5f);
//					if(Math.abs(b3[i] - b[i]) * 0.5f >= 128.0f){
//						System.out.println("OVER");
//					}
//				}

//				b2[i] = (byte) (b_0/a_0 * b[i] + b_1/a_0 * in1  + b_2/a_0 * in2 - a_1/a_0 * out1 - a_2/a_0 * out2);

//				in2  = in1;       // 2つ前の入力信号を更新
//				in1  = b[i];  // 1つ前の入力信号を更新

//				out2 = out1;      // 2つ前の出力信号を更新
//				out1 = b2[i]; // 1つ前の出力信号を更新
			}//*/

			//b[i] = (byte) (((reg & 1) - 0.5) * 2 * calcVolume(Volumes[3], VolumeDownUp, MVolDUM[3]) * percent);
			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
		}

		/*for(int i = (int) (Nokori / ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3])); i < b.length / ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]); i++)
		{
			reg >>>= 1;
			reg |= ((reg ^ (reg >>> ((Duty + 2 - old) == 1.0f ? 6 : 1))) & 1) << 15;
			b[(int) (i * ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]))] = (byte) (((reg & 1) - 0.5) * 2 * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp <= 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8) * percent);
        	for(int j = 1; j < ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]); j++)
        	{
    			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
        		if(i * ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]) + j < onecool) b[(int) (i * ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]) + j)] = b[(int) (i * ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]))];
        		else
        		{
        			TempN = b[b.length-1];
        			Nokori = (int)(i * ((Frequencyss[3] == SnN[15] && old != 2) ? SnN[14] : Frequencyss[3]) + j - onecool);
        		}
        	}
			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
		}//*/

		//Frequencyss[3] *= Moderation;
		//if(Frequencyss[3] < 1)Frequencyss[3] = 1.0;

		MusicNumbers[3] = MusicNumber;

		//if(noisetype >= 0 && noisetype <= 3) return b2;

		return b;
	}

	static byte FirstWave = 0;
	static double DPCMChange = 0;
	static int DPCMindex = 1;

	static byte[] DPCM(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		byte[] b = waves[Ch - 1];
		try
		{
			if(Frequency == -1.0 || DPCMo[(int)Duty].size() <= 0 || DPCMWaveInport.LOADING)
			{
		        for(int i = 0; i < b.length; i++)
		        {
		        	b[i] = 0;
		        }
				return b;
			}

			if(MusicNumbers[Ch - 1] != MusicNumber)
			{
				DPCMindex = 1;
				Frequencyss[Ch - 1] = Frequency;
				Numbers[Ch - 1] = 0;
				Volumes[Ch - 1] = (VolumeR+0.5) * 1.0;
				if(DPCMo[(int)Duty].size() > 0) FirstWave = (byte) DPCMo[(int)Duty].get(0);
				DPCMChange = 0;
				//System.out.println(SnD[(int)Frequency] + " " + FirstWave);
			}

			for(int i = 0; i < b.length; i++)
			{
				//while((DPCMChange += 4096) >= HzMu / SnD[(int)Frequency])
				DPCMChange += SnD[0];
				while(DPCMChange >= SnD[(int)Frequency])
				{
					//DPCMChange -=  HzMu / SnD[(int)Frequency];
					DPCMChange -=  SnD[(int)Frequency];
					if(DPCMindex == 0) FirstWave = (byte) DPCMo[(int)Duty].get(0);
					else FirstWave += DPCMo[(int)Duty].get(DPCMindex) * 2 - 1;
					if(FirstWave > 32 || FirstWave < -32)System.out.println(FirstWave);
					//System.out.println(DPCMo[(int)Duty].get(DPCMindex) * 2 - 1);
					DPCMindex++;
					//System.out.println(DPCMindex);
					if(DPCMindex >= DPCMo[(int)Duty].size()) DPCMindex = 0;
				}
				b[i] = (byte) (FirstWave * 4 >= 128 ? 127 : FirstWave * 4 < -128 ? -128 : FirstWave * 4);
				//System.out.println(b[i] + " " + DPCMindex);
				//Frequencyss[Ch - 1] *= Moderation;
			}//*/
			MusicNumbers[Ch - 1] = MusicNumber;
			Numbers[Ch - 1]++;

			return b;
		}
		catch(NullPointerException | IndexOutOfBoundsException e)
		{
	        for(int i = 0; i < b.length; i++)
	        {
	        	b[i] = 0;
	        }
		}

		return b;
	}

	private static byte calcVolume(double Volumes, double VolumeDownUp, int MVolDUM)
	{
		byte volume = 0;
		byte tempVolume = (byte)((int)(Volumes)*8 == 128 ? 127 : (int)(Volumes)*8);
		byte Downline = 0;
		byte Upline = 0;
		if(VolumeDownUp < 0)
		{
			Downline = 127;
			Upline = (byte) (MVolDUM == 16 ? 127 : MVolDUM * 8);
		}
		else if(VolumeDownUp > 0)
		{
			Downline = (byte) (MVolDUM == 16 ? 127 : MVolDUM * 8);
			Upline = 0;
		}
		else
		{
			Downline = 127;
			Upline = 0;
		}
		volume = (byte)Math.max(Math.min(tempVolume, Downline), Upline);
		//System.out.println(volume + " " + Downline + " " + Upline + " " + tempVolume + " " + Volumes + " " + MVolDUM);
		return volume;
	}

}
