package suan.mydns.jp.music;

import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class MM2
{
	public static double[][] Sn = new double[10][12];
	public static String[] Onkai = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	public static final int C = 0, Cs = 1, D = 2, Ds = 3, E = 4, F = 5, Fs = 6, G = 7, Gs = 8, A = 9, As = 10, B = 11;

	public static SourceDataLine On = null;
	public static SourceDataLine Tw = null;
	public static SourceDataLine Th = null;
	public static SourceDataLine Fo = null;

	public static int HzMu = 48000;

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
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				HzMu,
				Byte.SIZE,
				1,
				1,
				44100.0f,
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

		Timer T2 = new Timer();
		T2.scheduleAtFixedRate(new Audio1Task(), 0, 1000 / (HzMu / onecool));
		Timer T3 = new Timer();
		T3.scheduleAtFixedRate(new Audio2Task(), 0, 1000 / (HzMu / onecool));
		Timer T4 = new Timer();
		T4.scheduleAtFixedRate(new Audio3Task(), 0, 1000 / (HzMu / onecool));
		Timer T5 = new Timer();
		T5.scheduleAtFixedRate(new Audio4Task(), 0, 1000 / (HzMu / onecool));
	}

	private double[] MFreq = {0, 0, 0, 0};
	private float[] MDuty = {0f, 0f, 0f, 0f};
	private byte[] MVolu = {0, 0, 0, 0};
	private double[] MVDow = {0, 0, 0, 0};
	private double[] MModu = {0, 0, 0, 0};
	private boolean[] MMEna = {false, false, false, false};
	private byte[] MNumb = {0, 0, 0, 0};
	private long Tempsample = 0;

	public void ChStat(double Fr, float Du, int Vo, double VD, double Mo, boolean ME, int Nu, int Ch)
	{
		MFreq[Ch] = Fr;
		MDuty[Ch] = Du;
		MVolu[Ch] = (byte) Vo;
		MVDow[Ch] = VD;
		MModu[Ch] = Mo;
		MMEna[Ch] = ME;
		MNumb[Ch] = (byte) Nu;
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

	class Audio1Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			On.write(Square(Frequencys[0], Dutys[0], Volume[0], VDown[0], Mod[0], Mods[0], MNum[0], (byte)1), 0, onecool);
		}
	}

	class Audio2Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			Tw.write(Square(Frequencys[1], Dutys[1], Volume[1], VDown[1], Mod[1], Mods[1], MNum[1], (byte)2), 0, onecool);
		}
	}

	class Audio3Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			Th.write(Triangle(Frequencys[2], Dutys[2], Volume[2], VDown[2], Mod[2], Mods[2], MNum[2], (byte)3), 0, onecool);
		}
	}

	class Audio4Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			Fo.write(Noise(Frequencys[3], Dutys[3], Volume[3], VDown[3], Mod[3], Mods[3], MNum[3], (byte)4), 0, onecool);
		}
	}

	static byte[] MusicNumbers = {0, 0, 0, 0};
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

	static byte[] Square(double Frequency, float Duty, byte VolumeR, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		byte[] b = new byte[onecool];
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
			Volumes[Ch - 1] = (VolumeR+0.999) * 1.0;
		}
		for(int i = 0; i < b.length; i++)
        {
            double phase = (i + (onecool * Numbers[Ch - 1])) / (HzMu / Frequencyss[Ch - 1]);
            phase -= Math.floor(phase);
            b[i] = (byte)(((phase <= Duty ? 127 : -128) / 127.0) * Math.min(((byte)(Volumes[Ch - 1]+0.999)*8), 127));
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
		byte[] b = new byte[onecool];
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
            double phase = (i + (onecool * Numbers[2])) / (HzMu / (Frequencyss[2] / 1));
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
		byte[] b = new byte[onecool];

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
			Volumes[3] = (VolumeR+0.999) * 1.0;
		}

		for(int i = (int) (Nokori / Frequencyss[3]); i < b.length / Frequencyss[3]; i++)
		{
			reg >>>= 1;
			reg |= ((reg ^ (reg >>> (Duty == 1.0f ? 6 : 1))) & 1) << 15;
			b[(int) (i * Frequencyss[3])] = (byte) (((reg & 1) - 0.5) * 2 * Math.min((((byte)Volumes[3])*8), 127));
        	for(int j = 1; j < Frequencyss[3]; j++)
        	{
    			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
        		if(i * (int)Frequencyss[3] + j < onecool) b[(int) (i * (int)Frequencyss[3] + j)] = b[(int) (i * (int)Frequencyss[3])];
        		else
        		{
        			TempN = b[b.length-1];
        			Nokori = i * (int)Frequencyss[3] + j - onecool;
        		}
        	}
			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
		}

		Frequencyss[3] *= Moderation;
		MusicNumbers[3] = MusicNumber;

		return b;
	}
}
