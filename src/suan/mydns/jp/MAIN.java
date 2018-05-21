package suan.mydns.jp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MAIN
{
	public static double[][] Sn = new double[10][12];
	public static String[] Onkai = {"ド", "ド#", "レ", "レ#", "ミ", "ファ", "ファ#", "ソ", "ソ#", "ラ", "ラ#", "シ"};
	public static final int C = 0, Cs = 1, D = 2, Ds = 3, E = 4, F = 5, Fs = 6, G = 7, Gs = 8, A = 9, As = 10, B = 11;
	public static SourceDataLine audio1 = null;
	public static SourceDataLine audio2 = null;
	public static SourceDataLine audio3 = null;
	public static SourceDataLine audio4 = null;

	public static JFrame JF;
	public static JPanel[] JP = new JPanel[6];
	public static ArrayList<JLabel>[] JL = new ArrayList[4];
	public static ArrayList<Integer>[] Time = new ArrayList[4];
	public static ArrayList<Integer>[] Freq = new ArrayList[4];
	static public int TOP, LEFT, BOTTOM, RIGHT;

	public static MAIN MN = new MAIN();

	public static int HzMu = 48000;

	public static int onecool = 480 * 1;

	public static void main(String[] args)
	{
		// TODO 自動生成されたメソッド・スタブ

		for(int i = 0; i < JL.length; i++)
		{
			JL[i] = new ArrayList<>();
			Time[i] = new ArrayList<>();
			Freq[i] = new ArrayList<>();
		}

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

			//System.out.println((Math.pow(2, i - 4)) + " " + i + " " + (2 ^ i));
			for(int j = 0; j < 12; j++)
			{
				System.out.println(Onkai[j] + ":" + Sn[i][j]);
			}
		}

		AudioFormat fmt = new AudioFormat(44100, 8, 1, true, false);
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
				HzMu,
				Byte.SIZE,
				1,
				1,
				44100.0f,
				false);
		System.out.println("Format:" + format);
		System.out.println("Format:" + fmt);

        try
        {
        	audio1 = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			audio1.open(format);
        	audio2 = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			audio2.open(format);
        	audio3 = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			audio3.open(format);
        	audio4 = (SourceDataLine)AudioSystem.getSourceDataLine(format);
			audio4.open(format);
		}
        catch (LineUnavailableException e)
        {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

        audio1.start();
        audio2.start();
        audio3.start();
        audio4.start();

        /*while(true)
        {
        	writeNote(Triangle(Sn[4][E], (float)0.12, (byte)100, -0.0, 0, false, (byte)1, (byte)1), audio3);
        	writeNote(  Square(Sn[4][C], (float)0.50, (byte)25, -0.0, 0, false, (byte)1, (byte)1), audio1);
        	writeNote(  Square(Sn[4][G], (float)0.50, (byte)25, -0.0, 0, false, (byte)1, (byte)2), audio2);
        }//*/

		Timer T2 = new Timer();
		T2.scheduleAtFixedRate(new Audio1Task(), 0, 1000 / (HzMu / onecool));
		Timer T3 = new Timer();
		T3.scheduleAtFixedRate(new Audio2Task(), 0, 1000 / (HzMu / onecool));
		Timer T4 = new Timer();
		T4.scheduleAtFixedRate(new Audio3Task(), 0, 1000 / (HzMu / onecool));
		Timer T5 = new Timer();
		T5.scheduleAtFixedRate(new Audio4Task(), 0, 1000 / (HzMu / onecool));

        /*long l = 0;
        while(true)
        {
        	if(l < 100)
        	{
        		writeNote(Noise(2, 1.0f, (byte)100, -0.0, 0, false, (byte)1, (byte)1), audio4);
        	}
        	else if(l > 200 && l < 300)
        	{
        		writeNote(Triangle(Sn[4][E], (float)0.12, (byte)100, -0.0, 0, false, (byte)1, (byte)1), audio3);
        	}
        	System.out.println(l++);
        }//*/

		JF = new JFrame("NES DAW");
		//JF.setSize(1280, 720);
		JF.getContentPane().setPreferredSize(new Dimension(1280, 720));
		JF.pack();
		JF.setLocationRelativeTo(null);
		JF.setLayout(null);
		JF.setDefaultCloseOperation(JF.EXIT_ON_CLOSE);
		JF.setVisible(true);

		Insets JFI = JF.getInsets();
		TOP = 31;
		LEFT = 8;
		BOTTOM = 8;
		RIGHT = 8;
		TOP = JFI.top;
		LEFT = JFI.left;
		BOTTOM = JFI.bottom;
		RIGHT = JFI.right;//*/
		System.err.println("TOP:" + TOP + " BOTTOM:" + BOTTOM + " LEFT:" + LEFT + " RIGHT:" + RIGHT);
		//JF.setSize(JF.getWidth() + LEFT + RIGHT, JF.getHeight() + TOP + BOTTOM);
		System.err.println("Width:" + JF.getWidth() + " Height:" + JF.getHeight());
		System.err.println("Width:" + (1280 + LEFT + RIGHT) + " Height:" + (720 + TOP + BOTTOM));

		/*TOP = 0;
		LEFT = 0;
		BOTTOM = 0;
		RIGHT = 0;//*/

		for(int i = 0; i < 4; i++)
		{
			JP[i] = new JPanel();
			JP[i].setBackground(Color.gray);
			JP[i].setBounds(LEFT, TOP + ((JF.getHeight() - BOTTOM - TOP) / 5 - BOTTOM) * i, (JF.getWidth() - LEFT - RIGHT) / 6 - RIGHT, (JF.getHeight() - BOTTOM - TOP) / 5 - BOTTOM);

			JF.add(JP[i]);
		}

		JP[4] = new JPanel();
		JP[4].setBackground(Color.green);
		JP[4].setBounds(LEFT + (JF.getWidth() - LEFT - RIGHT) / 6 - RIGHT, TOP, ((JF.getWidth() - LEFT - RIGHT) / 6 * 5) - RIGHT, JF.getHeight() - BOTTOM - TOP - TOP - BOTTOM);
		JF.add(JP[4]);

		/*JP[5] = new JPanel();
		JP[5].setBackground(Color.green);
		JP[5].setBounds(LEFT + (JF.getWidth() - LEFT - RIGHT) / 6 - RIGHT, TOP, ((JF.getWidth() - LEFT - RIGHT) / 6 * 5) - RIGHT, JF.getHeight() - BOTTOM - TOP - TOP - BOTTOM);
		JF.add(JP[5]);//*/
	}

	private double[] MFreq = {0, 0, 0, 0};
	private float[] MDuty = {0f, 0f, 0f, 0f};
	private byte[] MVolu = {0, 0, 0, 0};
	private double[] MVDow = {0, 0, 0, 0};
	private double[] MModu = {0, 0, 0, 0};
	private boolean[] MMEna = {false, false, false, false};
	private byte[] MNumb = {0, 0, 0, 0};
	private long Tempsample = 0;

	static class Audio1Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			audio1.write(Square(Frequencys[0], Dutys[0], Volume[0], VDown[0], Mod[0], Mods[0], MNum[0], (byte)1), 0, onecool);
		}
	}

	static class Audio2Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			audio2.write(Square(Frequencys[1], Dutys[1], Volume[1], VDown[1], Mod[1], Mods[1], MNum[1], (byte)2), 0, onecool);
		}
	}

	static class Audio3Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			audio3.write(Triangle(Frequencys[2], Dutys[2], Volume[2], VDown[2], Mod[2], Mods[2], MNum[2], (byte)3), 0, onecool);
		}
	}

	static class Audio4Task extends TimerTask
	{
		@Override
		public void run()
		{
			// TODO 自動生成されたメソッド・スタブ
			audio4.write(Noise(Frequencys[3], Dutys[3], Volume[3], VDown[3], Mod[3], Mods[3], MNum[3], (byte)4), 0, onecool);
		}
	}

	static void writeNote(byte[] b, SourceDataLine line)
	{
        line.write(b, 0, b.length);
    }

	static byte[] MusicNumbers = {0, 0, 0, 0};
	static double[] Frequencys = {0, 0, 0, 0};
	static float[] Dutys       = {0f, 0f, 0f, 0f};
	static byte[] Volume       = {0, 0, 0, 0};
	static double[] VDown      = {0, 0, 0, 0};
	static double[] Volumes    = {0, 0, 0, 0};
	static double[] Mod        = {0, 0, 0, 0};
	static boolean[] Mods      = {false, false, false, false};
	static int[] Numbers       = {0, 0, 0, 0};
	static byte[] MNum         = {0, 0, 0, 0};

	static byte[] Square(double Frequency, float Duty, byte Volume, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
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
			Numbers[Ch - 1] = 0;
			Volumes[Ch - 1] = Volume * 1.0;
		}
		for(int i = 0; i < b.length; i++)
        {
            double phase = (i + (onecool * Numbers[Ch - 1])) / (HzMu / Frequency);
            phase -= Math.floor(phase);
            b[i] = (byte)(((phase <= Duty ? 127 : -128) / 127.0) * (Volumes[Ch - 1]));
			Volumes[Ch - 1] = Math.max(Math.min(Volumes[Ch - 1] + VolumeDownUp, 127), 0);
        }
		MusicNumbers[Ch - 1] = MusicNumber;
		Numbers[Ch - 1]++;
		//System.out.println(Volumes[Ch - 1]);
		return b;
	}

	static byte[] Tri = {0, 18, 36, 54, 72, 90, 108, 126};
	static byte Trinum = 0;
	static byte a = 1;
	static int bbold = -128;
	static byte[] Triangle(double Frequency, float Duty, byte Volume, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
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
			Numbers[2] = 0;
		}
		Volumes[2] = 100.0;
		//System.out.println("x");
		for(int i = 0; i < b.length; i++)
		{
            double phase = (i + (onecool * Numbers[2])) / (HzMu / (Frequency / 1));
            phase -= Math.floor(phase);
            int bb = (int)(Math.abs(phase - 0.5) * 510 - 128) + 128;
            if(bbold < bb) a = 1;
            else if(bbold > bb) a = -1;
            b[i] = (byte)(Tri[((bb / 16) <= 7 ? (bb / 16) : ((bb / 16) * -1 + 15))] * a);
            //System.out.println(((bb / 16) <= 7 ? (bb / 16) : ((bb / 16) * -1 + 15)) + " " + bb);
            bbold = bb;
		}
		MusicNumbers[2] = MusicNumber;
		Numbers[2]++;
		return b;
	}

	static int reg = 0x8000;
	static byte[] Noise(double Frequency, float Duty, byte Volume, double VolumeDownUp, double Moderation, boolean ModerationEnable, byte MusicNumber, byte Ch)
	{
		byte[] b = new byte[onecool];
		for(int i = 0; i < b.length / Frequency; i++)
		{
			reg >>>= 1;
			reg |= ((reg ^ (reg >>> (Duty == 1.0f ? 6 : 1))) & 1) << 15;
			b[(int) (i * Frequency)] = (byte) (((reg & 1) - 0.5) * 2 * Volume);
        	for(int j = 1; j < Frequency; j++)
        	{
        		if(i * Frequency + j < onecool) b[(int) (i * Frequency + j)] = b[(int) (i * Frequency)];
        	}
			//System.out.println(reg + " " + b[i]);
		}
		return b;
	}
}
