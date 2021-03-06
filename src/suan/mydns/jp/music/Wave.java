package suan.mydns.jp.music;

import processing.core.PApplet;
import suan.mydns.jp.Thoone;
import suan.mydns.jp.state.State;

public class Wave extends PApplet
{
	public static boolean Visible = true;
	public static Thoone th;

	@Override
	public void settings()
	{
		size(1530/*1020*/, 600);
	}

	@Override
	public void setup()
	{
		State.wv = this;
	}

	@Override
	public void draw()
	{
		if(this.Visible && th != null)
		{
			/*this.fill(255);
			this.rect(0, 0, 1020, 600);//*/
			background(255);
			this.fill(0);
			for(int i = 0; i < 5; i++)
			{
				byte[] b = null;
				//this.MVolDUM[i] = th.mm2.MVolDUM[i];
				//if(i < 2) b = Square(th.mm2.MFreq[i], th.mm2.MDuty[i], th.mm2.MVolu[i], th.mm2.MVDow[i], th.mm2.MModu[i], th.mm2.MMEna[i], th.mm2.MNumb[i], (byte)(i+1));
				//else if(i == 2) b = Triangle(th.mm2.MFreq[i], th.mm2.MDuty[i], th.mm2.MVolu[i], th.mm2.MVDow[i], th.mm2.MModu[i], th.mm2.MMEna[i], th.mm2.MNumb[i], (byte)(i+1));
				//else if(i == 3) b = Noise(th.mm2.MFreq[i], th.mm2.MDuty[i], th.mm2.MVolu[i], th.mm2.MVDow[i], th.mm2.MModu[i], th.mm2.MMEna[i], th.mm2.MNumb[i], (byte)(i+1));

				b = MM2.waves[i];

				if(i < 3)
				{
					for(int j = 0; j < MM2.onecool; j++)
					{
						this.point(20+MM2.onecool*i+j+20*i, (b[j] / MM2.percent)+150);
					}
				}
				else
				{
					for(int j = 0; j < MM2.onecool; j++)
					{
						this.point(20+MM2.onecool*(i-3)+j+20*(i-3), (b[j] / (j == 0 ? MM2.percent : 1))+420);
					}
				}
			}
		}
	}

	public void changeVisible()
	{
		Visible = ! Visible;
		getSurface().setVisible(Visible);
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

		for(int i = (int) (Nokori / /*(int)*/Frequencyss[3]); i < b.length / /*(int)*/Frequencyss[3]; i++)
		{
			reg >>>= 1;
			reg |= ((reg ^ (reg >>> (Duty == 1.0f ? 6 : 1))) & 1) << 15;
			b[(int) (i * /*(int)*/Frequencyss[3])] = (byte) (((reg & 1) - 0.5) * 2 * Math.max(Math.min(((byte)(Volumes[3])*8), VolumeDownUp < 0 ? 127 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8), VolumeDownUp >= 0 ? 0 : MVolDUM[3] == 16 ? 127 : MVolDUM[3] * 8));
        	for(int j = 1; j < /*(int)*/Frequencyss[3]; j++)
        	{
    			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
        		if(i * /*(int)*/Frequencyss[3] + j < MM2.onecool) b[(int) (i * /*(int)*/Frequencyss[3] + j)] = b[(int) (i * /*(int)*/Frequencyss[3])];
        		else
        		{
        			TempN = b[b.length-1];
        			Nokori = (int)(i * /*(int)*/Frequencyss[3] + j - MM2.onecool);
        		}
        	}
			Volumes[3] = Math.max(Math.min(Volumes[3] + VolumeDownUp, 16), 0);
		}

		Frequencyss[3] *= Moderation;
		MusicNumbers[3] = MusicNumber;

		return b;
	}
}
