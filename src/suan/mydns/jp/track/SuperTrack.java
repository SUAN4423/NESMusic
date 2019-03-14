package suan.mydns.jp.track;

import java.util.ArrayList;
import java.util.Random;

import suan.mydns.jp.Thoone;
import suan.mydns.jp.music.MM2;
import suan.mydns.jp.state.State;

public abstract class SuperTrack
{
	public ArrayList<Byte	> Volume = new ArrayList<>();
	public ArrayList<Double	> Freque = new ArrayList<>();
	public ArrayList<Integer> FrequI = new ArrayList<>();
	public ArrayList<Long	> Time	 = new ArrayList<>();
	public ArrayList<Integer> SoundT = new ArrayList<>();
	public ArrayList<Float	> Duty	 = new ArrayList<>();
	public ArrayList<Double	> Voldow = new ArrayList<>();
	public ArrayList<Double	> Fredow = new ArrayList<>();
	public ArrayList<Integer> VolDUM = new ArrayList<>();
	public ArrayList<Boolean> Moduration = new ArrayList<>();
	public int ShiftX = 0;
	public int ShiftY = 720;
	String[] Move = {"Å™", "Å´", "Å©", "Å®"};
	public int freq = 0;
	String[][] Dutys = {{"1:7", "1:3", "1:1", "3:1", "", "", "", ""},{"Long", "Short", "New", "Old", "", "", "", ""},{"1","2","3","4","5","6","7","8"}};
	float[][] fr = {{0.125f, 0.25f, 0.5f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f},{0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f},{0,1,2,3,4,5,6,7}};
	public int Nag = 4;
	protected boolean OnpuSet = false;
	protected boolean OnpuSet2 = false;
	protected String OnpuSetA = "4";
	public String[] Param = {"16", "1.0", "0.0", "1.0", "140.0", "16", "false"};
	protected String[] ParamName = {"Volume", "Frequency", "VolChange", "FreChange", "Tempo", "", "Mod"};
	protected boolean[] ParamB = {false, false, false, false, false, false, false};
	protected int VDUM = 16;
	protected byte Vol = 16;
	protected double Fre = 1.0, VolD = 0.0, FreD = 1.0;
	public double Tempo = 140.0;
	protected boolean mod = false;

	public void Draw(Thoone th)
	{
		th.fill(0x00, 0xFF, 0x00);
		th.rect(1280/6, 720/4, 1280/6*5, 720/4*3);
	}

	protected void Music(Thoone th)
	{
		for(int i = 0; i < this.Volume.size(); i++)
		{
			if(this.Duty.get(i) == 0.75) th.fill(0xFF, 0xB7, 0x4C);
			else if(this.Duty.get(i) == 0.5) th.fill(0x00, 0xFF, 0xFF);
			else if(this.Duty.get(i) == 0.25) th.fill(0xFF, 0xFF, 0x00);
			else th.fill(0x00, 0xFF, 0x00);
			th.rect(this.Time.get(i)/((60.0f / (float)this.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + this.ShiftX, this.FrequI.get(i)*-20+this.ShiftY-20, (1280/6.0f)*(this.SoundT.get(i)/((60.0f/(float)this.Tempo)*th.mm2.HzMu*4)), 20);
			th.fill(0);
			th.textSize(15);
			th.text(i, this.Time.get(i)/((60.0f / (float)this.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 70 + this.ShiftX, this.FrequI.get(i)*-20+this.ShiftY-5);
			//th.rect(b, this.FrequI.get(i)*-20+this.ShiftY-20, (1280/6.0f)*(this.SoundT.get(i)/((60.0f/(float)this.Tempo)*th.mm2.HzMu*4)), 20);
		}
	}

	protected boolean Clicked = false;

	protected void Norts(Thoone th, int i, int j, int Channel)
	{
		if(!Clicked && th.kmState.MLeft && th.kmState.IsMouseIn(1280/6+60, 720/4, 1280/6*5-60, 720/4*3))
		{
			int a = (th.kmState.Mouse[0]-1280/6-60-this.ShiftX);
			int k = -this.ShiftX/(1280/6)*this.Nag;
			while(true)
			{
				if(k*(1280/6)/Nag <= a && (k+1)*(1280/6)/Nag > a)
				{
					break;
				}
				k++;
			}
			long b = (long) ((k)*((60.0 / this.Tempo) * th.mm2.HzMu * 4 / this.Nag));

			this.Volume.add(this.Vol);
			if(Channel < 3)
			{
				double TempHZ = th.mm2.Sn[j][i] * this.Fre;
				int ChangeRate = (int)(MM2.FamicomHz / TempHZ + 0.5);
				TempHZ = MM2.FamicomHz / ChangeRate;
				this.Freque.add(TempHZ);
			}
			else if(Channel == 3)this.Freque.add(((i + j * 12) > 0 && (i + j * 12) < 17) ? th.mm2.SnN[i + j * 12 - 1] : 0);
			else this.Freque.add(((i + j * 12) > 0 && (i + j * 12) < 17) ? i + j * 12.0 - 1 : 0);
			//else this.Freque.add(i+j*12 + this.Fre);
			this.FrequI.add(i+j*12);
			this.Time.add((long)(b));
			this.SoundT.add((int) (((60.0 / this.Tempo) * th.mm2.HzMu * 4 / this.Nag)*State.NortsSize));
			this.Duty.add(this.fr[Channel == 3 ? 1 : Channel == 4 ? 2 : 0][this.freq]);
			this.Voldow.add(this.VolD);
			this.Fredow.add(this.FreD);
			this.VolDUM.add(this.VDUM);
			this.Moduration.add(this.mod);

			Clicked = true;
		}

		if(th.kmState.MRight && th.kmState.IsMouseIn(1280/6+60, 720/4, 1280/6*5-60, 720/4*3))
		{
			//int a = (th.kmState.Mouse[0]-1280/6-60+this.ShiftX);
			//int a = (int)(Math.abs(th.kmState.Mouse[0]-1280/6.0-60+this.ShiftX));
			int a = (th.kmState.Mouse[0]-1280/6-60-this.ShiftX);
			int k = -this.ShiftX/(1280/6)*this.Nag;
			while(true)
			{
				if(k*(1280/6)/Nag <= a && (k+1)*(1280/6)/Nag > a)
				{
					break;
				}
				k++;
			}
			long b = (long) ((k)*((60.0 / this.Tempo) * th.mm2.HzMu * 4 / this.Nag));

			for(int n = 0; n < this.Volume.size(); n++)
			{
				if(this.Time.get(n).equals(b))
				{
					this.Volume.remove(n);
					this.Freque.remove(n);
					this.FrequI.remove(n);
					this.Time.remove(n);
					this.SoundT.remove(n);
					this.Duty.remove(n);
					this.Voldow.remove(n);
					this.Fredow.remove(n);
					this.VolDUM.remove(n);
					this.Moduration.remove(n);
				}
			}
		}
	}

	protected int shift = 0;

	protected void Mouse(Thoone th)
	{
		if(th.kmState.IsMouseIn(1280/6+60, 720/4, 1280/6*5-60, 720/4*3))
		{
			int a = (th.kmState.Mouse[0]-1280/6-60-this.ShiftX);
			int k = -this.ShiftX/(1280/6)*this.Nag;
			while(true)
			{
				if(k*(1280/6)/Nag <= a && (k+1)*(1280/6)/Nag > a)
				{
					break;
				}
				k++;
			}
			long b = (long) ((k)*((60.0 / this.Tempo) * th.mm2.HzMu * 4 / this.Nag));

			th.fill(0xFF, 0x90, 0x90);
			th.rect(b/((60.0f / (float)this.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + this.ShiftX, (th.kmState.Mouse[1]/20)*20, (float)((1280/6/Nag)*State.NortsSize), 20);
			th.rect(b/((60.0f / (float)this.Tempo) * th.mm2.HzMu * 4) * (1280 / 6) + 1280/6 + 60 + this.ShiftX, 720/4, 0, 720/4*3);
		}
	}

	private final int tesi = 3;

	protected void parameter(Thoone th)
	{
		for(int i = 0; i < 5; i++)
		{
			th.fill(255);
			if(th.kmState.IsMouseIn(1280/12*(i+7), 720/8, 1280/12, 720/8/((i == tesi || i == 2) ? 3 : 2) * 2))
			{
				th.fill(0xFF, 0x00, 0x00);
			}
			if(this.ParamB[i] && th.kmState.IsMouseIn(1280/12*(i+7), 720/8, 1280/12, 720/8/((i == tesi || i == 2) ? 3 : 2) * 2))
			{
				th.fill(0x00, 0xFF, 0x00);
			}
			th.rect(1280/12*(i+7), 720/8, 1280/12, 720/8/((i == tesi || i == 2) ? 3 : 2) * 2);
			th.fill(0);
			th.textSize(15);
			th.text(this.ParamName[i] + "\n" + this.Param[i], 1280/12*(i+7)+10, 720/8 + 20);

			if(!th.mv.enableMove)
			{
				if(th.kmState.MLeft && !this.ParamB[i] && th.kmState.IsMouseIn(1280/12*(i+7), 720/8, 1280/12, 720/8/((i == tesi || i == 2) ? 3 : 2) * 2))
				{
					this.ParamB[i] = true;
					this.Param[i] = "";
				}
				else if(this.ParamB[i] && th.kmState.IsMouseIn(1280/12*(i+7), 720/8, 1280/12, 720/8/((i == tesi || i == 2) ? 3 : 2) * 2))
				{
					if(!this.OnpuSet2 && th.keyPressed)
					{
						if(i != 0 && th.key == '.')
						{
							this.Param[i] += th.key;
						}
						else if(th.key == '-')
						{
							this.Param[i] += th.key;
						}
						else
						{
							try
							{
								Integer.parseInt(th.key + "");
								this.Param[i] += th.key;
							}
							catch(Exception e)
							{

							}
						}
						this.OnpuSet2 = true;
					}
					else if(this.OnpuSet2 && !th.keyPressed)
					{
						this.OnpuSet2 = false;
					}
				}
				else if(this.ParamB[i])
				{
					if(this.Param[i] != "")
					{
						try
						{
							switch(i)
							{
							case 0:
								this.Vol = Byte.parseByte(this.Param[i]);
								break;
							case 1:
								this.Fre = Double.parseDouble(this.Param[i]);
								break;
							case 2:
								this.VolD = Double.parseDouble(this.Param[i]);
								break;
							case 3:
								this.FreD = Double.parseDouble(this.Param[i]);
								break;
							case 4:
								//this.Tempo = Double.parseDouble(this.Param[i]);
								//th.state.TempoSet(th, this.Tempo);
								th.state.TempoSet(th, Double.parseDouble(this.Param[i]));
								break;
							}
						}
						catch(NumberFormatException e)
						{
							e.printStackTrace();
						}
					}
					this.ParamB[i] = false;
				}
			}
		}

		for(int i = 0; i < 2; i++)
		{
			th.fill(255);
			if(th.kmState.IsMouseIn(1280/12*(9+i), 720/8+720/8/3*2, 1280/12, 720/8/3))
			{
				th.fill(0xFF, 0x00, 0x00);
			}
			if(this.ParamB[5 + i] && th.kmState.IsMouseIn(1280/12*(9+i), 720/8+720/8/3*2, 1280/12, 720/8/3))
			{
				th.fill(0x00, 0xFF, 0x00);
			}
			th.rect(1280/12*(9+i), 720/8+720/8/3*2, 1280/12, 720/8/3);
			th.fill(0);
			th.textSize(15);
			th.text(this.Param[5 + i] + "", 1280/12*(9+i)+10, 720/8+720/8/3*2 + 20);
		}

		if(!th.mv.enableMove)
		{
			for(int i = 0; i < 2; i++)
			{
				if(th.kmState.MLeft && !this.ParamB[5 + i] && th.kmState.IsMouseIn(1280/12*(9+i), 720/8+720/8/3*2, 1280/12, 720/8/3))
				{
					this.ParamB[5 + i] = true;
					this.Param[5 + i] = "";
				}
				else if(this.ParamB[5 + i] && th.kmState.IsMouseIn(1280/12*(9+i), 720/8+720/8/3*2, 1280/12, 720/8/3))
				{
					if(!this.OnpuSet2 && th.keyPressed)
					{
						try
						{
							if(i == 0)
							{
								Integer.parseInt(th.key + "");
								this.Param[5 + i] += th.key;
							}
							else
							{
								Integer.parseInt(th.key + "");
								if(th.key == '1') this.Param[5 + i] = "true";
								else this.Param[5 + i] = "false";
							}
						}
						catch(Exception e)
						{

						}
						this.OnpuSet2 = true;
					}
					else if(this.OnpuSet2 && !th.keyPressed)
					{
						this.OnpuSet2 = false;
					}
				}
				else if(this.ParamB[5 + i])
				{
					if(this.Param[5 + i] != "")
					{
						if(i == 0) VDUM = Integer.parseInt(this.Param[5 + i]);
						else
						{
							if(this.Param[5 + i].equals("true")) this.mod = true;
							else this.mod = false;
						}
					}
					this.ParamB[5 + i] = false;
				}
			}
		}

		this.Mouse(th);
	}

	protected void Onpu(Thoone th)
	{
		th.fill(255);
		if(th.kmState.IsMouseIn(1280/6*3, 720/8, 1280/12, 720/8))
		{
			th.fill(0xFF, 0x00, 0x00);
		}
		if(this.OnpuSet && th.kmState.IsMouseIn(1280/6*3, 720/8, 1280/12, 720/8))
		{
			th.fill(0x00, 0xFF, 0x00);
		}
		th.rect(1280/6*3, 720/8, 1280/12, 720/8);
		th.fill(0);
		th.textSize(20);
		th.text(this.OnpuSetA + "\nminutes\nnote", 1280/6*3+10, 720/8 + 20);

		if(!th.mv.enableMove)
		{
			if(th.kmState.MLeft && !this.OnpuSet && th.kmState.IsMouseIn(1280/6*3, 720/8, 1280/12, 720/8))
			{
				this.OnpuSet = true;
				this.OnpuSetA = "";
			}
			else if(this.OnpuSet && th.kmState.IsMouseIn(1280/6*3, 720/8, 1280/12, 720/8))
			{
				if(!this.OnpuSet2 && th.keyPressed)
				{
					try
					{
						Integer.parseInt(th.key + "");
						this.OnpuSetA += th.key;
					}
					catch(Exception e)
					{

					}
					this.OnpuSet2 = true;
				}
				else if(this.OnpuSet2 && !th.keyPressed)
				{
					this.OnpuSet2 = false;
				}
			}
			else if(this.OnpuSet)
			{
				if(this.OnpuSetA != "")
				{
					this.Nag = Integer.parseInt(this.OnpuSetA);
				}
				this.OnpuSet = false;
			}
		}

		this.parameter(th);
	}

	protected int temp = 1;

	protected void Onkai(Thoone th, int Channel)
	{
		boolean music = false;

		for(int j = 0; j < 10; j++)
		{
			for(int k = -1; k < 6; k++)
			{
				th.fill(0xF0, 0xF0, 0xF0);
				th.rect(1280/6*(k+1)+60-Math.abs(this.ShiftX)%(1280/6), (j+1)*12*-20+this.ShiftY, 1280/6, 12*20);
				th.fill(0);
				th.textSize(20);
				th.text(Math.abs(this.ShiftX/(1280/6))+k, 1280/6*(k+1)+60+this.ShiftX%(1280/6), 720/4+20);
			}
		}

		this.Music(th);

		th.fill(0x00, 0xFF, 0x00);
		th.rect(1280/6, 720/4, 60, 720/4*3);

		for(int j = 0; j < 10; j++)
		{
			for(int i = 0; i < 12; i++)
			{
				th.fill(0);
				th.textSize(20);
				th.text(j + " " + th.mm2.Onkai[i], 1280/6, (i+j*12)*-20+this.ShiftY);
				if(th.kmState.MLeft && (i+1+j*12)*-20+this.ShiftY + 20 > 720 / 4)
				{
					if(th.kmState.IsMouseIn(1280/6, (i+1+j*12)*-20+this.ShiftY, 1280/6*5, 20))
					{
						music = true;
						double TempHZ = th.mm2.Sn[j][i] * this.Fre;
						int ChangeRate = (int)(MM2.FamicomHz / TempHZ + 0.5);
						TempHZ = MM2.FamicomHz / ChangeRate;
						th.mm2.ChStat(TempHZ, fr[(Channel == 0 ? 0 : Channel == 1 ? 0 : 1)][freq], this.Vol, this.VolD, this.FreD, this.mod, temp, Channel, this.VDUM);
						if(!th.mv.enableMove) this.Norts(th, i, j, Channel);
					}
				}
				if(th.kmState.MRight && (i+1+j*12)*-20+this.ShiftY + 20 > 720 / 4)
				{
					if(th.kmState.IsMouseIn(1280/6, (i+1+j*12)*-20+this.ShiftY, 1280/6*5, 20))
					{
						if(!th.mv.enableMove) this.Norts(th, i, j, Channel);
					}
				}
			}
		}
		if(!music && th.kmState.IsMouseIn(1280/6, 720/4, 1280/6*5, 750/4*3))
		{
			this.Clicked = false;
			temp = R.nextInt(50)+2;
			if(!th.musics.start) th.mm2.ChStat(Channel == 3 ? 0 : -1, 0.5f, 16, 0, 0, false, 1, Channel, 16);
		}

		th.fill(255);
		th.rect(1280/6, 0, 1280/6*5, 720/8*2);

		this.Onpu(th);
	}

	Random R = new Random();

	protected void Line(Thoone th)
	{
		for(int i = 0; i < 5; i++)
		{
			th.fill(0xF0, 0xF0, 0xF0);
			th.rect(1280/6*(i+1)+60+this.ShiftX%(1280/6), 720/4, 1280/6, 720/4*3);
		}
	}

	protected void Shift(Thoone th)
	{
		for(int i = 0; i < 4; i++)
		{
			th.fill(255);
			th.rect(1280/6+1280/24*i, 720/8, 1280/24, 720/8);
			th.textSize(25);
			th.fill(0);
			th.text(Move[i], 1280/6+1280/24*i+20, 720/8/2*3+10);
			if(th.kmState.MLeft)
			{
				if(th.kmState.IsMouseIn(1280/6+1280/24*i, 720/8, 1280/24, 720/8))
				{
					switch(i)
					{
					case 0:
						this.ShiftY += 20;
						if(this.ShiftY > 2580) this.ShiftY = 2580;
						break;
					case 1:
						this.ShiftY -= 20;
						if(this.ShiftY < 720) this.ShiftY = 720;
						break;
					case 2:
						this.ShiftX += 20;
						if(this.ShiftX > 0) this.ShiftX = 0;
						break;
					case 3:
						this.ShiftX -= 20;
						break;
					}
				}
			}
		}
	}
}
